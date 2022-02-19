package com.woonders.lacemsjsf.view.search;

import com.woonders.lacemscommon.app.model.AdvancedSearchViewModel;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.laceenum.TipoRinnovo;
import com.woonders.lacemscommon.service.AmministrazioneService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.SearchService;
import com.woonders.lacemscommon.service.impl.AmministrazioneServiceImpl;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.SearchServiceImpl;
import com.woonders.lacemscommon.util.AdvancedSearchUtil;
import com.woonders.lacemsjsf.config.PropertiesUtil;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.woonders.lacemsjsf.view.search.Search.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class Search implements Serializable {

    public static final String NAME = "search";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(SearchServiceImpl.JSF_NAME)
    private SearchService searchService;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    private String cfSearch;
    private String cognomeSearch;
    private String telefono;
    private String nrSecci;
    private String nrContratto;
    private List<ClientePratica> clientiList = new ArrayList<>();
    private AdvancedSearchViewModel advancedSearchClienti;
    private AdvancedSearchViewModel advancedSearchNominativi;
    @ManagedProperty(AmministrazioneServiceImpl.JSF_NAME)
    private AmministrazioneService amministrazioneService;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(AdvancedSearchUtil.JSF_NAME)
    private AdvancedSearchUtil advancedSearchUtil;


    @PostConstruct
    public void init() {
        advancedSearchClienti = new AdvancedSearchViewModel();
        advancedSearchNominativi = new AdvancedSearchViewModel();
        advancedSearchNominativi.setTipoRinnovo(TipoRinnovo.COESISTENZA);
    }

    public boolean isDisabledAnniAssunzionePensionamento(final AdvancedSearchViewModel advancedSearchViewModel) {
        return !(advancedSearchViewModel.getImpieghiList() != null &&
                !advancedSearchViewModel.getImpieghiList().isEmpty());
    }

    public boolean isDisabledDateRinnovi(final AdvancedSearchViewModel advancedSearchViewModel) {
        return advancedSearchViewModel.getTipoRinnovo() == null;
    }

    public String searchCF() {

        clientiList = searchService.searchByCodiceFiscale(cfSearch);
        passaggioParametriUtils.getParametri().put(Parametro.LIST_NOMINATIVI_CLIENTI_PARAMETER, clientiList);
        return Constants.getDatatablePath(true);
    }

    public String searchCognome() {

        clientiList = searchService.searchByCognome(cognomeSearch);
        passaggioParametriUtils.getParametri().put(Parametro.LIST_NOMINATIVI_CLIENTI_PARAMETER, clientiList);
        return Constants.getDatatablePath(true);
    }

    public String searchTelefono() {
        clientiList = searchService.findByTelefono(telefono);
        passaggioParametriUtils.getParametri().put(Parametro.LIST_NOMINATIVI_CLIENTI_PARAMETER, clientiList);
        return Constants.getDatatablePath(true);
    }

    public String searchByNrContratto() {
        clientiList = searchService.findByNrContratto(nrContratto);
        passaggioParametriUtils.getParametri().put(Parametro.LIST_NOMINATIVI_CLIENTI_PARAMETER, clientiList);
        return Constants.getDatatablePath(true);
    }

    public String advancedSearchCliente() {
        if (advancedSearchClienti == null || advancedSearchUtil.checkIfFilterAdvancedSearchIsNull(advancedSearchClienti)) {
            FacesUtil.addMessage(propertiesUtil.getMsgAdvancedSearchEmptyFilter(), FacesMessage.SEVERITY_WARN);
            return null;
        }
        if (!advancedSearchUtil.isDateRightFormat(advancedSearchClienti)) {
            FacesUtil.addMessage(propertiesUtil.getMsgAdvancedSearchDataWrong(), FacesMessage.SEVERITY_WARN);
            return null;
        }
        if (!advancedSearchUtil.isDateRightComparable(advancedSearchClienti)) {
            FacesUtil.addMessage(propertiesUtil.getMsgAdvancedSearchDataWrong(), FacesMessage.SEVERITY_WARN);
            return null;
        }
        passaggioParametriUtils.getParametri().put(Parametro.ADVANCED_SEARCH_CLIENTI_PARAMETER, advancedSearchClienti);
        return Constants.getDatatableAdvancedSearchClientiPath(true);
    }

    public String advancedSearchNominativo() {
        if (advancedSearchNominativi == null || advancedSearchUtil.checkIfFilterAdvancedSearchIsNull(advancedSearchNominativi)) {
            FacesUtil.addMessage(propertiesUtil.getMsgAdvancedSearchEmptyFilter(), FacesMessage.SEVERITY_WARN);
            return null;
        }
        if (!advancedSearchUtil.isDateRightFormat(advancedSearchNominativi)) {
            FacesUtil.addMessage(propertiesUtil.getMsgAdvancedSearchDataWrong(), FacesMessage.SEVERITY_WARN);
            return null;
        }
        if (!advancedSearchUtil.isDateRightComparable(advancedSearchNominativi)) {
            FacesUtil.addMessage(propertiesUtil.getMsgAdvancedSearchDataWrong(), FacesMessage.SEVERITY_WARN);
            return null;
        }
        passaggioParametriUtils.getParametri().put(Parametro.ADVANCED_SEARCH_NOMINATIVI_PARAMETER, advancedSearchNominativi);
        return Constants.getDatatableAdvancedSearchNominativiPath(true);
    }


    public boolean isRenderedSelectItemsOperatorsNominativo() {
        if (httpSessionUtil.hasAnyAuthority(Role.RoleName.NOMINATIVI_READ_ALL, Role.RoleName.NOMINATIVI_READ_SUPER)) {
            return true;
        }
        if (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_OWN)) {
            return false;
        }
        return false;
    }

    public boolean isRenderedSelectItemsOperatorsCliente() {
        if (httpSessionUtil.hasAnyAuthority(Role.RoleName.CLIENTI_READ_ALL, Role.RoleName.CLIENTI_READ_SUPER)) {
            return true;
        }
        if (httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_OWN)) {
            return false;
        }
        return false;
    }

    public boolean isRicercaAvanzataClienteRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.CLIENTI_READ_OWN, Role.RoleName.CLIENTI_READ_ALL, Role.RoleName.CLIENTI_READ_SUPER);
    }

    public boolean isRicercaAvanzataNominativoRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.NOMINATIVI_READ_OWN, Role.RoleName.NOMINATIVI_READ_ALL, Role.RoleName.NOMINATIVI_READ_SUPER);
    }

    public List<AmministrazioneViewModel> completeAmministrazione(final String query) {
        return amministrazioneService.completeAmministrazione(query);
    }

    public boolean isAziendaRenderedCliente() {
        return httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_SUPER);
    }

    public boolean isAziendaRenderedNominativo() {
        return httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_SUPER);
    }

    public List<String> getOperatorListNominativi() {
        return searchService.getOperatorList(
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
                httpSessionUtil.getAziendaId());
    }

    public List<String> getOperatorListClienti() {
        return searchService.getOperatorList(
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
                httpSessionUtil.getAziendaId());
    }
    
    //*** Mod Cristian 11-11-2021 ***
    public List<String> getProvenienzaDescListClienti() {
        return searchService.getProvenienzaDescList();
    }
}
