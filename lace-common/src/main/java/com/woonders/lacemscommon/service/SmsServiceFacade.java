package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.exception.CannotSendSmsException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;
import com.woonders.lacemscommon.sms.MessageType;

/**
 * Created by ema98 on 7/17/2017.
 */

/**
 * Manages part of the logic useful to send SMS
 */
public interface SmsServiceFacade {

    /**
     * method to send SMS
     */
    void sendSms(String recipientNumber, String body, MessageType messageType, long idOperator,
                 long operatorAziendaId, String tenantName, long idClienteOrPratica, LogService.TipoLog tipoLog) throws SmsNotSentException, CannotSendSmsException;

    /**
     * return a boolean if the sender has enough balance to send SMSs'
     */
    boolean checkEnoughBalance(String recipientNumber, String body, long operatorId, long operatorAziendaId) throws UnableToCalculateSmsPrice;

    boolean checkEnoughBalance(String recipientNumber, String body, long operatorAziendaId) throws UnableToCalculateSmsPrice;

}
