package me.pedrolavor.keyence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SocketTest {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("192.168.0.124", 9004);
		System.out.println("connected");
		
		PrintStream print = new PrintStream(socket.getOutputStream());
		print.println("LON\r");
		
		
		BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		
		System.out.println(is.readLine());
		
		print.println("LOFF\r");
		Thread.sleep(100);
		
		socket.close();
	}
}
