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
			scanner.connect();
			for(int i = 0; i < 10; i++) {
				System.out.println(scanner.processCommand("LON"));
				Thread.sleep(3000);
			}
			scanner.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
