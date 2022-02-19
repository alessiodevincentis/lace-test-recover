package com.woonders.lacemscommon.db.entityenum;

public enum TipoAbitazione {
	
	PROPRIETA("Proprieta'"),
	AFFITTO("Affitto"),
	PARENTI("Parenti"),
	TERZI("Terzi");
	
	private final String value;

	private TipoAbitazione(String value) {
		this.value = value;
	}  
	
	public String getValue() {
		return value;
	}
	
}
