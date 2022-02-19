package com.woonders.lacemsjsf.navigation;

import com.woonders.lacemscommon.config.Constants;
import org.springframework.stereotype.Component;

/**
 * Created by emanuele on 24/11/16.
 */
@Component
public class DomainManager {

	public static final String NAME = "domainManager";
	public static final String JSF_NAME = "#{" + NAME + "}";

	public boolean isFromMainDomain(String hostname) {
		hostname = hostname.replace("www", "");
		hostname = hostname.replace(Constants.APP_DOMAIN, "");
		hostname = hostname.replace(".", "");
		hostname = hostname.replace(":", "");
		hostname = hostname.replace("80", "");
		hostname = hostname.replace("localhost:8080", "");
		return hostname.isEmpty();
	}

}
