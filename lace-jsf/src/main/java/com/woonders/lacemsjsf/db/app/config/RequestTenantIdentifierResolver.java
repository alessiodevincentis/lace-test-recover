package com.woonders.lacemsjsf.db.app.config;


import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.AsyncTenantStorage;
import com.woonders.lacemscommon.util.RequestUtil;

/**
 * Created by emanuele on 23/12/16.
 */
@Component
public class RequestTenantIdentifierResolver
		implements CurrentTenantIdentifierResolver, ApplicationListener<ContextRefreshedEvent> {

	public static final String NAME = "requestTenantIdentifierResolver";
	public static final String JSF_NAME = "#{" + NAME + "}";

	private boolean contextLoaded;

	@Autowired
	private RequestUtil requestUtil;
	@Autowired
	private AsyncTenantStorage asyncTenantStorage;

	@Override
	public String resolveCurrentTenantIdentifier() {
		return getTenantIdentifier();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

	/**
	 * Controlla prima se e stata eseguita una normale request http, allora prende il tenant dall host
	 * Altrimenti, proviamo a prendere il tenant dall asyncStorage.
	 * FONDAMENTALE eseguire prima il controllo sulla request altrimenti eventuali requests mentre e in esecuzione
	 * un metodo async prenderebbero il tenant dell async e non quello corretto della request!!!!!!
	 */
	public String getTenantIdentifier() {
		if (contextLoaded) {
			String tenantName;
			if (!StringUtils.isEmpty(requestUtil.getTenantName())) {
				//serve per rendere disponibile a logback altre property che possiamo usare nella configurazione, in questo caso per creare un file di log per ogni tenant
				tenantName = requestUtil.getTenantName();
				MDC.put(com.woonders.lacemscommon.config.Constants.TENANT_NAME_LOG_KEY, tenantName);
				return tenantName;
			} else if(!StringUtils.isEmpty(asyncTenantStorage.getTenantToUseName())) {
				tenantName = asyncTenantStorage.getTenantToUseName();
				MDC.put(com.woonders.lacemscommon.config.Constants.TENANT_NAME_LOG_KEY, tenantName);
				return tenantName;
			} else {
				// rimuoviamo il tenantName non esiste il tenant che ci hanno mandato
				MDC.remove(com.woonders.lacemscommon.config.Constants.TENANT_NAME_LOG_KEY);
				throw new RuntimeException("Tenant not found! HttpServletRequest was null");
			}
		} else {
			// rimuoviamo il tenantName non esiste il tenant che ci hanno mandato
			MDC.remove(com.woonders.lacemscommon.config.Constants.TENANT_NAME_LOG_KEY);
			return com.woonders.lacemscommon.config.Constants.EMPTY_DEFAULT_DB_NAME;
		}
	}

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
		contextLoaded = true;
	}

}
