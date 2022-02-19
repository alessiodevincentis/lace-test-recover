package com.woonders.lacemscommon.db.entityenum;

public enum CatPensioni {
	
	VO("VO"),
	VCOM("VCOM"),
	VL("VL"),
	VDAI("VDAI"),
	SOCPDEL("SOCPDEL"),
	ET("ET"),
	SO_VO("SO/VO");
	
	private final String value;

	private CatPensioni(String value) {
		this.value = value;
	}  
	
	public String getValue() {
		return value;
	}	
	

}
