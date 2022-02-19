package com.woonders.lacemsjsf.view.preferenza;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreferenzaStatoPraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityutil.AziendaUtil;
import com.woonders.lacemscommon.db.entityutil.SettingUtil;
import com.woonders.lacemscommon.exception.UnableToSendEmailException;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.PreferenzaStatoPraticaService;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.PreferenzaStatoPraticaServiceImpl;
import com.woonders.lacemscommon.service.impl.SettingServiceImpl;
import com.woonders.lacemscommon.util.LaceMailSender;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.ErrorConverter;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@ManagedBean(name = "settingView")
@ViewScoped
@Getter
@Setter
@Slf4j
public class SettingView implements Serializable {

    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    private List<PreferenzaStatoPraticaViewModel> preferenzaStatoPraticaViewModelList;
    @ManagedProperty(PreferenzaStatoPraticaServiceImpl.JSF_NAME)
    private PreferenzaStatoPraticaService preferenzaStatoPraticaService;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(SettingServiceImpl.JSF_NAME)
    private SettingService settingService;
    private SettingViewModel settingViewModel;
    @ManagedProperty(LaceMailSender.JSF_NAME)
    private LaceMailSender laceMailSender;
    @ManagedProperty(AziendaUtil.JSF_NAME)
    private AziendaUtil aziendaUtil;
    @ManagedProperty(SettingUtil.JSF_NAME)
    private SettingUtil settingUtil;
    private List<OperatorViewModel> selectedReceiveLeadOperatorViewModelList;
    private List<OperatorViewModel> allOperatorViewModel;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    @ManagedProperty(ErrorConverter.JSF_NAME)
    private ErrorConverter errorConverter;

    // TODO questi andranno tolti quando le preferenze che attualmente stanno in
    // azienda verranno spostate su settings!
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    private AziendaViewModel aziendaViewModel;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;

    @PostConstruct
    public void init() {
        refreshAllSettingsFromDb();
    }

    public void refreshAllSettingsFromDb() {
        if (FacesUtil.doesPageNeedToBeRefreshed()) {
            preferenzaStatoPraticaViewModelList = preferenzaStatoPraticaService.getAllPreferenzaStatoPraticaByAziendaId(aziendaSelectionView.getCurrentAziendaId());
            settingViewModel = settingService.getByAziendaId(aziendaSelectionView.getCurrentAziendaId());
            aziendaViewModel = aziendaService.getOne(aziendaSelectionView.getCurrentAziendaId());
            selectedReceiveLeadOperatorViewModelList = operatorService.getAllReceiveLeadEnabledOperatorByAziendaId(aziendaSelectionView.getCurrentAziendaId());
            //per l'assegnazione dei lead posso scegliere solo tra gli operatori che hanno il permesso di scrivere i lead
            allOperatorViewModel = operatorService.findByRoleNameInAndAzienda_Id(Arrays.asList(Role.RoleName.getNominativiWriteRoles()), aziendaSelectionView.getCurrentAziendaId());
        }
    }

    public void save() {
        if (selectedReceiveLeadOperatorViewModelList.isEmpty()) {
            FacesUtil.addMessage(propertiesUtil.getMsgAtLeastOneOperatorReceiveLeadSelected(), FacesMessage.SEVERITY_ERROR);
        } else {
            try {
                preferenzaStatoPraticaService.save(preferenzaStatoPraticaViewModelList);
                settingService.save(settingViewModel);
                aziendaService.save(aziendaViewModel);
                List<Long> operatorIds = new LinkedList<>();
                for (OperatorViewModel operatorViewModel : selectedReceiveLeadOperatorViewModelList) {
                    operatorIds.add(operatorViewModel.getId());
                }
                operatorService.updateReceiveLeadEnabledOperatorsByAziendaId(operatorIds, aziendaSelectionView.getCurrentAziendaId());
                FacesUtil.addMessage(propertiesUtil.getMsgSettingsSaveSuccess());
                FacesUtil.refresh();
            } catch (final DataAccessException e) {
                log.error(propertiesUtil.getMsgError(), e);
                FacesUtil.addMessage(propertiesUtil.getMsgError(), FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    public boolean hasAuthorityToEditPreferences() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.PREFERENZE_WRITE, Role.RoleName.PREFERENZE_WRITE_SUPER);
    }

    public boolean isDisabledToSendSms() {
        return !hasAuthorityToEditPreferences() || isDatiFatturazioneToBeCompleted();
    }

    public boolean isDatiFatturazioneToBeCompleted() {
        return aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaViewModel);
    }

    public boolean isIlComparatoreLeadReceptionEnabled() {
        return settingViewModel.isLeadIlComparatoreEnabled();
    }

    public void enableIlComparatoreLeadReception() {
        if (aziendaViewModel.getNomeAzienda() == null || aziendaViewModel.getNomeAzienda().isEmpty()
                || aziendaViewModel.getIndirizzo() == null || aziendaViewModel.getIndirizzo().isEmpty()
                || aziendaViewModel.getLuogo() == null || aziendaViewModel.getLuogo().isEmpty()
                || aziendaViewModel.getEmail() == null || aziendaViewModel.getEmail().isEmpty()
                || aziendaViewModel.getPiva() == null || aziendaViewModel.getPiva().isEmpty()
                || aziendaViewModel.getNomeTitolare() == null || aziendaViewModel.getNomeTitolare().isEmpty()
                || aziendaViewModel.getCognomeTitolare() == null || aziendaViewModel.getCognomeTitolare().isEmpty()) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorCampiRegistrazioneComparatoreObbligatori(),
                    FacesMessage.SEVERITY_ERROR);
        } else {
            try {
                String emailBody = "Hi Christophe, please see this new activation request:" + "\n\n" + "Name: "
                        + aziendaViewModel.getNomeAzienda() + "\nAddress: " + aziendaViewModel.getIndirizzo()
                        + "\nCity: " + aziendaViewModel.getLuogo() + "\nEmail: " + aziendaViewModel.getEmail()
                        + "\nPIVA: " + aziendaViewModel.getPiva() + "\nOwner name: "
                        + aziendaViewModel.getNomeTitolare() + "\nOwner surname: "
                        + aziendaViewModel.getCognomeTitolare()
                        + "\n\nPlease send us to info@lacems.com their ID as soon as you setup them. Thank you.";

                laceMailSender.sendEmail(Constants.IL_COMPARATORE_LEAD_ACTIVATION_EMAIL, "LaceMS", "info@lacems.com", "New Il-Comparatore LaceMS activation request", emailBody);
                settingViewModel = settingService.setLeadIlComparatoreEnabled(settingViewModel.getId(), true);
                log.info("Abilitazione il Comparatore per: " + aziendaViewModel.toString());
                FacesUtil.addMessage(propertiesUtil.getMsgEnableIlComparatoreLeadReception());
            } catch (UnableToSendEmailException e) {
                FacesUtil.addMessage(errorConverter.convertSendEmailError(e.getUnableToSendEmailError()), FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    public int getIlComparatorePanelColumnCount() {
        // ritorna 2 o 3 colonne in base ai pulsanti che dobbiamo visualizzare
        return settingViewModel.isLeadIlComparatoreEnabled() ? 1 : 2;
    }

    // segna non usato ma invece e usato in pagina preferenze nel javascript
    public String redirectILComparatore() {
        return propertiesUtil.getLinkComparatoreValue();
    }

    public boolean isSmsFieldLeadDisabled() {
        return !hasAuthorityToEditPreferences() || !settingViewModel.isSendSmsLead() || isDatiFatturazioneToBeCompleted();
    }

    public boolean isAliasFieldDisabled() {
        boolean aliasAlreadySet = false;
        if (settingViewModel.getAlias() != null){
            aliasAlreadySet = settingViewModel.getAlias().length() > 0;
        }
        return !hasAuthorityToEditPreferences() || isDatiFatturazioneToBeCompleted() || aliasAlreadySet;
    }

    public boolean isSmsFieldDisabled() {
        return !hasAuthorityToEditPreferences() || isDatiFatturazioneToBeCompleted();
    }

}
