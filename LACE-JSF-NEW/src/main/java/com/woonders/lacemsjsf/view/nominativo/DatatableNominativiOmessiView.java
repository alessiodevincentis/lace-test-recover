package com.woonders.lacemsjsf.view.nominativo;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.service.DashboardNominativoService;
import com.woonders.lacemscommon.service.impl.DashboardNominativoServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
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
import java.util.List;
import java.util.Map;

@ManagedBean(name = DatatableNominativiOmessiView.NAME)
@ViewScoped
@Getter
@Setter
public class DatatableNominativiOmessiView implements Serializable {

    public static final String NAME = "datatableNominativiOmessiView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private LazyDataModel<ClienteViewModel> clienteViewModelLazyDataModel;
    private ClienteViewModel selectedNominativoViewModel;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(DashboardNominativoServiceImpl.JSF_NAME)
    private DashboardNominativoService dashboardNominativoService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;


    @PostConstruct
    public void init() {
        retrieveNominativiByDataRecallExpired();
    }

    private void retrieveNominativiByDataRecallExpired() {
        clienteViewModelLazyDataModel = new LazyDataModel<ClienteViewModel>() {

            @Override
            public List<ClienteViewModel> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                               final Map<String, Object> filters) {
                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }
                final ViewModelPage<ClienteViewModel> viewModelPage = dashboardNominativoService.getNominativiWithDataRecallExpired(aziendaSelectionView.getCurrentAziendaId(),
                        httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
                        first, pageSize, sortField, QueryDSLHelper.SortOrder.valueOf(sortOrder.name()),
                        tableFilters);
                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public ClienteViewModel getRowData(final String rowKey) {
                return dashboardNominativoService.getNominativo(Long.parseLong(rowKey));
            }
        };

    }

    public void onRowSelect() throws IOException {
        if (Tipo.NOMINATIVO.getValue().equalsIgnoreCase(selectedNominativoViewModel.getTipo()))
            passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.NOMINATIVO_SELECTED_ON_SEARCH,
                    selectedNominativoViewModel);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsNominativoPath(false));
    }

    public void setNullDataRecall() {
        dashboardNominativoService.setNullDataRecallNominativo(selectedNominativoViewModel.getId());
    }

}
