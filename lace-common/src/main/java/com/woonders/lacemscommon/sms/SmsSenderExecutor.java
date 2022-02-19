package com.woonders.lacemscommon.sms;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.service.SmsService;
import com.woonders.lacemscommon.service.impl.SmsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.app.event.SendCampagnaDTO;
import com.woonders.lacemscommon.db.entity.Campagna;
import com.woonders.lacemscommon.db.entity.CampagnaSms;
import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;
import com.woonders.lacemscommon.db.repository.CampagnaRepository;
import com.woonders.lacemscommon.db.repository.CampagnaSmsRepository;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.exception.UnableToSendSmsException;
import com.woonders.lacemscommon.slack.SlackUtil;

import lombok.extern.slf4j.Slf4j;

import javax.faces.bean.ManagedProperty;

/**
 * Created by Emanuele on 30/03/2017.
 */
@Slf4j
@Component
public class SmsSenderExecutor {

	@Autowired
	private CampagnaSmsRepository campagnaSmsRepository;
	@Autowired
	private CampagnaRepository campagnaRepository;
	@Autowired
	private SmsStorer smsStorer;
	@Autowired
	private SlackUtil slackUtil;
	@Autowired
	private SmsUtil smsUtil;
	@Autowired
	private OperatorRepository operatorRepository;
	@ManagedProperty(SmsServiceImpl.JSF_NAME)
	private SmsService smsService;

//	@Async(AsyncConfig.SINGLE_TASK_EXECUTOR)
//	@Transactional
//	public void startSmsSendingOld(SendCampagnaDTO sendCampagnaDTO) {
//		Campagna campagna = campagnaRepository.findOne(sendCampagnaDTO.getCampagnaId());
//
//		Collection<EsitoSmsNotificaLead> esitoSmsNotificaLeadCollection = new LinkedList<>();
//		// caso in cui salvi e invii la campagna
//		esitoSmsNotificaLeadCollection.add(EsitoSmsNotificaLead.IN_SPEDIZIONE);
//		// caso in cui hai salvato ed invii la campagna successivamente
//		esitoSmsNotificaLeadCollection.add(EsitoSmsNotificaLead.DA_SPEDIRE);
//		// caso in cui hai inviato la campagna e si sono verificati errori
//		esitoSmsNotificaLeadCollection.add(EsitoSmsNotificaLead.NON_SPEDITO);
//
//		List<CampagnaSms> campagnaSmsDaSpedireList = campagnaSmsRepository
//				.findByCampagnaIdAndEsitoIn(sendCampagnaDTO.getCampagnaId(), esitoSmsNotificaLeadCollection);
//
//		log.info("Spedizione di " + campagnaSmsDaSpedireList.size() + " SMS");
//
//		boolean errorHappened = false;
//
//		Iterable<List<CampagnaSms>> partitionCampagnaSmsDaSpedireList = Iterables.partition(campagnaSmsDaSpedireList,
//				900);
//
//		for (List<CampagnaSms> partitionedCampagnaSmsDaSpedireList : partitionCampagnaSmsDaSpedireList) {
//			try {
//				List<SmsRequest> smsRequestList = new LinkedList<>();
//				for (CampagnaSms campagnaSms : partitionedCampagnaSmsDaSpedireList) {
//					smsRequestList.add(smsUtil.buildSmsRequest(campagnaSms.getNumeroDestinatario(), campagna.getText(),
//							sendCampagnaDTO.getNumeroMittente(), sendCampagnaDTO.getTenantName() + ":"
//									+ MessageType.SMS_CAMPAGNA.toString() + ":" + campagnaSms.getId()));
//				}
//				smsStorer.sendStoreAndRetry(smsRequestList, sendCampagnaDTO.getIdOperator(),
//						sendCampagnaDTO.getCurrentAziendaId());
//			} catch (SmsNotSentException | UnableToSendSmsException e) {
//				errorHappened = true;
//				log.error("Errore invio campagna SMS, tutti i tentativi falliti", e);
//				smsStorer.setStatusOnSms(partitionedCampagnaSmsDaSpedireList, EsitoSmsNotificaLead.NON_SPEDITO);
//			}
//		}
//		if (!errorHappened) {
//			log.info("Tutti gli sms sono stati inviati e salvati!");
//		} else {
//			log.error("Si sono verificati errori nella spedizione degli sms!");
//			slackUtil.sendMessage("Si sono verificati errori nella spedizione degli sms!",
//					SlackUtil.SlackEmojiIcon.BOMB);
//		}
//
//	}

	@Transactional
	public void startSmsSending(SendCampagnaDTO sendCampagnaDTO) {
		Campagna campagna = campagnaRepository.findOne(sendCampagnaDTO.getCampagnaId());

		Collection<EsitoSmsNotificaLead> esitoSmsNotificaLeadCollection = new LinkedList<>();
		// caso in cui salvi e invii la campagna
		esitoSmsNotificaLeadCollection.add(EsitoSmsNotificaLead.IN_SPEDIZIONE);
		// caso in cui hai salvato ed invii la campagna successivamente
		esitoSmsNotificaLeadCollection.add(EsitoSmsNotificaLead.DA_SPEDIRE);
		// caso in cui hai inviato la campagna e si sono verificati errori
		esitoSmsNotificaLeadCollection.add(EsitoSmsNotificaLead.NON_SPEDITO);

		List<CampagnaSms> campagnaSmsDaSpedireList = campagnaSmsRepository
				.findByCampagnaIdAndEsitoIn(sendCampagnaDTO.getCampagnaId(), esitoSmsNotificaLeadCollection);

		// Decrement the operator credit by the total number of sms to be sent
		int parts = SMSLenghtCalculator.getPartCount(campagna.getText());
		int totalNumerOfSms = campagnaSmsDaSpedireList.size()*parts;
		log.info("Spedizione di " + totalNumerOfSms + " SMS");
		Operator operator = operatorRepository.findOne(campagna.getSenderOperator().getId());
		operator.setSmsBalance(operator.getSmsBalance() - totalNumerOfSms);
		operatorRepository.save(operator);
		for (CampagnaSms campagnaSms : campagnaSmsDaSpedireList) {
			String destinatario  = campagnaSms.getNumeroDestinatario();
			String testo         = campagna.getText();
			long   idCampagnaSms = campagnaSms.getId();
			String mittente      = sendCampagnaDTO.getNumeroMittente();
			String tenantName 	 =  sendCampagnaDTO.getTenantName();

			try {
				smsStorer.sendStoreAndRetry(idCampagnaSms, tenantName, mittente, testo, destinatario);
			} catch (UnableToSendSmsException | SmsNotSentException e) {
				e.printStackTrace();
			}
		}
		log.info("Tutti gli sms sono stati inviati e salvati!");
	}
}
