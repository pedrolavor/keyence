package br.inf.cs.barcode;

import br.inf.cs.util.ASCII;

public class GS1 {
	private String GTIN;
	private String NHRN;
	private String SN;
	private String ExpirationDate;
	private String Lot;

	public String encode() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getGTIN());
		sb.append(ASCII.GS);
		sb.append(this.getNHRN());
		sb.append(ASCII.GS);
		sb.append(this.getSN());
		sb.append(ASCII.GS);
		sb.append(this.getExpirationDate());
		sb.append(ASCII.GS);
		sb.append(this.getLot());

		// System.out.println(sb.toString());
		return sb.toString();
	}

	public void decode(String encoded) throws NumberFormatException {
		String[] fields = encoded.split(Character.toString(ASCII.GS));

		if (fields.length == 5) {
			this.setGTIN(fields[0]);
			this.setNHRN(fields[1]);
			this.setSN(fields[2]);
			this.setExpirationDate(fields[3]);
			this.setLot(fields[4]);

		} else {
			if (fields.length == 3) {
				String s1 = fields[0];
				String s2 = fields[1];
				String s3 = fields[2];
				this.setGTIN(s1.substring(3, 16));
				this.setNHRN(s1.substring(19, (s1.length()) ));
				this.setSN(s2.substring(2, (s2.length()) ));
				this.setExpirationDate(s3.substring(2, 8 ));
				this.setLot(s3.substring(10, (s3.length()) ));
			} else {
				throw new IllegalArgumentException("O número de parametros recebidos é maior que o permitido.");
			}
		}
	}

	public static void main(String[] args) {
		GS1 gs1 = new GS1();
		gs1.setGTIN("7898157722141");
		gs1.setNHRN("10369460061");
		gs1.setSN("0");
		gs1.setExpirationDate("201219");
		gs1.setLot("C18006");

		String sTest = "7898157722141103694600610201219C18006";

		try {
			gs1.decode(gs1.encode());

			System.out.println(gs1.getGTIN());
			System.out.println(gs1.getNHRN());
			System.out.println(gs1.getSN());
			System.out.println(gs1.getExpirationDate());
			System.out.println(gs1.getLot());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String getGTIN() {
		return GTIN;
	}

	public void setGTIN(String gTIN) {
		GTIN = gTIN;
	}

	public String getNHRN() {
		return NHRN;
	}

	public void setNHRN(String nHRN) {
		NHRN = nHRN;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getExpirationDate() {
		return ExpirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		ExpirationDate = expirationDate;
	}

	public String getLot() {
		return Lot;
	}

	public void setLot(String lot) {
		Lot = lot;
	}
}
