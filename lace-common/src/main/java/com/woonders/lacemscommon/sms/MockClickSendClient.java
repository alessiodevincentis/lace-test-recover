package com.woonders.lacemscommon.sms;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.response.GetSmsHistoryMainResponse;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsDataResponse;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsMainResponse;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsResponse;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Emanuele on 14/04/2017.
 */
@Slf4j
public class MockClickSendClient implements SmsClient {

    @Override
    public SendSmsMainResponse sendSms(List<SmsRequest> smsRequestList) throws UnirestException {
        Random rnd = new Random();
        if (rnd.nextBoolean()) {
            throw new UnirestException("BUM");
        }

        List<SendSmsResponse> sendSmsResponseList = new LinkedList<>();
        for (SmsRequest smsRequest : smsRequestList) {
            sendSmsResponseList.add(SendSmsResponse.builder().body(smsRequest.getBody()).to(smsRequest.getTo())
                    .customString(smsRequest.getCustomString()).from(smsRequest.getFrom())
                    .messageId(UUID.randomUUID().toString()).messageParts(1).messagePrice(new BigDecimal("0.010"))
                    .status("SUCCESS").build());
        }
        SendSmsDataResponse sendSmsDataResponse = SendSmsDataResponse.builder().sendSmsResponseList(sendSmsResponseList)
                .build();
        SendSmsMainResponse sendSmsMainResponse = SendSmsMainResponse.builder().sendSmsDataResponse(sendSmsDataResponse)
                .build();
        sendSmsMainResponse.setHttpCode(200);
        sendSmsMainResponse.setResponseCode("SUCCESS");
        return sendSmsMainResponse;
    }

    @Override
    public GetSmsHistoryMainResponse getSmsHistory(long fromDate, long toDate, long page) throws UnirestException {
        return null;
    }

    @Override
    public CalcSmsInfo calculateSmsPrice(List<String> recipientNumberList, String messageText, String numeroMittente)
            throws UnableToCalculateSmsPrice {
        return CalcSmsInfo.builder().smsCount(10).destinatariCount(10).build();
    }
}
