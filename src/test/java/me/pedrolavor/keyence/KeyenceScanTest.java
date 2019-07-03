package me.pedrolavor.keyence;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import me.pedrolavor.keyence.Keyence;
import me.pedrolavor.keyence.exception.KeyenceException;

public class KeyenceScanTest {

	Keyence keyence;
	String host = "192.168.0.124";
	int port = 9004;

	@Before
	public void before() {
		keyence = new Keyence(host, port);
	}
	
	@Test
	public void scan() throws KeyenceException {
		keyence.connect();
		assertTrue("Keyence should be connected", keyence.isConnected());
		
		BufferedReader reader = keyence.listen();
		assertTrue("Keyence should return buffered reader", reader != null);
		
		keyence.execute(KeyenceCommand.SCAN);
		
		new Thread() {
			@Override
			public void run() {
				try {
					String response = null;
					while(response == null) {
						response = reader.readLine();
						System.out.println("resposta do keyence: " + response);
						keyence.execute(KeyenceCommand.END_SCAN);	
						keyence.disconnect();					
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
