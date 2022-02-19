package com.woonders.lacemsjsf.view.dashboard;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.laceenum.GraficoEnum;
import com.woonders.lacemscommon.service.DashboardService;
import com.woonders.lacemscommon.service.impl.DashboardServiceImpl;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "lineChartModelView")
@ViewScoped
@Getter
@Setter
public class LineChartModelView implements Serializable {

    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(DashboardServiceImpl.JSF_NAME)
    private DashboardService dashboardService;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    private GraficoEnum graficoEnum;

    private LineChartModel lineModel;
    private Map<String, Date> monthsChartMap;

    @PostConstruct
    public void init() {
        graficoEnum = GraficoEnum.MONTANTE_PERFEZIONATO;
        refreshChart();
    }

    public void refreshChart() {
        if (FacesUtil.doesPageNeedToBeRefreshed()) {
            monthsChartMap = generateMonthsChartMap();
            createLineModel();
        }
    }

    private LineChartModel initCategoryModel() {
        final LineChartModel model = new LineChartModel();

        final Role.RoleName graficiReadRoleName = httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.GRAFICI, OperatorViewModel.PermissionType.READ);

        //il break non ci sta di proposito, cosi se ho il permesso super, aggiungere super e quelli sottostanti
        //se ho all, aggiunge all e sottostante
        //se ho own, aggiunge solo own
        //it should never happen, but at the time of the first login if you didn-t set all the correct permissions it could happen
        if (graficiReadRoleName != null) {
            switch (graficiReadRoleName) {
                case GRAFICI_READ_SUPER:
                    model.addSeries(generateSelectedAgenziaOrAllMontanteChart());
                case GRAFICI_READ_ALL:
                    model.addSeries(generateOwnAgenziaMontanteChart());
                case GRAFICI_READ_OWN:
                    model.addSeries(generateOwnMontanteChart());
                    break;
            }
        }

        return model;
    }

    private ChartSeries generateOwnMontanteChart() {
        final ChartSeries montantePersonaleChart = new ChartSeries();
        montantePersonaleChart.setLabel("Personale");
        for (final Map.Entry<String, Date> monthsChartMapEntry : monthsChartMap.entrySet()) {
            montantePersonaleChart.set(monthsChartMapEntry.getKey(), dashboardService.calcTotalValueStatistics
                    (monthsChartMapEntry.getValue(), aziendaSelectionView.getCurrentAziendaId(),
                            Role.RoleName.GRAFICI_READ_OWN, httpSessionUtil.getOperatorId(),
                            httpSessionUtil.getAziendaId(), graficoEnum));
        }
        return montantePersonaleChart;
    }

    private ChartSeries generateOwnAgenziaMontanteChart() {
        final ChartSeries montanteAgenziaChart = new ChartSeries();
        montanteAgenziaChart.setLabel("Agenzia");
        for (final Map.Entry<String, Date> monthsChartMapEntry : monthsChartMap.entrySet()) {
            montanteAgenziaChart.set(monthsChartMapEntry.getKey(), dashboardService.calcTotalValueStatistics(
                    monthsChartMapEntry.getValue(), aziendaSelectionView.getCurrentAziendaId(),
                    Role.RoleName.GRAFICI_READ_ALL, httpSessionUtil.getOperatorId(),
                    httpSessionUtil.getAziendaId(), graficoEnum));
        }
        return montanteAgenziaChart;
    }

    private ChartSeries generateSelectedAgenziaOrAllMontanteChart() {
        final ChartSeries montanteAgenziaChart = new ChartSeries();
        montanteAgenziaChart.setLabel("Agenzia selezionata");
        for (final Map.Entry<String, Date> monthsChartMapEntry : monthsChartMap.entrySet()) {
            montanteAgenziaChart.set(monthsChartMapEntry.getKey(), dashboardService.calcTotalValueStatistics
                    (monthsChartMapEntry.getValue(), aziendaSelectionView.getCurrentAziendaId(),
                            Role.RoleName.GRAFICI_READ_SUPER, httpSessionUtil.getOperatorId(),
                            httpSessionUtil.getAziendaId(), graficoEnum));
        }
        return montanteAgenziaChart;
    }

    public void createLineModel() {
        lineModel = initCategoryModel();

        lineModel.setLegendPosition("ne");
        lineModel.setExtender("skinChart");
        lineModel.setShowPointLabels(true);
        lineModel.setAnimate(true);
        lineModel.getAxes().put(AxisType.X, new CategoryAxis());

        final Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
    }

    private Map<String, Date> generateMonthsChartMap() {
        final Map<String, Date> monthsChartMap = new LinkedHashMap<>();
        final Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.add(Calendar.YEAR, -1);
        for (int i = 1; i <= 13; i++) {
            monthsChartMap.put(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ITALIAN).toUpperCase() + "/"
                    + (cal.get(Calendar.YEAR) - 2000), cal.getTime());
            cal.add(Calendar.MONTH, 1);
        }
        return monthsChartMap;
    }

}
