package com.woonders.lacemscommon.db.entityenum;

/**
 * Created by Emanuele on 11/02/2017.
 */
public enum RicaricaType {

	SMS_MARKETING("SMS marketing"), SMS_AGENZIA("SMS agenzia");

	private final String value;

	private RicaricaType(String value) {
		this.value = value;
	}

	public static RicaricaType fromValue(final String value) {
		if (value == null) {
			return null;
		}
		for (RicaricaType ricaricaType : RicaricaType.values()) {
			if (ricaricaType.value.equalsIgnoreCase(value)) {
				return ricaricaType;
			}
		}
		throw new IllegalArgumentException("No constant with value " + value);
	}

	public String getValue() {
		return value;
	}
}
