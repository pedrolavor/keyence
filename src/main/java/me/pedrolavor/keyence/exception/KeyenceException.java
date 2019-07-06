package me.pedrolavor.keyence.exception;

public class KeyenceException extends Exception {
	
	public KeyenceException() {
		super("Keyence Error");
	}
	
	public KeyenceException(String msg) {
		super(msg);
	}

}
