package com.woonders.lacemscommon.db.entityenum;

public enum PreferenzaNumeroMittente {
	FISSO("Utilizza il Telefono Fisso come mittente"), CELLULARE("Utilizza il Cellulare come mittente");

	private final String value;

	public static PreferenzaNumeroMittente fromValue(final String value) {
		if (value == null) {
			return null;
		}
		for (PreferenzaNumeroMittente preferenzaNumeroMittente : PreferenzaNumeroMittente.values()) {
			if (preferenzaNumeroMittente.value.equalsIgnoreCase(value)) {
				return preferenzaNumeroMittente;
			}
		}
		throw new IllegalArgumentException("No constant with value " + value);
	}

	PreferenzaNumeroMittente(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
