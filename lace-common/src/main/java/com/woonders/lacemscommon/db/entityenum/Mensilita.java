package com.woonders.lacemscommon.db.entityenum;

public enum Mensilita {

    MENSILITA_12(12),
    MENSILITA_6(6),
    MENSILITA_3(3),
    MENSILITA_2(2),
    MENSILITA_1(1);


    private final Integer value;

    Mensilita(final Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
