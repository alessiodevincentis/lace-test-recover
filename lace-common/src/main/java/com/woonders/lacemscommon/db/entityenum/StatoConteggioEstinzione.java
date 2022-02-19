package com.woonders.lacemscommon.db.entityenum;

public enum StatoConteggioEstinzione {

    RICHIESTA_CONTEGGIO("Richiesta Conteggio"),
    ATTESA_CONTEGGIO("Attesa Conteggio"),
    CONTEGGIO_RICEVUTO("Conteggio Ricevuto");

    private final String value;

    private StatoConteggioEstinzione(final String value) {
        this.value = value;
    }

    public static StatoConteggioEstinzione fromValue(final String value) {
        if (value == null) {
            return null;
        }
        for (final StatoConteggioEstinzione stato : StatoConteggioEstinzione.values()) {
            if (stato.value.equalsIgnoreCase(value)) {
                return stato;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value);
    }

    public String getValue() {
        return value;
    }
}
