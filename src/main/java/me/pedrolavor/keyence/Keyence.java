package me.pedrolavor.keyence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	
	private String name;
	private String host = "localhost";
	private int port = 9004;

	private Socket socket = null;
	private PrintStream socketWriter = null;
	
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
		logger.info("Connecting to " + host + ":" + port + " (" + name + ")");
		if (!isConnected()){
			try {
				socket = new Socket(host, port);
				socketWriter = new PrintStream(socket.getOutputStream());
				logger.info("Successfully connected!");
			} catch (Exception e) {
				logger.error("Error connecting to " + host + ":" + port);
				throw new KeyenceConnectionException(e.getCause().getMessage());
			}
		} else {
			logger.info("Connection to " + host + ":" + port + " (" + name + ") already existent.");
		}
	}

	public void disconnect() throws KeyenceConnectionException {
		logger.info("Disconnecting from " + host + ":" + port + " (" + name + ")");
		try {
			socketWriter.close();
			socket.close();
			socketWriter = null;
			socket = null;
		} catch (Exception e) {
			logger.info("Disconnecting from " + host + ":" + port + " (" + name + ")");
			throw new KeyenceConnectionException(e.getCause().getMessage());
		}
	}
	
	public void execute(String command) throws KeyenceException {
		logger.debug("Sending command: " + command);
		System.out.println("Sending command: " + command);
		
		if (!isConnected()) {
			logger.debug("Scanner not connected.");
			throw new KeyenceException("Not connected.");
		}

		socketWriter.flush();
		socketWriter.println(command + '\r');
		socketWriter.flush();
	}
	
	public void execute(KeyenceCommand keyenceCommand) throws KeyenceException {
		execute(keyenceCommand.getCommand());
	}
	
	public BufferedReader listen() throws KeyenceException {
		BufferedReader buff = null;
		if (!isConnected()) {
			logger.debug("Scanner not connected.");
			throw new KeyenceException("Not connected!");
		}
		try {
			buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new KeyenceException("Error on getting socket stream.");
		}
		return buff;
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