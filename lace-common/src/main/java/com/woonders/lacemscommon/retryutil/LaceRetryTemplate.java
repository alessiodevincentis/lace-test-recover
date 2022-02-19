package com.woonders.lacemscommon.retryutil;

import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Emanuele on 27/02/2017.
 */
@Component
// TODO non mi fa impazzire per niente, ma per il momento non ho capito
// come farlo meglio. es. farlo in un thread separato per non far
// aspettare l'utente
public class LaceRetryTemplate extends RetryTemplate {

	public LaceRetryTemplate() {
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3);

		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(1500);

		setRetryPolicy(retryPolicy);
		setBackOffPolicy(backOffPolicy);
	}

}
