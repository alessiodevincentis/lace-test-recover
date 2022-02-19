package com.woonders.lacemsjsf.view.gestionepermessi;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.RoleViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.exception.IllegalPermissionStateException;
import com.woonders.lacemscommon.laceenum.PermissionProfile;
import com.woonders.lacemscommon.service.*;
import com.woonders.lacemscommon.service.impl.*;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.woonders.lacemsjsf.view.gestionepermessi.GestionePermessiView.NAME;

/**
 * Created by Emanuele on 04/05/2017.
 */
@Getter
@Setter
@ViewScoped
@ManagedBean(name = NAME)
public class GestionePermessiView {

    public static final String NAME = "gestionePermessiView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private LazyDataModel<OperatorViewModel> operatorViewModelLazyDataModelList;

    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    @ManagedProperty(RoleServiceImpl.JSF_NAME)
    private RoleService roleService;

    private List<RoleViewModel> roleViewModelList;
    private PermissionProfile permissionProfile;
    private OperatorViewModel operatorToAssignProfile;
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

    @PostConstruct
    public void init() {
        roleViewModelList = roleService.findAll();
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

    public void saveOperatorPermission() {
        try {
            gestionePermessiService.saveNewPermission(operatorToAssignProfile, permissionProfile);
            FacesUtil
                    .addMessage("Nuovi permessi salvati con successo. I cambiamenti avranno effetto al prossimo login");
            FacesUtil.closeDialog(FacesUtil.DialogType.MODIFICA_PERMESSI);
        } catch (final IllegalPermissionStateException e) {
            FacesUtil.addMessage(
                    "Si è verificato un errore nel salvataggio dei permessi. La configurazione non è corretta. "
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

    public void setProfilePermission() {
        if (permissionProfile != null) {
            operatorToAssignProfile
                    .setSectionPermissionMap(PermissionProfile.getMenuSectionPermissionMap(permissionProfile));
            FacesUtil.addMessage("Profilo selezionato, clicca salva per salvare le modifiche!");
        }
    }

    public void setOperatorToAssignProfileFromDatalist(final OperatorViewModel operatorViewModel) {
        OperatorViewModel operatorViewModelToAssign = null;
        if (operatorViewModel != null) {
            operatorViewModelToAssign = operatorService.findByUsernameViewModel(operatorViewModel.getUsername());
        }
        setOperatorToAssignProfile(operatorViewModelToAssign);
        permissionProfile = null;
        simulatorEnabled = operatorViewModelToAssign != null && settingService.getByAziendaId(operatorViewModelToAssign.getAzienda().getId()).isSimulatorEnabled();
    }

    // setta i permessi in maniera corretta quando uno viene cambiato
    public void permissionChangedEvent(final AjaxBehaviorEvent event) {
        // http://stackoverflow.com/questions/11879138/when-to-use-valuechangelistener-or-fajax-listener
        // http://stackoverflow.com/questions/18611479/primefaces-datatable-celledit-get-entity-object
        final OperatorViewModel.MenuSection changedMenuSection = FacesContext.getCurrentInstance().getApplication()
                .evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{menusection}",
                        OperatorViewModel.MenuSection.class);
        final OperatorViewModel.PermissionType changedPermissionType = FacesContext.getCurrentInstance().getApplication()
                .evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{permissionType}",
                        OperatorViewModel.PermissionType.class);
        final Map<OperatorViewModel.MenuSection, Map<OperatorViewModel.PermissionType, Role.RoleName>> sectionPermissionTypeMap = operatorToAssignProfile
                .getSectionPermissionMap();

        final Role.RoleName changedRoleName = sectionPermissionTypeMap.get(changedMenuSection).get(changedPermissionType);
        switch (changedRoleName) {
            // CLIENTI
            case CLIENTI_READ_NOT:
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                        .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.CLIENTI_WRITE_NOT);
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                        .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CLIENTI_DELETE_NOT);
                // non puoi vedere antiriciclaggio se non puoi vedere i clienti
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.ANTI_RICICLAGGIO)
                        .put(OperatorViewModel.PermissionType.READ, Role.RoleName.ANTI_RICICLAGGIO_READ_NOT);
                break;
            case CLIENTI_READ_OWN:
                if (Role.RoleName.CLIENTI_WRITE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.WRITE))
                        || Role.RoleName.CLIENTI_WRITE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.CLIENTI_WRITE_OWN);
                }
                if (Role.RoleName.CLIENTI_DELETE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.DELETE))
                        || Role.RoleName.CLIENTI_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CLIENTI_DELETE_OWN);
                }
                break;
            case CLIENTI_READ_ALL:
                if (Role.RoleName.CLIENTI_WRITE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                            .put(OperatorViewModel.PermissionType.WRITE, RoleName.CLIENTI_WRITE_ALL);
                }
                if (Role.RoleName.CLIENTI_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CLIENTI_DELETE_ALL);
                }
                break;
            case CLIENTI_WRITE_NOT:
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                        .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CLIENTI_DELETE_NOT);
                break;
            case CLIENTI_WRITE_OWN:
                if (Role.RoleName.CLIENTI_DELETE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.DELETE))
                        || Role.RoleName.CLIENTI_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CLIENTI_DELETE_OWN);
                }
                break;
            case CLIENTI_WRITE_ALL:
                if (Role.RoleName.CLIENTI_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CLIENTI).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CLIENTI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CLIENTI_DELETE_ALL);
                }
                break;
            // NOMINATIVI
            case NOMINATIVI_READ_NOT:
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                        .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.NOMINATIVI_WRITE_NOT);
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                        .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.NOMINATIVI_DELETE_NOT);
                break;
            case NOMINATIVI_READ_OWN:
                if (Role.RoleName.NOMINATIVI_WRITE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.NOMINATIVI).get(OperatorViewModel.PermissionType.WRITE))
                        || Role.RoleName.NOMINATIVI_WRITE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                                .get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.NOMINATIVI_WRITE_OWN);
                }
                if (Role.RoleName.NOMINATIVI_DELETE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.NOMINATIVI).get(OperatorViewModel.PermissionType.DELETE))
                        || Role.RoleName.NOMINATIVI_DELETE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                                .get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.NOMINATIVI_DELETE_OWN);
                }
                break;
            case NOMINATIVI_READ_ALL:
                if (Role.RoleName.NOMINATIVI_WRITE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.NOMINATIVI).get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.NOMINATIVI_WRITE_ALL);
                }
                if (Role.RoleName.NOMINATIVI_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.NOMINATIVI).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.NOMINATIVI_DELETE_ALL);
                }
                break;
            case NOMINATIVI_WRITE_NOT:
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                        .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.NOMINATIVI_DELETE_NOT);
                break;
            case NOMINATIVI_WRITE_OWN:
                if (Role.RoleName.NOMINATIVI_DELETE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.NOMINATIVI).get(OperatorViewModel.PermissionType.DELETE))
                        || Role.RoleName.NOMINATIVI_DELETE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                                .get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.NOMINATIVI_DELETE_OWN);
                }
                break;
            case NOMINATIVI_WRITE_ALL:
                if (Role.RoleName.NOMINATIVI_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.NOMINATIVI).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOMINATIVI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.NOMINATIVI_DELETE_ALL);
                }
                break;
            // FATTURAZIONE
            case FATTURAZIONE_READ_OWN:
                if (Role.RoleName.FATTURAZIONE_WRITE.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.FATTURAZIONE).get(OperatorViewModel.PermissionType.WRITE))
                        || RoleName.FATTURAZIONE_WRITE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.FATTURAZIONE)
                                .get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.FATTURAZIONE)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.FATTURAZIONE_WRITE_NOT);
                }
                break;
            case FATTURAZIONE_READ_ALL:
                if (RoleName.FATTURAZIONE_WRITE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.FATTURAZIONE).get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.FATTURAZIONE)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.FATTURAZIONE_WRITE);
                }
                break;
            // CALENDARIO
            case CALENDARIO_READ_OWN:
                if (Role.RoleName.CALENDARIO_WRITE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CALENDARIO).get(OperatorViewModel.PermissionType.WRITE))
                        || Role.RoleName.CALENDARIO_WRITE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                                .get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.CALENDARIO_WRITE_OWN);
                }
                if (Role.RoleName.CALENDARIO_DELETE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CALENDARIO).get(OperatorViewModel.PermissionType.DELETE))
                        || Role.RoleName.CALENDARIO_DELETE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                                .get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CALENDARIO_DELETE_OWN);
                }
                break;
            case CALENDARIO_READ_ALL:
                if (Role.RoleName.CALENDARIO_WRITE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CALENDARIO).get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.CALENDARIO_WRITE_ALL);
                }
                if (Role.RoleName.CALENDARIO_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CALENDARIO).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CALENDARIO_DELETE_ALL);
                }
                break;
            case CALENDARIO_WRITE_OWN:
                if (Role.RoleName.CALENDARIO_DELETE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CALENDARIO).get(OperatorViewModel.PermissionType.DELETE))
                        || Role.RoleName.CALENDARIO_DELETE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                                .get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CALENDARIO_DELETE_OWN);
                }
                break;
            case CALENDARIO_WRITE_ALL:
                if (Role.RoleName.CALENDARIO_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.CALENDARIO).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.CALENDARIO)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.CALENDARIO_DELETE_ALL);
                }
                break;
            // SIMULATORI
            case SIMULATORI_READ_OWN:
                if (Role.RoleName.SIMULATORI_WRITE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.SIMULATORI).get(OperatorViewModel.PermissionType.WRITE))
                        || Role.RoleName.SIMULATORI_WRITE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                                .get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.SIMULATORI_WRITE_OWN);
                }
                if (Role.RoleName.SIMULATORI_DELETE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.SIMULATORI).get(OperatorViewModel.PermissionType.DELETE))
                        || Role.RoleName.SIMULATORI_DELETE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                                .get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.SIMULATORI_DELETE_OWN);
                }
                break;
            case SIMULATORI_READ_ALL:
                if (Role.RoleName.SIMULATORI_WRITE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.SIMULATORI).get(OperatorViewModel.PermissionType.WRITE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                            .put(OperatorViewModel.PermissionType.WRITE, Role.RoleName.SIMULATORI_WRITE_ALL);
                }
                if (Role.RoleName.SIMULATORI_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.SIMULATORI).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.SIMULATORI_DELETE_ALL);
                }
                break;
            case SIMULATORI_WRITE_NOT:
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                        .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.SIMULATORI_DELETE_NOT);
                break;
            case SIMULATORI_WRITE_OWN:
                if (Role.RoleName.SIMULATORI_DELETE_ALL.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.SIMULATORI).get(OperatorViewModel.PermissionType.DELETE))
                        || Role.RoleName.SIMULATORI_DELETE_SUPER
                        .equals(sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                                .get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.SIMULATORI_DELETE_OWN);
                }
                break;
            case SIMULATORI_WRITE_ALL:
                if (Role.RoleName.SIMULATORI_DELETE_SUPER.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.SIMULATORI).get(OperatorViewModel.PermissionType.DELETE))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.SIMULATORI)
                            .put(OperatorViewModel.PermissionType.DELETE, Role.RoleName.SIMULATORI_DELETE_ALL);
                }
                break;
            case NOTICE_BOARD_WRITE_SUPER:
                sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOTICE_BOARD)
                        .put(OperatorViewModel.PermissionType.READ, Role.RoleName.NOTICE_BOARD_READ_SUPER);
                break;
            case NOTICE_BOARD_READ_SUPER:
                if (RoleName.NOTICE_BOARD_WRITE.equals(sectionPermissionTypeMap
                        .get(OperatorViewModel.MenuSection.NOTICE_BOARD).get(OperatorViewModel.PermissionType.READ))) {
                    sectionPermissionTypeMap.get(OperatorViewModel.MenuSection.NOTICE_BOARD)
                            .put(OperatorViewModel.PermissionType.WRITE, RoleName.NOTICE_BOARD_WRITE_SUPER);
                }
                break;
        }

    }

    public String calcNotPermissionValue(final OperatorViewModel.MenuSection menuSection,
                                         final OperatorViewModel.PermissionType permissionType) {
        for (final Role.RoleName roleName : Role.RoleName.values()) {
            final String roleNameString = roleName.toString();
            if (roleNameString.contains(menuSection.toString()) && roleNameString.contains(permissionType.toString())
                    && roleNameString.contains("NOT")) {
                return roleName.getValue();
            }
        }
        return null;
    }

    public boolean hasGestionePermessiRole() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getGestionePermessiWriteRoles());
    }

    public List<Role.RoleName> getPermission(final OperatorViewModel.MenuSection menuSection, final OperatorViewModel.PermissionType permissionType) {
        final List<Role.RoleName> roleNameList = new LinkedList<>();
        for (final Role.RoleName roleName : Role.RoleName.values()) {
            if (roleName.toString().contains(menuSection.toString())
                    && roleName.toString().contains(permissionType.toString())) {
                if (!simulatorEnabled && roleName.toString().contains("SIMULATOR")) {
                    break;
                }

                if ((aziendaCount == 1 && !roleName.toString().contains("SUPER")) || (aziendaCount > 1)) {
                    roleNameList.add(roleName);
                }
            }
        }
        return roleNameList;
    }
}
