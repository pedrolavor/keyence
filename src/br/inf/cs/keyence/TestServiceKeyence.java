package br.inf.cs.keyence;

public class TestServiceKeyence {

	public static void main(String[] args) {
		
		String HOST = "192.168.0.124";
		int PORT = 9004;
		
		Keyence scanner = new Keyence();
		
		scanner.setHost(HOST);
		scanner.setPort(PORT);
		scanner.setName("KEYENCE SR-1000");

		try {
			scanner.configure();
			scanner.connect();
//			scanner.processCommand("LON");
//			Thread.sleep(5000);
			scanner.processCommand("LOFF");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
