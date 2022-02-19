package com.woonders.lacemsjsf.view.dashboard;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.service.*;
import com.woonders.lacemscommon.service.impl.*;
import com.woonders.lacemscommon.service.impl.DashboardServiceImpl.NotificationCategory;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@ManagedBean(name = "dashboardNotificationView")
@ViewScoped
@Getter
@Setter
@Slf4j
public class DashboardNotificationView implements Serializable {

    @ManagedProperty(DashboardServiceImpl.JSF_NAME)
    private DashboardService dashboardService;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(DashboardNominativoServiceImpl.JSF_NAME)
    private DashboardNominativoService dashboardNominativoService;
    @ManagedProperty(SettingServiceImpl.JSF_NAME)
    private SettingService settingService;
    @ManagedProperty(StatoNominativoServiceImpl.JSF_NAME)
    private StatoNominativoService statoNominativoService;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    private Integer sumSmsBalanceAziende;

    public int balanceSmsNotificaLead() {
        //visto che aziendaId non puo essere null, se e null vuol dire che sta su qualsiasi e allora gli facciamo vedere la somma di tutti gli sms
        final Long aziendaId = aziendaSelectionView.getCurrentAziendaId();
        if (aziendaId == null) {
            if (sumSmsBalanceAziende == null) {
                sumSmsBalanceAziende = 0;
                for (final SettingViewModel settingViewModel : settingService.findAll()) {
                    sumSmsBalanceAziende += settingViewModel.getNotificaLeadSmsBalance();
                }
            }
            return sumSmsBalanceAziende;
        }

        return settingService.getByAziendaId(aziendaId).getNotificaLeadSmsBalance();
    }

    public String balanceSmsMarketingOperatore() {
        return String.valueOf(operatorService.getOne(httpSessionUtil.getOperatorId()).getSmsBalance());
    }

    // WIDGET CLIENTI //

    public long numberRinnovoPratica() {
        return dashboardService
                .countByDataRinnovoBeforeThanDays(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ), aziendaService.getOne(httpSessionUtil.getAziendaId()).getGiorniNotificheCliente());
    }

    public long numberRinnovoCoesistenza() {
        return dashboardService
                .countByDataRinnovoTrattenutaBeforeThanDays(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ), aziendaService.getOne(httpSessionUtil.getAziendaId()).getGiorniNotificheCliente());
    }

    public long numberRecall() {
        return dashboardService.countByDataRecallToday(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ));
    }

    public long preventiviInCorsoSize() {
        return dashboardService.countByPreventiviInCorso(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ));
    }

    public void findClienteToRecall(final String category) throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.NOTIFICATION_CATEGORY_PARAMETER,
                NotificationCategory.fromValue(category));
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Constants.getDatatableDashboardNotificationPath(false));
    }

    public void findPraticaRinnovabile(final String category) throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.NOTIFICATION_CATEGORY_PARAMETER,
                NotificationCategory.fromValue(category));
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Constants.getDatatableDashboardNotificationPath(false));
    }

    public void findCoesistenzaRinnovabile(final String category) throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.NOTIFICATION_CATEGORY_PARAMETER,
                NotificationCategory.fromValue(category));
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Constants.getDatatableDashboardNotificationPath(false));
    }

    public void findPreventivo() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Constants.getDatatableDashboardNotificationPreventivoPath(false));
    }

    //FINE WIDGET CLIENTI

    // PANNELLO INFO NOMINATIVI //

    public long nominativiDaLavorareSize() {
        final String statoNominativo = StatoNominativo.DA_LAVORARE.getValue();
        return statoNominativoService.countNominativiByStatoNominativo(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ), statoNominativo);
    }

    public long impegniRinnovabiliSize() {
        return dashboardNominativoService
                .countImpegniRinnovabili(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ), aziendaService.getOne(httpSessionUtil.getAziendaId()).getGiorniNotificheNominativi());
    }

    public long nominativiRecallSize() {
        return dashboardNominativoService.countNominativiInDataRecall(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ), new Date());
    }

    public long newLeadSize() {
        return dashboardNominativoService.countNewLead(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ));
    }

    public long nominativiOmessiSize() {
        return dashboardNominativoService.countNominativiOmessi(aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ));
    }

    // FINE PANNELLO INFO NOMINATIVI

}
