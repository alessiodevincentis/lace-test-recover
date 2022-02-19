package com.woonders.lacemscommon.db.entityenum;

public enum AccountVersion {

    DEMO("Demo", 0),
    BASIC("Basic", 3),
    STANDARD("Standard", 6),
    PROFESSIONAL("Professional", 10),
    ENTERPRISE("Enterprise", 15);

    private final String name;
    private final int value;

    AccountVersion(final String name, final int value) {
        this.name = name;
        this.value = value;
    }

    public static AccountVersion fromName(final String name) {
        if (name == null) {
            return null;
        }
        for (final AccountVersion accountVersion : AccountVersion.values()) {
            if (accountVersion.toString().equalsIgnoreCase(name)) {
                return accountVersion;
            }
        }
        throw new IllegalArgumentException("No constant with name " + name);
    }

    public static AccountVersion fromValue(final int value) {
        for (final AccountVersion accountVersion : AccountVersion.values()) {
            if (accountVersion.value == value) {
                return accountVersion;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
