package com.woonders.lacemsjsf.view.eliminaoperatore;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.OperatorViewModelCreator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.DeleteOperatorService;
import com.woonders.lacemscommon.service.GestionePermessiService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.DeleteOperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.GestionePermessiServiceImpl;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.SettingServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import com.woonders.lacemsjsf.view.deleteclientepratica.DeleteView;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by Cristian on 25/10/2021.
 */
@Getter
@Setter
@ViewScoped
@ManagedBean(name = EliminaOperatoreView.NAME)
public class EliminaOperatoreView {

    public static final String NAME = "eliminaOperatoreView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private LazyDataModel<OperatorViewModel> operatorViewModelLazyDataModelList;

    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    
    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;
    
    @Inject
    private OperatorViewModel selectedOperator;

    private static final Logger logger = LoggerFactory.getLogger(DeleteView.class);
    
    
    @ManagedProperty(GestionePermessiServiceImpl.JSF_NAME)
    private GestionePermessiService gestionePermessiService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    private long aziendaCount;
    @ManagedProperty(SettingServiceImpl.JSF_NAME)
    private SettingService settingService;
    private boolean simulatorEnabled;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
    
    @ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
	private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
    
    @ManagedProperty(DeleteOperatorServiceImpl.JSF_NAME)
	private DeleteOperatorService deleteOperatorService;
    

    @PostConstruct
    public void init() {
    	
    	operatorViewModelLazyDataModelList = new LazyDataModel<OperatorViewModel>() {

            @Override
            public List<OperatorViewModel> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                                final Map<String, Object> filters) {

                final ViewModelPage<OperatorViewModel> viewModelPage = operatorService.findAll(first, pageSize, sortField,
                        null, null, httpSessionUtil.getOperatorId(),
                        httpSessionUtil.hasAuthority(RoleName.GESTIONE_PERMESSI_WRITE), aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.hasAuthority(RoleName.GESTIONE_PERMESSI_WRITE_SUPER));

                // jsf di merda che non prendi i long!
                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();

            }

        };
        aziendaCount = aziendaService.count();
    }
    
    
    
    public LazyDataModel<OperatorViewModel> getLazyModel() {
        return operatorViewModelLazyDataModelList;
    }
    
    public OperatorViewModel getSelectedOperator() {
        return selectedOperator;
    }
    
    public void setSelectedOperator(OperatorViewModel selectedOperator) {
        this.selectedOperator = selectedOperator;
    }
    
    public void setEliminaOperator(final OperatorViewModel operatorViewModel) {
        try {
        	
            if (operatorViewModel != null) {
            	
            	
            	operatorViewModel.getSectionPermissionMap();
            	final Map<OperatorViewModel.MenuSection, Map<OperatorViewModel.PermissionType, Role.RoleName>> sectionPermissionTypeMap = operatorViewModel
                        .getSectionPermissionMap();
            	
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                	.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.CLIENTI_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
            		.put(OperatorViewModel.PermissionType.READ, Role.RoleName.CLIENTI_READ_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
            		.put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CLIENTI_DELETE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.AMMINISTRAZIONI)
        			.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.AMMINISTRAZIONI_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.ANTI_RICICLAGGIO)
    				.put(OperatorViewModel.PermissionType.READ, Role.RoleName.ANTI_RICICLAGGIO_READ_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
    				.put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CALENDARIO_DELETE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
    				.put(OperatorViewModel.PermissionType.READ, Role.RoleName.CALENDARIO_READ_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
    				.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.CALENDARIO_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CAMPAGNE_MARKETING)
    				.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.CAMPAGNE_MARKETING_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.DATI_AZIENDA)
    				.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.DATI_AZIENDA_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.FATTURAZIONE)
    				.put(OperatorViewModel.PermissionType.READ, Role.RoleName.FATTURAZIONE_READ_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.FATTURAZIONE)
    				.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.FATTURAZIONE_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.GESTIONE_PERMESSI)
    				.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.GESTIONE_PERMESSI_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.GRAFICI)
    				.put(OperatorViewModel.PermissionType.READ, Role.RoleName.GRAFICI_READ_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
    				.put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.NOMINATIVI_DELETE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
					.put(OperatorViewModel.PermissionType.READ, Role.RoleName.NOMINATIVI_READ_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
					.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.NOMINATIVI_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOTICE_BOARD)
					.put(OperatorViewModel.PermissionType.READ, Role.RoleName.NOTICE_BOARD_READ_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOTICE_BOARD)
					.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.NOTICE_BOARD_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.PREFERENZE)
					.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.PREFERENZE_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.RICARICA)
					.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.RICARICA_WRITE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
					.put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.SIMULATORI_DELETE_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
					.put(OperatorViewModel.PermissionType.READ, Role.RoleName.SIMULATORI_READ_NOT);
            	sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
					.put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.SIMULATORI_WRITE_NOT);
            	
            	
            	operatorViewModel.setSectionPermissionMap(sectionPermissionTypeMap);
            	gestionePermessiService.saveNewPermission(operatorViewModel, null);
            	
            	
            	deleteOperatorService.deleteById(operatorViewModel.getId());
				
            	FacesUtil.addMessage(propertiesUtil.getMsgDeleteSuccess());
				logger.info(propertiesUtil.getMsgDeleteSuccess());
            	
            	//operatorService.delete(operatorViewModel);
//                FacesUtil
//                        .addMessage("Eliminato con succeso l'operatore selezionato. I cambiamenti avranno effetto al prossimo login");
//                FacesUtil.closeDialog(FacesUtil.DialogType.OPERATOR);
            }      
        	
        } catch (final Exception e) {
            FacesUtil.addMessage(
                    "Si è verificato un errore nel eliminare l'operatore. La configurazione non è corretta. "
                            + e.getMessage(),
                    FacesMessage.SEVERITY_ERROR);
        }
    }
    
    public String getProfilePermissionFromOperator(final OperatorViewModel operatorViewModel) {
        if (operatorViewModel != null && operatorViewModel.getPermissionProfile() != null) {
            return operatorViewModel.getPermissionProfile().getValue();
        }
        return null;
    }
    
    public boolean isDeleteOpearator() {
		return httpSessionUtil.hasAnyAuthority(RoleName.GESTIONE_PERMESSI_WRITE);
	}

}
