package com.woonders.lacemsjsf.view.nominativo.statonominativo;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.service.DashboardNominativoService;
import com.woonders.lacemscommon.service.NominativoService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.StatoNominativoService;
import com.woonders.lacemscommon.service.impl.DashboardNominativoServiceImpl;
import com.woonders.lacemscommon.service.impl.NominativoServiceImpl;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.StatoNominativoServiceImpl;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = DatatableStatoNominativoView.NAME)
@ViewScoped
@Getter
@Setter
@Slf4j
public class DatatableStatoNominativoView implements Serializable {

	public static final String NAME = "datatableStatoNominativoView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	private LazyDataModel<ClienteViewModel> clienteViewModelLazyDataModel;
	private ClienteViewModel selectedNominativoViewModel;
	private ArrayList<ClienteViewModel> selectedNominativoViewModelList;
	@ManagedProperty(StatoNominativoServiceImpl.JSF_NAME)
	private StatoNominativoService statoNominativoService;
	@ManagedProperty(DashboardNominativoServiceImpl.JSF_NAME)
	private DashboardNominativoService dashboardNominativoService;
	private String statoNominativo;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;
	private int selectionLimit = 25;
	private OperatorViewModel operator;
	@ManagedProperty(NominativoServiceImpl.JSF_NAME)
	private NominativoService nominativoService;
	@ManagedProperty(OperatorServiceImpl.JSF_NAME)
	private OperatorService operatorService;
	
	private List<String> operatoriSelezionati;

	@PostConstruct
	public void init() {
		operator = null;
		selectedNominativoViewModelList = new ArrayList<ClienteViewModel>();
		statoNominativo = (String) passaggioParametriUtils.getParametri().get(Parametro.STATO_NOMINATIVO_PARAMETER);
		//*** add Cristian 14-01-22
		operatoriSelezionati = (List<String>) passaggioParametriUtils.getParametri().get(Parametro.OPERATORS);
		
		retrieveNominativiByStatoNominativo();
	}

	private void retrieveNominativiByStatoNominativo() {
		clienteViewModelLazyDataModel = new LazyDataModel<ClienteViewModel>() {

			@Override
			public List<ClienteViewModel> load(int first, int pageSize, String sortField, SortOrder sortOrder,
				Map<String, Object> filters) 
			{
				ViewModelPage<ClienteViewModel> viewModelPage;
				Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
				for (Map.Entry<String, Object> entry : filters.entrySet()) {
					tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
				}
				if (operatoriSelezionati != null && operatoriSelezionati.size() > 0)
				{
					viewModelPage = statoNominativoService.getNominativiByStatoNominativoAndOperators(
							aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
							statoNominativo, operatoriSelezionati, first, pageSize, sortField, QueryDSLHelper.SortOrder.valueOf(sortOrder.name()),
							tableFilters);
				}
				else
				{
					viewModelPage = statoNominativoService.getNominativiByStatoNominativo(
							aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
							statoNominativo, first, pageSize, sortField, QueryDSLHelper.SortOrder.valueOf(sortOrder.name()),
							tableFilters);
				}
				
				setRowCount((int) viewModelPage.getTotalElements());
				return viewModelPage.getContent();
			}

			@Override
			public ClienteViewModel getRowData(String rowKey) {
				return dashboardNominativoService.getNominativo(Long.parseLong(rowKey));
			}
		};

	}

	public void onRowSelect() throws IOException {
		if (selectedNominativoViewModelList.size() > 0) {
			selectedNominativoViewModel = selectedNominativoViewModelList.get(selectedNominativoViewModelList.size() - 1);
			if (Tipo.NOMINATIVO.getValue().equalsIgnoreCase(selectedNominativoViewModel.getTipo()))
				passaggioParametriUtils.getParametri().put(Parametro.NOMINATIVO_SELECTED_ON_SEARCH,
						selectedNominativoViewModel);
			FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsNominativoPath(false));
		}
	}

	public void onRowSelectCheckbox(SelectEvent event) throws IOException {
		log.info("ABLE --------------------------onRowSelectCheckbox");
//		log.info("selectedNominativoViewModelList size: " + selectedNominativoViewModelList.size());
		if (selectedNominativoViewModelList.size()>=selectionLimit) {
			FacesUtil.addMessage("E' possibile selezionare un massimo di " + selectionLimit + " nominativi");
			ClienteViewModel selected = (ClienteViewModel) event.getObject();
			if (selectedNominativoViewModelList.contains(selected))
				selectedNominativoViewModelList.remove(selected);
		}
//		log.info(Arrays.toString(selectedNominativoViewModelList.toArray()));
		log.info("selectedNominativoViewModelList size: " + selectedNominativoViewModelList.size());

	}
	public void onRowUnSelectCheckbox() throws IOException {
		log.info("ABLE --------------------------onRowUnSelectCheckbox");
//		log.info("selectedNominativoViewModelList size: " + selectedNominativoViewModelList.size());
//		log.info(Arrays.toString(selectedNominativoViewModelList.toArray()));
		log.info("selectedNominativoViewModelList size: " + selectedNominativoViewModelList.size());
	}

	public void onToggleSelect(ToggleSelectEvent event) throws IOException {
		log.info("ABLE --------------------------onToggleSelect");
		if (event != null && event.isSelected()){
			selectedNominativoViewModelList = new ArrayList<ClienteViewModel>();
			List<ClienteViewModel> wrappedData = (List<ClienteViewModel>) clienteViewModelLazyDataModel.getWrappedData();
			for (ClienteViewModel cvm: wrappedData){
				if (cvm != null && selectedNominativoViewModelList.size()<selectionLimit)
					selectedNominativoViewModelList.add(cvm);
				else{
					if (cvm != null) {
						FacesUtil.addMessage("E' possibile selezionare un massimo di " + selectionLimit + " nominativi");
						break;
					}
				}
			}
		}
		else if (event != null && !event.isSelected())
			selectedNominativoViewModelList = new ArrayList<ClienteViewModel>();
//		log.info(Arrays.toString(selectedNominativoViewModelList.toArray()));
		log.info("selectedNominativoViewModelList size: " + selectedNominativoViewModelList.size());
	}

	public boolean isDisabledButtonAssegnaNominativo() {
		if (!canWriteNominativo())
				return true;
		if (selectedNominativoViewModelList != null)
			return !(selectedNominativoViewModelList.size() > 0);
		else
			return true;
	}

	public boolean isRenderedButtonAssegnaNominativo() {
		if (selectedNominativoViewModelList != null)
			return (selectedNominativoViewModelList.size() > 0);
		else
			return false;
	}

	public void assegnaNominativi() {
		if (operator == null){
			FacesUtil.addMessage("Seleziona un operatore");
			return;
		}
		log.info("Selected operator " + operator.getUsername());
		try{
			nominativoService.updateNominativiOperator(selectedNominativoViewModelList, operator.getId());
		}
		catch (Exception e){
			FacesUtil.addMessage("Errore nell'assegnazione dei nominativi");
			e.printStackTrace();
			return;
		}

		FacesUtil.addMessage("Nominativi assegnati con successo");
		resetSelection();
		FacesUtil.closeDialog(FacesUtil.DialogType.DIALOG_ASSEGNA_NOMINATIVO);
		FacesUtil.refresh();
	}

	public void resetSelection(){
		selectedNominativoViewModelList = new ArrayList<ClienteViewModel>();
		operator = null;
		FacesUtil.closeDialog(FacesUtil.DialogType.DIALOG_ASSEGNA_NOMINATIVO);
	}

	public boolean canWriteNominativo() {
		return httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_WRITE_SUPER)
				|| (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_WRITE_ALL));
	}
}
