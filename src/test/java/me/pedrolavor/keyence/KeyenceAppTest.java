package me.pedrolavor.keyence;

import me.pedrolavor.keyence.Keyence;
import me.pedrolavor.keyence.KeyenceCommand;

public class KeyenceAppTest {

	public static void main(String[] args) throws Exception {
		String host = "192.168.0.124";
		int port = 9004;
		Keyence keyence = new Keyence(host, port);
		
		keyence.connect();
		System.out.println("Connected: " + keyence.isConnected());
		
		KeyenceListener listener = new KeyenceListener(keyence);
		listener.start();

		keyence.execute(KeyenceCommand.SCAN);
		
		Thread.sleep(5000);
		keyence.execute(KeyenceCommand.END_SCAN);
		listener.close();
		keyence.disconnect();
	}
}
