package com.woonders.lacemscommon.db.entityenum;

public enum StatoPatrimoniale {
	
	SEPARAZIONE("Separazione"),
	COMUNIONE("Comunione");

	private final String value;

	private StatoPatrimoniale(String value) {
		this.value = value;
	}  
	
	public String getValue() {
		return value;
	}
}
