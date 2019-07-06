package br.inf.cs.keyence;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Keyence2 {
	
	private Socket socket;
	private boolean connected;
	private String host = "localhost";
	private int port = 9004;
	private PrintStream print;
	
	
	// CONSTRUTORES
	public Keyence2() {}
	
	public Keyence2(String host) {
		this.host = host;
	}
	
	public Keyence2(int port) {
		this.port = port;
	}
	
	public Keyence2(String host, int port) {
		this.host = host;
		this.port = port;
	}

	// CONECTAR AO SCANNER
	public void connect() throws IOException {
		socket = new Socket(host, port);
		System.out.println("CONECTADO!");
		print = new PrintStream(socket.getOutputStream());
		connected = true;
	}

	// ENVIAR COMANDOS
	public void send(String cmd) throws ConnectException, IOException {
		String command = cmd + '\r';
		System.out.println(connected);
		if (!connected) {
			throw new ConnectException("Not connected to scanner!");
		}
		print.println(command);
	}

	// PEGAR STREAM DO SOCKET
	public InputStream getStream() throws ConnectException, IOException {
		InputStream is = null;
		if (!connected) {
			throw new ConnectException("Not connected to scanner!");
		}
		is = socket.getInputStream();
		return is;
	}
	
	// DISCONECTAR DO SCANNER
	public void disconnect() throws IOException {
		socket.close();
		connected = false;
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
	
	
}
