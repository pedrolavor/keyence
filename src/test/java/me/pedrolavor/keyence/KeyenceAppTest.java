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
		
		keyence.startListener(new KeyenceEventListener() {			
			@Override
			public void onStart() {}
			@Override
			public void onRead(Object object) {System.out.println(object.toString());}
			@Override
			public void onError(Object object) {}
			@Override
			public void onClose() {}
		});

		keyence.execute(KeyenceCommand.SCAN);
		
		Thread.sleep(5000);
		keyence.execute(KeyenceCommand.END_SCAN);
		keyence.stopListener();
		keyence.disconnect();
	}
}
