package com.woonders.lacemscommon.config.beanconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.woonders.lacemscommon.sms.AwsSnsClient;
import com.woonders.lacemscommon.sms.MockClickSendClient;
import com.woonders.lacemscommon.sms.SmsClient;

/**
 * Created by Emanuele on 14/04/2017. Spettacolo!
 * http://stackoverflow.com/questions/34350865/spring-choose-bean-implementation-at-runtime
 */
@Configuration
public class ProductionBeanConfiguration {

	@Bean
	@Conditional(MockCondition.class)
	public SmsClient mockSmsClient() {
		return new MockClickSendClient();

	}

	@Bean
	@Conditional(ProductionCondition.class)
	public SmsClient smsClient() {
//		return new ClickSendClient();
		return new AwsSnsClient();
	}
}
