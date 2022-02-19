package com.woonders.lacemscommon.sms;

import java.util.List;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.response.GetSmsHistoryMainResponse;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsMainResponse;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;
import com.woonders.lacemscommon.network.BaseClient;
import com.woonders.lacemscommon.sms.aws.AWSSmsManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AwsSnsClient extends BaseClient implements SmsClient {

	@Override
	public SendSmsMainResponse sendSms(List<SmsRequest> smsRequestList) throws UnirestException {
		for(SmsRequest smsReq : smsRequestList) {
    		String senderNumber = smsReq.getFrom();
    		String body         = smsReq.getBody();
    		String recipient    = smsReq.getTo();

			try {
				AWSSmsManager.shared().sendSMSMessage(senderNumber, body, recipient);
			} catch (SmsNotSentException e) {
				throw new UnirestException(e);
			}
		}
		return null;
	}

	@Override
	public GetSmsHistoryMainResponse getSmsHistory(long fromDate, long toDate, long page) throws UnirestException {
		// TODO 
		return null;
	}

	@Override
	public CalcSmsInfo calculateSmsPrice(List<String> recipientNumberList, String messageText, String numeroMittente)
			throws UnableToCalculateSmsPrice {
		log.info("AwsSnsClient.calculateSMSPrice - num of sms per message: " + SMSLenghtCalculator.getPartCount(messageText));
		log.info("AwsSnsClient.calculateSMSPrice - num of recipients: " + recipientNumberList.size());
		return CalcSmsInfo.builder().smsCount(SMSLenghtCalculator.getPartCount(messageText)).destinatariCount(recipientNumberList.size()).build();
	}

	@Override
	protected void addAuthorization(HttpRequest httpRequest) {
		// TODO Auto-generated method stub
		
	}

}
