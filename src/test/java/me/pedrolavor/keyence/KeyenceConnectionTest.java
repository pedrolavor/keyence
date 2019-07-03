package me.pedrolavor.keyence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import me.pedrolavor.keyence.Keyence;
import me.pedrolavor.keyence.exception.KeyenceConnectionException;

public class KeyenceConnectionTest {
	
	Keyence keyence;
	String host = "192.168.0.124";
	int port = 9004;
	
	@Test
	public void testConexaoKeyence() {
		Keyence keyence = new Keyence();
		keyence.setHost(host);
		keyence.setPort(port);
		
		try {
			
			keyence.connect();
			assertTrue("Keyence should be connected", keyence.isConnected());
			
			keyence.disconnect();
			assertFalse("Keyence should be disconnect", keyence.isConnected());
			
		} catch (KeyenceConnectionException e) {
			System.out.println(e.getMessage());
		}
	}
}
