package com.woonders.lacemscommon.laceenum;

public enum TipoRinnovo {

    PRATICA("Pratica"), COESISTENZA("Coesistenza");

    private final String value;

    TipoRinnovo(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
