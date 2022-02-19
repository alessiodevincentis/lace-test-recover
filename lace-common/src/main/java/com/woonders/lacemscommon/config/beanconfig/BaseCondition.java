package com.woonders.lacemscommon.config.beanconfig;

/**
 * Created by Emanuele on 14/04/2017.
 */
public class BaseCondition {

	public boolean isProduction() {
		return System.getProperty("sentry.environment").equalsIgnoreCase("production");
	}
}
