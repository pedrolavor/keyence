package br.inf.cs.keyence;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TesteSocket {

	public static void main(String[] args) throws IOException {
		
		int PORT = 3000;
		ServerSocket server = new ServerSocket(PORT);
		System.out.println("Porta aberta: " + PORT);
		
		Socket client = server.accept();
		System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());
		
		InputStream in = client.getInputStream();
		Scanner scanner = new Scanner(in);
		
		while(scanner.hasNextLine()) {
			System.out.println(in.read());
			System.out.println(scanner.nextLine());
		}
		
		scanner.close();
		client.close();
		server.close();
		System.out.println("Server closed!");
	}
}
