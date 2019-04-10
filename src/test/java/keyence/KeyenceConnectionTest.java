package keyence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import me.pedrolavor.keyence.Keyence;
import me.pedrolavor.keyence.KeyenceException;

public class KeyenceConnectionTest {
	
	Keyence keyence;
	String host = "192.168.0.124";
	int port = 9004;
	
	@Before
	public void before() {
		keyence = new Keyence(host, port);
	}

	@Test
	public void testConnection() throws KeyenceException {
		keyence.connect();
		assertTrue("Should connect successfully.", keyence.isConnected());
	}

	@Test
	public void closeConnection() throws KeyenceException {
		keyence.close();
		assertFalse("Should close connection.", keyence.isConnected());
	}
}
