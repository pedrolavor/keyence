package keyence;

import java.io.BufferedReader;
import java.io.InputStream;

import me.pedrolavor.keyence.Keyence;
import me.pedrolavor.keyence.KeyenceCommand;
import me.pedrolavor.keyence.KeyenceException;

public class KeyenceExecutionTest {

	public static void main(String[] args) throws KeyenceException {
		Keyence keyence = new Keyence("192.168.0.124", 9004);
		keyence.connect();
		System.out.println(keyence.isConnected());
		
		new Thread() {
			int times = 10;
			InputStream buffer;
			
			public void run() {
				try {
					buffer = keyence.listen();
					if(buffer != null) {
						while(times > 0) {
							keyence.execute(KeyenceCommand.SCAN);
							System.out.println(buffer.read());
							times--;
						}						
					}
					
					keyence.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error on reading...");
				}
			}
		}.start();
	}
}
