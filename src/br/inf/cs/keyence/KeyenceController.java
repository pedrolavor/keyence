package br.inf.cs.keyence;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;

import br.inf.cs.keyence.Keyence;

public class KeyenceController {
	
	Keyence scanner = null;
	
	public KeyenceController() {
		this.scanner = new Keyence();
	}
	
	public void conectar() throws UnknownHostException, ConnectException, IOException {
		this.scanner.connect();
	}
	
	public void desconectar() throws ConnectException, IOException {
		this.scanner.close();
	}
	
	public InputStream iniciarLeitura(int bank) throws ConnectException, IOException {
		this.scanner.send("LON," + (bank < 10 ? "0" : "") + bank);
		return this.scanner.getStream();
	}
	
	public InputStream iniciarLeitura() throws ConnectException, IOException {
		return iniciarLeitura(1);
	}
	
	public void pararLeitura() throws ConnectException, IOException {
		this.scanner.send("LOFF");
	}
	
	public String focar() {
		String resposta = null;
		try {
			resposta = this.scanner.processCommand("FTUNE");
		} catch (Exception e) {
			System.out.println("Erro ao tentar focar: " + e.getMessage());
		}
		return resposta;
	}
	
	public String tunar(int bank) {
		String resposta = null;
		try {
			resposta = this.scanner.processCommand("TUNE," + (bank < 10 ? "0" : "") + bank);
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
			resposta = this.scanner.processCommand("TQUIT");
		} catch (Exception e) {
			System.out.println("Erro ao tentar scanear: " + e.getMessage());
		}
		return resposta;
	}	
	
	
	
	

	
	public boolean conectado() {
		return this.scanner.isConnected();
	}
	
	public String nome() {
		return this.scanner.getName();
	}
	
	public void nome(String name) {
		this.scanner.setName(name);
	}
	
	public String host() {
		return this.scanner.getHost();
	}
	
	public void host(String host) {
		this.scanner.setHost(host);
	}
	
	public int porta() {
		return this.scanner.getPort();
	}
	
	public void porta(int port) {
		this.scanner.setPort(port);
	}
}
