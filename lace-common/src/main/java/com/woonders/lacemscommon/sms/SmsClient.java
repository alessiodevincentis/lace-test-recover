package com.woonders.lacemscommon.sms;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.response.GetSmsHistoryMainResponse;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsMainResponse;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;

import java.util.List;

/**
 * Created by Emanuele on 14/04/2017.
 */
public interface SmsClient {

    String CUSTOM_STRING_SEPARATOR = ":";

    SendSmsMainResponse sendSms(List<SmsRequest> smsRequestList) throws UnirestException;

    GetSmsHistoryMainResponse getSmsHistory(long fromDate, long toDate, long page) throws UnirestException;

    CalcSmsInfo calculateSmsPrice(List<String> recipientNumberList, String messageText, String numeroMittente)
            throws UnableToCalculateSmsPrice;
}
