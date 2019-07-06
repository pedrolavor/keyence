package me.pedrolavor.keyence.exception;

public class KeyenceConnectionException extends KeyenceException {
	
	public KeyenceConnectionException() {
		super("Error on keyence connection.");
	}
	
	public KeyenceConnectionException(String msg) {
		super(msg);
	}

}
