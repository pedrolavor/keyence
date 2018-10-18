package br.inf.cs.keyence;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;

import br.inf.cs.keyence.Keyence;

public class KeyenceController {
	
	Keyence keyence = null;
	
	public KeyenceController() {
		this.keyence = new Keyence();
	}
	
	public void conectar() throws UnknownHostException, ConnectException, IOException {
		this.keyence.connect();
	}
	
	public void desconectar() throws ConnectException, IOException {
		this.keyence.close();
	}
	
	public InputStream iniciarLeitura(int bank) throws ConnectException, IOException {
		this.keyence.send("LON," + (bank < 10 ? "0" : "") + bank);
		return this.keyence.getStream();
	}
	
	public InputStream iniciarLeitura() throws ConnectException, IOException {
		return iniciarLeitura(1);
	}
	
	public void pararLeitura() throws ConnectException, IOException {
		this.keyence.send("LOFF");
	}
	
	public String focar() {
		String resposta = null;
		try {
			resposta = this.keyence.processCommand("FTUNE");
		} catch (Exception e) {
			System.out.println("Erro ao tentar focar: " + e.getMessage());
		}
		return resposta;
	}
	
	public String tunar(int bank) {
		String resposta = null;
		try {
			resposta = this.keyence.processCommand("TUNE," + (bank < 10 ? "0" : "") + bank);
		} catch (Exception e) {
			System.out.println("Erro ao tentar tunar: " + e.getMessage());
		}
		return resposta;
	}
	
	public String tunar() {
		return tunar(1);
	}
	
	public String pararTunagem() {
		String resposta = null;
		try {
			resposta = this.keyence.processCommand("TQUIT");
		} catch (Exception e) {
			System.out.println("Erro ao tentar scanear: " + e.getMessage());
		}
		return resposta;
	}	
	
	
	
	

	
	public boolean conectado() {
		return this.keyence.isConnected();
	}
	
	public String nome() {
		return this.keyence.getName();
	}
	
	public void nome(String name) {
		this.keyence.setName(name);
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
