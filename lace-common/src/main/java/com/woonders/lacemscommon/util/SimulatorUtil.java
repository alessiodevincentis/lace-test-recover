package com.woonders.lacemscommon.util;

import com.google.common.annotations.VisibleForTesting;
import com.woonders.lacemscommon.app.model.SimulatorResult;
import com.woonders.lacemscommon.app.model.util.bigdecimal.BigDecimalUtils;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableDetailsViewModel;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.db.entityenum.FeeType;
import com.woonders.lacemscommon.db.entityenum.SimulatorRoundingMode;
import com.woonders.lacemscommon.service.SimulatorTableService;
import org.apache.poi.ss.formula.functions.FinanceLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ema98 on 8/19/2017.
 */
//https://exceljet.net/excel-functions/excel-rate-function
//http://www.calcolatoremutui.it/tan-e-taeg/
//https://poi.apache.org/apidocs/org/apache/poi/ss/formula/functions/FinanceLib.html
//http://www.telemutuo.it/culturamutui/formule-mutui.html
//http://www.calcoloprestito.org/
//http://www.fineretum.it/html/Calcolatore4.html
//http://www.calcoloprestiti.org/calcolo-taeg-prestito.htm
//spese sul finanziato vanno sommate a importoErogato negativo per calcolo rata
//spese sul taeg vanno sommate a importoErogato negativo per il calcolo del taeg
@Component
public class SimulatorUtil {

    public static final String NAME = "simulatorUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private final SimulatorTableService simulatorTableService;
    private final NumberUtil numberUtil;

    @Autowired
    public SimulatorUtil(final SimulatorTableService simulatorTableService, final NumberUtil numberUtil) {
        this.simulatorTableService = simulatorTableService;
        this.numberUtil = numberUtil;
    }

    public List<SimulatorResult> calcByAmount(final BigDecimal amount, final long simulatorTableId, final BigDecimal agentFees) {
        final SimulatorTableViewModel simulatorTableViewModel = simulatorTableService.findOne(simulatorTableId);
        final List<SimulatorTableDetailsViewModel> simulatorTableDetailsViewModelList = simulatorTableService.getDetails(simulatorTableId);
        simulatorTableViewModel.setSimulatorTableDetailsViewModelList(simulatorTableDetailsViewModelList);
        return calcByAmount(amount, simulatorTableViewModel, agentFees);
    }

    /**
     * @param amount importo desiderato
     * @return used for simple loan (Prestito Personale)
     */
    @VisibleForTesting
    public List<SimulatorResult> calcByAmount(final BigDecimal amount, final SimulatorTableViewModel simulatorTableViewModel, final BigDecimal agentFees) {

        final List<SimulatorResult> simulatorResultList = new LinkedList<>();

        final SimulatorRoundingMode simulatorRoundingMode = simulatorTableViewModel.getSimulatorRoundingMode();

        for (final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel : simulatorTableViewModel.getSimulatorTableDetailsViewModelList()) {

            final int lenght = simulatorTableDetailsViewModel.getLength();
            final int mensilita = simulatorTableDetailsViewModel.getMensilita();
            final BigDecimal rate = simulatorTableDetailsViewModel.getTan();
            final BigDecimal maxTaeg = simulatorTableDetailsViewModel.getMaxTaeg();
            final BigDecimal maxAgentFees = simulatorTableDetailsViewModel.getMaxAgentFees();

            final BigDecimal financialCosts = calcSumCostsByAmount(CostType.FINANCIAL, amount, simulatorTableViewModel, simulatorTableDetailsViewModel);

            final BigDecimal taegCosts = calcSumCostsByAmount(CostType.TAEG, amount, simulatorTableViewModel, simulatorTableDetailsViewModel);
            final BigDecimal payment = calcPayment(rate, mensilita, lenght,
                    amount, simulatorTableViewModel.getSimulatorRoundingMode(), financialCosts);
            final BigDecimal taeg = calcTaeg(mensilita, lenght, amount, simulatorRoundingMode, taegCosts, payment);

            final BigDecimal amountTotal = calcAmountTotal(amount, financialCosts);
            final BigDecimal upright = calcUpright(payment, lenght);

            final BigDecimal interests = calcInterestsByAmount(upright, amount, financialCosts);

            final BigDecimal agentFeesTotal = calcPercentageOnAmount(interests, numberUtil.ifNullToZero(agentFees));


            boolean exceededTaeg = false;
            if (maxTaeg != null) {
                exceededTaeg = BigDecimalUtils.is(taeg).gt(maxTaeg);
            }

            boolean exceededAgentFees = false;
            if (agentFees != null && maxAgentFees != null) {
                exceededAgentFees = BigDecimalUtils.is(agentFees).gt(maxAgentFees);
            }


            simulatorResultList.add(SimulatorResult.builder().length(lenght).payment(payment).taeg(taeg).totalCosts(financialCosts)
                    .exceededTaeg(exceededTaeg).exceededAgentFees(exceededAgentFees).amountTotal(amountTotal)
                    .interests(interests).upright(upright).agentFeesTotal(agentFeesTotal).tan(simulatorTableDetailsViewModel.getTan()).build());
        }

        return simulatorResultList;
    }

    public List<SimulatorResult> calcByPayment(final BigDecimal payment, final BigDecimal agentFees, final long simulatorTableId, final BigDecimal uprightRemaining) {
        final SimulatorTableViewModel simulatorTableViewModel = simulatorTableService.findOne(simulatorTableId);
        final List<SimulatorTableDetailsViewModel> simulatorTableDetailsViewModelList = simulatorTableService.getDetails(simulatorTableId);
        simulatorTableViewModel.setSimulatorTableDetailsViewModelList(simulatorTableDetailsViewModelList);
        return calcByPayment(payment, agentFees, simulatorTableViewModel, uprightRemaining);
    }

    /**
     * @param payment   rata
     * @param agentFees commissioni
     * @return used for employee loan (Cessione del Quinto)
     */
    @VisibleForTesting
    public List<SimulatorResult> calcByPayment(final BigDecimal payment, final BigDecimal agentFees, final SimulatorTableViewModel simulatorTableViewModel, final BigDecimal uprightRemaining) {
        final List<SimulatorResult> simulatorResultList = new LinkedList<>();

        final SimulatorRoundingMode simulatorRoundingMode = simulatorTableViewModel.getSimulatorRoundingMode();

        for (final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel : simulatorTableViewModel.getSimulatorTableDetailsViewModelList()) {

            final int lenght = simulatorTableDetailsViewModel.getLength();
            final int mensilita = simulatorTableDetailsViewModel.getMensilita();
            final BigDecimal rate = simulatorTableDetailsViewModel.getTan();
            final BigDecimal maxTaeg = simulatorTableDetailsViewModel.getMaxTaeg();
            final BigDecimal maxAgentFees = simulatorTableDetailsViewModel.getMaxAgentFees();
            final BigDecimal agentFeesPayment = numberUtil.ifNullToZero(agentFees);

            //calcolo
            final BigDecimal amount = calcLoanAmount(rate, mensilita, lenght, payment, simulatorRoundingMode);

            final BigDecimal upright = calcUpright(payment, lenght);

            final BigDecimal agentFeesTotal = calcPercentageOnAmount(upright.subtract(numberUtil.ifNullToZero(uprightRemaining)), agentFeesPayment);

            final BigDecimal financialCosts = calcSumCostsByPayment(CostType.FINANCIAL, upright, payment, agentFeesTotal, simulatorTableViewModel, simulatorTableDetailsViewModel);

            final BigDecimal taegCosts = calcSumCostsByAmount(CostType.TAEG, amount, simulatorTableViewModel, simulatorTableDetailsViewModel);

            final BigDecimal amountSubstractCosts = amount.subtract(financialCosts);

            final BigDecimal interests = calcInterestsByPayment(upright, amountSubstractCosts, financialCosts, agentFeesTotal);

            final BigDecimal taeg = calcTaeg(mensilita, lenght, amountSubstractCosts, simulatorRoundingMode, taegCosts, payment);


            boolean exceededTaeg = false;
            if (maxTaeg != null) {
                exceededTaeg = BigDecimalUtils.is(taeg).gt(maxTaeg);
            }

            boolean exceededAgentFees = false;
            if (agentFeesPayment != null && maxAgentFees != null) {
                exceededAgentFees = BigDecimalUtils.is(agentFeesPayment).gt(maxAgentFees);
            }


            simulatorResultList.add(SimulatorResult.builder().length(lenght).payment(payment).taeg(taeg)
                    .exceededTaeg(exceededTaeg).exceededAgentFees(exceededAgentFees).amount(amountSubstractCosts)
                    .interests(interests).upright(upright).agentFeesTotal(agentFeesTotal).tan(simulatorTableDetailsViewModel.getTan())
                    .totalCosts(financialCosts).build());
        }

        return simulatorResultList;
    }


    private BigDecimal calcUpright(final BigDecimal payment, final Integer lenght) {
        if (payment != null && lenght != null) {
            return payment.multiply(new BigDecimal(lenght));
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calcInterestsByAmount(final BigDecimal upright, final BigDecimal amount, final BigDecimal costs) {
        if (upright != null && amount != null && costs != null) {
            return upright.subtract(costs).subtract(amount);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calcInterestsByPayment(final BigDecimal upright, final BigDecimal amount, final BigDecimal costs, final BigDecimal agentFeesTotal) {
        if (upright != null && amount != null && costs != null && agentFeesTotal != null) {
            return upright.subtract(costs).subtract(amount).subtract(agentFeesTotal);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calcAmountTotal(final BigDecimal amount, final BigDecimal costs) {
        if (amount != null && costs != null) {
            return amount.add(costs);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calcSumCostsByAmount(final CostType costType, final BigDecimal amount, final SimulatorTableViewModel simulatorTableViewModel, final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel) {
        BigDecimal costs = BigDecimal.ZERO;

        costs = calcCosts(costType, costs, amount, simulatorTableViewModel, simulatorTableDetailsViewModel);

        return costs;
    }

    private BigDecimal calcSumCostsByPayment(final CostType costType, final BigDecimal upright, final BigDecimal payment, final BigDecimal agentFeesTotal, final SimulatorTableViewModel simulatorTableViewModel, final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel) {
        BigDecimal costs = BigDecimal.ZERO;

        costs = costs.add(agentFeesTotal);

        costs = calcCosts(costType, costs, upright, simulatorTableViewModel, simulatorTableDetailsViewModel);

        return costs;
    }

    private BigDecimal calcCosts(final CostType costType, BigDecimal costs, final BigDecimal fromWhat, final SimulatorTableViewModel simulatorTableViewModel, final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel) {
        if (costType == null) {
            throw new IllegalArgumentException();
        }

        switch (costType) {
            case TAEG:
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getInsuranceCostFeeType(), simulatorTableDetailsViewModel.getInsuranceCostTaeg(), simulatorTableDetailsViewModel.getMaxInsuranceCost()));
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getInquiryCostFeeType(), simulatorTableDetailsViewModel.getInquiryCostTaeg(), simulatorTableDetailsViewModel.getMaxInquiryCost()));
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getManagementFeesFeeType(), simulatorTableDetailsViewModel.getManagementFeesTaeg(), simulatorTableDetailsViewModel.getMaxManagementFees()));
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getStampDutyFeeType(), simulatorTableDetailsViewModel.getStampDutyCostTaeg(), simulatorTableDetailsViewModel.getMaxStampDutyCost()));
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getOtherCostsFeeType(), simulatorTableDetailsViewModel.getOtherCostsTaeg(), simulatorTableDetailsViewModel.getMaxOtherCosts()));
                break;
            case FINANCIAL:
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getInsuranceCostFeeType(), simulatorTableDetailsViewModel.getInsuranceCost(), simulatorTableDetailsViewModel.getMaxInsuranceCost()));
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getInquiryCostFeeType(), simulatorTableDetailsViewModel.getInquiryCost(), simulatorTableDetailsViewModel.getMaxInquiryCost()));
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getManagementFeesFeeType(), simulatorTableDetailsViewModel.getManagementFees(), simulatorTableDetailsViewModel.getMaxManagementFees()));
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getStampDutyFeeType(), simulatorTableDetailsViewModel.getStampDutyCost(), simulatorTableDetailsViewModel.getMaxStampDutyCost()));
                costs = costs.add(calcCost(fromWhat, simulatorTableViewModel.getOtherCostsFeeType(), simulatorTableDetailsViewModel.getOtherCosts(), simulatorTableDetailsViewModel.getMaxOtherCosts()));
                break;
        }

        return costs;
    }

    private BigDecimal calcCost(final BigDecimal amount, final FeeType feeType, final BigDecimal cost, final BigDecimal maxCost) {
        BigDecimal costToReturn = BigDecimal.ZERO;

        //se entrambi diversi da null posso fare il calcolo
        if (feeType != null && cost != null) {
            switch (feeType) {
                case FIXED:
                    costToReturn = cost;
                    break;
                case PERCENTAGE:
                    costToReturn = calcPercentageOnAmount(amount, cost);
                    break;
            }
            if (maxCost != null) {
                if (BigDecimalUtils.is(costToReturn).gt(maxCost)) {
                    costToReturn = maxCost;
                }
            }
            return costToReturn;
        }

        //se cost != null ma non ho messo feeType, throw exception
        if (feeType == null && cost != null) {
            throw new IllegalArgumentException("Si è verificato un errore nella simulazione, verificare tutti i parametri scelti e riprovare.");
        }

        return BigDecimal.ZERO;

    }

    //divisione e arrotondamento da fare tutto insieme. Prima faceva calcPercentage su cost, e arrotondava subito cost facendo sbagliare i calcoli
    private BigDecimal calcPercentageOnAmount(final BigDecimal amount, final BigDecimal cost) {
        if (cost != null && amount != null) {
            return amount.multiply(cost).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Scale is always 2
     */
    private BigDecimal round(final double value, final SimulatorRoundingMode simulatorRoundingMode) {
        return new BigDecimal(value).setScale(2, simulatorRoundingMode.getRoundingMode());
    }

    /**
     * FutureValue is always 0
     * Type is always false (passare 0 se soldi prelevati inizio mese, 1 se fine mese)
     *
     * @param rate                    rate given by the table (TAN)
     * @param numberOfPaymentsInAYear number of e.g. monthly installments
     * @param loanLength              lenght of the loan in months
     * @param loanAmount              amount of the loan
     * @param simulatorRoundingMode   rounding mode given by the table
     * @return
     */
    public BigDecimal calcPayment(final BigDecimal rate, final int numberOfPaymentsInAYear, final int loanLength, final BigDecimal loanAmount, final SimulatorRoundingMode simulatorRoundingMode, final BigDecimal costs) {
        //negative loanAmount
        //divide by 100 because rate is in %
        final double payment = FinanceLib.pmt(rate.doubleValue() / numberOfPaymentsInAYear / 100, loanLength, (loanAmount.add(numberUtil.ifNullToZero(costs)).doubleValue()) * -1, 0, false);
        return round(payment, simulatorRoundingMode);
    }

    /**
     * @param rate                    rate given by the table (TAN)
     * @param numberOfPaymentsInAYear number of e.g. monthly installments
     * @param loanLength              lenght of the loan in months
     * @param payment                 amount of the e.g. monthly installment
     * @param simulatorRoundingMode   rounding mode given by the table
     * @return
     */
    public BigDecimal calcLoanAmount(final BigDecimal rate, final int numberOfPaymentsInAYear, final int loanLength, final BigDecimal payment, final SimulatorRoundingMode simulatorRoundingMode) {
        //negative payment
        //divide by 100 because rate is in %
        //divide by 12 because to calculate the parameter "n" we need the number of payments, NOT the length of the loan
        final double loanAmount = FinanceLib.pv(rate.doubleValue() / numberOfPaymentsInAYear / 100, numberOfPaymentsInAYear * loanLength / 12, payment.doubleValue() * -1, 0, false);
        return round(loanAmount, simulatorRoundingMode);
    }

    /**
     * @param lenght                  lenght of the loan in months
     * @param numberOfPaymentsInAYear number of e.g. monthly installments
     * @param payment                 amount of the e.g. monthly installment
     * @param loanAmount              amount of the loan
     * @param simulatorRoundingMode   rounding mode given by the table
     * @return
     */
    public BigDecimal calcRate(final int lenght, final int numberOfPaymentsInAYear, final BigDecimal payment, final BigDecimal loanAmount, final SimulatorRoundingMode simulatorRoundingMode, final BigDecimal costs) {
        //multiply * 100 to have the rate in %
        //negative loanAmount
        //divide by 12 because to calculate the parameter "n" we need the number of payments, NOT the length of the loan
        final double rate = calcRate(numberOfPaymentsInAYear * lenght / 12, payment.doubleValue(), (loanAmount.subtract(costs)).doubleValue() * -1, 0, 0, 0.10) * numberOfPaymentsInAYear * 100;
        return round(rate, simulatorRoundingMode);
    }

    /**
     * @param rate                    rate calculated with calcRate
     * @param numberOfPaymentsInAYear number of e.g. monthly installments
     * @param simulatorRoundingMode   rounding mode given by the table
     * @return
     */
    public BigDecimal calcTaeg(final BigDecimal rate, final int numberOfPaymentsInAYear, final SimulatorRoundingMode simulatorRoundingMode) {
        //divide by 100 because rate is in %, then multiply by 100 again to have the result in %
        final double taeg = (Math.pow(1 + (rate.doubleValue() / numberOfPaymentsInAYear / 100), numberOfPaymentsInAYear) - 1) * 100;
        return round(taeg, simulatorRoundingMode);
    }

    public BigDecimal calcTaeg(final int numberOfPaymentsInAYear, final int loanLength, final BigDecimal loanAmount, final SimulatorRoundingMode simulatorRoundingMode, final BigDecimal costs, final BigDecimal payment) {
        //togliere spese a calcRate
        final BigDecimal rateAfterCalc = calcRate(loanLength, numberOfPaymentsInAYear, payment, loanAmount, simulatorRoundingMode, numberUtil.ifNullToZero(costs));
        return calcTaeg(rateAfterCalc, numberOfPaymentsInAYear, simulatorRoundingMode);
    }

    /**
     * https://github.com/cuba-platform/apache-poi/blob/master/poi/src/java/org/apache/poi/ss/formula/functions/Rate.java
     * Calcola TAE (tasso da calcolare prima del TAEG)
     *
     * @param nper  durata
     * @param pmt   rata
     * @param pv    importo erogato
     * @param fv    valore futuro, passare 0
     * @param type  passare 0 se soldi prelevati inizio mese, 1 se fine mese
     * @param guess passare 0.10 di default
     * @return
     */
    private double calcRate(final double nper, final double pmt, final double pv, final double fv, final double type, final double guess) {
        //FROM MS http://office.microsoft.com/en-us/excel-help/rate-HP005209232.aspx
        final int FINANCIAL_MAX_ITERATIONS = 20;//Bet accuracy with 128
        final double FINANCIAL_PRECISION = 0.0000001;//1.0e-8

        double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
        double rate = guess;
        if (Math.abs(rate) < FINANCIAL_PRECISION) {
            y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
        } else {
            f = Math.exp(nper * Math.log(1 + rate));
            y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
        }
        y0 = pv + pmt * nper + fv;
        y1 = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;

        // find root by Newton secant method
        i = x0 = 0.0;
        x1 = rate;
        while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS)) {
            rate = (y1 * x0 - y0 * x1) / (y1 - y0);
            x0 = x1;
            x1 = rate;

            if (Math.abs(rate) < FINANCIAL_PRECISION) {
                y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
            } else {
                f = Math.exp(nper * Math.log(1 + rate));
                y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
            }

            y0 = y1;
            y1 = y;
            ++i;
        }
        return rate;
    }


    private enum CostType {
        FINANCIAL, TAEG
    }
}
