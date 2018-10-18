package br.inf.cs.keyence;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Scanner;

import br.inf.cs.barcode.GS1;

public class TestKeyenceController {

	public static void main(String[] args) {

		String HOST = "192.168.0.124";
		int PORT = 9004;
		Scanner entrada = new Scanner(System.in);
		Integer comando = 0;
		KeyenceController keyence = new KeyenceController();
		EscutadorSocket escutarSocket = null;
		Scanner socketStream = null;
		keyence.host(HOST);
		keyence.porta(PORT);

		while (comando != -1) {
			menu(keyence);
			comando = Integer.valueOf(entrada.next());

			switch (comando) {
			case 1:
				try {
					keyence.conectar();
					System.out.println("Conexão realizada com sucesso!");
				} catch (UnknownHostException ex) {
					System.out.println("Scanner não encontrado ou host não existe nesta rede!");
					System.out.println();
				} catch (ConnectException ex) {
					System.out.println("Falha ao tentar conectar ao scanner! Verifique se outro cliente já está conectado.");
					System.out.println();
				} catch (IOException ix) {
					System.out.println("Falha de leitura de conexão do scanner!");
					System.out.println();
				}
				break;
			case 2:
				try {
					if(escutarSocket != null) escutarSocket.fechar();
					keyence.pararLeitura();
					keyence.desconectar();
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar se desconectar!");
					e.printStackTrace();
					System.out.println();
				}
				break;
			case 3:
				try {
					if(escutarSocket != null) escutarSocket.fechar();
					InputStream is = keyence.iniciarLeitura();
					escutarSocket = new EscutadorSocket(is);
					escutarSocket.start();
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar realizar leitura!");
					e.printStackTrace();
					System.out.println();
				}
				break;
			case 4:
				System.out.println("Parando leitura...");
				try {
					if(escutarSocket != null) escutarSocket.fechar();
					keyence.pararLeitura();
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar parar leitura!");
					e.printStackTrace();
					System.out.println();
				}
				break;
			case 5:
				try {
					System.out.println(keyence.focar());
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar focar o scanner!");
					e.printStackTrace();
					System.out.println();
				}
				break;
			case 6:
				try {
					System.out.println(keyence.tunar());
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar realizar o tuning!");
					e.printStackTrace();
					System.out.println();
				}
				break;
			case 7:
				try {
					if(escutarSocket != null) escutarSocket.fechar();
					keyence.pararLeitura();
					keyence.desconectar();
					System.exit(0);
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar sair do programa! Error: " + e.getMessage());
					e.printStackTrace();
					System.out.println();
				}
				break;

			default:
				System.out.println("Comando não encontrado!");
				System.out.println();
				break;
			}
		}
	}

	public static void menu(KeyenceController keyence) {
		System.out.println("+---------------------+");
		System.out.println("| " + (keyence.conectado() ? "Conectado :)        |" : "Desconectado :(     |"));
		System.out.println("+---------------------+");
		System.out.println("| 1 - Conectar        |");
		System.out.println("| 2 - Desconectar     |");
		System.out.println("| 3 - Iniciar Leitura |");
		System.out.println("| 4 - Parar Leitura   |");
		System.out.println("| 5 - Auto Focus      |");
		System.out.println("| 6 - Tuning          |");
		System.out.println("| 7 - Sair            |");
		System.out.println("+---------------------+");
		System.out.println();
	}
}

class EscutadorSocket extends Thread {
	InputStream stream;
	Scanner scanner;
	boolean aberto;
	
	public EscutadorSocket(InputStream stream) {
		this.stream = stream;
		aberto = true;
	}
	
	public void fechar() {
		this.aberto = false;
	}
	
	@Override
	public void run() {
		scanner = new Scanner(this.stream);
		
		while(this.aberto) {
			String s = scanner.nextLine();
			if(this.aberto) System.out.println(s);
		}

		System.out.println("Escutador finalizado!");
	}
}
