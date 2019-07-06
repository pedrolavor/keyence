package br.inf.cs.keyence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;

public class TestKeyenceSocket {

	public static void main(String[] args) {
		
		String HOST = "192.168.0.124";
		int PORT = 9004;
		
		Socket socket;
		
		BufferedWriter output;
		BufferedReader input;
		BufferedWriter b;
		Writer writer;
		try {
			socket = new Socket(HOST, PORT);
			System.out.println("Conexão estabelecida!");
			
//			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream print = new PrintStream(socket.getOutputStream());
			
			String comando;
			Scanner scanner = new Scanner(System.in);
			Escutador escutador = null;
			b = new BufferedWriter(new FileWriter("keyence.txt"));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("keyence.txt"), "utf-8"));
			
			do {
				System.out.println(" 1 - LIGAR CAMERA");
				System.out.println(" 2 - DESLIGAR CAMERA");
				System.out.println();
				
				comando = scanner.nextLine();
				System.out.println(comando);
				
				if(comando.equals("1")) {
					System.out.println("LIGAR CAMERA!");
					print.println("LON");
					
					escutador = new Escutador(input, writer);
					escutador.start();
					
				} else if(comando.equals("2")) {
					System.out.println("DESLIGAR CAMERA!");
					if(escutador != null) escutador.interrupt();
					print.println("LOFF");
					b.close();
					writer.close();
				}
				
			} while(comando != "0");

			System.out.println("FECHANDO SOCKET!");
			socket.close();
			scanner.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

class Escutador extends Thread {
	
	BufferedReader input;
	BufferedWriter b;
	Writer writer;
	
	public Escutador(BufferedReader input, Writer writer) {
		this.input = input;
		this.writer = writer;
	}
	
	@Override
	public void run() {
		System.out.println("ESCUTANDO CAMERA:");
		try {
			while(true) {
				String s = input.readLine();
				System.out.println(s);

				writer.write(s + "\r\n");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
