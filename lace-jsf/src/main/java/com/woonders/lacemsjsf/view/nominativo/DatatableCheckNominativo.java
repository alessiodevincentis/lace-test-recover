package com.woonders.lacemsjsf.view.nominativo;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.service.CheckNominativoService;
import com.woonders.lacemscommon.service.NominativoService;
import com.woonders.lacemscommon.service.impl.CheckNominativoServiceImpl;
import com.woonders.lacemscommon.service.impl.NominativoServiceImpl;
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

@ManagedBean(name = "datatableCheckNominativo")
@ViewScoped
@Getter
@Setter
public class DatatableCheckNominativo implements Serializable {

	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(CheckNominativoServiceImpl.JSF_NAME)
	private CheckNominativoService checkNominativoService;
	@ManagedProperty(NominativoServiceImpl.JSF_NAME)
	private NominativoService nominativoService;
	private List<ClienteViewModel> nominativiViewModelList = new ArrayList<>();
	private ClienteViewModel selectedNominativoViewModel;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	private String cognome;
	private String nome;
	private Date data;
	private Cliente.Sesso sesso;
	private String luogo;
	private String telefonoCellulare;
	private String telefonoFisso;
	@ManagedProperty(CheckPermissionUtils.JSF_NAME)
	private CheckPermissionUtils checkPermissionUtils;

	@PostConstruct
	public void init() {
		checkNominativi();
	}

	public void onRowSelect() throws IOException {

		if (Tipo.NOMINATIVO.getValue().equalsIgnoreCase(selectedNominativoViewModel.getTipo())) {
			if (!checkPermissionUtils.isAccessibleFromOperator(selectedNominativoViewModel)) {
				FacesUtil.showMessageInDialog(FacesMessage.SEVERITY_WARN, propertiesUtil.getMsgAttenzione(),
						propertiesUtil.getMsgAccessoNonConsentito());
			} else {
				passaggioParametriUtils.getParametri().put(Parametro.NOMINATIVO_SELECTED_ON_SEARCH,
						selectedNominativoViewModel);
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(Constants.getFormsNominativoPath(false));
			}
		} else if (Tipo.CLIENTE.getValue().equalsIgnoreCase(selectedNominativoViewModel.getTipo())) {
			passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_DA_CHECK_NOMINATIVO_PARAMETER,
					selectedNominativoViewModel.getCognome());
			FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getDatatablePath(false));
		}
	}

	private void checkNominativi() {
		nominativiViewModelList = (List<ClienteViewModel>) passaggioParametriUtils.getParametri()
				.get(Parametro.NOMINATIVI_CLIENTI_LIST_CHECK_NOMINATIVO_PARAMETER);
		passaggioParametriUtils.getParametri().remove(Parametro.NOMINATIVI_CLIENTI_LIST_CHECK_NOMINATIVO_PARAMETER);
	}

	public String valueOfOperator(final ClienteViewModel clienteNominativoViewModel) {
		if (Tipo.CLIENTE.getValue().equalsIgnoreCase(clienteNominativoViewModel.getTipo())) {
			return Constants.getEmptyString();
		} else {
			return clienteNominativoViewModel.getOperatoreNominativo().getUsername();
		}
	}

	public void goToFormsNominativo() throws IOException {
		passaggioParametriUtils.getParametri().put(Parametro.NUOVO_NOMINATIVO_PARAMETER, true);
		FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsNominativoPath(false));
	}

}
