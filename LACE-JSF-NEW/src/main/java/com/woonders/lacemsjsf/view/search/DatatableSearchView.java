package com.woonders.lacemsjsf.view.search;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.service.SearchService;
import com.woonders.lacemscommon.service.impl.SearchServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.CheckPermissionUtils;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.woonders.lacemsjsf.view.search.DatatableSearchView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class DatatableSearchView implements Serializable {

	public static final String NAME = "datatableSearchView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	@ManagedProperty(SearchServiceImpl.JSF_NAME)
	SearchService searchService;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	private List<ClientePratica> clientiList = new ArrayList<>();
	private ClientePratica selectedClienteNominativo;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	@ManagedProperty(CheckPermissionUtils.JSF_NAME)
	private CheckPermissionUtils checkPermissionUtils;

	@PostConstruct
	public void init() {
		retrieveClienteNominativoListFromParameter();
		retrieveClienteDaCheckNominativoFromParameter();
	}

	private void retrieveClienteNominativoListFromParameter() {
		clientiList = (List<ClientePratica>) passaggioParametriUtils.getParametri()
				.get(Parametro.LIST_NOMINATIVI_CLIENTI_PARAMETER);
	}

	private void retrieveClienteDaCheckNominativoFromParameter() {
		final String cognomeFromParameter = (String) passaggioParametriUtils.getParametri()
				.get(Parametro.CLIENTE_DA_CHECK_NOMINATIVO_PARAMETER);
		if (cognomeFromParameter != null) {
			clientiList = searchService.filterListByCliente(searchService.searchByCognome(cognomeFromParameter));
		}
	}

	public void onRowSelect() throws IOException {
		if (!checkPermissionUtils.isAccessibleFromOperator(selectedClienteNominativo)) {
			FacesUtil.showMessageInDialog(FacesMessage.SEVERITY_WARN, propertiesUtil.getMsgAttenzione(),
					propertiesUtil.getMsgAccessoNonConsentito());
		} else {
			if (Tipo.CLIENTE.getValue().equalsIgnoreCase(selectedClienteNominativo.getClienteViewModel().getTipo())) {
				passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_SELECTED_ON_SEARCH,
						selectedClienteNominativo);
				FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsPath(false));

			} else if (Tipo.NOMINATIVO.getValue()
					.equalsIgnoreCase(selectedClienteNominativo.getClienteViewModel().getTipo())) {
				final ClienteViewModel nominativoViewModel = searchService
						.getSelectedClientePratica(selectedClienteNominativo);
				passaggioParametriUtils.getParametri().put(Parametro.NOMINATIVO_SELECTED_ON_SEARCH,
						nominativoViewModel);
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(Constants.getFormsNominativoPath(false));
			}
		}
	}

	public String valueOfTypeOperation(final ClientePratica clienteNominativo) {
		if (Tipo.CLIENTE.getValue().equalsIgnoreCase(clienteNominativo.getClienteViewModel().getTipo())) {
			return clienteNominativo.getPraticaViewModel().getTipoPratica();
		} else {
			return Constants.getEmptyString();
		}
	}

	public Date valueOfInsertionDate(final ClientePratica clienteNominativo) {
		if (Tipo.CLIENTE.getValue().equalsIgnoreCase(clienteNominativo.getClienteViewModel().getTipo())) {
			return clienteNominativo.getPraticaViewModel().getDataCaricamento();
		} else {
			return clienteNominativo.getClienteViewModel().getDataIns();
		}
	}

	public String valueOfOperator(final ClientePratica clienteNominativo) {
		if (Tipo.CLIENTE.getValue().equalsIgnoreCase(clienteNominativo.getClienteViewModel().getTipo())) {
			return clienteNominativo.getPraticaViewModel().getOperatore().getUsername();
		} else {
			return clienteNominativo.getClienteViewModel().getOperatoreNominativo().getUsername();
		}
	}

	public String getRowKey(final ClientePratica selectedClienteNominativo) {
		if (Tipo.CLIENTE.getValue().equalsIgnoreCase(selectedClienteNominativo.getClienteViewModel().getTipo())) {
			return Tipo.CLIENTE.toString() + selectedClienteNominativo.getPraticaViewModel().getCodicePratica();
		} else {
			return Tipo.NOMINATIVO.toString() + selectedClienteNominativo.getClienteViewModel().getId();
		}
	}

}
