package com.woonders.lacemsjsf.view.dashboard;

import com.woonders.lacemscommon.app.model.ClientePreventivo;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.service.DashboardService;
import com.woonders.lacemscommon.service.impl.DashboardServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "datatableDashboardNotificationPreventivoView")
@ViewScoped
@Getter
@Setter
public class DatatableDashboardNotificationPreventivoView implements Serializable {

    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    private LazyDataModel<ClientePreventivo> clientiPreventiviList;
    private ClientePreventivo selectedClientePreventivo;
    @ManagedProperty(DashboardServiceImpl.JSF_NAME)
    private DashboardService dashboardService;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;

    @PostConstruct
    public void init() {
        clientiPreventiviList = new LazyDataModel<ClientePreventivo>() {

            @Override
            public List<ClientePreventivo> load(final int first, final int pageSize, final String sortField,
                                                final SortOrder sortOrder, final Map<String, Object> filters) {

                final ViewModelPage<ClientePreventivo> viewModelPage = dashboardService.findByPreventiviInCorso(
                        aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAziendaId(),
                        httpSessionUtil.getOperatorId(),
                        httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                        first, pageSize, sortField,
                        com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()));

                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public ClientePreventivo getRowData(final String rowKey) {
                return dashboardService.getClientePreventivo(Long.parseLong(rowKey));
            }
        };
    }

    public void onRowSelect() throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_SELECTED_ON_SEARCH,
                dashboardService.getPraticaOnPreventivoCliente(selectedClientePreventivo,
                        httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                        httpSessionUtil.getOperatorId()));
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsPath(false));
    }

    public void setNotificationPreventivo() {
        dashboardService.removeNotificaPreventivo(selectedClientePreventivo.getPreventivoViewModel());
    }

}
