package com.woonders.lacemsjsf.view.dashboard;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.service.DashboardService;
import com.woonders.lacemscommon.service.impl.DashboardServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "datatableDashboardView")
@ViewScoped
@Getter
@Setter
public class DatatableDashboardView implements Serializable {

	private LazyDataModel<ClientePratica> clientePraticaLazyDataModel;
	private ClientePratica selectedCliente;
	@ManagedProperty(DashboardServiceImpl.JSF_NAME)
	private DashboardService dashboardService;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;

	@PostConstruct
	public void init() {
		clientePraticaLazyDataModel = new LazyDataModel<ClientePratica>() {
			@Override
			public List<ClientePratica> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				ViewModelPage<ClientePratica> viewModelPage = dashboardService.getLastPraticheClientiCaricati(
						aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(),
						httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
						first, pageSize, sortField,
						com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), filters);
				// jsf di merda che non prendi i long!
				setRowCount((int) viewModelPage.getTotalElements());
				return viewModelPage.getContent();
			}

			@Override
			public ClientePratica getRowData(String rowKey) {
				return dashboardService.getPratica(Long.parseLong(rowKey));
			}
		};
	}

	public void onRowSelect() throws IOException {
		// qui, e ovunque lo usiamo, dobbiamo passare solo l'id!!
		passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_SELECTED_ON_SEARCH, selectedCliente);
		FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsPath(false));
	}

}
