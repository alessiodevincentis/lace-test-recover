package com.woonders.lacemscommon.service;

import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.exception.FatturaNonCreataException;
import com.woonders.lacemscommon.exception.FatturaNonDisponibileException;
import com.woonders.lacemscommon.exception.FatturaNonEliminataException;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Pagamento;

/**
 * Created by Emanuele on 21/02/2017.
 * Manages invoicing creation with FattureInCloud for sms topups
 */
public interface FattureInCloudService {

	/**
	 * Creates a new invoice
	 */
	String createNewDoc(AziendaViewModel aziendaViewModel, OggettoFattura oggettoFattura, List<Articolo> articoloList,
			List<Pagamento> pagamentoList);

	/**
	 * Send email with invoice to [mailDestinatario]
	 */
	void inviaMailFattura(String idFattura, String mailDestinatario, OggettoFattura oggettoFattura, String messaggio);

	/**
	 * Deletes an invoice by its id
	 */
	void eliminaFattura(String idFattura) throws FatturaNonEliminataException;

	/**
	 * Returns the link of an invoice by its id
	 */
	String getLinkFattura(String idFattura, long ricaricaId, String tenantName) throws FatturaNonDisponibileException;

	enum OggettoFattura {
		ACQUISTO_SMS("Acquisto SMS");

		private String value;

		OggettoFattura(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
