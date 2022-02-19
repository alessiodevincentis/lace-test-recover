package com.woonders.lacemsjsf.view.analytics;

import com.lowagie.text.*;
import com.woonders.lacemscommon.app.model.AnalyticsProvvigioneCliente;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.laceenum.AnalyticsDateEnum;
import com.woonders.lacemscommon.service.AnalyticsClientiService;
import com.woonders.lacemscommon.service.impl.AnalyticsClientiServiceImpl;
import com.woonders.lacemscommon.util.AnalyticsUtil;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemscommon.util.NumberUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.PieChartModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = AnalyticsClientiView.NAME)
@ViewScoped
@Getter
@Setter
public class AnalyticsClientiView implements Serializable {

    public static final String NAME = "analyticsClientiView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String TOT_PROVVIGIONE_AGENZIA = "Totale provvigione agenzia: ";

    private List<String> operatoriSelezionati;
    private AnalyticsDateEnum presetDate;
    private LocalDate from;
    private LocalDate to;
    private PieChartModel pieModelPraticheTotali;
    private PieChartModel pieModelPraticheCaricate;
    private PieChartModel pieModelPraticheLiquidate;

    @ManagedProperty(AnalyticsClientiServiceImpl.JSF_NAME)
    private AnalyticsClientiService analyticsClientiService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(DateConversionUtil.JSF_NAME)
    private DateConversionUtil dateConversionUtil;
    @ManagedProperty(NumberUtil.JSF_NAME)
    private NumberUtil numberUtil;
    private List<AnalyticsProvvigioneCliente> analyticsProvvigioneClienteList;
    private BigDecimal totaleProvvigioneAgenzia;
    @ManagedProperty(AnalyticsUtil.JSF_NAME)
    private AnalyticsUtil analyticsUtil;


    @PostConstruct
    public void init() {
        setDateFromPreset();
        analyticsProvvigioneClienteList = new ArrayList<>();
    }

    public void setDateFromPreset() {
        from = analyticsUtil.getFromDateFormPresetDate(presetDate);
        to = analyticsUtil.getToDateFormPresetDate(presetDate);
    }

    public void setListProvvigioniOperator() {
        analyticsProvvigioneClienteList = analyticsClientiService.getAnalyticsProvvigioneClienteList(
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId(), aziendaSelectionView.getCurrentAziendaId(),
                dateConversionUtil.calcDateFromLocalDate(from), dateConversionUtil.calcDateFromLocalDate(to), operatoriSelezionati);
    }

    public BigDecimal getTotaleProvvigioniAgenzia() {
        totaleProvvigioneAgenzia = analyticsClientiService.sumProvvigioniAgenzia(analyticsProvvigioneClienteList);
        return totaleProvvigioneAgenzia;
    }

    public PieChartModel getPieModelPraticheCaricate() {
        pieModelPraticheCaricate = new PieChartModel();
        final List<String> statoPraticheCaricate = StatoPratica.getStatiPraticheCaricate();

        for (final Pratica.TipoPratica tipoPratica : Pratica.TipoPratica.values()) {
            pieModelPraticheCaricate.set(tipoPratica.getValue(),
                    getNumberPraticheByTipoPraticaAndStatoPratica(tipoPratica, statoPraticheCaricate));
        }

        setOptionPieChart(pieModelPraticheCaricate);
        return pieModelPraticheCaricate;
    }

    public long getTotalePraticheCaricate() {
        return getNumberPraticheByTipoPraticaAndStatoPratica(null, StatoPratica.getStatiPraticheCaricate());
    }

    public PieChartModel getPieModelPraticheLiquidate() {
        pieModelPraticheLiquidate = new PieChartModel();
        final List<String> statoPraticheLiquidate = StatoPratica.getStatiPraticheLiquidate();

        for (final Pratica.TipoPratica tipoPratica : Pratica.TipoPratica.values()) {
            pieModelPraticheLiquidate.set(tipoPratica.getValue(),
                    getNumberPraticheByTipoPraticaAndStatoPratica(tipoPratica, statoPraticheLiquidate));
        }

        setOptionPieChart(pieModelPraticheLiquidate);
        return pieModelPraticheLiquidate;
    }

    public long getTotalePraticheLiquidate() {
        return getNumberPraticheByTipoPraticaAndStatoPratica(null, StatoPratica.getStatiPraticheLiquidate());
    }

    public PieChartModel getPieModelPraticheTotali() {
        pieModelPraticheTotali = new PieChartModel();
        final List<String> statoPratichePrincipali = StatoPratica.getMainStatiPratica();

        for (final Pratica.TipoPratica tipoPratica : Pratica.TipoPratica.values()) {
            pieModelPraticheTotali.set(tipoPratica.getValue(),
                    getNumberPraticheByTipoPraticaAndStatoPratica(tipoPratica, statoPratichePrincipali));
        }

        setOptionPieChart(pieModelPraticheTotali);
        return pieModelPraticheTotali;
    }

    public long getTotalePratiche() {
        return getNumberPraticheByTipoPraticaAndStatoPratica(null, StatoPratica.getMainStatiPratica());
    }

    private void setOptionPieChart(final PieChartModel pieChart) {
        pieChart.setShowDataLabels(true);
        pieChart.setLegendPosition("ne");
        pieChart.setExtender("skinPie");
    }

    private long getNumberPraticheByTipoPraticaAndStatoPratica(final Pratica.TipoPratica tipoPratica,
                                                               final List<String> statoPraticheList) {

        if (from != null && to != null && from.compareTo(to) <= 0) {
            return analyticsClientiService.countPraticheByStatoPraticheCollectionAndBetweenDateAndTipoPratica(
                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                    httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId(), aziendaSelectionView.getCurrentAziendaId(),
                    statoPraticheList, null, dateConversionUtil.calcDateFromLocalDate(from),
                    dateConversionUtil.calcDateFromLocalDate(to), tipoPratica, operatoriSelezionati);
        }
        return 0;
    }

    private BigDecimal getSumMontanteOrErogateCaricateOrLiquidate(final List<String> statoPraticaList, final boolean isPratichePrestito,
                                                                  final boolean isSumMontante) {

        if (from != null && to != null && from.compareTo(to) <= 0) {
            return analyticsClientiService.sumByMontanteOrErogatoPraticheByStatoPraticheCollectionAndBetweenDateAndTipoPratica(
                    httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
                    httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId(), aziendaSelectionView.getCurrentAziendaId(),
                    statoPraticaList, isPratichePrestito, dateConversionUtil.calcDateFromLocalDate(from),
                    dateConversionUtil.calcDateFromLocalDate(to), isSumMontante, null, operatoriSelezionati);
        }
        return BigDecimal.ZERO;
    }

    public String getSumMontanteCaricate() {
        return numberUtil.italianCurrencyFormat(getSumMontanteOrErogateCaricateOrLiquidate(StatoPratica.getStatiPraticheCaricate(),
                false, true));
    }

    public String getSumMontanteLiquidate() {
        return numberUtil.italianCurrencyFormat(getSumMontanteOrErogateCaricateOrLiquidate(StatoPratica.getStatiPraticheLiquidate(),
                false, true));
    }

    public String getSumErogatoCaricate() {
        return numberUtil.italianCurrencyFormat(getSumMontanteOrErogateCaricateOrLiquidate(StatoPratica.getStatiPraticheCaricate(),
                true, false));
    }

    public String getSumErogatoLiquidate() {
        return numberUtil.italianCurrencyFormat(getSumMontanteOrErogateCaricateOrLiquidate(StatoPratica.getStatiPraticheLiquidate(),
                true, false));
    }

    public void preProcessPDF(final Object document) throws IOException, DocumentException {
        final Document pdf = (Document) document;
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.open();

        final Phrase phraseAfter1 = new Paragraph(dateConversionUtil.calcStringFromLocalDate(from) + " - " +
                dateConversionUtil.calcStringFromLocalDate(to));
        final Phrase phraseAfter2 = new Paragraph(TOT_PROVVIGIONE_AGENZIA + totaleProvvigioneAgenzia.toString());
        final Phrase phraseAfter3 = new Paragraph(System.lineSeparator());

        pdf.add(phraseAfter1);
        pdf.add(phraseAfter2);
        pdf.add(phraseAfter3);

    }
}
