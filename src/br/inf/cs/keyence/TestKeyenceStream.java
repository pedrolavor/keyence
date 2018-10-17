package br.inf.cs.keyence;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import br.inf.cs.barcode.GS1;

public class TestKeyenceStream {

	public static void main(String[] args) {

		String HOST = "192.168.0.124";
		int PORT = 9004;
		Scanner entrada = new Scanner(System.in);
		Integer comando = 0;
		KeyenceController keyence = new KeyenceController();
		keyence.host(HOST);
		keyence.porta(PORT);

		while (comando != 3) {
			menu(keyence);
			comando = Integer.valueOf(entrada.next());

			switch (comando) {
			case 0:
				try {
					keyence.pararLeitura();
					keyence.desconectar();
					System.exit(0);
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar se desconectar!");
					System.out.println();
				}
				break;
			case 1:
				try {
					keyence.conectar();
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar se conectar!");
					System.out.println();
				}
				break;
			case 2:
				Scanner stream = null;
				try {
					InputStream is = keyence.stream();
					stream = new Scanner(is);
					keyence.scanear();
					while (true) {
						System.out.println(stream.nextLine());
					}
				} catch (Exception e) {
					stream.close();
					System.out.println("Houve um problema ao iniciar stream!");
					System.out.println();
				}
				break;
			case 4:
				try {
					System.out.println(keyence.pararLeitura());
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar parar leitura!");
					System.out.println();
				}
				break;

			default:
				System.out.println("Comando não encontrado!");
				System.out.println();
				break;
			}
			
			entrada.reset();
		}
	}

	public static void menu(KeyenceController keyence) {
		System.out.println("+--------------------+");
		System.out.println("| " + (keyence.conectado() ? "Conectado :)       |" : "Desconectado :(    |"));
		System.out.println("+--------------------+");
		System.out.println("| 1 - Conectar       |");
		System.out.println("| 2 - Iniciar Stream |");
		System.out.println("| 3 - Fechar Stream  |");
		System.out.println("| 3 - Parar Leitura  |");
		System.out.println("| 0 - Desconectar    |");
		System.out.println("+--------------------+");
		System.out.println();
	}
}
