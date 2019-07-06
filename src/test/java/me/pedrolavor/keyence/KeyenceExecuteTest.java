package me.pedrolavor.keyence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import me.pedrolavor.keyence.Keyence;

public class KeyenceExecuteTest {

	Keyence keyence;
	String host = "192.168.0.124";
	int port = 9004;

	@Before
	public void before() {
		keyence = new Keyence(host, port);
	}
	
	@Test
	public void scan() throws Exception {
		try {
			keyence.connect();
			assertTrue("Keyence should be connected", keyence.isConnected());
			
			InputStream reader = keyence.listen();
			assertTrue("Keyence should return buffered reader", reader != null);
			
			boolean c = keyence.execute(KeyenceCommand.SCAN);
			assertFalse("LON command should have none error", c);
			
			Thread.sleep(1000);
			
			c = keyence.execute(KeyenceCommand.END_SCAN);
			assertFalse("LOFF command should have none error", c);

			keyence.disconnect();
			assertFalse("Keyence should be disconnected", keyence.isConnected());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
