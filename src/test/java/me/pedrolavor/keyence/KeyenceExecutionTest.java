package me.pedrolavor.keyence;

import java.io.BufferedReader;

import me.pedrolavor.keyence.Keyence;
import me.pedrolavor.keyence.KeyenceCommand;

public class KeyenceExecutionTest {

	public static void main(String[] args) throws Exception {
		Keyence keyence = new Keyence("192.168.0.124", 9004);
		keyence.connect();
		System.out.println(keyence.isConnected());
		int times = 5;

		BufferedReader buffer;
		try {
			buffer = keyence.listen();
			if(buffer != null) {
				while(times > 0) {
					keyence.execute(KeyenceCommand.SCAN);
					System.out.println(1234);
					System.out.println(buffer.ready());
					System.out.println(buffer.read());
					times--;
				}						
			}
			
			keyence.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on reading...");
		}
	}
}
