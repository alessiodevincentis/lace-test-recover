package com.woonders.lacemscommon.db.entityenum;

public enum Provenienza {

	AGENZIA("Agenzia"), WEB("Web"), COLLABORATORE("Collaboratore"), LEAD("Lead"), BANCA("Banca"), CAMPAGNA_DA_FILE(
			"Campagna da file"), FACEBOOK("Facebook");

	private final String value;

	private Provenienza(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
