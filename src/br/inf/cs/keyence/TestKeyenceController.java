package br.inf.cs.keyence;

import java.util.Scanner;

import br.inf.cs.barcode.GS1;

public class TestKeyenceController {

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
				try {
					GS1 gs1 = new GS1();
					gs1.decode(keyence.scanear());
					System.out.println();
					System.out.println("+----------------");
					System.out.println("- GTIN    : " + gs1.getGTIN());
					System.out.println("- NHRN    : " + gs1.getNHRN());
					System.out.println("- Serial  : " + gs1.getSN());
					System.out.println("- Validade: " + gs1.getExpirationDate());
					System.out.println("- Lote    : " + gs1.getLot());
					System.out.println("+----------------");
					System.out.println();
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar scanear código!");
					System.out.println();
				}
				break;
			case 3:
				try {
					System.out.println(keyence.focar());
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar focar o scanner!");
					System.out.println();
				}
				break;
			case 4:
				try {
					System.out.println(keyence.tunar());
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar realizar o tuning!");
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
		System.out.println("+-----------------+");
		System.out.println("| " + (keyence.conectado() ? "Conectado :)    |" : "Desconectado :( |"));
		System.out.println("+-----------------+");
		System.out.println("| 1 - Conectar    |");
		System.out.println("| 2 - Scanear     |");
		System.out.println("| 3 - AutoFocus   |");
		System.out.println("| 4 - Tuning      |");
		System.out.println("| 0 - Desconectar |");
		System.out.println("+-----------------+");
		System.out.println();
	}
}
