package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entityutil.SettingUtil;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.exception.CannotSendSmsException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;
import com.woonders.lacemscommon.service.*;
import com.woonders.lacemscommon.sms.MessageType;
import com.woonders.lacemscommon.sms.SmsClient;
import com.woonders.lacemscommon.sms.SmsUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ema98 on 7/17/2017.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class SmsServiceFacadeImpl implements SmsServiceFacade {

    public static final String NAME = "smsServiceFacadeImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private SettingUtil settingUtil;
    @Autowired
    private SettingService settingService;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private OperatorRepository operatorRepository;

    @Transactional
    @Override
    public void sendSms(String recipientNumber, String body, MessageType messageType, long idOperator, long operatorAziendaId, String tenantName,
                        long idClienteOrPratica, LogService.TipoLog tipoLog) throws SmsNotSentException, CannotSendSmsException {
//        String senderNumber = settingUtil.getNumeroMittenteByPreferenza(settingService.getByAziendaId(operatorAziendaId),
//                aziendaService.getOne(operatorAziendaId));
        String senderNumber = settingService.getByAziendaId(operatorAziendaId).getAlias();

        if (StringUtils.isEmpty(senderNumber) || StringUtils.isEmpty(recipientNumber) || StringUtils.isEmpty(body)) {
            throw new CannotSendSmsException();
        }

        List<SmsRequest> smsRequestList = new ArrayList<>();
        smsRequestList.add(smsUtil.buildSmsRequest(recipientNumber, body, senderNumber,
                String.join(SmsClient.CUSTOM_STRING_SEPARATOR, tenantName, MessageType.SMS_SINGLE.toString(), String.valueOf(idClienteOrPratica),
                        tipoLog.toString(), String.valueOf(idOperator))));
        
        smsService.sendSms(smsRequestList, MessageType.SMS_SINGLE, idOperator, operatorAziendaId);
    }
    
//TODO FIX boolean return
    @Override
    public boolean checkEnoughBalance(String recipientNumber, String body, long operatorId, long operatorAziendaId) throws UnableToCalculateSmsPrice {
        Operator operator = operatorRepository.findOne(operatorId);
        log.info("Operator: "+operator.toString());
        CalcSmsInfo calcSmsInfo = smsService.calcSmsPricingInfo(Collections.singletonList(recipientNumber), body, operatorAziendaId);
        log.info("calcSmsInfo: "+calcSmsInfo.toString());
        return operator != null && calcSmsInfo != null && operator.getSmsBalance() > calcSmsInfo.getSmsCount();
    }

  //TODO FIX boolean return
    @Override
    public boolean checkEnoughBalance(String recipientNumber, String body, long operatorAziendaId) throws UnableToCalculateSmsPrice {
        SettingViewModel settingViewModel = settingService.getByAziendaId(operatorAziendaId);
        CalcSmsInfo calcSmsInfo = smsService.calcSmsPricingInfo(Collections.singletonList(recipientNumber), body, operatorAziendaId);
        log.info("calcSmsInfo: destinatariCount="+calcSmsInfo.getDestinatariCount()+" smsCount: " +calcSmsInfo.getSmsCount());

        return calcSmsInfo != null && settingViewModel.getNotificaLeadSmsBalance() > calcSmsInfo.getSmsCount();
    }

}
