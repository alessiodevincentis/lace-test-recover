package com.woonders.lacemscommon.db.entityenum;

public enum EsitoSmsNotificaLead {

	IN_SPEDIZIONE("In spedizione"), SPEDITO("Spedito, in attesa di ricevuta di consegna"), NON_SPEDITO(
			"Non spedito - Errore di spedizione"), RICEVUTO("Ricevuto dal destinatario"), NON_RICEVUTO(
					"Non ricevuto dal destinatario"), NON_SPEDITO_CREDITO_INSUFFICIENTE(
							"Non spedito - credito insufficiente"), SPEDIZIONE_DISABILITATA(
									"Spedizione disabilitata"), NON_SPEDITO_DESTINATARIO_NON_VALIDO(
											"Non spedito - Destinatario non valido"), DA_SPEDIRE(
													"Pronto per essere spedito");

	private final String value;

	private EsitoSmsNotificaLead(String value) {
		this.value = value;
	}

	public static EsitoSmsNotificaLead fromValue(final String value) {
		if (value == null) {
			return null;
		}
		for (EsitoSmsNotificaLead esitoSmsValue : EsitoSmsNotificaLead.values()) {
			if (esitoSmsValue.value.equalsIgnoreCase(value)) {
				return esitoSmsValue;
			}
		}
		throw new IllegalArgumentException("No constant with value " + value);
	}

	public static EsitoSmsNotificaLead fromClickSendStatusCode(final int value) {
		switch (value) {
		case 200:
			return EsitoSmsNotificaLead.SPEDITO;
		case 201:
			return EsitoSmsNotificaLead.RICEVUTO;
		case 300:
			return EsitoSmsNotificaLead.NON_RICEVUTO;
		case 301:
			return EsitoSmsNotificaLead.NON_RICEVUTO;
		case 302:
			return EsitoSmsNotificaLead.NON_RICEVUTO;
		}
		throw new IllegalArgumentException("No constant with value " + value);
	}

	public static EsitoSmsNotificaLead fromClickSendStatusCode(final String value) {
		if (value == null) {
			return null;
		}

		switch (value) {
		case "SUCCESS":
			return EsitoSmsNotificaLead.SPEDITO;
		case "INVALID_RECIPIENT":
			return EsitoSmsNotificaLead.NON_SPEDITO_DESTINATARIO_NON_VALIDO;
		default:
			return EsitoSmsNotificaLead.NON_SPEDITO;
		}
	}

	public String getValue() {
		return value;
	}
}
