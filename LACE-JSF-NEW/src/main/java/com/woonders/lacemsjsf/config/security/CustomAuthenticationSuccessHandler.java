package com.woonders.lacemsjsf.config.security;

import com.woonders.lacemscommon.app.viewmodel.ResetPasswordViewModel;
import com.woonders.lacemscommon.app.viewmodel.TimeParametersViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.network.NetworkUtil;
import com.woonders.lacemscommon.security.CustomSecurityUser;
import com.woonders.lacemscommon.service.AccessLogService;
import com.woonders.lacemscommon.service.ResetPasswordService;
import com.woonders.lacemscommon.service.TimeParametersService;
import com.woonders.lacemscommon.service.impl.LoginAttemptServiceImpl;
import com.woonders.lacemscommon.util.DateToCalendar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Emanuele on 14/09/2016.
 */
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	private LoginAttemptServiceImpl loginAttemptService;

	@Autowired
	private NetworkUtil networkUtil;

	@Autowired
	private AccessLogService accessLogService;
	
	@Autowired
	private ResetPasswordService resetPasswordService;
	@Autowired
	private TimeParametersService timeParametersService;
	@Autowired
    private OperatorRepository operatorRepository;


	@Override
	public void onAuthenticationSuccess(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final Authentication authentication)
			throws IOException {
		accessLogService.saveLogin(authentication.getName());
		loginAttemptService.storeSucceededLogin(networkUtil.getRequestIp());
		final List<String> authorityList = authentication.getAuthorities().stream().map(auth -> (auth.toString()))
				.collect(Collectors.toList());
		
		/**
		 * Create by Cristian on 22-10-2021
		 * Controllo che la scadenza della password dell'utente per mandare in reset 
		 */
		/*
		 * Devo recuperare nella tabella time_parameters il valore del code SP "scadenza passowrd"
		 */
		int scadenzaPassword = 30;
		
		try {
			TimeParametersViewModel timeParameters = timeParametersService.findByCodeIdentify("SP");
			scadenzaPassword = (timeParameters != null) ? Integer.parseInt(timeParameters.getValueParameter()) : 30;
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Calendar now = Calendar.getInstance();
		Date todaysDate = now.getTime();
		CustomSecurityUser obj = (CustomSecurityUser) authentication.getPrincipal();
		long id = obj.getId();
		ResetPasswordViewModel resetPswViewModel = resetPasswordService.getOne(id);
		Operator operator = operatorRepository.findOne(id);
		boolean passwordScaduta = false;
		if (resetPswViewModel == null)
		{
			resetPasswordService.saveResetPassword(operator, todaysDate, scadenzaPassword, true);
		}
		else
		{
			long diffDate = DateToCalendar.differenceDatesInDays(resetPswViewModel.getModification_data(), todaysDate);
			if (diffDate > scadenzaPassword)
				passwordScaduta = true;
			
		}
		
		
		
		
		/*************************************/
		
		if (authorityList.contains(Role.RoleName.ANTI_RICICLAGGIO_OUT.toString())) {
			redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse,
					Constants.getAntiriciclaggioPath(true));
		} else if (passwordScaduta)	{
			redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, Constants.getResetPasswordPath(false));
		}
		else {
			redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, Constants.getDashboardPath(true));
		}
	}
}
