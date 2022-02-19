package com.woonders.lacemsjsf.config.security;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.network.NetworkUtil;
import com.woonders.lacemscommon.service.impl.LoginAttemptServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.viewmodel.ExceptionErrorViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Emanuele on 14/09/2016.
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
	@Autowired
	private PropertiesUtil propertiesUtil;
	@Autowired
	private LoginAttemptServiceImpl loginAttemptService;

	@Autowired
	private NetworkUtil networkUtil;

	@Override
	public void onAuthenticationFailure(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final AuthenticationException e)
			throws IOException, ServletException {
		logger.info("Invalid login", e);
		final ExceptionErrorViewModel exceptionErrorViewModel = new ExceptionErrorViewModel();
		exceptionErrorViewModel.setException(e);
		if (loginAttemptService.isBlocked(networkUtil.getRequestIp())) {
			exceptionErrorViewModel.setMessage(propertiesUtil.getMsgBlockedIp());
		} else {
			exceptionErrorViewModel.setMessage(propertiesUtil.getMsgInvalidLogin());
		}
		httpServletRequest.getSession().setAttribute(Constants.EXCEPTION_ERROR, exceptionErrorViewModel);
		loginAttemptService.storeFailedLogin(networkUtil.getRequestIp());
		httpServletResponse.sendRedirect(Constants.getLoginPath(true));
	}
}
