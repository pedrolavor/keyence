package me.pedrolavor.keyence;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pedrolavor.keyence.exception.KeyenceConnectionException;
import me.pedrolavor.keyence.exception.KeyenceException;

/**
 * Provides Ethernet communications a Keyence Camera Controller
 * Tested with a SR-1000.
 */

public class Keyence {

	private static final Logger logger = LoggerFactory.getLogger(Keyence.class);
	private static int count = 0;
	
	private Socket socket;
	private PrintStream socketWriter = null;
	
	private String name;
	private String host = "localhost";
	private int port = 9004;
	
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
	
	public void connect() throws KeyenceConnectionException {
		if (!isConnected()){
			try {
				socket = new Socket(host, port);
				socketWriter = new PrintStream(socket.getOutputStream());
			} catch (Exception e) {
				logger.error("Error connecting to " + host + ":" + port);
				throw new KeyenceConnectionException(e.getCause().getMessage());
			}
		} else {
			logger.info("Connection to " + host + ":" + port + " (" + name + ") existent.");
		}
	}

	public void disconnect() throws KeyenceConnectionException {
		logger.info("Disconnecting from " + host + ":" + port + " (" + name + ")");
		try {
			
			// disconnection does not wait outputstream finish
			// so, this is a workaround to wait
			Thread.sleep(100);
			
			if(socketWriter != null) {
				socketWriter.close();
			}
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Disconnecting from " + host + ":" + port + " (" + name + ")");
			throw new KeyenceConnectionException(e.getCause().getMessage());
		}
	}
	
	public boolean execute(String command) throws Exception {
		logger.debug("Sending command: " + command);
		System.out.println("Sending command: " + command);
		
		if (!isConnected()) {
			logger.debug("Scanner not connected.");
			throw new KeyenceException("Not connected.");
		}

		socketWriter.println(command + '\r');
		return socketWriter.checkError();
	}
	
	public boolean execute(KeyenceCommand keyenceCommand) throws Exception {
		return execute(keyenceCommand.getCommand());
	}
	
	public InputStream listen() throws KeyenceException {
		InputStream stream = null;
		if (!isConnected()) {
			logger.debug("Scanner not connected.");
			throw new KeyenceException("Not connected!");
		}
		try {
			stream = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			throw new KeyenceException("Error on getting socket stream.");
		}
		return stream;
	}
	
	private void setStandartName() {
		name = "KEYENCE-SR-1000-" + count++;
	}
	
	public boolean isConnected() {
		return socket != null && !socket.isClosed();
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}