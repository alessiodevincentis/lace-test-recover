package com.woonders.lacemsjsf.view.aziendaselection;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemsjsf.ui.AppMenuSection;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

import static com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView.NAME;

/**
 * Created by Emanuele on 23/05/2017.
 */
@ManagedBean(name = NAME)
@SessionScoped
@Getter
@Setter
@Slf4j
public class AziendaSelectionView implements Serializable {

    public static final String NAME = "aziendaSelectionView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private Long selectedAziendaId;
    private List<AziendaViewModel> aziendaViewModelList;
    private AppMenuSection currentMenuSection;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    private long aziendaCount;
    //azienda dell operatore, non quella selezionata
    private Long operatorAziendaId;

    @PostConstruct
    public void init() {
        aziendaViewModelList = aziendaService.findAll();
        aziendaCount = aziendaViewModelList.size();
        selectedAziendaId = httpSessionUtil.getAziendaId();
        operatorAziendaId = httpSessionUtil.getAziendaId();
    }

    public boolean isAziendaSelectShown() {
        if (aziendaCount == 1) {
            return false;
        } else {
            boolean canReadAllAziende = false;
            if (currentMenuSection != null) {
                switch (currentMenuSection) {
                    case DATI_AZIENDA:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.DATI_AZIENDA_WRITE_SUPER);
                        break;
                    case PREFERENZE:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.PREFERENZE_WRITE_SUPER);
                        break;
                    case DASHBOARD:
                        //nella dashboard che gli passooooooooooo
                        //aggiungiamo la sezione dashboard che useremo solo in questo caso??
                        canReadAllAziende = httpSessionUtil.hasAnyAuthority(Role.RoleName.CLIENTI_READ_SUPER, Role.RoleName.NOMINATIVI_READ_SUPER, Role.RoleName.GRAFICI_READ_SUPER);
                        break;
                    case CAMPAGNE_MARKETING:
                        canReadAllAziende = httpSessionUtil.hasAnyAuthority(Role.RoleName.CLIENTI_READ_SUPER, Role.RoleName.NOMINATIVI_READ_SUPER);
                        break;
                    case NOMINATIVI:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_SUPER);
                        break;
                    case STATO_PRATICA:
                    case CLIENTI:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_SUPER);
                        break;
                    case CERCA:
                        break;
                    case FATTURAZIONE:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.FATTURAZIONE_READ_SUPER);
                        break;
                    //TODO PORCATA! Solo perche devo nascondere la select, questo NON dovra mai esistere, e quando cambieremo i filtri deve sparire!!!
                    case FATTURAZIONE_AFTER_SEARCH:
                        return false;
                    case AMMINISTRAZIONI:
                        //amministrazioni sempre condivise
                        break;
                    case ANTI_RICICLAGGIO:
                        canReadAllAziende = httpSessionUtil.hasAnyAuthority(Role.RoleName.ANTI_RICICLAGGIO_OUT, Role.RoleName.CLIENTI_READ_SUPER);
                        break;
                    //TODO PORCATA! Solo perche devo nascondere la select, questo NON dovra mai esistere, e quando cambieremo i filtri deve sparire!!!
                    case ANTIRICICLAGGIO_AFTER_SEARCH:
                        return false;
                    case GESTIONE_PERMESSI:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.GESTIONE_PERMESSI_WRITE_SUPER);
                        break;
                    case RICARICA:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.RICARICA_WRITE_SUPER);
                        break;
                    case SIMULATORI_NEW:
                        canReadAllAziende = false;
                        break;
                    case SIMULATORI_LIST:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.SIMULATORI_READ_SUPER);
                        break;
                    case SIMULATORI_RUN:
                        canReadAllAziende = false;
                        break;
                    case NOTICE_BOARD_LIST:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.NOTICE_BOARD_WRITE_SUPER);
                        break;
                    case NOTICE_BOARD_ADD:
                        canReadAllAziende = false;
                        break;
                    case ACCESS_LOG:
                        canReadAllAziende = httpSessionUtil.hasAuthority(Role.RoleName.ACCESS_LOG_READ_SUPER);
                        break;
                }
            }
            //nei casi speciali di dati azienda e preferenze, dove non si puo mettere qualsiasi
            //vedi metodo isQualsiasiAziendaDisabled() abbiamo gia inizializzato il valore con il valore dell operatore
            if (!canReadAllAziende) {
                selectedAziendaId = operatorAziendaId;
            }
            return canReadAllAziende;
        }
    }

    public Long getCurrentAziendaId() {
        //siccome viene invocato prima il postConstruct dei bean e dopo questi metodi, se seleziono "Qualsiasi" e poi navigo in una sezione con qualsiasi disabilitato
        //selectedAziendaId rimane null e fa crashare i bean, quindi lo ripristino al valore dell-azienda dell operatore corrente
        //TODO FIX nell if non ci entra mai perche selectedAzienda viene modificato da isQualsiasiAziendaDisabled, bisognerebbe pensare un modo migliore!!
        if (isQualsiasiAziendaDisabled() && selectedAziendaId == null) {
            selectedAziendaId = operatorAziendaId;
        }
        return selectedAziendaId;
    }

    /**
     * Usato nelle tabelle per visualizzare i dati
     */
    public boolean isAziendaColumnShown() {
        return aziendaCount > 1 && selectedAziendaId == null;
    }

    /**
     * Usato dove serve nelle pagine quando si inseriscono dei dati, non ci interessa sapere selectedAziendaId perche non e mai stato selezionato
     */
    public boolean isAziendaColumnShownInPage() {
        return aziendaCount > 1;
    }

    public boolean isQualsiasiAziendaDisabled() {
        //inseriti nello switch solo i casi in cui deve essere disabilitato
        //quindi resetto anche selectedAzienda al valore dell'operatore
        if (currentMenuSection != null) {
            switch (currentMenuSection) {
                case DATI_AZIENDA:
                case PREFERENZE:
                case RICARICA:
                case STATO_PRATICA:
                    if (selectedAziendaId == null) {
                        selectedAziendaId = operatorAziendaId;
                    }
                    return true;
            }
        }
        return false;
    }

}
