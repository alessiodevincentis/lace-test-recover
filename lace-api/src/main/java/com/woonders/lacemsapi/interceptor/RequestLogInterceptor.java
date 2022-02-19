package com.woonders.lacemsapi.interceptor;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woonders.lacemsapi.controller.DevisProxPushLeadDisputeController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.woonders.lacemsapi.config.app.RequestTenantIdentifierResolver;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.config.TenantManager;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 15/11/2016.
 */
@Slf4j
@Component
public class RequestLogInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
	@Autowired
	private TenantManager tenantManager;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {
		final long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		log.info("Request URL::" + request.getRequestURL().toString() + ":: Start Time=" + System.currentTimeMillis());

		if (request.getRequestURL().toString().contains((DevisProxPushLeadDisputeController.PUSH_LEAD_DISPUTE_URL))){
			log.info("JSON REQUEST, RETURNING TRUE");
			return true;
		}

		try {
			String tenantName = requestTenantIdentifierResolver.getTenantIdentifier();
			log.info("tenantName: " + tenantName);
			if (!StringUtils.isEmpty(tenantName) && tenantManager.isTenantAvailable(tenantName)) {
				MDC.put(Constants.TENANT_NAME_LOG_KEY, tenantName);
			} else {
				MDC.remove(Constants.TENANT_NAME_LOG_KEY);
			}
		} catch (RuntimeException e) {
			// qualsiasi exception ci sia rimuoviamo il tenantName perche
			// sicuramente e esploso getTenantIdentifier perche non esiste il
			// tenant che ci hanno mandato
			log.info("Exception in RequestLogInterceptor");
			MDC.remove(Constants.TENANT_NAME_LOG_KEY);
		}

		log.info("Request parameters: " + request.getParameterMap().entrySet().stream().map(entry -> {
			String[] entryValue = entry.getValue();
			String parameters = null;
			if (entryValue != null) {
				parameters = Arrays.stream(entryValue).collect(Collectors.joining("- "));
			}
			return entry.getKey() + "=" + parameters;
		}).collect(Collectors.joining(", ")));

		// if returned false, we need to make sure 'response' is sent
		return true;
	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) throws Exception {
		log.info("Request URL::" + request.getRequestURL().toString() + " Sent to Handler :: Current Time="
				+ System.currentTimeMillis());
		// we can add attributes in the modelAndView and use that in the view
		// page
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler, final Exception ex) throws Exception {
		final long startTime = (Long) request.getAttribute("startTime");
		log.info("Request URL::" + request.getRequestURL().toString() + ":: End Time=" + System.currentTimeMillis());
		log.info("Request URL::" + request.getRequestURL().toString() + ":: Time Taken="
				+ (System.currentTimeMillis() - startTime));
	}
}
