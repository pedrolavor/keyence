package keyence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import me.pedrolavor.keyence.Keyence;
import me.pedrolavor.keyence.KeyenceCommand;
import me.pedrolavor.keyence.KeyenceException;

public class KeyenceScanTest {

	Keyence keyence;
	String host = "192.168.0.124";
	int port = 9004;
	
	@Test
	public void scan() throws KeyenceException {
		keyence = new Keyence(host, port);
		keyence.connect();
		new Thread() {
			int times = 10;
			InputStream buffer;
			
			public void run() {
				try {
					buffer = keyence.listen();
					while(times > 0) {
						keyence.execute(KeyenceCommand.SCAN);
						System.out.println(buffer.read());
						times--;
					}
				} catch (Exception e) {
					System.out.println("Error on reading...");
				}
				
			}
		}.start();
		
		assertEquals(0, 0);
	}
	
	@After
	public void after() throws KeyenceException {
		keyence.close();
	}
}
