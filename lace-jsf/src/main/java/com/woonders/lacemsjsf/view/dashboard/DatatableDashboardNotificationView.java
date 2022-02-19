package com.woonders.lacemsjsf.view.dashboard;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.DashboardService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.DashboardServiceImpl;
import com.woonders.lacemscommon.service.impl.DashboardServiceImpl.NotificationCategory;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "datatableDashboardNotificationView")
@ViewScoped
@Getter
@Setter
public class DatatableDashboardNotificationView implements Serializable {

    private final static String RINNOVO_PRATICA = "Pratiche rinnovabili";
    private final static String RINNOVO_COESISTENZE = "Coesistenze Rinnovabili";
    private final static String DA_RICHIAMARE = "Clienti Da Richiamare";
    private LazyDataModel<ClientePratica> clientiListLazy;

    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    private ClientePratica selectedCliente;
    private boolean renderedRecallColumn;
    private boolean renderedRinnovoPraticaColumn;
    private boolean renderedRinnovoCoesistenzaColumn;
    @ManagedProperty(DashboardServiceImpl.JSF_NAME)
    private DashboardService dashboardService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    private String tableTitle;

    @PostConstruct
    public void init() {
        allClientiPratica();
    }


    private void allClientiPratica() {

        final NotificationCategory category = (NotificationCategory) passaggioParametriUtils.getParametri()
                .get(Parametro.NOTIFICATION_CATEGORY_PARAMETER);

        clientiListLazy = new LazyDataModel<ClientePratica>() {

            @Override
            public List<ClientePratica> load(final int first, final int pageSize, final String sortField,
                                             final SortOrder sortOrder, final Map<String, Object> filters) {

                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }

                ViewModelPage<ClientePratica> viewModelPage = new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);

                if (category != null)
                    switch (category) {
                        case RECALL:
                            viewModelPage = dashboardService.findByDataRecallToday(
                                    aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAziendaId(),
                                    httpSessionUtil.getOperatorId(),
                                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                                    first, pageSize, sortField,
                                    com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);

                            setTableTitle(DA_RICHIAMARE);
                            setRenderedRecallColumn(true);
                            break;
                        case PRATICA:

                            viewModelPage = dashboardService
                                    .findByDataRinnovoBeforeThanDays(aziendaSelectionView.getCurrentAziendaId(),
                                            httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(),
                                            httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                                            aziendaService.getOne(httpSessionUtil.getAziendaId()).getGiorniNotificheCliente(), first,
                                            pageSize, sortField,
                                            com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);

                            setTableTitle(RINNOVO_PRATICA);
                            setRenderedRinnovoPraticaColumn(true);
                            break;
                        case COESISTENZA:

                            viewModelPage = dashboardService
                                    .findByDataRinnovoTrattenutaBeforeThanDays(aziendaSelectionView.getCurrentAziendaId(),
                                            httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(),
                                            httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                                            aziendaService.getOne(httpSessionUtil.getAziendaId()).getGiorniNotificheCliente(), first,
                                            pageSize, sortField,
                                            com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);

                            setTableTitle(RINNOVO_COESISTENZE);
                            setRenderedRinnovoCoesistenzaColumn(true);
                            break;
                    }

                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public ClientePratica getRowData(final String rowKey) {
                return dashboardService.getPratica(Long.parseLong(rowKey));
            }
        };
    }

    public void onRowSelect() throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_SELECTED_ON_SEARCH, selectedCliente);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsPath(false));
    }

    public void setNotification() {
        if (isRenderedRecallColumn()) {
            dashboardService.removeNotificaRecall(selectedCliente.getPraticaViewModel());
        } else if (isRenderedRinnovoPraticaColumn()) {
            dashboardService.removeNotificaRinnovoPratica(selectedCliente.getPraticaViewModel());
        } else {
            dashboardService.removeNotificaTrattenuta(selectedCliente.getTrattenuteViewModel());
        }
    }
}
