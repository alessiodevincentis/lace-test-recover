package com.woonders.lacemsjsf.login;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.viewmodel.ExceptionErrorViewModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Emanuele on 14/09/2016.
 */
@ManagedBean
@RequestScoped
public class LoginBackingBean implements Serializable {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String loginAction() throws IOException, ServletException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		final RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
				.getRequestDispatcher("/j_spring_security_check");
		dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
		FacesContext.getCurrentInstance().responseComplete();
		
		return null;
	}

	public void updateMessages() {
		// retrieve eventual exception set in CustomAuthenticationFailureHandler
		// http://stackoverflow.com/questions/8919103/jsf-spring-security-login-error-message
		final ExceptionErrorViewModel exceptionErrorViewModel = (ExceptionErrorViewModel) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(Constants.EXCEPTION_ERROR);

		if (exceptionErrorViewModel != null) {
			FacesUtil.addMessage(exceptionErrorViewModel.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void logoutAction() throws IOException {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout((HttpServletRequest) context.getRequest(),
					(HttpServletResponse) context.getResponse(), auth);
		}
		FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getLoginPath(false));
	}
}
