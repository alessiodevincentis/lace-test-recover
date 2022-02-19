package com.woonders.lacemsjsf.view.nominativo;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.DashboardNominativoService;
import com.woonders.lacemscommon.service.NominativoService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.DashboardNominativoServiceImpl;
import com.woonders.lacemscommon.service.impl.NominativoServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Data;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = DashboardNominativo.NAME)
@ViewScoped
@Data
public class DashboardNominativo implements Serializable {

    public static final String NAME = "dashboardNominativo";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @ManagedProperty(DashboardNominativoServiceImpl.JSF_NAME)
    private DashboardNominativoService dashboardNominativoService;

    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;

    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;

    private Date dateRecall = new Date();
    private LazyDataModel<ClienteViewModel> clienteViewModelLazyDataModel;
    private ClienteViewModel selectedNominativo;
    @ManagedProperty(NominativoServiceImpl.JSF_NAME)
    private NominativoService nominativoService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;

    @PostConstruct
    public void init() {
        listRecall();
    }

    public void onRowSelect() throws IOException {
        if (Tipo.NOMINATIVO.getValue().equalsIgnoreCase(selectedNominativo.getTipo()))
            passaggioParametriUtils.getParametri().put(Parametro.NOMINATIVO_SELECTED_ON_SEARCH, selectedNominativo);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsNominativoPath(false));
    }

    public void listRecall() {
        clienteViewModelLazyDataModel = new LazyDataModel<ClienteViewModel>() {

            @Override
            public List<ClienteViewModel> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                               final Map<String, Object> filters) {
                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }
                final ViewModelPage<ClienteViewModel> viewModelPage = dashboardNominativoService.getNominativiInDataRecall(
                        aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
                        dateRecall, first, pageSize, sortField, QueryDSLHelper.SortOrder.valueOf(sortOrder.name()),
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

    public void onDateSelect(final SelectEvent event) {
        dateRecall = (Date) event.getObject();
        listRecall();
    }

    public void viewRinnovoImpegni() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Constants.getDatatableNominativoRinnoviPath(false));
    }

    public void viewNominativiDaLavorare() throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.STATO_NOMINATIVO_PARAMETER, StatoNominativo.DA_LAVORARE.getValue());
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Constants.getDatatableStatoNominativoPath(true));
    }

    public void viewNominativiOmessi() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(Constants.getDatatableNominativoOmessiPath(false));
    }

}
