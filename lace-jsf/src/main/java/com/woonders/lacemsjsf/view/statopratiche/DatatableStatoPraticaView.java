package com.woonders.lacemsjsf.view.statopratiche;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Pratica.TipoPratica;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.exception.UnableToUpdateException;
import com.woonders.lacemscommon.service.StatoPraticheService;
import com.woonders.lacemscommon.service.impl.StatoPraticheServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.log.LoggerConstants;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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

import static com.woonders.lacemsjsf.view.statopratiche.DatatableStatoPraticaView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class DatatableStatoPraticaView implements Serializable {

    public static final String NAME = "datatableStatoPraticaView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private static final Logger logger = LoggerFactory.getLogger(DatatableStatoPraticaView.class);
    private final String STATO_PRATICA_COLUMN_HEADER_TEXT = "StatoPratica";
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(StatoPraticheServiceImpl.JSF_NAME)
    private StatoPraticheService statoPraticheService;
    private LazyDataModel<ClientePratica> clientePraticaLazyDataModel;
    private ClientePratica selectedClientePratica;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    private String statoPratica;
    private String tipoPratica;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;

    @PostConstruct
    public void init() {
        statoPratica = (String) passaggioParametriUtils.getParametri().get(Parametro.STATO_PRATICA_PARAMETER);
        tipoPratica = (String) passaggioParametriUtils.getParametri()
                .get(Parametro.TIPO_PRATICA_PANNELLO_STATO_PRATICA_PARAMETER);
        retrieveClientePraticheListFromParameter();
    }

    public void onRowSelect() throws IOException {
        passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_SELECTED_ON_SEARCH, selectedClientePratica);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsPath(false));
    }

    private void retrieveClientePraticheListFromParameter() {
        clientePraticaLazyDataModel = new LazyDataModel<ClientePratica>() {

            @Override
            public List<ClientePratica> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                             final Map<String, Object> filters) {
                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }
                final ViewModelPage<ClientePratica> viewModelPage = statoPraticheService
                        .findByStatoPraticaAndTipoClienteAndUsername(aziendaSelectionView.getCurrentAziendaId(),
                                httpSessionUtil.getOperatorId(),
                                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI,
                                        OperatorViewModel.PermissionType.READ),
                                statoPratica, tipoPratica, first, pageSize, sortField,
                                QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);
                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public ClientePratica getRowData(final String rowKey) {
                return statoPraticheService.getClientePratica(Long.parseLong(rowKey));
            }
        };
    }

    // http://stackoverflow.com/questions/19548838/updating-entire-pdatatable-on-complete-of-pajax-event-celledit
    public void onCellEdit(final CellEditEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final PraticaViewModel praticaViewModel = context.getApplication().evaluateExpressionGet(context,
                "#{clientePratica.praticaViewModel}", PraticaViewModel.class);
        try {
            if (event != null) {
                if (STATO_PRATICA_COLUMN_HEADER_TEXT.equalsIgnoreCase(event.getColumn().getHeaderText())) {
                    statoPraticheService.updateStatoPraticaByCodicePratica((String) event.getNewValue(),
                            praticaViewModel.getCodicePratica());
                    FacesUtil.addMessage(propertiesUtil.getMsgSuccessEditStatoPratica());
                } else {
                    statoPraticheService.updateDataNotificaByCodicePratica((Date) event.getNewValue(),
                            praticaViewModel.getCodicePratica());
                    FacesUtil.addMessage(propertiesUtil.getMsgSuccessEditDataNotifica());
                }
            }
        } catch (final UnableToUpdateException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgError(), FacesMessage.SEVERITY_ERROR);
            logger.error(LoggerConstants.msgErrorEditStatoPratica, e);
        }
    }

    private void updateListWithNewValue(final List<ClientePratica> clientePraticaList, final PraticaViewModel praticaViewModel) {
        for (final ClientePratica clientePratica : clientePraticaList) {
            if (praticaViewModel.getCodicePratica() == clientePratica.getPraticaViewModel().getCodicePratica()) {
                clientePratica.setPraticaViewModel(praticaViewModel);
                break; // interrompe, una volta trovato l'elemento non ha senso
                // continuare a scorrere
            }
        }
    }

    public boolean isPannelloStatoPraticaCellEditorRendered() {
        return httpSessionUtil.hasAnyAuthority(RoleName.CLIENTI_WRITE_OWN, RoleName.CLIENTI_WRITE_ALL, RoleName.CLIENTI_WRITE_SUPER);
    }

    public boolean isDataIstruitaColumnRendered() {
        return !TipoPratica.PRESTITO.getValue().equalsIgnoreCase(tipoPratica);
    }
}
