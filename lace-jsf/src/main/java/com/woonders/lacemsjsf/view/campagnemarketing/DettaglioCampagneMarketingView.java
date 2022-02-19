package com.woonders.lacemsjsf.view.campagnemarketing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.CampagnaSmsViewModel;
import com.woonders.lacemscommon.app.viewmodel.CampagnaViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;
import com.woonders.lacemscommon.service.CampagnaMarketingFacade;
import com.woonders.lacemscommon.service.CampagnaMarketingService;
import com.woonders.lacemscommon.service.impl.CampagnaMarketingFacadeImpl;
import com.woonders.lacemscommon.service.impl.CampagnaMarketingServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 29/03/2017.
 */
@ManagedBean(name = DettaglioCampagneMarketingView.NAME)
@ViewScoped
@Getter
@Setter
public class DettaglioCampagneMarketingView implements Serializable {

	public static final String NAME = "dettaglioCampagneMarketingView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	private LazyDataModel<CampagnaSmsViewModel> campagnaSmsViewModelLazyDataModel;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(CampagnaMarketingServiceImpl.JSF_NAME)
	private CampagnaMarketingService campagnaMarketingService;
	private CampagnaViewModel campagnaViewModel;
	@ManagedProperty(CampagnaMarketingFacadeImpl.JSF_NAME)
	private CampagnaMarketingFacade campagnaMarketingFacade;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;

	@PostConstruct
	public void init() {

		campagnaViewModel = campagnaMarketingService.findCampagna(
				(long) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.CAMPAGNA_ID));

		campagnaSmsViewModelLazyDataModel = new LazyDataModel<CampagnaSmsViewModel>() {

			@Override
			public List<CampagnaSmsViewModel> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
				for (Map.Entry<String, Object> entry : filters.entrySet()) {
					tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
				}
				ViewModelPage<CampagnaSmsViewModel> viewModelPage = campagnaMarketingService.getCampagnaSmsList(
						campagnaViewModel.getId(), first, pageSize, sortField,
						com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);

				setRowCount((int) viewModelPage.getTotalElements());
				return viewModelPage.getContent();
			}

			@Override
			public CampagnaSmsViewModel getRowData(String rowKey) {
				return campagnaMarketingService.findCampagnaSms(Long.parseLong(rowKey));
			}
		};

	}

	public void importaNominativiAction() {
		campagnaMarketingFacade.importaNominativi(campagnaViewModel.getId());
		FacesUtil.addMessage(propertiesUtil.getMsgImportazioneStart());
	}

	public boolean isRenderedInviaSmsButton() {
		long idCampagna = (long) passaggioParametriUtils.getParametri()
				.get(PassaggioParametriUtils.Parametro.CAMPAGNA_ID);
		return campagnaMarketingService.countByListEsitoCampagnaSms(idCampagna) > 0;
	}

	public boolean isDisabledInviaSmsButton() {
		return httpSessionUtil.getOperatorId() != campagnaViewModel.getCreatorOperatorViewModel().getId();
	}

	public void invioSmsNonSpediti() {
		long idCampagna = (long) passaggioParametriUtils.getParametri()
				.get(PassaggioParametriUtils.Parametro.CAMPAGNA_ID);
		try {
			campagnaMarketingFacade.sendCampagna(idCampagna, httpSessionUtil.getOperatorId(), aziendaSelectionView.getCurrentAziendaId());
			FacesUtil.addMessage(propertiesUtil.getMsgSendCampagnaSmsNonSpediti(), FacesMessage.SEVERITY_INFO);
		} catch (NotEnoughCreditException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgSmsErrorNotEnoughBalance(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public boolean canImportCampagna() {
		return httpSessionUtil.hasAnyAuthority(Role.RoleName.getNominativiWriteRoles());
	}

}
