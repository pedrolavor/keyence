package me.pedrolavor.keyence;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides Ethernet communications a Keyence Camera Controller
 * Tested with a SR-1000.
 */

public class Keyence {

	private static final Logger logger = LoggerFactory.getLogger(Keyence.class);
	private static int count = 0;
	
	private String name;
	private String stdName = "KEYENCE-SR-1000";
	private String host = "localhost";
	private int port = 9004;
	private int socketTimeout = 5000;
	private boolean connected = false;

	private Socket socket;
	private PrintStream socketPrinter;
	private SocketChannel socketChannel;
    private static Charset charset = Charset.forName("US-ASCII");
    private static CharsetEncoder encoder = charset.newEncoder();
	private final Object socketAccessLock = new Object();
	private ByteBuffer bb = ByteBuffer.allocate(4096);
	
	public Keyence() {
		setStandartName();
	}
	
	public Keyence(String host) {
		this.host = host;
		setStandartName();
	}
	
	public Keyence(String host, int port) {
		this.host = host;
		this.port = port;
		setStandartName();
	}
	
	public void connect() throws KeyenceException {
		logger.info("Connecting to " + host + ":" + port + " (" + name + ")");
		try {
			if (!connected){
				socket = new Socket(host, port);
				connected = true;
				logger.info("Successfully connected!");
				cleanPipe();
			}
		} catch (UnknownHostException ex) {
			logger.error("Host not found.");
			throw new KeyenceException("Host not found.");
		} catch (IOException ix) {
			logger.error("Error on connect.");
			throw new KeyenceException("Error on connect.");
		}
	}

	public void close() throws KeyenceException {
		logger.info("Disconnecting from " + host + ":" + port + " (" + name + ")");
		connected = false;
		if (socketChannel != null) {
			try {
				socketChannel.close();
				socketChannel = null;
			} catch (IOException ex) {
				logger.error("Error on closing connection.");
				throw new KeyenceException(ex.getMessage());
			}
		}
	}
	
	public void execute(String command) throws KeyenceException {
		String cmd = command + '\r';
		logger.debug("Sending command: " + cmd);
		System.out.println("Sending command: " + cmd);
			
		if (!isConnected()) {
			logger.debug("Scanner not connected.");
			throw new KeyenceException("Not connected.");
		}
		
		try{
			this.cleanPipe();
			this.socketChannel.write(encoder.encode(CharBuffer.wrap(cmd)));
			this.bb.clear();	
		} catch (SocketTimeoutException ex) {
			throw new KeyenceException("Command read timeout: " + cmd);
		} catch (IOException ex) {
			throw new KeyenceException("Error sending command: " + cmd);
		}
	}
	
	public void execute(KeyenceCommand keyenceCommand) throws KeyenceException {
		execute(keyenceCommand.getCommand());
	}
	
	public InputStream listen() throws KeyenceException {
		InputStream buffer = null;
		
		if (!isConnected()) {
			logger.debug("Scanner not connected.");
			throw new KeyenceException("Not connected!");
		}
		
		try {
			buffer = socketChannel.socket().getInputStream();
		} catch (IOException e) {
			throw new KeyenceException("Error listen to scanner.");
		}
		
		return buffer;
	}
	
	private void cleanPipe() throws IOException {
		synchronized (socketAccessLock) {
			if(connected){
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
	
	private void setStandartName() {
		name = stdName + "-" + count++;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getSocketTimeout() {
		return socketTimeout;
	}
	
	public void setSocketTimeout(int timeout) {
		this.socketTimeout = timeout;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}