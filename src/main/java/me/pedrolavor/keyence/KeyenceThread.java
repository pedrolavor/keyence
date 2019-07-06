package me.pedrolavor.keyence;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class KeyenceThread extends Thread {
	
	private Keyence keyence;
	private KeyenceEventListener eventListener;
	private boolean stopped = false;
	
	public KeyenceThread(Keyence keyence, KeyenceEventListener eventListener) {
		this.keyence = keyence;
		this.eventListener = eventListener;
	}

	@Override
	public void run() {
		eventListener.onStart();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(keyence.listen()));
			while(!stopped) {
				if(!stopped && reader.ready()) {
					String response = reader.readLine();
					eventListener.onRead(response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			eventListener.onError(e);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void close() {
		stopped = true;
		this.stop();
		this.interrupt();
		eventListener.onClose();
	}
}
