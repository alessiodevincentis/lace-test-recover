package com.woonders.lacemscommon.db.entityenum;

public enum ProvenienzaDesc {
	
	AGENZIA("Agenzia"), WEB("Web"), COLLABORATORE("Collaboratore"), LEAD("Lead"), BANCA("Banca"), CAMPAGNA_DA_FILE(
			"Campagna da file"), FACEBOOK("Facebook"), SMARTIKA("Smartika");

	private final String value;

	private ProvenienzaDesc(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
