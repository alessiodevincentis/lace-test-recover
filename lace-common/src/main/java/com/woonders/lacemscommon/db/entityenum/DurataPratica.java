package com.woonders.lacemscommon.db.entityenum;

public enum DurataPratica {
	
	DURATA_120(120),
	DURATA_108(108),
	DURATA_96(96),
	DURATA_84(84),
	DURATA_72(72),
	DURATA_64(60),
	DURATA_48(48),
	DURATA_36(36),
	DURATA_24(24),
	DURATA_18(18),
	DURATA_12(12);
	

	private final Integer value;

	private DurataPratica(Integer value) {
		this.value = value;
	}  
	
	public Integer getValue() {
		return value;
	}	
	

}
