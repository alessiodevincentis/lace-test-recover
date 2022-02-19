package com.woonders.lacemscommon.laceenum;

public enum GraficoEnum {

    MONTANTE_PERFEZIONATO("Montante pratiche perfezionate"), MONTANTE("Montante Cessioni/Deleghe"),
    EROGATO("Erogato Prestiti");

    private final String value;

    GraficoEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
