package com.woonders.lacemscommon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.exception.EmailFatturaNonInviataException;
import com.woonders.lacemscommon.exception.FatturaNonCreataException;
import com.woonders.lacemscommon.exception.FatturaNonDisponibileException;
import com.woonders.lacemscommon.exception.FatturaNonEliminataException;
import com.woonders.lacemscommon.fattureincloud.FattureInCloudClient;
import com.woonders.lacemscommon.fattureincloud.network.request.dettaglidoc.DettagliDocRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.request.eliminadoc.EliminaDocRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.request.inviamail.InviaMailRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.NewDocRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Pagamento;
import com.woonders.lacemscommon.fattureincloud.network.response.dettaglidoc.DettagliDocResponse;
import com.woonders.lacemscommon.fattureincloud.network.response.eliminadoc.EliminaDocResponse;
import com.woonders.lacemscommon.fattureincloud.network.response.inviamail.InviaMailResponse;
import com.woonders.lacemscommon.fattureincloud.network.response.newdoc.NewDocResponse;
import com.woonders.lacemscommon.retryutil.LaceRetryTemplate;
import com.woonders.lacemscommon.service.FattureInCloudService;
import com.woonders.lacemscommon.slack.SlackUtil;
import com.woonders.lacemscommon.util.IvaUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 21/02/2017.
 */
@Service
@Slf4j
public class FattureInCloudServiceImpl implements FattureInCloudService {

	private static final String SEZIONALE_FATTURE_LACE = "/LACE";
	private static final String MITTENTE_FATTURE_IN_CLOUD_DEFAULT = "no-reply@fattureincloud.it";
	private final FattureInCloudClient fattureInCloudClient;
	@Autowired
	private SlackUtil slackUtil;
	@Autowired
	private IvaUtil ivaUtil;
	@Autowired
	private LaceRetryTemplate laceRetryTemplate;

	@Autowired
	public FattureInCloudServiceImpl(FattureInCloudClient fattureInCloudClient) {
		this.fattureInCloudClient = fattureInCloudClient;
	}

	@Retryable(value = {
			FatturaNonCreataException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000, multiplier = 1.3))
	@Override
	public String createNewDoc(AziendaViewModel aziendaViewModel, OggettoFattura oggettoFattura,
			List<Articolo> articoloList, List<Pagamento> pagamentoList) {
		NewDocRequestBody newDocRequestBody = NewDocRequestBody.builder().nome(aziendaViewModel.getNomeAzienda())
				.indirizzoVia(aziendaViewModel.getIndirizzo()).indirizzoCitta(aziendaViewModel.getLuogo())
				.indirizzoCap(aziendaViewModel.getCap()).indirizzoProvincia(aziendaViewModel.getProvincia())
				.piva(aziendaViewModel.getPiva()).cf(aziendaViewModel.getCf())
				.oggettoVisibile(oggettoFattura.getValue()).oggettoInterno(oggettoFattura.getValue())
				.listaArticoli(articoloList).listaPagamenti(pagamentoList).prezziIvati(true).build();

		String fatturaId = null;
		try {
			fatturaId = laceRetryTemplate.execute((RetryCallback<String, Throwable>) retryContext -> {
				try {
					NewDocResponse newDocResponse = fattureInCloudClient.createNewDoc(newDocRequestBody);
					if (newDocResponse.isSuccess()) {
						return newDocResponse.getNewId();
					}
					throw new FatturaNonCreataException("Fattura NON creata per " + aziendaViewModel.getNomeAzienda());
				} catch (UnirestException e) {
					throw new FatturaNonCreataException("Fattura NON creata per " + aziendaViewModel.getNomeAzienda(),
							e);
				}
			}, retryContext -> {
				log.error(retryContext.getLastThrowable().getMessage(), retryContext.getLastThrowable());
				slackUtil.sendMessage(retryContext.getLastThrowable().getMessage(), SlackUtil.SlackEmojiIcon.BOMB,
						retryContext.getLastThrowable());
				return null;
			});
		} catch (Throwable throwable) {
			log.error(throwable.getMessage());
			slackUtil.sendMessage(throwable.getMessage(), SlackUtil.SlackEmojiIcon.BOMB, throwable);
		}

		return fatturaId;
	}

	@Override
	public void inviaMailFattura(String idFattura, String mailDestinatario, OggettoFattura oggettoFattura,
			String messaggio) {
		InviaMailRequestBody inviaMailRequestBody = InviaMailRequestBody.builder().id(idFattura)
				.mailDestinatario(mailDestinatario).mailMittente(MITTENTE_FATTURE_IN_CLOUD_DEFAULT)
				.oggetto(oggettoFattura.getValue()).messaggio(messaggio).includiAllegato(true).allegaPdf(true)
				.includiDocumento(true).build();

		try {
			laceRetryTemplate.execute((RetryCallback<Void, Throwable>) retryContext -> {
				try {
					InviaMailResponse inviaMailResponse = fattureInCloudClient.inviaMailFattura(inviaMailRequestBody);
					if (!inviaMailResponse.isSuccess()) {
						throw new EmailFatturaNonInviataException(
								"Email fattura NON inviata a " + mailDestinatario + " idFattura " + idFattura);
					}
					return null;
				} catch (UnirestException e) {
					throw new EmailFatturaNonInviataException(
							"Email fattura NON inviata a " + mailDestinatario + " idFattura " + idFattura);
				}
			}, retryContext -> {
				log.error(retryContext.getLastThrowable().getMessage(), retryContext.getLastThrowable());
				slackUtil.sendMessage(retryContext.getLastThrowable().getMessage(), SlackUtil.SlackEmojiIcon.BOMB,
						retryContext.getLastThrowable());
				return null;
			});
		} catch (Throwable throwable) {
			log.error(throwable.getMessage());
			slackUtil.sendMessage(throwable.getMessage(), SlackUtil.SlackEmojiIcon.BOMB, throwable);
		}
	}

	@Override
	public void eliminaFattura(String idFattura) throws FatturaNonEliminataException {
		EliminaDocRequestBody eliminaDocRequestBody = EliminaDocRequestBody.builder().id(idFattura).build();
		try {
			EliminaDocResponse eliminaDocResponse = fattureInCloudClient.eliminaFattura(eliminaDocRequestBody);
			if (!eliminaDocResponse.isSuccess()) {
				log.error("Fattura NON eliminata, id " + idFattura);
				slackUtil.sendMessage("Fattura NON eliminata, id " + idFattura, SlackUtil.SlackEmojiIcon.BOMB);
				throw new FatturaNonEliminataException("Fattura non eliminata!");
			}
		} catch (UnirestException e) {
			log.error("Fattura NON eliminata, id " + idFattura, e);
			slackUtil.sendMessage("Fattura NON eliminata, id " + idFattura, SlackUtil.SlackEmojiIcon.BOMB, e);
			throw new FatturaNonEliminataException("Fattura non eliminata!", e);
		}
	}

	@Override
	public String getLinkFattura(String idFattura, long ricaricaId, String tenantName)
			throws FatturaNonDisponibileException {
		DettagliDocRequestBody dettagliDocRequestBody = DettagliDocRequestBody.builder().id(idFattura).build();
		try {
			DettagliDocResponse dettagliDocResponse = fattureInCloudClient.getDettagliFattura(dettagliDocRequestBody);
			if (dettagliDocResponse.isSuccess() && dettagliDocResponse.getDettagliDocumento() != null
					&& !StringUtils.isEmpty(dettagliDocResponse.getDettagliDocumento().getLinkDocumentoPdf())) {
				return dettagliDocResponse.getDettagliDocumento().getLinkDocumentoPdf();
			} else {
				log.error("Fattura non disponibile, idFattura " + idFattura + " ricaricaId " + ricaricaId
						+ " tenantName " + tenantName);
				slackUtil.sendMessage("Fattura non disponibile, idFattura " + idFattura + " ricaricaId " + ricaricaId
						+ " tenantName " + tenantName, SlackUtil.SlackEmojiIcon.BOMB);
				throw new FatturaNonDisponibileException("Fattura non disponibile!");
			}
		} catch (UnirestException e) {
			log.error("Fattura non disponibile, idFattura " + idFattura + " ricaricaId " + ricaricaId + " tenantName "
					+ tenantName, e);
			slackUtil.sendMessage("Fattura non disponibile, idFattura " + idFattura + " ricaricaId " + ricaricaId
					+ " tenantName " + tenantName, SlackUtil.SlackEmojiIcon.BOMB, e);
			throw new FatturaNonDisponibileException("Fattura non disponibile!", e);
		}
	}

}
