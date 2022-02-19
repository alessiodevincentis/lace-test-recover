package com.woonders.lacemsjsf.interceptor;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.config.TenantManager;
import com.woonders.lacemsjsf.navigation.DomainManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Emanuele on 20/01/2017.
 */
@Slf4j
@WebFilter("/*")
public class CheckTenantSubdomainRequestInterceptor implements Filter {

	private final String ERROR_404_PAGE_URL = "http://" + Constants.APP_DOMAIN + "/404.xhtml";
	@Autowired
	private TenantManager tenantManager;
	@Autowired
	private DomainManager domainManager;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = ((HttpServletRequest) servletRequest);
		String hostname = httpServletRequest.getHeader("host");
		// remove the hostname and eventually remaining dots
		String tenantName = hostname.replace(Constants.APP_DOMAIN, "").replace(".", "");
		if (domainManager.isFromMainDomain(hostname)) {
			if (httpServletRequest.getServletPath().endsWith("login.xhtml")) {
				((HttpServletResponse) servletResponse).sendRedirect(ERROR_404_PAGE_URL);
			} else {
				filterChain.doFilter(servletRequest, servletResponse);
			}
		} else {
			if (tenantManager.isTenantAvailable(tenantName)) {
				if (httpServletRequest.getServletPath().endsWith("index.xhtml")
						|| httpServletRequest.getServletPath().endsWith("choosedomain.xhtml")) {
					((HttpServletResponse) servletResponse).sendRedirect("/app/login.xhtml");
				} else {
					filterChain.doFilter(servletRequest, servletResponse);
				}
			} else {
				((HttpServletResponse) servletResponse).sendRedirect(ERROR_404_PAGE_URL);
			}
		}
	}

	@Override
	public void destroy() {

	}
}
