package com.woonders.lacemsjsf.view.analytics;

import com.woonders.lacemscommon.app.model.AnalyticsNominativiOperatore;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entityenum.Provenienza;
import com.woonders.lacemscommon.laceenum.AnalyticsDateEnum;
import com.woonders.lacemscommon.service.AnalyticsNominativiService;
import com.woonders.lacemscommon.service.impl.AnalyticsNominativiServiceImpl;
import com.woonders.lacemscommon.util.AnalyticsUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.PieChartModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = AnalyticsNominativiView.NAME)
@ViewScoped
@Getter
@Setter
public class AnalyticsNominativiView {

    public static final String NAME = "analyticsNominativiView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String NO_FORNITORE = "Nessun fornitore di Lead";

    private List<String> operatoriSelezionati;
    private AnalyticsDateEnum presetDate;
    private LocalDate from;
    private LocalDate to;
    private PieChartModel pieModelNominativiTotali;
    private PieChartModel pieModelNominativiToClienti;
    private PieChartModel pieModelNominativiLeadTotali;
    private PieChartModel pieModelNominativiLeadToClienti;

    @ManagedProperty(AnalyticsNominativiServiceImpl.JSF_NAME)
    private AnalyticsNominativiService analyticsNominativiService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(AnalyticsUtil.JSF_NAME)
    private AnalyticsUtil analyticsUtil;
    private List<AnalyticsNominativiOperatore> analyticsNominativiOperatoreList;

    @PostConstruct
    public void init() {
        setDateFromPreset();
        analyticsNominativiOperatoreList = new ArrayList<>();
    }

    public void setDateFromPreset() {
        from = analyticsUtil.getFromDateFormPresetDate(presetDate);
        to = analyticsUtil.getToDateFormPresetDate(presetDate);
    }

    public void setListNominativiOperatoreList() {
        analyticsNominativiOperatoreList = analyticsNominativiService.getListNominativiTotalAndBecomeClienti(
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
                httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId(), aziendaSelectionView.getCurrentAziendaId(),
                from, to, operatoriSelezionati);
    }

    public PieChartModel getPieModelNominativiTotaliByProvenienza() {
        pieModelNominativiTotali = new PieChartModel();

        for (final Provenienza provenienza : Provenienza.values()) {
            pieModelNominativiTotali.set(provenienza.getValue(), countNominativiByProvenienza(provenienza));
        }

        setOptionPieChart(pieModelNominativiTotali);
        return pieModelNominativiTotali;
    }

    public PieChartModel getPieModelNominativiToClientiByProvenienza() {
        pieModelNominativiToClienti = new PieChartModel();

        for (final Provenienza provenienza : Provenienza.values()) {
            pieModelNominativiToClienti.set(provenienza.getValue(), countNominativiToClientiByProvenienza(provenienza));
        }

        setOptionPieChart(pieModelNominativiToClienti);
        return pieModelNominativiToClienti;
    }

    public PieChartModel getPieModelNominativiLeadByFornitore() {
        pieModelNominativiLeadTotali = new PieChartModel();
        final List<String> allFornitoriLead = getAllFornitoriLead();

        if (allFornitoriLead != null && !allFornitoriLead.isEmpty()) {
            for (final String fornitore : allFornitoriLead) {
                pieModelNominativiLeadTotali.set(fornitore, countNominativiProvenienzaLead(fornitore));
            }
        } else {
            pieModelNominativiLeadTotali.set(NO_FORNITORE, 0);
        }

        setOptionPieChart(pieModelNominativiLeadTotali);
        return pieModelNominativiLeadTotali;
    }

    public PieChartModel getPieModelNominativiLeadToClientiByFornitore() {
        pieModelNominativiLeadToClienti = new PieChartModel();
        final List<String> allFornitoriLead = getAllFornitoriLead();

        if (allFornitoriLead != null && !allFornitoriLead.isEmpty()) {
            for (final String fornitore : allFornitoriLead) {
                pieModelNominativiLeadToClienti.set(fornitore, countNominativiProvenienzaLeadToCliente(fornitore));
            }
        } else {
            pieModelNominativiLeadToClienti.set(NO_FORNITORE, 0);
        }

        setOptionPieChart(pieModelNominativiLeadToClienti);
        return pieModelNominativiLeadToClienti;
    }

    private List<String> getAllFornitoriLead() {
        return analyticsNominativiService.getAllFornitoriLead(from, to);
    }

    public long getNominativiTotali() {
        return countNominativiByProvenienza(null);
    }

    public long getNominativiToClienti() {
        return countNominativiToClientiByProvenienza(null);
    }


    private long countNominativiByProvenienza(final Provenienza provenienza) {
        return analyticsNominativiService.countNominativiTotaliByProvenienza(
                aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI,
                        OperatorViewModel.PermissionType.READ), provenienza, from, to,
                httpSessionUtil.getAziendaId(), operatoriSelezionati, null);
    }

    private long countNominativiToClientiByProvenienza(final Provenienza provenienza) {
        return analyticsNominativiService.countNominativiDiventatiClientiByProvenienza(
                aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getOperatorId(),
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI,
                        OperatorViewModel.PermissionType.READ), provenienza, from, to,
                httpSessionUtil.getAziendaId(), operatoriSelezionati, null);
    }

    private long countNominativiProvenienzaLead(final String fornitore) {
        return analyticsNominativiService.countNominativiProvenienzaLead(
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI,
                        OperatorViewModel.PermissionType.READ), httpSessionUtil.getOperatorId(),
                httpSessionUtil.getAziendaId(), aziendaSelectionView.getCurrentAziendaId(), from, to,
                operatoriSelezionati, fornitore);
    }

    private long countNominativiProvenienzaLeadToCliente(final String fornitore) {
        return analyticsNominativiService.countNominativiProvenienzaLeadToCliente(
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI,
                        OperatorViewModel.PermissionType.READ), httpSessionUtil.getOperatorId(),
                httpSessionUtil.getAziendaId(), aziendaSelectionView.getCurrentAziendaId(), from, to,
                operatoriSelezionati, fornitore);
    }

    private void setOptionPieChart(final PieChartModel pieChart) {
        pieChart.setShowDataLabels(true);
        pieChart.setLegendPosition("ne");
        pieChart.setExtender("skinPie");
    }
}
