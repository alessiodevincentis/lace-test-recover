package com.woonders.lacemscommon.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.db.entity.NotificaLeadSms;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;
import com.woonders.lacemscommon.db.entityutil.SettingUtil;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.NotificaLeadSmsRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.service.SmsLeadService;
import com.woonders.lacemscommon.service.SmsService;
import com.woonders.lacemscommon.sms.MessageType;
import com.woonders.lacemscommon.sms.SmsUtil;
import com.woonders.lacemscommon.sms.aws.AWSSmsManager;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
public class SmsLeadServiceImpl implements SmsLeadService {

	public static final String NAME = "smsLeadServiceImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";
	@Autowired
	private SettingService settingService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private NotificaLeadSmsRepository notificaLeadSmsRepository;
	@Autowired
	private SettingUtil settingUtil;
	@Autowired
	private AziendaService aziendaService;
	@Autowired
	private SmsUtil smsUtil;

	private boolean isCreditAvailable(SettingViewModel settingViewModel) {
		return settingViewModel.getNotificaLeadSmsBalance() > 0;
	}

	private boolean isNotificaSmsLeadEnabled(SettingViewModel settingViewModel) {
		return settingViewModel.isSendSmsLead();
	}

	@Autowired
    private OperatorRepository operatorRepository;
	private void decrementaBalance(int sumMessageParts, MessageType messageType, long idOperator, long currentAziendaId) {
		switch (messageType) {
			case SMS_SINGLE:
			case SMS_CALENDARIO:
			case SMS_NOTIFICA_LEAD: {
			SettingViewModel settingViewModel = settingService.getByAziendaId(currentAziendaId);
			log.info("Balance SMS lead " + settingViewModel.getNotificaLeadSmsBalance());
			settingViewModel.setNotificaLeadSmsBalance(settingViewModel.getNotificaLeadSmsBalance() - sumMessageParts);
			settingService.save(settingViewModel);
			log.info("Balance SMS lead decrementato a " + settingViewModel.getNotificaLeadSmsBalance());
			break;
		}
		case SMS_CAMPAGNA:
			// TODO utilizzare OperatorService e non OperatorRepository una
			// volta che abbiamo spostato OperatorService su common
			Operator operator = operatorRepository.findOne(idOperator);
			log.info("Operator: " + operator.getUsername() + " Balance SMS " + operator.getSmsBalance());
			operator.setSmsBalance(operator.getSmsBalance() - sumMessageParts);
			Operator operatorSaved = operatorRepository.save(operator);
			log.info("Operator: " + operatorSaved.getUsername() + " Balance SMS " + operatorSaved.getSmsBalance());
			break;
		}
	}
	
	
	@Override
	@Transactional
	public void sendLeadNotificaSms(ClienteViewModel clienteViewModel, String tenantId, long currentAziendaId) {
		SettingViewModel settingViewModel = settingService.getByAziendaId(currentAziendaId);
		if (isNotificaSmsLeadEnabled(settingViewModel)) {
			if (isCreditAvailable(settingViewModel)) {
				AziendaViewModel aziendaViewModel = aziendaService.getOne(currentAziendaId);

				SmsRequest smsReq = smsUtil.buildSmsRequest(clienteViewModel.getTelefono(), settingViewModel.getTestoSmsLead(),
										settingUtil.getAliasMittente(settingViewModel, aziendaViewModel),
										tenantId + ":" + MessageType.SMS_NOTIFICA_LEAD.toString(), true);
				String senderNumber = smsReq.getFrom();
	    		String body         = smsReq.getBody();
	    		String recipient    = smsReq.getTo();

				String messageId    = null;
				try {

					messageId = AWSSmsManager.shared().sendSMSMessage(senderNumber, body, recipient);
					if (messageId != null && messageId.length()==36) {
						decrementaBalance(1, MessageType.SMS_NOTIFICA_LEAD, 0, currentAziendaId);
						notificaLeadSmsRepository.save(NotificaLeadSms.builder()
								.dataEsito(LocalDateTime.now(ZoneId.of("GMT"))).esito(EsitoSmsNotificaLead.SPEDITO)
								.cliente(clienteRepository.findOne(clienteViewModel.getId()))
								.smsId(messageId).build());
						log.info("SMS lead spedito");
					}
				} catch (SmsNotSentException e) {
					notificaLeadSmsRepository.save(NotificaLeadSms.builder()
							.dataEsito(LocalDateTime.now(ZoneId.of("GMT"))).esito(EsitoSmsNotificaLead.NON_SPEDITO)
							.cliente(clienteRepository.findOne(clienteViewModel.getId())).build());
					log.error("Errore invio SMS lead");
				}
			}
		}
	}
}
