package me.pedrolavor.keyence;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class KeyenceTest {
	
	String host = "192.168.0.124";
	int port = 9004;
	String name = "My Keyence";

	@Test
	public void testSetKeyence() {
		Keyence keyence = new Keyence();
		
		keyence.setHost(host);
		keyence.setPort(port);
		keyence.setName(name);
		
		assertTrue("Keyence host should be " + host, host.equals(keyence.getHost()));
		assertTrue("Keyence port should be " + port, Integer.valueOf(port).equals(keyence.getPort()));
		assertTrue("Keyence name should be " + name, name.equals(keyence.getName()));
	}
}
