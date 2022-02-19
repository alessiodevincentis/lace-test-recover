package com.woonders.lacemsjsf.view.deleteclientepratica;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.service.DeleteClienteService;
import com.woonders.lacemscommon.service.impl.DeleteClienteServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = DatatableDeletePraticaView.NAME)
@ViewScoped
@Getter
@Setter
public class DatatableDeletePraticaView implements Serializable {

	public static final String NAME = "datatableDeletePraticaView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	private static final Logger logger = LoggerFactory.getLogger(DatatableDeletePraticaView.class);
	private List<ClientePratica> clientiList;
	private ClientePratica selectedPratica;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(DeleteClienteServiceImpl.JSF_NAME)
	private DeleteClienteService deleteClienteService;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	@ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
	private RequestTenantIdentifierResolver requestTenantIdentifierResolver;

	@PostConstruct
	public void init() {
		retrieveListPraticheToDeleteFromParameter();
	}

	private void retrieveListPraticheToDeleteFromParameter() {
		clientiList = (List<ClientePratica>) passaggioParametriUtils.getParametri()
				.get(Parametro.DELETE_PRATICA_LIST_PARAMETER);
		passaggioParametriUtils.getParametri().remove(Parametro.DELETE_PRATICA_LIST_PARAMETER);
	}

	public void onRowSelect() throws IOException {

		try {
			deleteClienteService.deletePratica(requestTenantIdentifierResolver.getTenantIdentifier(), selectedPratica.getPraticaViewModel().getCodicePratica());
			FacesUtil.addMessage(propertiesUtil.getMsgDeletePraticaSuccess());
			logger.info(propertiesUtil.getMsgDeletePraticaSuccess());
			FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getDeletePath(false));
		} catch (final DataAccessException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgDeleteError(), FacesMessage.SEVERITY_ERROR);
			logger.error(propertiesUtil.getMsgDeleteError(), e);
			FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getDeletePath(false));
		}
	}

	public void confirmDelete() {
		RequestContext.getCurrentInstance().execute("PF('confirmDialogWidgetVar').show();");
	}
}
