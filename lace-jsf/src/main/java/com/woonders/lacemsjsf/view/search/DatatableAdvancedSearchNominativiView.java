package com.woonders.lacemsjsf.view.search;

import com.woonders.lacemscommon.app.model.AdvancedSearchViewModel;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.SearchService;
import com.woonders.lacemscommon.service.impl.SearchServiceImpl;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = DatatableAdvancedSearchNominativiView.NAME)
@ViewScoped
@Getter
@Setter
public class DatatableAdvancedSearchNominativiView implements Serializable {

    public static final String NAME = "datatableAdvancedSearchNominativiView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    @ManagedProperty(SearchServiceImpl.JSF_NAME)
    private SearchService searchService;
    private LazyDataModel<ClienteViewModel> clienteViewModelLazyDataModel;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    private ClienteViewModel selectedNominativo;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    private AdvancedSearchViewModel advancedSearchNominativiViewModel;

    @PostConstruct
    public void init() {
        advancedSearchNominativiViewModel = (AdvancedSearchViewModel)
                passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.ADVANCED_SEARCH_NOMINATIVI_PARAMETER);

        clienteViewModelLazyDataModel = new LazyDataModel<ClienteViewModel>() {

            @Override
            public List<ClienteViewModel> load(final int first, final int pageSize, final String sortField,
                                               final SortOrder sortOrder, final Map<String, Object> filters) {

                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }

                final ViewModelPage<ClienteViewModel> viewModelPage = searchService.advancedSearchNominativi(
                        httpSessionUtil.getOperatorId(),
                        httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
                        httpSessionUtil.getAziendaId(), advancedSearchNominativiViewModel,
                        first, pageSize, sortField,
                        com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);

                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public ClienteViewModel getRowData(final String rowKey) {
                try {
                    return searchService.findOne(Long.parseLong(rowKey));
                } catch (final ItemNotFoundException e) {
                    FacesUtil.addMessage("Nominativo non trovato", FacesMessage.SEVERITY_ERROR);
                    return null;
                }
            }
        };
    }

    public void onRowSelect() throws IOException {
        if (Tipo.NOMINATIVO.getValue().equalsIgnoreCase(selectedNominativo.getTipo()))
            passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.NOMINATIVO_SELECTED_ON_SEARCH,
                    selectedNominativo);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsNominativoPath(false));
    }
}
