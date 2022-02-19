package com.woonders.lacemscommon.db.entityenum;

public enum Assicurazioni {

	NET("NetInsurance"),
	VITTORIA("Vittoria"),
	HDI("HDI Assicurazione"),
	AXA("AXA"),
	CATTOLICA("Cattolica"),
	ERGO("Ergo"),
	METLIFE("MetLife"),
	GAIL("Gaiil-MetLife"),
	CARDIF("Cardif"),
	CREDIT("Creditlife"),
	AVIVA("Aviva");
	
	private final String value;

	private Assicurazioni(String value) {
		this.value = value;
	}  
	
	public String getValue() {
		return value;
	}	
	
	
}
