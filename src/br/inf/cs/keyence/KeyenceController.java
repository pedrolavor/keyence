package br.inf.cs.keyence;

public class KeyenceController {
	
	Keyence scanner;
	
	public KeyenceController() {
		this.scanner = new Keyence();
	}

	public void conectar() throws Exception {
		this.scanner.connect();
	}
	
	public void desconectar() throws Exception {
		this.scanner.close();
	}
	
	public void iniciar() {
		
	}
	
	public void parar() {
		
	}
	
	public void comando(String command) throws Exception {
		this.scanner.processCommand(command);
	}
	
	public void testar() throws Exception {
		if(this.scanner.isConnected()) {
			this.comando("TEST1");
			Thread.sleep(3000);
			this.comando("QUIT");
			System.out.println("Teste realizado com sucesso!");
		} else {
			throw new Exception("O scanner não está conectado!");
		}
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
