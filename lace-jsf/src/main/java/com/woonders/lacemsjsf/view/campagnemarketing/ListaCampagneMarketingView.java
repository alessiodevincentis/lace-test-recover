package com.woonders.lacemsjsf.view.campagnemarketing;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.CampagnaViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.service.CampagnaMarketingService;
import com.woonders.lacemscommon.service.impl.CampagnaMarketingServiceImpl;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
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

/**
 * Created by Emanuele on 29/03/2017.
 */
@ManagedBean(name = ListaCampagneMarketingView.NAME)
@ViewScoped
@Getter
@Setter
@Slf4j
public class ListaCampagneMarketingView implements Serializable {

	public static final String NAME = "listaCampagneMarketingView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	private LazyDataModel<CampagnaViewModel> campagnaViewModelLazyDataModel;
	private CampagnaViewModel campagnaViewModelSelected;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(CampagnaMarketingServiceImpl.JSF_NAME)
	private CampagnaMarketingService campagnaMarketingService;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;

	@PostConstruct
	public void init() {

		campagnaViewModelLazyDataModel = new LazyDataModel<CampagnaViewModel>() {

			@Override
			public List<CampagnaViewModel> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
				for (Map.Entry<String, Object> entry : filters.entrySet()) {
					tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
				}

				ViewModelPage<CampagnaViewModel> viewModelPage = campagnaMarketingService.getCampagnaList(aziendaSelectionView.getCurrentAziendaId(), first,
						pageSize, sortField,
						com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);

				setRowCount((int) viewModelPage.getTotalElements());
				return viewModelPage.getContent();
			}

			@Override
			public CampagnaViewModel getRowData(String rowKey) {
				return campagnaMarketingService.findCampagna(Long.parseLong(rowKey));
			}
		};
	}

	public void onRowSelect(SelectEvent event) {
		try {
			if (event.getObject() != null) {
				CampagnaViewModel campagnaViewModel = (CampagnaViewModel) event.getObject();
				passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.CAMPAGNA_ID,
						campagnaViewModel.getId());
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(Constants.getDettaglioCampagnaMarketingPath(true));
			}
		} catch (IOException e) {
			log.error("Errore nel selezionare una campagna", e);
		}
	}

}
