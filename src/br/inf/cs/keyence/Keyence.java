package br.inf.cs.keyence;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

import javax.imageio.ImageIO;


/*-
 * Copyright © 2009 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * Provides Ethernet communications a Keyence Camera Controller
 *
 * Tested with a CV-3001P but should work with others as well.
 * Triggering is done in software. If you require hardware (external) triggering,
 * that would not be very hard to implement.
 */
public class Keyence {

	private String host = "localhost";
	private int port = 9004;
	private int socketTimeout = 5000;
	private String name = "KEYENCE";
	private boolean connected = false;
	private String imageFormat = "png";
	private ArrayList<String> startupCommands = new ArrayList<String>();
	private ByteBuffer bb = ByteBuffer.allocate(4096);
    private static Charset charset = Charset.forName("US-ASCII");
    private static CharsetEncoder encoder = charset.newEncoder();
    private static CharsetDecoder decoder = charset.newDecoder();
    private final Object socketAccessLock = new Object(); // Object used to grant a thread exclusive access to the socket, bb and connected
	private SocketChannel socketChannel;
	
	
	// CONSTRUTORES
	public Keyence() {}
	
	public Keyence(String host) {
		this.host = host;
	}
	
	public Keyence(int port) {
		this.port = port;
	}
	
	public Keyence(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * Set up initial connections to socket and wrap the stream in buffered reader and writer.
	 * @throws DeviceException
	 */
	public void connect() throws Exception {
		try {
			synchronized (this.socketAccessLock) {
				if (!isConnected()){
					InetSocketAddress inetAddr = new InetSocketAddress(this.host, this.port);
					this.socketChannel = SocketChannel.open();
					this.socketChannel.connect(inetAddr);
					this.socketChannel.socket().setSoTimeout(this.socketTimeout);
					this.socketChannel.configureBlocking(true);
					this.socketChannel.finishConnect();
					this.connected = true;
					
					System.out.println("Connection stablished!");

					this.cleanPipe();
					this.doStartupScript();
				}
			}
		} catch (UnknownHostException ex) {
			// this could be fatal as reconnect attempts are futile.
			System.out.println(getName() + ": connect: " + ex.getMessage());
		} catch (ConnectException ex) {
			System.out.println(getName() + ": connect: " + ex.getMessage());
			System.out.println("Please, verify if other device is already connected to " + getName());
		} catch (IOException ix) {
			System.out.println(getName() + ": connect: " + ix.getMessage());
		}
	}

	/**
	 * Tidy existing socket streams and try to connect them again within the thread. This method is synchronized as both
	 * the main thread and run thread use this method.
	 * @throws DeviceException
	 */
	public synchronized void reconnect() throws Exception {
		try {
			this.close();
		} catch (Exception ex) {
			System.out.println(this.getName() + ": reconnect: " + ex.getMessage());
		}
		this.connect();
	}

	/**
	 * Close method.
	 * @throws DeviceException
	 */
	public void close() throws Exception {
		synchronized (socketAccessLock) {
			connected = false;
			if (socketChannel != null) {
				try {
					socketChannel.close();
					socketChannel = null;
					System.out.println("Connection closed!");
				} catch (IOException ex) {
					throw new Exception(ex.getMessage(), ex);
				}
			}
		}
	}

	/**
	 * Send command to the server.
	 *
	 * @param msg
	 *            an unterminated command
	 * @return the reply string.
	 * @throws DeviceException
	 */
	public String processCommand(String msg) throws Exception {
		String command = msg + '\r';
		String reply = null;
		System.out.println(this.getName() + ": sent command: |" + msg + "|");
		synchronized (this.socketAccessLock) {
			if (!this.isConnected()) {
				throw new Exception("Not connected to device!");
			}
			try{
				this.cleanPipe();
				this.socketChannel.write(encoder.encode(CharBuffer.wrap(command)));
				this.bb.clear();
				this.socketChannel.read(this.bb);
				this.bb.flip();
				reply = decoder.decode(this.bb).toString();
				System.out.println(this.getName() + ": got reply: |" + reply.trim() + "|");				
			} catch (SocketTimeoutException ex) {
				throw new Exception("SendCommand read timeout " + ex.getMessage(), ex);
			} catch (IOException ex) {
				connected = false;
				throw new Exception("SendCommand: " + ex.getMessage(), ex);
			}
		}
		return reply;
	}


	/**
	 * Listen to the socket inputStream.
	 */
	public InputStream listenStream() throws Exception {
		InputStream is = null;
		synchronized (this.socketAccessLock) {
			if (!this.isConnected()) {
				throw new Exception("Not connected to device!");
			}
			try{
				is = this.socketChannel.socket().getInputStream();
			} catch (IOException ex) {
				throw new Exception("SendCommand: " + ex.getMessage(), ex);
			}
		}
		return is;
	}

	private void doStartupScript() throws Exception {
		for (String command : startupCommands) {
			String reply = processCommand(command);
			if (reply.startsWith("ER")) {
				System.out.println("error sending command " + command + " received error reply from device: " + reply);
			} else {
				System.out.println("\" " + command + "\" successfuly replied: " + reply);
			}
		}
	}

	private void cleanPipe() throws IOException {
		synchronized (socketAccessLock) {
			if(isConnected()){
				try{
					socketChannel.configureBlocking(false);
					while (socketChannel.read(bb) > 0) {
						bb.clear();
					}
					socketChannel.configureBlocking(true);
				} catch (IOException ex) {
					try {
						close();
					} catch (Exception e) {
						// we know that already
					}
					throw ex;
				}
			}
		}
	}
	
	/**
	 * Returns the state of the socket connection
	 *
	 * @return true if connected
	 */
	public boolean isConnected() {
		return connected;
	}
	
	/**
	 * Set the keyence name
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the keyence name
	 */
	public String getName() {
		return this.name + "(" + this.host + ":" + this.port + ")";
	}
	
	/**
	 * Set the keyence host
	 *
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the keyence host name
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * Set the command port
	 *
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the command port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * @param startupCommands
	 */
	public void setStartupCommands(ArrayList<String> startupCommands) {
		this.startupCommands = startupCommands;
	}

	/**
	 * @return an array list of startup commands to be processed by da.server on startup
	 */
	public ArrayList<String> getStartupCommands() {
		return startupCommands;
	}

	
	
	
	/***********************
	 * CODIGO NAO UTILIZADO
	 ***********************/

	private String writeImage(BufferedImage image) throws IOException {
//		String fileName = PathConstructor.createFromDefaultProperty()+"/"+getName()+"-"+dateFormat.format(new Date())+"."+imageFormat;
//		File imageFile = new File(fileName );
//		ImageIO.write(image, imageFormat, imageFile);
//		return fileName;
		return null;
	}
	
	/**
	 *
	 * @return the filename
	 * @throws IOException
	 * @throws DeviceException
	 */
	public String saveLastMeasurementImage() throws IOException, Exception {
		return writeImage(getLastMeasurementImage());
	}
	
	/**
	 *
	 * @return the camera shot of the last measurement
	 * @throws DeviceException
	 * @throws IOException
	 */
	public BufferedImage getLastMeasurementImage() throws IOException, Exception {
		byte[] image = (byte[]) processImageRequest("BR,CM,1,0,NW", 5)[5];
		return ImageIO.read(new ByteArrayInputStream(image));
	}

	/**
	 *
	 * @throws IOException
	 * @throws DeviceException
	 */
	public void saveScreenShot(String fileName) throws IOException, Exception {
		File imageFile = new File(fileName );
		ImageIO.write(getScreenShot(), imageFormat, imageFile);
	}

	/**
	 *
	 * @return the filename
	 * @throws IOException
	 * @throws DeviceException
	 */
	public String saveScreenShot() throws IOException, Exception {
		return writeImage(getScreenShot());

	}
	
	/**
	 *
	 * @return a screenshot
	 * @throws DeviceException
	 * @throws IOException
	 */
	public BufferedImage getScreenShot() throws IOException, Exception {
		byte[] image = (byte[]) processImageRequest("BC,CM", 2)[2];
		return ImageIO.read(new ByteArrayInputStream(image));
	}
	
	/**
	 * Send command to the server.
	 *
	 * @param msg
	 *            an unterminated command
	 * @param expectedReplyItems
	 * @return the reply string.
	 * @throws DeviceException
	 */
	public Object[] processImageRequest(String msg, int expectedReplyItems) throws Exception {
		if (expectedReplyItems < 2) throw new IllegalArgumentException("need at least two values for images (length definition and data)");
		String command = msg + '\r';
		Object reply[] = new Object[expectedReplyItems+1];
		System.out.println(getName() + ": sent command: " + msg);
		synchronized (socketAccessLock) {
			try{
				if (!isConnected()) {
					throw new Exception("not connected");
				}
				cleanPipe();
				socketChannel.write(encoder.encode(CharBuffer.wrap(command)));

				ByteBuffer singleByte = ByteBuffer.allocate(1);

				StringBuilder sb = new StringBuilder();
				int argCounter = 0;
				while(argCounter < expectedReplyItems) {
					singleByte.clear();
					socketChannel.socket().setSoTimeout(this.socketTimeout);
					socketChannel.configureBlocking(true);
					while (singleByte.position() == 0)
						socketChannel.read(singleByte);
					singleByte.flip();
					String c = decoder.decode(singleByte).toString();
					System.out.println(c.toString());
					if (c.equals(",")) {
						reply[argCounter] = sb.toString();
						sb = new StringBuilder();
						argCounter++;
					} else if (c.equals("\r")) {
						throw new Exception("sendCommand: not enough data for image received - suspect an error");
					} else {
						sb.append(c);
					}
				}

				int imageLength = Integer.parseInt(reply[expectedReplyItems-1].toString());

				byte[] imageData = new byte[imageLength];
				ByteBuffer bybu = ByteBuffer.wrap(imageData);

				while(bybu.remaining() != 0) {
						socketChannel.read(bybu);
					}

				reply[expectedReplyItems] = imageData;
			} catch (SocketTimeoutException ex) {
				throw new Exception("sendCommand read timeout " + ex.getMessage(),ex);
			} catch (IOException ex) {
				// treat as fatal
				connected = false;
				throw new Exception("sendCommand: " + ex.getMessage(),ex);
			}
		}
		return reply;
	}

	public double[] getPosition() throws Exception {

		String reply = processCommand("T1");
		String[] posStrings = reply.split(",");
		if (!"T1".equals(posStrings[0])) {
			throw new Exception("communication or measurement error (device not in run mode?): \"" + reply + "\"");
		}

		int positionsRead = posStrings.length-1;
//		if (extraNames.length>0 && extraNames.length != positionsRead) throw new Exception("unexpected number of measurements, are we running the right program? expected <"+extraNames.length+"> got <"+positionsRead+">. Reply was \"" + (reply.replace("\n", "\\n").replace("\r", "\\r")) + "\"");

		double[] positions = new double[positionsRead];
		for (int i = 1; i < posStrings.length; i++) {
			positions[i - 1] = Double.parseDouble(posStrings[i]);
		}
		return positions;
	}

	public void asynchronousMoveTo(Object position) throws Exception {
	}

	public boolean isBusy() throws Exception {
		return false;
	}

	private void runKeyence() {
		try {
			runKeyenceLoop();
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted while running Keyence {}" + getName());
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

	private void runKeyenceLoop() throws InterruptedException {
		while (true) {
			Thread.sleep(21987);
			try {
				cleanPipe();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!isConnected()) {
				try {
					reconnect();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}