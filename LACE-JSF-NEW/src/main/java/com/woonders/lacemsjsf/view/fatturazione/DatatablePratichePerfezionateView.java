package com.woonders.lacemsjsf.view.fatturazione;

import com.lowagie.text.*;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.service.FatturazioneService;
import com.woonders.lacemscommon.service.impl.FatturazioneServiceImpl;
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
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.woonders.lacemsjsf.view.fatturazione.DatatablePratichePerfezionateView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class DatatablePratichePerfezionateView implements Serializable {

    public static final String NAME = "datatablePratichePerfezionateView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(FatturazioneServiceImpl.JSF_NAME)
    private FatturazioneService fatturazioneService;
    private BigDecimal totaleMontante;
    private BigDecimal totaleProvvigione;
    private String provenienza;
    private String provenienzaDesc;
    private LazyDataModel<ClientePratica> clientePraticaLazyDataModel;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;

    @PostConstruct
    public void init() {
        retrievePratichePerfezionateFromParameter();
    }

    private void retrievePratichePerfezionateFromParameter() {
    	 //*** mod by Cristian 23-11-21
    	provenienza = (String) passaggioParametriUtils.getParametri()
                .get(Parametro.PROVENIENZA);
    	provenienzaDesc = (String) passaggioParametriUtils.getParametri()
                .get(Parametro.PROVENIENZA_DESC);
    	//******
    	
        final Date dataInizio = (Date) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_PERFEZIONATE_DATAINIZIO_PARAMETER);
        final Date dataFine = (Date) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_PERFEZIONATE_DATAFINE_PARAMETER);
        final String operatore = (String) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_PERFEZIONATE_OPERATORE_PARAMETER);

        totaleMontante = fatturazioneService.sumTotaleMontantePratichePerfezionate(provenienza, provenienzaDesc, dataInizio, dataFine, operatore, aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                httpSessionUtil.getAziendaId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.READ));
        totaleProvvigione = fatturazioneService.sumTotaleProvvigionePratichePerfezionate(provenienza, provenienzaDesc, dataInizio, dataFine,
                operatore, aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                httpSessionUtil.getAziendaId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.READ));

        clientePraticaLazyDataModel = new LazyDataModel<ClientePratica>() {

            @Override
            public List<ClientePratica> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                             final Map<String, Object> filters) {

                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }

                final ViewModelPage<ClientePratica> viewModelPage = fatturazioneService.searchPratichePerfezionate(provenienza, provenienzaDesc, dataInizio,
                        dataFine, operatore, aziendaSelectionView.getCurrentAziendaId(), first, pageSize, sortField,
                        QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters, httpSessionUtil.getOperatorId(),
                        httpSessionUtil.getAziendaId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.READ));

                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public ClientePratica getRowData(final String rowKey) {
                return fatturazioneService.getPratica(Long.parseLong(rowKey));
            }
        };
    }

    public void preProcessPDF(final Object document) throws IOException, BadElementException, DocumentException {
        final Document pdf = (Document) document;
        pdf.setPageSize(PageSize.A3.rotate());
        pdf.open();

        final Phrase phraseAfter3 = new Paragraph("   ");
        final Phrase phraseAfter6 = new Paragraph("   Montante Totale: " + totaleMontante.toString());
        final Phrase phraseAfter7 = new Paragraph("   Provvigione Totale: " + totaleProvvigione.toString());
        final Phrase phraseAfter8 = new Paragraph("   ");

        pdf.add(phraseAfter3);
        pdf.add(phraseAfter6);
        pdf.add(phraseAfter7);
        pdf.add(phraseAfter8);

    }

}
