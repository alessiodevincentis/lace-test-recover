package com.woonders.lacemsjsf.view.simulator;


import com.woonders.lacemscommon.app.model.SimulatorResult;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.laceenum.StringCalc;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import lombok.Getter;
import lombok.Setter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ManagedBean(name = SimulatorUtilsView.NAME)
@ViewScoped
@Getter
@Setter
public class SimulatorUtilsView implements Serializable {

    public static final String NAME = "simulatorUtilsView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;


    public Object getObjectFromSimulatorResultRowChoosen(final boolean isPraticaViewModel) {
        final SimulatorResult simulatorResult = (SimulatorResult) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_RESULT_ROW_CHOOSEN);
        if (simulatorResult != null) {
            final Pratica.TipoPratica simulatorTableType = (Pratica.TipoPratica) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_TYPE_CHOOSEN);
            final BigDecimal paymentChoosen = (BigDecimal) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_PAYMENT_CHOOSEN);
            final BigDecimal amountChoosen = (BigDecimal) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_AMOUNT_CHOOSEN);
            final BigDecimal agentFees = (BigDecimal) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_AGENT_FEES_CHOOSEN);
            final SimulatorTableViewModel simulatorTableViewModel = (SimulatorTableViewModel) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_VIEW_MODEL);

            BigDecimal payment = BigDecimal.ZERO;
            BigDecimal amount = BigDecimal.ZERO;
            BigDecimal amountTotal = BigDecimal.ZERO;
            String type = null;

            if (simulatorTableType != null) {
                switch (simulatorTableType) {
                    case PRESTITO:
                        type = Pratica.TipoPratica.PRESTITO.getValue();
                        payment = simulatorResult.getPayment();
                        amount = amountChoosen;
                        amountTotal = simulatorResult.getAmountTotal();
                        break;
                    case CESSIONE_S:
                        type = Pratica.TipoPratica.CESSIONE_S.getValue();
                        payment = paymentChoosen;
                        amount = simulatorResult.getAmount();
                        break;
                    case DELEGA:
                        type = Pratica.TipoPratica.DELEGA.getValue();
                        payment = paymentChoosen;
                        amount = simulatorResult.getAmount();
                        break;
                    case CESSIONE_P:
                        type = Pratica.TipoPratica.CESSIONE_P.getValue();
                        payment = paymentChoosen;
                        amount = simulatorResult.getAmount();
                        break;
                }

            }

            passaggioParametriUtils.getParametri().remove(PassaggioParametriUtils.Parametro.SIMULATOR_RESULT_ROW_CHOOSEN);

            if (isPraticaViewModel) {
                return getPraticaViewModelWithSimulatorResultRowChoosen(simulatorResult, payment, amount, agentFees, amountTotal, type, simulatorTableViewModel);
            } else {
                return getPreventivoViewModelWithSimulatorResultRowChoosen(simulatorResult, payment, amount, agentFees, type, simulatorTableViewModel);
            }

        }
        return null;

    }


    private PraticaViewModel getPraticaViewModelWithSimulatorResultRowChoosen(final SimulatorResult simulatorResult, final BigDecimal payment, final BigDecimal amount,
                                                                              final BigDecimal agentFees, final BigDecimal amountTotal, final String type,
                                                                              final SimulatorTableViewModel simulatorTableViewModel) {

        final OperatorViewModel operatorViewModel = operatorService.getOne(httpSessionUtil.getOperatorId());
        return PraticaViewModel.builder().durata(simulatorResult.getLength()).rata(payment).spese(simulatorResult.getTotalCosts())
                .montante(simulatorResult.getUpright()).interessi(simulatorResult.getInterests()).importoErogato(amount).operatore(operatorViewModel)
                .provTotale(simulatorResult.getAgentFeesTotal()).percProv(agentFees).capitaleFinanziato(amountTotal.toString()).tipoProvvigione(StringCalc.PERC.getValue())
                .tipoPratica(type).taeg(simulatorResult.getTaeg()).tan(simulatorResult.getTan()).simulatorTableViewModel(simulatorTableViewModel).build();

    }

    private PreventivoViewModel getPreventivoViewModelWithSimulatorResultRowChoosen(final SimulatorResult simulatorResult, final BigDecimal payment, final BigDecimal amount,
                                                                                    final BigDecimal agentFees, final String type, final SimulatorTableViewModel simulatorTableViewModel) {

        return PreventivoViewModel.builder().durata(simulatorResult.getLength()).rata(payment).spese(simulatorResult.getTotalCosts())
                .importo(amount).percProvvigione(agentFees).provTotale(simulatorResult.getAgentFeesTotal()).dataCreazione(new Date())
                .tipoProvvigione(StringCalc.PERC.getValue()).tipoPratica(type).taeg(simulatorResult.getTaeg()).tan(simulatorResult.getTan())
                .simulatorTableViewModel(simulatorTableViewModel).interessi(simulatorResult.getInterests()).build();
    }

}
