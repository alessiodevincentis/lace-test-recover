package com.woonders.lacemscommon.db.entityenum;

/**
 * Created by ema98 on 8/10/2017.
 */
public enum FeeType {

    FIXED("Euro"), PERCENTAGE("%");

    private final String value;

    FeeType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
