package br.inf.cs.keyence;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;

import br.inf.cs.keyence.Keyence;

public class KeyenceController2 {
	
	Keyence2 keyence = null;
	
	public KeyenceController2() {
		this.keyence = new Keyence2();
	}
	
	public void conectar() throws UnknownHostException, ConnectException, IOException {
		this.keyence.connect();
	}
	
	public void desconectar() throws ConnectException, IOException {
		this.keyence.disconnect();
	}
	
	public InputStream iniciarLeitura(int bank) throws ConnectException, IOException {
		this.keyence.send("LON," + (bank < 10 ? "0" : "") + bank);
		return this.keyence.getStream();
	}
	
	public InputStream iniciarLeitura() throws ConnectException, IOException {
		return iniciarLeitura(1);
	}
	
	public void pararLeitura() throws ConnectException, IOException {
		System.out.println("BEFORE LOFF!");
		this.keyence.send("LOFF");
		System.out.println("AFTER LOFF!");
	}
	
	public void focar() throws ConnectException, IOException {
		this.keyence.send("FTUNE");
	}
	
	public void tunar(int bank) throws ConnectException, IOException {
		this.keyence.send("TUNE," + (bank < 10 ? "0" : "") + bank);
	}
	
	public void tunar() throws ConnectException, IOException {
		tunar(1);
	}
	
	public void pararTunagem() throws ConnectException, IOException {
		this.keyence.send("TQUIT");
	}	
	
	
	
	

	
	public boolean conectado() {
		return this.keyence.isConnected();
	}
	
	public String host() {
		return this.keyence.getHost();
	}
	
	public void host(String host) {
		this.keyence.setHost(host);
	}
	
	public int porta() {
		return this.keyence.getPort();
	}
	
	public void porta(int port) {
		this.keyence.setPort(port);
	}
}
