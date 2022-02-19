package com.woonders.lacemscommon.db.entityenum;

import java.util.LinkedList;
import java.util.List;

public enum Impiego {

    STATALE("Statale"),
    PARAPUBBLICO("Parapubblico"),
    PENSIONATO("Pensionato"),
    PUBBLICO("Pubblico"),
    PRIVATO("Privato"),
    AUTONOMO("Autonomo"),
    AERONAUTICA("Aeronautica"),
    POLIZIA("Polizia"),
    CARABINIERE("Carabiniere"),
    FINANZA("Finanza"),
    MILITARE("Militare"),
    ALTRO("Altro");

    private final String value;

    private Impiego(final String value) {
        this.value = value;
    }

    public static List<String> getImpiegoListString() {
        final List<String> impieghiList = new LinkedList<>();
        for (final Impiego impiego : Impiego.values()) {
            impieghiList.add(impiego.value);
        }
        return impieghiList;
    }

    public String getValue() {
        return value;
    }

}
