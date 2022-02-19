package com.woonders.lacemsjsf.navigation;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.service.impl.SettingServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import lombok.Setter;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by Emanuele on 15/09/2016.
 */
@ApplicationScoped
@ManagedBean
@Setter
public class NavigationBackingBean implements Serializable {

    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(SettingServiceImpl.JSF_NAME)
    private SettingService settingService;

    public String getDashboardOutcome() {
        return Constants.getDashboardPath(true);
    }

    public String getNuovaPraticaCheckCfOutcome() {
        return Constants.getNuovaPraticaCheckCfPath(true);
    }

    public String getSearchClienteNominativoOutcome() {
        return Constants.getSearchClienteNominativoPath(true);
    }

    public String getDeleteClientePraticaOutcome() {
        return Constants.getDeleteClientePraticaPath(true);
    }

    public String getFatturazioneOutcome() {
        return Constants.getFatturazionePath(true);
    }

    public String getNuovaAmministrazioneCheckRagioneSocialeOutcome() {
        return Constants.getNuovaAmministrazioneCheckRagioneSocialePath(true);
    }

    public String getSearchAmministrazioneOutcome() {
        return Constants.getSearchAmministrazionePath(true);
    }

    public String getDashboardNominativoOutcome() {
        return Constants.getDashboardNominativoPath(true);
    }

    public String getAntiriciclaggioOutcome() {
        return Constants.getAntiriciclaggioPath(true);
    }

    public String getUsername() {
        return httpSessionUtil.getUsername();
    }

    public String getNuovoNominativoOutcome() {
        return Constants.getNuovoNominativoPath(true);
    }

    public String getRoleTag() {
        // TODO QUESTA DA CAMBIARE PER IL PROFILO DA VISUALIZZARE!!
        return httpSessionUtil.getRoleNameList().get(0).getValue();
    }

    public String getManageUsersOutcome() {
        return Constants.getManageUsersPath(true);
    }

    public String getManageRolesOutcome() {
        return Constants.getManageRolesPath(true);
    }

    public String getStatoPraticheCessioniDelegheOutcome() {
        return Constants.getStatoPraticheCessioniDeleghePath(true);
    }

    public String getStatoPratichePrestitiOutcome() {
        return Constants.getStatoPratichePrestitiPath(true);
    }

    public boolean isAjaxRequest() {
        return FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
    }

    public String getNuovaCampagnaMarketingOutcome() {
        return Constants.getNuovaCampagnaMarketingPath(true);
    }

    public String getInfoCampagneMarketingOutcome() {
        return Constants.getInfoCampagneMarketingPath(true);
    }

    public String getSettingsCampagneMarketingOutcome() {
        return Constants.getSettingsCampagneMarketingPath(true);
    }

    public String getAntiriciclaggioAdminOutcome() {
        return Constants.getAntiriciclaggioAdminPath(true);
    }

    public String getAziendaOutcome() {
        return Constants.getAziendaPath(true);
    }

    public String getDatiPersonaliOutcome() {
        return Constants.getDatiPersonaliPath(true);
    }

    public String getPreferenzeOutcome() {
        return Constants.getPreferenzePath(true);
    }

    public String getCambioPasswordOutcome() {
        return Constants.getCambioPasswordPath(true);
    }

    public String getCambioPasswordAntiriciclaggioOutcome() {
        return Constants.getCambioPasswordAntiriciclaggioPath(true);
    }

    public String getRicaricaSmsOutcome() {
        return Constants.getRicaricaSmsPath(true);
    }

    public String getStatoNominativiOutcome() {
        return Constants.getStatoNominativiPath(true);
    }
    
    
    public String getNuovoOperatoreOutcome() {
        return Constants.getNuovoOperatorePath(true);
    }
    public String getEliminaOperatoreOutcome() {
        return Constants.getEliminaOperatorePath(true);
    }
    
    
    public String getGestionePermessiOutcome() {
        return Constants.getGestionePermessiPath(true);
    }
    
    public String getBackupOutcome() {
        return Constants.getBackupPath(true);
    }

    public String getAccessLogOutcome() {
        return Constants.getAccessLogPath(true);
    }

    public boolean isSimulatorSectionRendered() {
        return settingService.isSimulatorEnabledByAziendaId(httpSessionUtil.getAziendaId()) &&
                httpSessionUtil.hasAnyAuthority(Role.RoleName.getSimulatoriReadRoles());
    }

    public boolean isSimulatorCreateTableSectionRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getSimulatoriWriteRoles());
    }

    public boolean isClienteSectionRendered() {
        return httpSessionUtil.hasAnyAuthority(RoleName.getClientiReadRoles());
    }

    public boolean isNominativoSectionRendered() {
        return httpSessionUtil.hasAnyAuthority(RoleName.getNominativiReadRoles());
    }

    public boolean isClienteOrNominativoReadRendered() {
        return httpSessionUtil.hasAnyAuthority(RoleName.getClientiReadRoles())
                || httpSessionUtil.hasAnyAuthority(RoleName.getNominativiReadRoles());
    }

    public boolean isAntiriciclaggioSectionRendered() {
        return httpSessionUtil.hasAuthority(RoleName.ANTI_RICICLAGGIO_READ)
                && httpSessionUtil.hasAnyAuthority(RoleName.getClientiReadRoles());
    }

    public boolean isDeleteClienteSectionRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getClientiDeleteRoles());
    }

    public boolean isNuovaPraticaRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getClientiWriteRoles());
    }

    public boolean isNuovoNominativoRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getNominativiWriteRoles());
    }

    public boolean isNuovaAmministrazioneRendered() {
        return httpSessionUtil.hasAuthority(RoleName.AMMINISTRAZIONI_WRITE);
    }

    public boolean isBackupSectionRendered() {
        return httpSessionUtil.hasAuthority(RoleName.BACKUP_DOWNLOAD);
    }

    public boolean isAccessLogSectionRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getAccessLogReadRoles());
    }

    public boolean isRicaricaSectionRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getRicaricaWriteRoles());
    }
    
    public boolean isGestioneUtentiRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getGestionePermessiWriteRoles());
    }
    
    public boolean isDeleteUtenteRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getUtentiDeleteRoles());
    }
    
    public boolean isNuovoUtenteRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getUtentiWriteRoles());
    }
    
    public boolean isGestioneLeadsDataRetentionRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getLeadsDataRetentionWriteRoles());
    }
    
    public boolean isGestionePricavyRendered() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getPrivacyWriteRoles());
    }

    public String getCreateNewSimulatorTableOutcome() {
        return Constants.getCreateNewSimulatorTablePath(true);
    }

    public String getSimulatorTableListOutcome() {
        return Constants.getSimulatorTableListTablePath(true);
    }

    public String getRunSimulatorOutcome() {
        return Constants.getRunSimulatorPath(true);
    }

    public String getNominativiOmessiOutcome() {
        return Constants.getDatatableNominativoOmessiPath(true);
    }

    public String getInfoAccountOutcome() {
        return Constants.getInfoAccountPath(true);
    }

    public String getInviteFriendsOutcome() {
        return Constants.getInviteFriendsPath(true);
    }

    public String getAnalyticsClientiOutcome() {
        return Constants.getAnalyticsClientiPath(true);
    }

    public String getAnalyticsNominativiOutcome() {
        return Constants.getAnalyticsNominativiPath(true);
    }

    public String getNoticeBoardOutcome() {
        return Constants.getNoticeBoardPath(true);
    }

    public String getNoticeBoardDatatableOutcome() {
        return Constants.getNoticeBoardDatatablePath(true);
    }
    
    public String getGestioneLeadsOutcome() {
        return Constants.getGestioneLeadsPath(true);
    }

    public String getGestionePrivacyTemplateOutcome() {
        return Constants.getGestionePrivacyTemplatePath(true);
    }

}
