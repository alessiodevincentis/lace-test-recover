package com.woonders.lacemscommon.sms;

import com.google.common.collect.Iterables;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.woonders.lacemscommon.app.clicksend.model.request.SendSmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.response.GetSmsHistoryMainResponse;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsMainResponse;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;
import com.woonders.lacemscommon.network.BaseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by Emanuele on 16/01/2017.
 */
@Slf4j
public class ClickSendClient extends BaseClient implements SmsClient {

    private final static String CLICK_SEND_BASE_URL = "https://rest.clicksend.com/v3";
    private final static String USERNAME = "lacems";
    private final static String KEY = "3CD610F3-88ED-45BA-CB92-29DA510E37BA";
    private final static String SEND_SMS_URL = CLICK_SEND_BASE_URL + "/sms/send";
    private final static String GET_SMS_HISTORY_URL = CLICK_SEND_BASE_URL + "/sms/history";
    private final static String CALCULATE_SMS_PRICE_URL = CLICK_SEND_BASE_URL + "/sms/price";
    private final static String ENCODED_AUTH_STRING = "Basic "
            + Base64.getEncoder().encodeToString((USERNAME + ":" + KEY).getBytes());
    private final static String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    protected void addAuthorization(HttpRequest httpRequest) {
        httpRequest.header(AUTHORIZATION_HEADER, ENCODED_AUTH_STRING);
    }

    @Override
    public SendSmsMainResponse sendSms(List<SmsRequest> smsRequestList) throws UnirestException {
        // numero di test, non viene scalato il credito, risponde sempre ok!
        // +61411111111
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSmsRequestList(smsRequestList);
        return createPostRequest(SEND_SMS_URL, sendSmsRequest).asObject(SendSmsMainResponse.class).getBody();
    }

    @Override
    public GetSmsHistoryMainResponse getSmsHistory(long fromDate, long toDate, long page) throws UnirestException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("date_from", new Date().getTime() - 50000000);
        parameterMap.put("date_to", new Date().getTime());
        parameterMap.put("page", page);
        return createGetRequest(GET_SMS_HISTORY_URL, parameterMap).asObject(GetSmsHistoryMainResponse.class).getBody();
    }

    private CalcSmsInfo calculateSmsPrice(List<SmsRequest> smsRequestList)
            throws UnirestException, UnableToCalculateSmsPrice {
        log.info("entering calculateSmsPrice...");
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSmsRequestList(smsRequestList);
        SendSmsMainResponse sendSmsMainResponse = createPostRequest(CALCULATE_SMS_PRICE_URL, sendSmsRequest)
                .asObject(SendSmsMainResponse.class).getBody();
        if (sendSmsMainResponse != null && sendSmsMainResponse.getSendSmsDataResponse() != null
                && sendSmsMainResponse.getSendSmsDataResponse().getSendSmsResponseList() != null) {
            // esegue la somma dei message parts
            int sumMessageParts = sendSmsMainResponse.getSendSmsDataResponse().getSendSmsResponseList().stream().reduce(
                    0, (sum, sendSmsResponse) -> sum += sendSmsResponse.getMessageParts(), (sum1, sum2) -> sum1 + sum2);
            int destinatariCount = (int) sendSmsMainResponse.getSendSmsDataResponse().getSendSmsResponseList().stream()
                    .filter(value -> EsitoSmsNotificaLead.SPEDITO
                            .equals(EsitoSmsNotificaLead.fromClickSendStatusCode(value.getStatus())))
                    .count();
            log.info("sms price for list calculated!");
            return CalcSmsInfo.builder().smsCount(sumMessageParts).destinatariCount(destinatariCount).build();
        }
        throw new UnableToCalculateSmsPrice();
    }

    @Override
    public CalcSmsInfo calculateSmsPrice(List<String> recipientNumberList, String messageText, String numeroMittente)
            throws UnableToCalculateSmsPrice {
        log.info("entering calculateSmsPrice...");
        List<CalcSmsInfo> calcSmsInfoList = new LinkedList<>();
        Iterable<List<String>> partitionRecipientNumberList = Iterables.partition(recipientNumberList, 900);

        List<Future<CalcSmsInfo>> futureList = new LinkedList<>();

        for (List<String> recipientNumberList1 : partitionRecipientNumberList) {
            futureList.add(
                    threadPoolTaskExecutor.submit(new CalcSmsPriceCallable(recipientNumberList1, numeroMittente, messageText)));
        }
        log.info("futures created...");

        try {
            for (Future<CalcSmsInfo> future : futureList) {
                calcSmsInfoList.add(future.get());
            }

            log.info("got futures responses!!!");
            int destinatariCount = 0;
            int smsCount = 0;
            for (CalcSmsInfo calcSmsInfo : calcSmsInfoList) {
                destinatariCount += calcSmsInfo.getDestinatariCount();
                smsCount += calcSmsInfo.getSmsCount();
            }

            return CalcSmsInfo.builder().smsCount(smsCount).destinatariCount(destinatariCount).build();

        } catch (Exception e) {
            throw new UnableToCalculateSmsPrice(e);
        }

    }

    private class CalcSmsPriceCallable implements Callable<CalcSmsInfo> {

        private List<String> recipientNumberList;
        private String numeroMittente;
        private String messageText;

        public CalcSmsPriceCallable(List<String> recipientNumberList, String numeroMittente, String messageText) {
            this.recipientNumberList = recipientNumberList;
            this.numeroMittente = numeroMittente;
            this.messageText = messageText;
        }

        @Override
        public CalcSmsInfo call() throws Exception {
            List<SmsRequest> smsRequestList = new LinkedList<>();
            for (String recipientNumber : recipientNumberList) {
                smsRequestList.add(smsUtil.buildSmsRequest(recipientNumber, messageText, numeroMittente, null));
            }
            log.info("calling calculateSmsPrice...");
            return calculateSmsPrice(smsRequestList);
        }
    }

}
