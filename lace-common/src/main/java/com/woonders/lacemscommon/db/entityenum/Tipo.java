package com.woonders.lacemscommon.db.entityenum;

public enum Tipo {
	CLIENTE("Cliente"),
	NOMINATIVO("Nominativo");
	
	private final String value;

	private Tipo(String value) {
		this.value = value;
	}  
	
	public String getValue() {
		return value;
	}	

}
