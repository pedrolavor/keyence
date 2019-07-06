package me.pedrolavor.keyence;

public interface KeyenceEventListener {
	
	public void onStart();

	public void onRead(Object object);
	
	public void onError(Object object);
	
	public void onClose();
}
