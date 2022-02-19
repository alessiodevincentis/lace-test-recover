package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsMainResponse;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;
import com.woonders.lacemscommon.sms.MessageType;

import java.util.List;

/**
 * Created by Emanuele on 11/02/2017.
 */

/**
 * Manages part of the logic useful to send SMS
 */
public interface SmsService {

    String FIXED_IT_COUNTRY = "IT";
    String SOURCE = "LaceMS";
    String SMS_SPECIAL_CHAR_PLACEHOLDER = "#";
    String DATE_PLACEHOLDER = SMS_SPECIAL_CHAR_PLACEHOLDER + "data" + SMS_SPECIAL_CHAR_PLACEHOLDER;
    String TIME_PLACEHOLDER = SMS_SPECIAL_CHAR_PLACEHOLDER + "ora" + SMS_SPECIAL_CHAR_PLACEHOLDER;

    /**
     * Same method of SmsServiceFacade but we need a facade class for sending SMSs in async way (Spring framework stuff)
     */
    SendSmsMainResponse sendSms(List<SmsRequest> smsRequestList, MessageType messageType, long idOperator,
                                long currentAziendaId) throws SmsNotSentException;

    /**
     * Update of SMS status when we receive a report from ClickSend (for SMS for third party Lead received)
     */
    void updateStatusNotificaSmsLead(String smsId, int statusCode, long timestamp) throws ItemNotFoundException;

    /**
     * Update of SMS status when we receive a report from ClickSend (for SMS for marketing campaign)
     */
    void updateStatusNotificaSmsMarketing(String id, int statusCode, long timestamp) throws ItemNotFoundException;

    /**
     * return an SMS quote before of sending
     */
    CalcSmsInfo calcSmsPricingInfo(FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel,
                                   String messageText, boolean isClientiReadSuper, boolean isNominativiReadSuper, boolean isClientiReadAll,
                                   boolean isNominativiReadAll, boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorId,
                                   long currentOperatorAziendaId, Long currentAziendaId) throws UnableToCalculateSmsPrice;

    CalcSmsInfo calcSmsPricingInfo(List<String> recipientNumberList, String messageText,
                                   long currentAziendaId) throws UnableToCalculateSmsPrice;
}
