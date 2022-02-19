package com.woonders.lacemscommon.laceenum;

public enum FiltroRinnoviPraticaCoesistenza {

	ALL_RINNOVI("Pratiche/Coesistenze Rinnovabili"), RINNOVI_PRATICA("Solo Rinnovi Pratica"), RINNOVI_COESISTENZA(
			"Solo Rinnovi Coesistenza");

	private final String value;

	private FiltroRinnoviPraticaCoesistenza(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
