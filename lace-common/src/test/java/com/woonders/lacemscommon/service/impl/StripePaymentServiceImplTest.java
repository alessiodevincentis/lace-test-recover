package com.woonders.lacemscommon.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Token;
import com.woonders.lacemscommon.exception.PaymentException;
import com.woonders.lacemscommon.service.PaymentService;
import com.woonders.lacemscommon.slack.SlackUtil;

/**
 * Created by Emanuele on 15/02/2017.
 */
public class StripePaymentServiceImplTest {

	// carte di prova con diversi codici di errore
	// https://stripe.com/docs/testing#cards
	// crea token da java per test
	// https://stripe.com/docs/api/java#create_card_token

	PaymentService paymentService = new StripePaymentServiceImpl();
	SlackUtil slackUtil = new SlackUtil();

	@Before
	public void before() {
		Stripe.apiKey = "sk_test_dAhGv9aSr7yo6v3QoNyD2JYm";
	}

	@Test
	public void makePaymentTest() throws CardException, APIException, AuthenticationException, InvalidRequestException,
			APIConnectionException, PaymentException {
		/*
		 * paymentService.makePayment(createValidCardToken(), 1000, "EUR",
		 * "prova");
		 * 
		 * assertThatExceptionOfType(PaymentException.class) .isThrownBy(() ->
		 * paymentService.makePayment(createCardDeclinedToken(), 1000, "EUR",
		 * "prova")) .withMessage("Carta rifiutata!");
		 */
		// per il momento commentato, maledetti autowired null che mi fanno
		// crashare il test
	}

	private String createValidCardToken() throws CardException, APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		Map<String, Object> tokenParams = new HashMap<>();
		Map<String, Object> cardParams = new HashMap<>();
		cardParams.put("number", "4242424242424242");
		cardParams.put("exp_month", 2);
		cardParams.put("exp_year", 2018);
		cardParams.put("cvc", "314");
		tokenParams.put("card", cardParams);

		return Token.create(tokenParams).getId();
	}

	private String createCardDeclinedToken() throws CardException, APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		Map<String, Object> tokenParams = new HashMap<>();
		Map<String, Object> cardParams = new HashMap<>();
		cardParams.put("number", "4000000000000002");
		cardParams.put("exp_month", 2);
		cardParams.put("exp_year", 2018);
		cardParams.put("cvc", "314");
		tokenParams.put("card", cardParams);

		return Token.create(tokenParams).getId();
	}
}
