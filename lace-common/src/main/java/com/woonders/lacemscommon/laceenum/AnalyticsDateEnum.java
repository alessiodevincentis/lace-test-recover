package com.woonders.lacemscommon.laceenum;

public enum AnalyticsDateEnum {

    LAST_30("Ultimi 30 giorni"), CURRENT_MONTH("Mese corrente"), LAST_60("Ultimi 60 giorni"), LAST_90("Ultimi 90 giorni"), CURRENT_YEAR("Anno corrente"), LAST_YEAR("Scorso anno");

    private final String value;

    AnalyticsDateEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
