package br.inf.cs.keyence;

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


/*-
 * Copyright � 2009 Diamond Light Source Ltd.
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
	private int socketTimeout = 15000;
	private String name = "KEYENCE";
	private boolean connected = false;
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

	// CONECTAR AO SCANNER
	public void connect() throws UnknownHostException, ConnectException, IOException {
		synchronized (this.socketAccessLock) {
			if (!isConnected()){
				InetSocketAddress inetAddr = new InetSocketAddress(this.host, this.port);
				this.socketChannel = SocketChannel.open();
				this.socketChannel.connect(inetAddr);
				this.socketChannel.socket().setSoTimeout(this.socketTimeout);
				this.socketChannel.configureBlocking(true);
				this.socketChannel.finishConnect();
				this.connected = true;
				this.cleanPipe();
			}
		}
	}

	// DESCONECTAR DO SCANNER
	public void close() throws ConnectException, IOException {
		synchronized (socketAccessLock) {
			connected = false;
			if (socketChannel != null) {
				socketChannel.close();
				socketChannel = null;
			}
		}
	}

	// ENVIAR COMANDOS
	public void send(String cmd) throws ConnectException, IOException {
		String command = cmd + '\r';
		if (!this.isConnected()) {
			throw new ConnectException("Not connected to scanner!");
		}

		synchronized (this.socketAccessLock) {
			// limpar canal socket
			socketChannel.configureBlocking(false);
			while (socketChannel.read(bb) > 0) bb.clear();
			socketChannel.configureBlocking(true);
			
			// enviar comando
			this.bb.clear();
			this.socketChannel.write(encoder.encode(CharBuffer.wrap(command)));
		}
	}

	// PEGAR STREAM DO SOCKET
	public InputStream getStream() throws ConnectException, IOException {
		InputStream is = null;
		if (!this.isConnected()) {
			throw new ConnectException("Not connected to device!");
		}
		is = this.socketChannel.socket().getInputStream();
		return is;
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

	// LIMPAR CANAL SOCKET
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
	

	public boolean isConnected() {
		return connected;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name + "(" + this.host + ":" + this.port + ")";
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return this.host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}
}