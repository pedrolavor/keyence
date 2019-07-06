package me.pedrolavor.keyence;

public enum KeyenceCommand {

	SCAN("LON"),
	END_SCAN("LOFF"),
	FOCUS("FTUNE"),
	TUNE("TUNE"),
	END_TUNE("TQUIT"),
	RATE_TEST("TEST1"),
	TIME_TEST("TEST2"),
	END_TEST("QUIT"),
	RESET("RESET"),
	POINTER_ON("AMON"),
	POINTER_OFF("AMOFF"),
	PRESET("PRON"),
	END_PRESET("PROFF"),
	CODE("RCON"),
	END_CODE("RCOFF"),
	GET_CODE("RCCHK"),
	CLEAN_BUFF("BCLR"),
	VERSION("KEYENCE"),
	MAC("EMAC");
	
	private String command;
	
	KeyenceCommand(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
}
