package com.woonders.lacemscommon.db.entityenum;

public enum FornitoreLead {

    DEVIS_PROX("DevisProx"), DIRECT_RESPONSE("DirectResponse"), QUINTO_SUBITO("QuintoSubito"),
    LEAD_LEADERS("LeadLeaders"), PAY_CLICK("PayClick"), LEAD_STORE("Lead Store"),
    FACILE_IT("facile.it"), FREEDOM_CONSULT("FreedomConsult"), EDISCOM("Ediscom"), ELEADS("Eleads");

    private final String value;

    FornitoreLead(final String value) {
        this.value = value;
    }

    public static FornitoreLead fromValue(final String value) {
        if (value == null) {
            return null;
        }
        for (final FornitoreLead fornitore : FornitoreLead.values()) {
            if (fornitore.value.equalsIgnoreCase(value)) {
                return fornitore;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value);
    }

    public String getValue() {
        return value;
    }
}
