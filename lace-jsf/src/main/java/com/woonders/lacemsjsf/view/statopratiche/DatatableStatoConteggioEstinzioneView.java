package com.woonders.lacemsjsf.view.statopratiche;


import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.EstinzionePraticaCliente;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Trattenute;
import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import com.woonders.lacemscommon.exception.UnableToUpdateException;
import com.woonders.lacemscommon.service.StatoPraticheService;
import com.woonders.lacemscommon.service.impl.StatoPraticheServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.log.LoggerConstants;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
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

import static com.woonders.lacemsjsf.view.statopratiche.DatatableStatoConteggioEstinzioneView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class DatatableStatoConteggioEstinzioneView implements Serializable {

    public static final String NAME = "datatableStatoConteggioEstinzioneView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final Logger logger = LoggerFactory.getLogger(DatatableStatoConteggioEstinzioneView.class);
    private final String STATO_CONTEGGIO_ESTINZIONE_COLUMN_HEADER_TEXT = "StatoConteggioEstinzione";
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(StatoPraticheServiceImpl.JSF_NAME)
    private StatoPraticheService statoPraticheService;
    private LazyDataModel<EstinzionePraticaCliente> estinzioneViewModelLazyDataModel;
    private EstinzionePraticaCliente selectedEstinzione;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    private StatoConteggioEstinzione statoConteggioEstinzione;
    private String tipoEstinzione;

    @PostConstruct
    public void init() {
        statoConteggioEstinzione = (StatoConteggioEstinzione) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.STATO_CONTEGGIO_ESTINZIONE_PARAMETER);
        tipoEstinzione = (String) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.TIPO_ESTINZIONE_PANNELLO_STATO_CONTEGGIO_ESTINZIONE_PARAMETER);
        retrieveClientePraticheListFromParameter();
    }

    public void onRowSelect() throws IOException {
        final ClientePratica selectedClientePratica = statoPraticheService.getClientePratica(selectedEstinzione.getPraticaViewModel().getCodicePratica());
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.CLIENTE_SELECTED_ON_SEARCH, selectedClientePratica);
        FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsPath(false));
    }


    private void retrieveClientePraticheListFromParameter() {
        estinzioneViewModelLazyDataModel = new LazyDataModel<EstinzionePraticaCliente>() {

            @Override
            public List<EstinzionePraticaCliente> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                                       final Map<String, Object> filters) {
                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }
                final ViewModelPage<EstinzionePraticaCliente> viewModelPage = statoPraticheService
                        .findEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(aziendaSelectionView.getCurrentAziendaId(),
                                httpSessionUtil.getOperatorId(),
                                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI,
                                        OperatorViewModel.PermissionType.READ),
                                statoConteggioEstinzione, tipoEstinzione, first, pageSize, sortField,
                                QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);
                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public EstinzionePraticaCliente getRowData(final String rowKey) {
                return statoPraticheService.getEstinzionePraticaCliente(Long.parseLong(rowKey));
            }
        };
    }

    // http://stackoverflow.com/questions/19548838/updating-entire-pdatatable-on-complete-of-pajax-event-celledit
    public void onCellEdit(final CellEditEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final EstinzioneViewModel estinzioneViewModel = context.getApplication().evaluateExpressionGet(context,
                "#{estinzionePratica.estinzioneViewModel}", EstinzioneViewModel.class);
        try {
            if (event != null) {
                if (STATO_CONTEGGIO_ESTINZIONE_COLUMN_HEADER_TEXT.equalsIgnoreCase(event.getColumn().getHeaderText())) {
                    statoPraticheService.updateStatoConteggioEstinzioneByIdEstinzione((StatoConteggioEstinzione) event.getNewValue(),
                            estinzioneViewModel.getIdEstinzione());
                    FacesUtil.addMessage(propertiesUtil.getMsgSuccessEditStatoConteggioEstinzione());
                } else {
                    statoPraticheService.updateDataNotificaConteggioEstinzioneByIdEstinzione((Date) event.getNewValue(),
                            estinzioneViewModel.getIdEstinzione());
                    FacesUtil.addMessage(propertiesUtil.getMsgSuccessEditDataNotificaConteggioEstinzione());
                }
            }
        } catch (final UnableToUpdateException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgError(), FacesMessage.SEVERITY_ERROR);
            logger.error(LoggerConstants.msgErrorEditStatoConteggioEstinzione, e);
        }
    }


    public boolean isDataNotificaStatoConteggioRendered() {
        return !StatoConteggioEstinzione.RICHIESTA_CONTEGGIO.equals(statoConteggioEstinzione);
    }

    public boolean isDataRinnovoColumnRendered() {
        return !Trattenute.TipoImpegno.PRESTITO.getValue().equalsIgnoreCase(tipoEstinzione);
    }
}
