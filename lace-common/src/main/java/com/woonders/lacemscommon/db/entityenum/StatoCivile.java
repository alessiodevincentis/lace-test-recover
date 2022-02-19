package com.woonders.lacemscommon.db.entityenum;

public enum StatoCivile {
	
	VEDOVO("Vedovo"),
	CONIUGATO("Coniugato"),
	NUBILE("Nubile"),
	CELIBE("Celibe"),
	DIVORZIATO("Divorziato"),
	SEPARATO("Separato");
	
	private final String value;

	private StatoCivile(String value) {
		this.value = value;
	}  
	
	public String getValue() {
		return value;
	}	
	

}
