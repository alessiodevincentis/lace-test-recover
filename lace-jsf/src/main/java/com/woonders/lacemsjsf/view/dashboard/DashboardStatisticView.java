package com.woonders.lacemsjsf.view.dashboard;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.GeneralSettingViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.security.CustomSecurityUser;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.DashboardService;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.DashboardServiceImpl;
import com.woonders.lacemscommon.service.impl.SettingServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;

@ManagedBean(name = "dashboardStatisticView")
@ViewScoped
@Getter
@Setter
public class DashboardStatisticView implements Serializable {

    @ManagedProperty(DashboardServiceImpl.JSF_NAME)
    private DashboardService dashboardService;

    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;

    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;

    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;

    @ManagedProperty(SettingServiceImpl.JSF_NAME)
    private SettingService settingService;

    // TODO questo va rimosso dopo aver sistemato il datasource con l'hostname
    @PostConstruct
    public void init() {
        final AziendaViewModel aziendaViewModel = aziendaService.getOne(httpSessionUtil.getAziendaId());
        final CustomSecurityUser customSecurityUser = (CustomSecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        final GeneralSettingViewModel generalSettingViewModel = settingService.getGeneralSetting();
        customSecurityUser.setStorageEnabled(generalSettingViewModel.isStorageEnabled());
        customSecurityUser.setPrintPdfEnabled(aziendaViewModel.isPrintPdfEnabled());
    }

    public long numeroPraticheCaricate(final String tipoPratica) {
        return dashboardService.countByTipoPraticaInMeseCorrente(tipoPratica, aziendaSelectionView.getCurrentAziendaId(),
                httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(),
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ));
    }

    public long numeroPratichePerfezionate(final String tipoPratica) {
        return dashboardService.countByTipoPraticaPerfezionataInAnnoCorrente(tipoPratica, aziendaSelectionView.getCurrentAziendaId(),
                httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(),
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.READ));
    }
}
