package com.woonders.lacemscommon.config.beanconfig;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by Emanuele on 14/04/2017.
 */
public class MockCondition extends BaseCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return !isProduction();
	}
}
