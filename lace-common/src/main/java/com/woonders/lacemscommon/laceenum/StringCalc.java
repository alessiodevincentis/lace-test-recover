package com.woonders.lacemscommon.laceenum;

public enum StringCalc {

	DATA("Scadenza"), RATE("Rate"), EURO("Euro"), PERC("Perc");

	private final String value;

	private StringCalc(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
