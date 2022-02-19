package com.woonders.lacemsjsf.view.search;


import com.woonders.lacemscommon.app.model.AdvancedSearchViewModel;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.laceenum.TipoRinnovo;
import com.woonders.lacemscommon.service.SearchService;
import com.woonders.lacemscommon.service.impl.SearchServiceImpl;
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
import java.util.List;
import java.util.Map;

@ManagedBean(name = DatatableAdvancedSearchClientiView.NAME)
@ViewScoped
@Getter
@Setter
public class DatatableAdvancedSearchClientiView implements Serializable {

    public static final String NAME = "datatableAdvancedSearchClientiView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    @ManagedProperty(SearchServiceImpl.JSF_NAME)
    private SearchService searchService;
    private LazyDataModel<ClientePratica> clientePraticaLazyDataModel;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    private ClientePratica selectedCliente;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    private AdvancedSearchViewModel advancedSearchClientiViewModel;

    public boolean isRenderedColumnCoesistenza() {
        return advancedSearchClientiViewModel != null && TipoRinnovo.COESISTENZA.equals(advancedSearchClientiViewModel.getTipoRinnovo());
    }

    @PostConstruct
    public void init() {
        advancedSearchClientiViewModel = (AdvancedSearchViewModel)
                passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.ADVANCED_SEARCH_CLIENTI_PARAMETER);

        clientePraticaLazyDataModel = new LazyDataModel<ClientePratica>() {

            @Override
            public List<ClientePratica> load(final int first, final int pageSize, final String sortField,
                                             final SortOrder sortOrder, final Map<String, Object> filters) {

                final ViewModelPage<ClientePratica> viewModelPage = searchService.advancedSearchClienti(
                        httpSessionUtil.getOperatorId(),
                        httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                        httpSessionUtil.getAziendaId(), advancedSearchClientiViewModel,
                        first, pageSize, sortField,
                        com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()));

                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public ClientePratica getRowData(final String rowKey) {
                return searchService.getPratica(Long.parseLong(rowKey));
            }
        };
    }

    public void onRowSelect() throws IOException {
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.CLIENTE_SELECTED_ON_SEARCH,
                selectedCliente);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsPath(false));
    }
}
