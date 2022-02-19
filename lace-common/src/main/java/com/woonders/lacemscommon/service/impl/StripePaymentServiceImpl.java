package com.woonders.lacemscommon.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.RicaricaComunicazione;
import com.woonders.lacemscommon.db.entityenum.RicaricaType;
import com.woonders.lacemscommon.db.entityutil.AziendaUtil;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.db.repository.RicaricaComunicazioneRepository;
import com.woonders.lacemscommon.exception.DatiFatturazioneToBeCompletedException;
import com.woonders.lacemscommon.exception.PaymentException;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.PaymentService;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.slack.SlackUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 15/02/2017.
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class StripePaymentServiceImpl implements PaymentService {

	public static final String NAME = "stripePaymentServiceImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";
	private static final String STRIPE_SECRET_KEY = "STRIPE_SECRET_KEY";
	private static final String EUR = "EUR";
	@Autowired
	private RicaricaComunicazioneRepository ricaricaComunicazioneRepository;
	@Autowired
	private SettingService settingService;
	@Autowired
	private SlackUtil slackUtil;
	@Autowired
	private AziendaService aziendaService;
	@Autowired
	private AziendaUtil aziendaUtil;
	@Autowired
	private OperatorRepository operatorRepository;

	public StripePaymentServiceImpl() {
		Stripe.apiKey = System.getProperty(STRIPE_SECRET_KEY);
	}

	@Transactional(rollbackFor = { PaymentException.class, DatiFatturazioneToBeCompletedException.class })
	@Override
	public long makePayment(long payerOperatorId, String tokenId, Articolo.TipoArticolo tipoArticolo, String currency,
			RicaricaType ricaricaType, OperatorViewModel operatoreRicaricato, long currentAziendaId)
			throws PaymentException, DatiFatturazioneToBeCompletedException {

		Map<String, Object> params = new HashMap<>();
		params.put("amount", tipoArticolo.getAmount());
		params.put("currency", EUR);
		params.put("description", tipoArticolo.getDescription());
		params.put("source", tokenId);

		Operator payerOperator = operatorRepository.findOne(payerOperatorId);
		String payerOperatorString = payerOperator.getUsername();
		try {

			if (aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaService.getOne(currentAziendaId))) {
				throw new DatiFatturazioneToBeCompletedException();
			}

			BigDecimal prezzoLordo = tipoArticolo.getPrezzoLordo();
			int smsQuantity = tipoArticolo.getSmsQuantity();

			RicaricaComunicazione.RicaricaComunicazioneBuilder ricaricaComunicazioneBuilder = RicaricaComunicazione
					.builder().dateTime(LocalDateTime.now(ZoneId.of("GMT"))).payer(payerOperator).amount(prezzoLordo)
					.paymentMethod(RicaricaComunicazione.PaymentMethod.CREDIT_CARD).quantity(smsQuantity)
					.ricaricaType(ricaricaType);
			if (operatoreRicaricato != null) {
				ricaricaComunicazioneBuilder
						.operatoreRicaricato(operatorRepository.findByUsername(operatoreRicaricato.getUsername()));
			}
			RicaricaComunicazione ricaricaComunicazione = ricaricaComunicazioneBuilder.build();
			ricaricaComunicazione = ricaricaComunicazioneRepository.save(ricaricaComunicazione);
			increaseSmsBalance(tipoArticolo.getSmsQuantity(), operatoreRicaricato, currentAziendaId);
			Charge charge = Charge.create(params);
			if (charge.getPaid()) {
				log.info("Notifica ricarica SMS: " + payerOperatorString + " ha acquistato " + smsQuantity + " SMS per "
						+ prezzoLordo + " EURO");
				slackUtil.sendMessage("Notifica ricarica SMS: " + payerOperatorString + " ha acquistato " + smsQuantity
						+ " SMS per " + prezzoLordo + " EURO", SlackUtil.SlackEmojiIcon.CASH);
				return ricaricaComunicazione.getId();
			} else {
				log.error("Transazione rifiutata " + charge);
				slackUtil.sendMessage("Transazione rifiutata per " + payerOperatorString + " con charge " + charge,
						SlackUtil.SlackEmojiIcon.BOMB);
				throw new PaymentException();
			}
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException e) {
			log.error("Errore generico nel pagamento per " + payerOperatorString, e);
			slackUtil.sendMessage("Errore generico nel pagamento per " + payerOperatorString,
					SlackUtil.SlackEmojiIcon.BOMB, e);
			throw new PaymentException();
		} catch (CardException e) {
			try {
				switch (ProcessingCode.valueOf(e.getCode().toUpperCase())) {
				case CARD_DECLINED:
					log.error("Carta rifiutata " + payerOperatorString, e);
					slackUtil.sendMessage("Carta rifiutata " + payerOperatorString, SlackUtil.SlackEmojiIcon.BOMB, e);
					throw new PaymentException();
				default:
					log.error("Errore carta nel pagamento per " + payerOperatorString, e);
					slackUtil.sendMessage("Errore carta nel pagamento per " + payerOperatorString,
							SlackUtil.SlackEmojiIcon.BOMB, e);
					throw new PaymentException();
				}
				// improbabile che ci mandino un codice null gli amici di
				// stripe, ma sia mai!
			} catch (NullPointerException | IllegalArgumentException e1) {
				log.error("Errore carta nel pagamento per " + payerOperatorString + ". Codice charge errato "
						+ e.getCode(), e1);
				slackUtil.sendMessage("Errore carta nel pagamento per " + payerOperatorString
						+ ". Codice charge errato " + e.getCode(), SlackUtil.SlackEmojiIcon.BOMB, e);
				throw new PaymentException();
			}
		}

	}

	private void increaseSmsBalance(int quantity, OperatorViewModel operatoreRicaricato, long currentAziendaId) {
		if (operatoreRicaricato == null) {
			SettingViewModel settingViewModel = settingService.getByAziendaId(currentAziendaId);
			settingViewModel.setNotificaLeadSmsBalance(settingViewModel.getNotificaLeadSmsBalance() + quantity);
			settingService.save(settingViewModel);
		} else {
			Operator operator = operatorRepository.findOne(operatoreRicaricato.getId());
			operator.setSmsBalance(operator.getSmsBalance() + quantity);
			operatorRepository.save(operator);
		}
	}

}
