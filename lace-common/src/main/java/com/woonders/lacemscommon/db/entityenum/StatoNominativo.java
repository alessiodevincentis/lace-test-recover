package com.woonders.lacemscommon.db.entityenum;

import com.woonders.lacemscommon.util.LeadContestatoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum StatoNominativo {

    DA_LAVORARE("Da Lavorare"), IN_LAVORAZIONE("In Lavorazione"), NUMERO_INESISTENTE("Numero Inesistente"),
            DA_RICHIAMARE("Da Richiamare"), NON_RISPONDE("Non Risponde"), ATTESA_DOC("Attesa Documenti"),
            VALUTAZIONE_ATC("Valutazione ATC"), APPUNTAMENTO_FISSATO("Appuntamento Fissato"),
            ATTESA_PREVENTIVO("Attesa Preventivo"), PREVENTIVO("Preventivo Inviato"), RINNOVABILE(
            "Rinnovabile"), NON_ASSUMIBILE("Non Assumibile"), TFR_NO(
            "TFR Insufficiente"), NON_FATTIBILE("Non Fattibile"), NON_INTERESSATO(
            "Non Interessato"), PREV_ACCETTATO("Preventivo Accettato"), CONTESTATO("Nominativo Contestato"),
            CONTESTAZIONE_ACCETTATA("Contestazione Accettata"), CONTESTAZIONE_RIFIUTATA("Contestazione Rifiutata");

    private final String value;

    StatoNominativo(final String value) {
        this.value = value;
    }

    public static StatoNominativo fromValue(final String value) {
        if (value == null) {
            return null;
        }
        for (final StatoNominativo stato : StatoNominativo.values()) {
            if (stato.value.equalsIgnoreCase(value)) {
                return stato;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value);
    }

    public String getValue() {
        return value;
    }

    public boolean isSelectable() {
    	if (this == CONTESTATO || this == CONTESTAZIONE_ACCETTATA || this == CONTESTAZIONE_RIFIUTATA)
    		return false;
    	return true;
    }

    public boolean isSelectable(String actualStato) {
//    	if (actualStato.equalsIgnoreCase(CONTESTATO.getValue()) || 
//    		actualStato.equalsIgnoreCase(CONTESTAZIONE_ACCETTATA.getValue()) || 
//    		actualStato.equalsIgnoreCase(CONTESTAZIONE_RIFIUTATA.getValue()))
//    		return false;
    	return isSelectable();
    }
    
}
