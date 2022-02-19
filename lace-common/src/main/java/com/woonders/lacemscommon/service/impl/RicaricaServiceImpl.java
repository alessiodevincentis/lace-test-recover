package com.woonders.lacemscommon.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.RicaricaComunicazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.RicaricaComunicazioneViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.QRicaricaComunicazione;
import com.woonders.lacemscommon.db.entity.RicaricaComunicazione;
import com.woonders.lacemscommon.db.entityenum.RicaricaType;
import com.woonders.lacemscommon.db.repository.RicaricaComunicazioneRepository;
import com.woonders.lacemscommon.exception.DatiFatturazioneToBeCompletedException;
import com.woonders.lacemscommon.exception.FatturaNonDisponibileException;
import com.woonders.lacemscommon.exception.PaymentException;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Pagamento;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.FattureInCloudService;
import com.woonders.lacemscommon.service.PaymentService;
import com.woonders.lacemscommon.service.RicaricaService;
import com.woonders.lacemscommon.slack.SlackUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 25/02/2017.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class RicaricaServiceImpl implements RicaricaService {

	public static final String NAME = "ricaricaServiceImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private FattureInCloudService fattureInCloudService;
	@Autowired
	private AziendaService aziendaService;
	@Autowired
	private SlackUtil slackUtil;
	@Autowired
	private RicaricaComunicazioneRepository ricaricaComunicazioneRepository;
	@Autowired
	private QueryDSLHelper queryDSLHelper;
	@Autowired
	private RicaricaComunicazioneViewModelCreator ricaricaComunicazioneViewModelCreator;

	private List<Pagamento> createPagamentoList(BigDecimal amount) {
		List<Pagamento> pagamentoList = new LinkedList<>();
		Pagamento pagamento = Pagamento.builder().dataSaldo(LocalDate.now()).dataScadenza(LocalDate.now())
				.metodo(Pagamento.Tipo.STRIPE.getValue()).importo(amount.toString()).build();
		pagamentoList.add(pagamento);
		return pagamentoList;
	}

	private List<Articolo> createArticoloList(Articolo.TipoArticolo tipoArticolo) {
		List<Articolo> articoloList = new LinkedList<>();
		articoloList.add(Articolo.builder().codice(tipoArticolo.name()).nome(tipoArticolo.getName())
				.descrizione(tipoArticolo.getDescription()).tassabile(true).prezzoLordo(tipoArticolo.getPrezzoLordo())
				.build());
		return articoloList;
	}

	@Transactional
	@Override
	public void makePayment(long payerOperatorId, String tokenId, String currency, Articolo.TipoArticolo tipoArticolo,
			RicaricaType ricaricaType, OperatorViewModel operatoreRicaricato, long currentAziendaId)
			throws PaymentException, DatiFatturazioneToBeCompletedException {

		long ricaricaComunicazioneId = paymentService.makePayment(payerOperatorId, tokenId, tipoArticolo, currency,
				ricaricaType, operatoreRicaricato, currentAziendaId);

		AziendaViewModel aziendaViewModel = aziendaService.getOne(currentAziendaId);

		String fatturaId = fattureInCloudService.createNewDoc(aziendaViewModel,
				FattureInCloudService.OggettoFattura.ACQUISTO_SMS, createArticoloList(tipoArticolo),
				createPagamentoList(tipoArticolo.getPrezzoLordo()));

		if (!StringUtils.isEmpty(fatturaId)) {

			RicaricaComunicazione ricaricaComunicazione = ricaricaComunicazioneRepository
					.findOne(ricaricaComunicazioneId);
			ricaricaComunicazione.setFatturaId(fatturaId);
			ricaricaComunicazioneRepository.save(ricaricaComunicazione);

			fattureInCloudService.inviaMailFattura(fatturaId, aziendaViewModel.getEmail(),
					FattureInCloudService.OggettoFattura.ACQUISTO_SMS,
					String.format(
							"Gentile cliente,<br/><br/>le inviamo in allegato la fattura per l'acquisto di %s sul portale Lace.<br/><br/>Cordiali saluti,<br/>Team Woonders",
							tipoArticolo.getDescription()));
		}

	}

	// TODO a questo punto mettere in un metodo dentro un component cosi nn si
	// duplica il codice!
	@PreAuthorize("hasAuthority('RICARICA_WRITE_SUPER') or (hasAuthority('RICARICA_WRITE') && @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))")
	@Override
	public ViewModelPage<RicaricaComunicazioneViewModel> getTopUpList(Long currentAziendaId, int firstElementIndex,
			int pageSize, String sortField, QueryDSLHelper.SortOrder sortOrder,
			Map<QueryDSLHelper.TableField, Object> filters) {
		// filters non usati ora nella tabella

		QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

		Page<RicaricaComunicazione> ricaricaComunicazioneViewModelPage;

		if (currentAziendaId != null) {
			ricaricaComunicazioneViewModelPage = ricaricaComunicazioneRepository.findAll(
					QRicaricaComunicazione.ricaricaComunicazione.payer.azienda.id.eq(currentAziendaId),
					new PageRequest(firstElementIndex / pageSize, pageSize, qSort));
		} else {
			ricaricaComunicazioneViewModelPage = ricaricaComunicazioneRepository
					.findAll(new PageRequest(firstElementIndex / pageSize, pageSize, qSort));
		}

		return new ViewModelPageImpl<>(
				ricaricaComunicazioneViewModelCreator
						.createViewModelList(ricaricaComunicazioneViewModelPage.getContent()),
				ricaricaComunicazioneViewModelPage.getTotalPages(),
				ricaricaComunicazioneViewModelPage.getTotalElements());
	}

	@Override
	public String findFatturaPdfLinkByRicaricaId(long id, String tenantName) throws FatturaNonDisponibileException {
		RicaricaComunicazione ricaricaComunicazione = ricaricaComunicazioneRepository.findOne(id);
		if (ricaricaComunicazione != null && !StringUtils.isEmpty(ricaricaComunicazione.getFatturaId())) {
			return fattureInCloudService.getLinkFattura(ricaricaComunicazione.getFatturaId(),
					ricaricaComunicazione.getId(), tenantName);
		} else {
			log.error("Fattura non disponibile, ricaricaId " + id + " tenantName " + tenantName);
			slackUtil.sendMessage("Fattura non disponibile, ricaricaId " + id + " tenantName " + tenantName,
					SlackUtil.SlackEmojiIcon.BOMB);
			throw new FatturaNonDisponibileException("Fattura non disponibile!");
		}
	}

}
