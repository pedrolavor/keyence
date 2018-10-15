package keyence;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.sql.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class TesteKeyence {

	public static void main(String[] args) {
		
//		try {
//			String host = "192.168.0.124";
//			int commandPort = 9004;
//			int socketTimeOut = 2000;
//
//		    Charset charset = Charset.forName("US-ASCII");
//		    CharsetEncoder encoder = charset.newEncoder();
//		    CharsetDecoder decoder = charset.newDecoder();
//
//			ByteBuffer bb = ByteBuffer.allocate(4096);
//			
//			SocketChannel socketChannel;
//			
//			InetSocketAddress inetAddr = new InetSocketAddress(host, commandPort);
//			socketChannel = SocketChannel.open();
//			socketChannel.connect(inetAddr);
//
//			socketChannel.socket().setSoTimeout(socketTimeOut);
//			socketChannel.configureBlocking(true);
//			socketChannel.finishConnect();
//			
//			
//			
//			
//			
//			
//			
//			
//
//			String command = "LOFF" + '\r';
//			System.out.println(command);
//			String reply = null;
//			socketChannel.write(encoder.encode(CharBuffer.wrap(command)));
//			bb.clear();
//			socketChannel.read(bb);
//			bb.flip();
//			reply = decoder.decode(bb).toString();
//			System.out.println(reply);
//			
//			
//			
//			
//			
//			
//			
//		} catch (Exception e) {
//			System.out.println("Erro: " + e.getMessage());
//			e.printStackTrace();
//		}
		
		
		try {
			Socket cliente = new Socket("192.168.0.124", 9004);
			System.out.println("Conexão estabelecida!");
			
			OutputStream out = cliente.getOutputStream();
			PrintStream print = new PrintStream(out);
			Scanner input = new Scanner(cliente.getInputStream());
			
			String command = "LOFF";
//			out.write(command.getBytes("US-ASCII"));
			print.println(command);
//			print.println(command.getBytes("US-ASCII"));
			while(true) {
				System.out.println(input.nextLine());
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
			e.printStackTrace();
		}

	}
}
