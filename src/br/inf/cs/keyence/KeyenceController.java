package br.inf.cs.keyence;

import java.io.InputStream;

import br.inf.cs.keyence.Keyence;

public class KeyenceController {
	
	Keyence scanner = null;
	
	public KeyenceController() {
		this.scanner = new Keyence();
	}
	
	public void conectar() throws Exception {
		this.scanner.connect();
	}
	
	public void desconectar() throws Exception {
		this.scanner.close();
	}
	
	public String scanear(int bank) {
		String resposta = null;
		try {
			resposta = this.scanner.processCommand("LON," + (bank < 10 ? "0" : "") + bank);
		} catch (Exception e) {
			System.out.println("Erro ao tentar scanear: " + e.getMessage());
		}
		return resposta;
	}
	
	public String scanear() {
		return scanear(1);
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
	
	public String pararLeitura() {
		String resposta = null;
		try {
			resposta = this.scanner.processCommand("LOFF");
		} catch (Exception e) {
			System.out.println("Erro ao tentar parar leitura: " + e.getMessage());
		}
		return resposta;
	}
	
	public InputStream stream() {
		try {
			return this.scanner.listenStream();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	
	
	

	
	public boolean conectado() {
		return this.scanner.isConnected();
	}	
	
	public void nome(String name) {
		this.scanner.setName(name);
	}
	
	public void host(String host) {
		this.scanner.setHost(host);
	}
	
	public void porta(int port) {
		this.scanner.setPort(port);
	}
}
