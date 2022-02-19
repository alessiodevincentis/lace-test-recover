package com.woonders.lacemsjsf.navigation;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.config.TenantManager;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;


/**
 * Created by emanuele on 24/11/16.
 */
@RequestScoped
@ManagedBean
@Getter
@Setter
public class ChooseDomainView implements Serializable {

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	@ManagedProperty(DomainManager.JSF_NAME)
	private DomainManager domainManager;
	private String tenantDomain;
	@ManagedProperty(TenantManager.JSF_NAME)
	private TenantManager tenantManager;

	public String sendToLoginOrDomain() {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		final String hostname = httpServletRequest.getHeader("host");
		if (domainManager.isFromMainDomain(hostname)) {
			return Constants.getChooseDomainPath(true);
		} else {
			return Constants.getLoginPath(true);
		}
	}

	public void goToAgency() throws IOException {
		if (tenantManager.isTenantAvailable(tenantDomain)) {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("http://" + tenantDomain + "." + Constants.APP_DOMAIN + Constants.getLoginPath(false));
		} else {
			FacesUtil.addMessage(propertiesUtil.getMsgAgenziaNotFound(), FacesMessage.SEVERITY_WARN);
		}

	}
}
