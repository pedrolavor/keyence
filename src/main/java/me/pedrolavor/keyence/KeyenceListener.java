package me.pedrolavor.keyence;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class KeyenceListener extends Thread {
	
	private Keyence keyence;
	private boolean stopped = false;
	
	public KeyenceListener(Keyence keyence) {
		this.keyence = keyence;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(keyence.listen()));
			while(!stopped) {
				if(!stopped && reader.ready()) {
					String response = reader.readLine();
					System.out.println(response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void close() {
		stopped = true;
		this.stop();
		this.interrupt();
	}
}
