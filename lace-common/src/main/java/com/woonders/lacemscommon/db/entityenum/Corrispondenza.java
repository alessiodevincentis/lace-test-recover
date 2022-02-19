package com.woonders.lacemscommon.db.entityenum;

public enum Corrispondenza {
	
	RESIDENZA("Residenza"),
	DOMICILIO("Domicilio"),
	ALTRO("Altro");
	
	private final String value;

	private Corrispondenza(String value) {
		this.value = value;
	}  
	
	public String getValue() {
		return value;
	}
	
}
