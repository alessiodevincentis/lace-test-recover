package com.woonders.lacemscommon.service.impl;

import com.google.common.collect.Lists;
import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsMainResponse;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.db.entity.CampagnaSms;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.NotificaLeadSms;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;
import com.woonders.lacemscommon.db.entityutil.SettingUtil;
import com.woonders.lacemscommon.db.repository.CampagnaSmsRepository;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.NotificaLeadSmsRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.service.SmsService;
import com.woonders.lacemscommon.sms.MessageType;
import com.woonders.lacemscommon.sms.SMSLenghtCalculator;
import com.woonders.lacemscommon.sms.SmsClient;
import com.woonders.lacemscommon.sms.aws.AWSSmsManager;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemscommon.util.PredicateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Emanuele on 11/02/2017.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class SmsServiceImpl implements SmsService {

    public static final String NAME = "smsServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private SmsClient smsClient;

    @Autowired
    private DateConversionUtil dateConversionUtil;

    @Autowired
    private NotificaLeadSmsRepository notificaLeadSmsRepository;

    @Autowired
    private CampagnaSmsRepository campagnaSmsRepository;
    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private SettingUtil settingUtil;
    @Autowired
    private SettingService settingService;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private OperatorRepository operatorRepository;

    public void decrementaBalance(int sumMessageParts, MessageType messageType, long idOperator, long currentAziendaId) {
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

    @Transactional
    @Override
    public SendSmsMainResponse sendSms(List<SmsRequest> smsRequestList, MessageType messageType, long idOperator, long currentAziendaId) throws SmsNotSentException {
    	for(SmsRequest smsReq : smsRequestList) {
    		String senderNumber = smsReq.getFrom();
    		String body         = smsReq.getBody();
    		String recipient    = smsReq.getTo();
    		int    parts        = SMSLenghtCalculator.getPartCount(body);
    		try {
                String smsID = AWSSmsManager.shared().sendSMSMessage(senderNumber, body, recipient);
                if (smsID != null && smsID.length()==36)
                    decrementaBalance(parts, messageType, idOperator, currentAziendaId);
            }
            catch (SmsNotSentException e){
                throw new SmsNotSentException(e);
            }

    	}
    	return new SendSmsMainResponse();
    }

    @Override
    @Transactional
    public void updateStatusNotificaSmsLead(String smsId, int statusCode, long timestamp) throws ItemNotFoundException {
        NotificaLeadSms notificaLeadSms = notificaLeadSmsRepository.findBySmsId(smsId);
        EsitoSmsNotificaLead esitoSmsNotificaLead = EsitoSmsNotificaLead.fromClickSendStatusCode(statusCode);
        if (notificaLeadSms != null) {
            if (checkIfStatusNeedsToBeChanged(notificaLeadSms.getEsito())) {
                notificaLeadSms.setEsito(esitoSmsNotificaLead);
                notificaLeadSms.setDataEsito(dateConversionUtil.calcUTCLocalDateTimeFromTimestamp(timestamp));
                notificaLeadSmsRepository.save(notificaLeadSms);
            } else {
                log.info("Stato SMS era gia ricevuto " + smsId);
            }
        } else {
            log.error("Notifica sms con smsId non trovata: " + smsId);
            throw new ItemNotFoundException("Notifica sms con smsId non trovata: " + smsId);
        }
    }

    @Transactional
    @Override
    public void updateStatusNotificaSmsMarketing(String id, int statusCode, long timestamp)
            throws ItemNotFoundException {
        CampagnaSms campagnaSms = campagnaSmsRepository.findOne(Long.valueOf(id));
        EsitoSmsNotificaLead esitoSmsNotificaLead = EsitoSmsNotificaLead.fromClickSendStatusCode(statusCode);
        if (campagnaSms != null) {
            if (checkIfStatusNeedsToBeChanged(campagnaSms.getEsito())) {
                campagnaSms.setDataEsito(dateConversionUtil.calcUTCLocalDateTimeFromTimestamp(timestamp));
                campagnaSms.setEsito(esitoSmsNotificaLead);
                campagnaSmsRepository.save(campagnaSms);
            } else {
                log.info("Stato SMS era gia ricevuto " + id);
            }
        } else {
            log.error("Notifica sms con id non trovata: " + id);
            throw new ItemNotFoundException("Notifica sms con id non trovata: " + id);
        }
    }

    private boolean checkIfStatusNeedsToBeChanged(EsitoSmsNotificaLead esitoSmsNotificaLead) {
        return !EsitoSmsNotificaLead.RICEVUTO.equals(esitoSmsNotificaLead);
    }

    @Override
    public CalcSmsInfo calcSmsPricingInfo(FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel,
                                          String messageText, boolean isClientiReadSuper, boolean isNominativiReadSuper, boolean isClientiReadAll,
                                          boolean isNominativiReadAll, boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorId,
                                          long currentOperatorAziendaId, Long currentAziendaId) throws UnableToCalculateSmsPrice {

        List<Cliente> clienteList = Lists.newArrayList(clienteRepository.findAll(predicateHelper
                .getPredicateForNuovaCampagnaMarketing(filtroCampagnaMarketingViewModel, isClientiReadSuper,
                        isNominativiReadSuper, isClientiReadAll, isNominativiReadAll, isClientiReadOwn,
                        isNominativiReadOwn, currentOperatorId, currentOperatorAziendaId, currentAziendaId)));

        return getSmsPrice(clienteList.stream().map(Cliente::getTelefono).collect(Collectors.toList()), messageText, currentAziendaId);

    }

    private CalcSmsInfo getSmsPrice(List<String> recipientNumberList, String messageText, long currentAziendaId)
            throws UnableToCalculateSmsPrice {
        String numeroMittente = settingUtil.getAliasMittente(
                settingService.getByAziendaId(currentAziendaId), aziendaService.getOne(currentAziendaId));
        return smsClient.calculateSmsPrice(recipientNumberList, messageText, numeroMittente);
    }

    @Override
    public CalcSmsInfo calcSmsPricingInfo(List<String> recipientNumberList, String messageText, long currentAziendaId) throws UnableToCalculateSmsPrice {
    	return getSmsPrice(recipientNumberList, messageText, currentAziendaId);
    }

}
