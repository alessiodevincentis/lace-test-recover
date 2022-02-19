package com.woonders.lacemsjsf.view.nominativo;

import com.woonders.lacemscommon.app.model.ClienteTrattenuta;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.DashboardNominativoService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.DashboardNominativoServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Data;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.woonders.lacemsjsf.view.nominativo.DatatableNominativoRinnoviView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Data
public class DatatableNominativoRinnoviView implements Serializable {

	public static final String NAME = "datatableNominativoRinnoviView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(DashboardNominativoServiceImpl.JSF_NAME)
	private DashboardNominativoService dashboardNominativoService;
	@ManagedProperty(AziendaServiceImpl.JSF_NAME)
	private AziendaService aziendaService;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;

	private ClienteTrattenuta selectedClienteTrattenuta;
	private LazyDataModel<ClienteTrattenuta> clienteTrattenutaLazyDataModel;

	@PostConstruct
	public void init() {
		clienteTrattenutaLazyDataModel = new LazyDataModel<ClienteTrattenuta>() {
			@Override
			public List<ClienteTrattenuta> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
				for (Map.Entry<String, Object> entry : filters.entrySet()) {
					tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
				}
				ViewModelPage<ClienteTrattenuta> viewModelPage = dashboardNominativoService.findImpegniRinnovabili(
						aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
						aziendaService.getOne(httpSessionUtil.getAziendaId()).getGiorniNotificheNominativi(), first, pageSize, sortField,
						com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);
				// jsf di merda che non prendi i long!
				setRowCount((int) viewModelPage.getTotalElements());
				return viewModelPage.getContent();
			}

			@Override
			public ClienteTrattenuta getRowData(String rowKey) {
				return dashboardNominativoService.getTrattenuta(Long.parseLong(rowKey));
			}
		};
	}

	public void onRowSelect() throws IOException {
		if (Tipo.NOMINATIVO.getValue().equalsIgnoreCase(selectedClienteTrattenuta.getClienteViewModel().getTipo()))
			passaggioParametriUtils.getParametri().put(Parametro.NOMINATIVO_SELECTED_ON_SEARCH,
					selectedClienteTrattenuta.getClienteViewModel());
		FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsNominativoPath(false));
	}

	public void setNotiCoes() {
		dashboardNominativoService.setRinnovoTrattenute(selectedClienteTrattenuta.getTrattenuteViewModel());
	}

}
