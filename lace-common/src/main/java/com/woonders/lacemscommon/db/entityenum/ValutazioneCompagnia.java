package com.woonders.lacemscommon.db.entityenum;

public enum ValutazioneCompagnia {
	
	NON_ASS("Non Assumibile"),
	UNOUNO("1/1"),
	UNODUE("1/2"),
	UNOTRE("1/3"),
	UNOQUATTRO("1/4"),
	UNOCINQUE("1/5"),
	STATALE("Statale"),
	PUBBLICO("Pubblico"),
	PRIVATO("Privato"),
	PARAPUBBLICO("Parapubblico");
	
	private final String value;

	private ValutazioneCompagnia(String value) {
		this.value = value;
	}  
	
	public String getValue() {
		return value;
	}	

}
