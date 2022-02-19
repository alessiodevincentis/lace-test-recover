package com.woonders.lacemscommon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.woonders.lacemscommon.app.clicksend.model.request.SendSmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.network.typeadapter.ClickSendLocalDateTimeTypeAdapter;
import com.woonders.lacemscommon.network.typeadapter.ItalianPatternLocalDateTypeAdapter;
import com.woonders.lacemscommon.sms.MockClickSendClient;
import com.woonders.lacemscommon.sms.SmsClient;

/**
 * Created by Emanuele on 16/01/2017. numero di test, non viene scalato il
 * credito, risponde sempre ok! +61411111111
 */

public class MarketingServiceImplTest {

	// private Gson gson = new Gson();

	// USARE SOLO PER TEST MANUALI, ANDREBBE MOCKATO!

	@Before
	public void setObjectMapper() {
		Unirest.setObjectMapper(new ObjectMapper() {
			private Gson fattureInCloudGson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new ItalianPatternLocalDateTypeAdapter()).create();
			private Gson clickSendGson = new GsonBuilder()
					.registerTypeAdapter(LocalDateTime.class, new ClickSendLocalDateTimeTypeAdapter()).create();

			@Override
			public <T> T readValue(String value, Class<T> valueType) {
				if (value != null) {
					if (valueType.toString().contains("fatture")) {
						return fattureInCloudGson.fromJson(value, valueType);
					} else {
						return clickSendGson.fromJson(value, valueType);
					}
				}
				return null;
			}

			@Override
			public String writeValue(Object value) {
				if (value != null) {
					if (value.getClass().getPackage().toString().contains("fatture")) {
						return fattureInCloudGson.toJson(value);
					} else {
						return clickSendGson.toJson(value);
					}
				}
				return null;
			}
		});
	}

	@Test
	public void sendSmsTest() {

		SendSmsRequest messages = new SendSmsRequest();
		SmsRequest sms = SmsRequest.builder().from("ciao").to("+393936268353")
				.body("bellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabellabella")
				.build();
		List<SmsRequest> smsList = new LinkedList<>();
		smsList.add(sms);
		messages.setSmsRequestList(smsList);

		SmsClient smsClient = new MockClickSendClient();
		// l'init ora sta solo dentro fatture in cloud perche ho centralizzato
		// il codice di Unirest, questo test andra sistemato un giorno!!
		/*
		 * try { // SendSmsMainResponse sendSmsResponse =
		 * clickSendClient.sendSms(); // GetSmsHistoryMainResponse
		 * getSmsHistoryMainResponse = // clickSendClient.getSmsHistory(0, 0,
		 * 1); // GetSmsHistoryMainResponse getSmsHistoryMainResponse2 = //
		 * clickSendClient.getSmsHistory(0, 0, 2); SendSmsMainResponse
		 * sendSmsMainResponse = clickSendClient.calculateSmsPrice(smsList); int
		 * a = 1; } catch (UnirestException e) { e.printStackTrace(); }
		 */

	}
}
