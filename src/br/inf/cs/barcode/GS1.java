package br.inf.cs.barcode;

import br.inf.cs.util.ASCII;

public class GS1 {
	private Long GTIN;
	private Long NHRN;
	private String SN;
	private Integer ExpirationDate;
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

		System.out.println(sb.toString());
		return sb.toString();
	}

	public void decode(String encoded) throws NumberFormatException {
		String[] fields = encoded.split(Character.toString(ASCII.GS));

		if (fields.length == 5) {
			this.setGTIN(Long.valueOf(fields[0]));
			this.setNHRN(Long.valueOf(fields[1]));
			this.setSN(fields[2]);
			this.setExpirationDate(Integer.valueOf(fields[3]));
			this.setLot(fields[4]);

		} else {
			throw new IllegalArgumentException("O número de parametros recebidos é maior que o permitido.");
		}
	}

	public static void main(String[] args) {
		GS1 gs1 = new GS1();
		gs1.setGTIN(7898157722141L);
		gs1.setNHRN(10369460061L);
		gs1.setSN("0");
		gs1.setExpirationDate(201219);
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

	public Long getGTIN() {
		return GTIN;
	}

	public void setGTIN(Long gTIN) {
		GTIN = gTIN;
	}

	public Long getNHRN() {
		return NHRN;
	}

	public void setNHRN(Long nHRN) {
		NHRN = nHRN;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public Integer getExpirationDate() {
		return ExpirationDate;
	}

	public void setExpirationDate(Integer expirationDate) {
		ExpirationDate = expirationDate;
	}

	public String getLot() {
		return Lot;
	}

	public void setLot(String lot) {
		Lot = lot;
	}
}
