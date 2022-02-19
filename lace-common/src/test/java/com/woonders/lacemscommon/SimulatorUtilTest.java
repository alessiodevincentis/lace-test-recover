package com.woonders.lacemscommon;

import com.woonders.lacemscommon.app.model.SimulatorResult;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableDetailsViewModel;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.db.entityenum.FeeType;
import com.woonders.lacemscommon.db.entityenum.SimulatorRoundingMode;
import com.woonders.lacemscommon.util.NumberUtil;
import com.woonders.lacemscommon.util.SimulatorUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by ema98 on 8/19/2017.
 */
public class SimulatorUtilTest {

    private final SimulatorUtil simulatorUtil = new SimulatorUtil(null, new NumberUtil());

    @Test
    public void pmtTest() {
        BigDecimal tassoInteresse = new BigDecimal("8.9");
        int durata = 120;
        int mensilita = 12;
        BigDecimal importoErogato = new BigDecimal("15856");

        assertEquals(new BigDecimal("200.00"), simulatorUtil.calcPayment(tassoInteresse, mensilita, durata, importoErogato, SimulatorRoundingMode.UP, BigDecimal.ZERO));
        assertEquals(new BigDecimal("199.99"), simulatorUtil.calcPayment(tassoInteresse, mensilita, durata, importoErogato, SimulatorRoundingMode.DOWN, BigDecimal.ZERO));
        assertEquals(new BigDecimal("200.00"), simulatorUtil.calcPayment(tassoInteresse, mensilita, durata, importoErogato, SimulatorRoundingMode.CLASSIC, BigDecimal.ZERO));

        tassoInteresse = new BigDecimal("8");
        durata = 120;
        mensilita = 12;
        importoErogato = new BigDecimal("10000");
        assertEquals(new BigDecimal("121.33"), simulatorUtil.calcPayment(tassoInteresse, mensilita, durata, importoErogato, SimulatorRoundingMode.CLASSIC, BigDecimal.ZERO));
    }

    @Test
    public void pvTest() {
        BigDecimal rata = new BigDecimal("200");
        BigDecimal tassoInteresse = new BigDecimal("8.9");
        int durata = 120;
        int mensilita = 12;

        assertEquals(new BigDecimal("15856.01"), simulatorUtil.calcLoanAmount(tassoInteresse, mensilita, durata, rata, SimulatorRoundingMode.UP));
        assertEquals(new BigDecimal("15856.00"), simulatorUtil.calcLoanAmount(tassoInteresse, mensilita, durata, rata, SimulatorRoundingMode.DOWN));
        assertEquals(new BigDecimal("15856.00"), simulatorUtil.calcLoanAmount(tassoInteresse, mensilita, durata, rata, SimulatorRoundingMode.CLASSIC));

        rata = new BigDecimal("121.33");
        tassoInteresse = new BigDecimal("8");
        durata = 120;
        mensilita = 12;
        assertEquals(new BigDecimal("10000.20"), simulatorUtil.calcLoanAmount(tassoInteresse, mensilita, durata, rata, SimulatorRoundingMode.CLASSIC));
    }

    @Test
    public void rateTest() {
        final int durata = 84;
        final BigDecimal rata = new BigDecimal("179.07");
        final int mensilita = 12;
        final BigDecimal importoErogato = new BigDecimal("12000");

        assertEquals(new BigDecimal("6.66"), simulatorUtil.calcRate(durata, mensilita, rata, importoErogato, SimulatorRoundingMode.UP, BigDecimal.ZERO));
        assertEquals(new BigDecimal("6.65"), simulatorUtil.calcRate(durata, mensilita, rata, importoErogato, SimulatorRoundingMode.DOWN, BigDecimal.ZERO));
        assertEquals(new BigDecimal("6.65"), simulatorUtil.calcRate(durata, mensilita, rata, importoErogato, SimulatorRoundingMode.CLASSIC, BigDecimal.ZERO));

    }

    //http://www.fineretum.it/html/Calcolatore4.html
    @Test
    public void calcTaegTest() {
        final int durata = 120;
        final int mensilita = 12;
        final BigDecimal importoErogato = new BigDecimal("10000");
        final BigDecimal rate = new BigDecimal("8");

        final BigDecimal rata = simulatorUtil.calcPayment(rate, mensilita, durata, importoErogato, SimulatorRoundingMode.CLASSIC, BigDecimal.ZERO);
        assertEquals(new BigDecimal("121.33"), rata);

        final BigDecimal rateAfterCalc = simulatorUtil.calcRate(durata, mensilita, rata, importoErogato, SimulatorRoundingMode.CLASSIC, BigDecimal.ZERO);
        assertEquals(new BigDecimal("8.00"), rateAfterCalc);

        assertEquals(new BigDecimal("8.30"), simulatorUtil.calcTaeg(rateAfterCalc, mensilita, SimulatorRoundingMode.CLASSIC));

        assertEquals(new BigDecimal("8.30"), simulatorUtil.calcTaeg(mensilita, durata, importoErogato, SimulatorRoundingMode.CLASSIC, null, rata));
    }

    @Test
    public void calcByAmountTest() {
        final SimulatorTableViewModel simulatorTableViewModel = SimulatorTableViewModel.builder().simulatorRoundingMode(SimulatorRoundingMode.CLASSIC).build();
        final List<SimulatorTableDetailsViewModel> simulatorTableDetailsViewModelList = new LinkedList<>();
        simulatorTableViewModel.setSimulatorTableDetailsViewModelList(simulatorTableDetailsViewModelList);
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(12).mensilita(12).tan(new BigDecimal("2")).maxTaeg(new BigDecimal("0.3")).build());
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(24).mensilita(12).tan(new BigDecimal("3")).maxTaeg(new BigDecimal("4")).build());
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(36).mensilita(12).tan(new BigDecimal("4")).maxAgentFees(new BigDecimal("0.01")).build());

        final BigDecimal amount = new BigDecimal("10000");

        //without agent fees

        final List<SimulatorResult> simulatorResultList = simulatorUtil.calcByAmount(amount, simulatorTableViewModel, null);
        assertEquals(3, simulatorResultList.size());

        final List<BigDecimal> paymentToCheckList = Arrays.asList(new BigDecimal("842.39"), new BigDecimal("429.81"), new BigDecimal("295.24"));
        final List<Integer> lenghtToCheckList = Arrays.asList(12, 24, 36);
        final List<BigDecimal> taegList = Arrays.asList(new BigDecimal("2.02"), new BigDecimal("3.04"), new BigDecimal("4.07"));
        final List<Boolean> exceededTaegList = Arrays.asList(true, false, false);
        final List<Boolean> exceededAgentFeesList = Arrays.asList(false, false, false);

        checkSimulatorResultListByAmount(simulatorResultList, paymentToCheckList, lenghtToCheckList, taegList, exceededTaegList, exceededAgentFeesList);

    }

    private void checkSimulatorResultListByAmount(final List<SimulatorResult> simulatorResultList, final List<BigDecimal> paymentToCheckList, final List<Integer> lenghtToCheckList,
                                                  final List<BigDecimal> taegList, final List<Boolean> exceededTaegList, final List<Boolean> exceededAgentFees) {
        for (int i = 0; i < simulatorResultList.size(); i++) {
            final SimulatorResult simulatorResult = simulatorResultList.get(i);
            assertEquals(paymentToCheckList.get(i), simulatorResult.getPayment());
            checkSimulatorResultListCommonValues(simulatorResult, lenghtToCheckList.get(i), taegList.get(i), exceededTaegList.get(i), exceededAgentFees.get(i));
        }
    }

    private void checkSimulatorResultListByPayment(final List<SimulatorResult> simulatorResultList, final List<BigDecimal> amountToCheckList, final List<Integer> lenghtToCheckList,
                                                   final List<BigDecimal> taegList, final List<Boolean> exceededTaegList, final List<Boolean> exceededAgentFees) {
        for (int i = 0; i < simulatorResultList.size(); i++) {
            final SimulatorResult simulatorResult = simulatorResultList.get(i);
            assertEquals(amountToCheckList.get(i), simulatorResult.getAmount());
            checkSimulatorResultListCommonValues(simulatorResult, lenghtToCheckList.get(i), taegList.get(i), exceededTaegList.get(i), exceededAgentFees.get(i));
        }
    }

    private void checkSimulatorResultListCommonValues(final SimulatorResult simulatorResult, final int lenghtToCheck,
                                                      final BigDecimal taeg, boolean exceededTaeg, final boolean exceededAgent) {
        assertEquals(lenghtToCheck, simulatorResult.getLength());
        assertEquals(taeg, simulatorResult.getTaeg());
        assertEquals(exceededTaeg, simulatorResult.isExceededTaeg());
        assertEquals(exceededAgent, simulatorResult.isExceededAgentFees());
    }

    @Test
    public void calcByPaymentTest() {

        final SimulatorTableViewModel simulatorTableViewModel = SimulatorTableViewModel.builder().simulatorRoundingMode(SimulatorRoundingMode.CLASSIC).build();
        final List<SimulatorTableDetailsViewModel> simulatorTableDetailsViewModelList = new LinkedList<>();
        simulatorTableViewModel.setSimulatorTableDetailsViewModelList(simulatorTableDetailsViewModelList);
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(12).mensilita(12).tan(new BigDecimal("5.5")).maxTaeg(new BigDecimal("0.3")).build());
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(24).mensilita(12).tan(new BigDecimal("5.5")).maxTaeg(new BigDecimal("8")).maxAgentFees(new BigDecimal("2")).build());
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(36).mensilita(12).tan(new BigDecimal("5.5")).maxAgentFees(new BigDecimal("0.01")).build());

        BigDecimal payment = new BigDecimal("200");
        BigDecimal agentFees = new BigDecimal("2");
        BigDecimal uprightRemaining = BigDecimal.ZERO;

        List<SimulatorResult> simulatorResultList;

        try {
            simulatorResultList = simulatorUtil.calcByPayment(payment, agentFees, simulatorTableViewModel, uprightRemaining);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Si è verificato un errore nella simulazione, verificare tutti i parametri scelti e riprovare."));
        }

        simulatorTableViewModel.setInquiryCostFeeType(FeeType.FIXED);

        simulatorResultList = simulatorUtil.calcByPayment(payment, agentFees, simulatorTableViewModel, uprightRemaining);
        assertEquals(simulatorTableDetailsViewModelList.size(), simulatorResultList.size());

        List<BigDecimal> netAmountToCheckList = Arrays.asList(new BigDecimal("2282.00"), new BigDecimal("4439.59"), new BigDecimal("6479.42"));
        List<Integer> lenghtToCheckList = Arrays.asList(12, 24, 36);
        List<BigDecimal> taegList = Arrays.asList(new BigDecimal("9.83"), new BigDecimal("7.88"), new BigDecimal("7.21"));
        List<Boolean> exceededTaegList = Arrays.asList(true, false, false);
        List<Boolean> exceededAgentFeesList = Arrays.asList(false, false, true);

        checkSimulatorResultListByPayment(simulatorResultList, netAmountToCheckList, lenghtToCheckList, taegList, exceededTaegList, exceededAgentFeesList);

        //NEW TEST with inquiry cost
        simulatorTableDetailsViewModelList.clear();
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(84).mensilita(12).tan(new BigDecimal("5.5")).inquiryCost(new BigDecimal("300.23")).build());
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(120).mensilita(6).tan(new BigDecimal("5.23")).inquiryCost(new BigDecimal("158.69")).build());

        payment = new BigDecimal("153.19");
        agentFees = new BigDecimal("1.58");
        uprightRemaining = new BigDecimal("3456.27");

        simulatorResultList = simulatorUtil.calcByPayment(payment, agentFees, simulatorTableViewModel, uprightRemaining);
        assertEquals(simulatorTableDetailsViewModelList.size(), simulatorResultList.size());

        netAmountToCheckList = Arrays.asList(new BigDecimal("10211.44"), new BigDecimal("6739.18"));
        lenghtToCheckList = Arrays.asList(84, 120);
        taegList = Arrays.asList(new BigDecimal("7.03"), new BigDecimal("6.66"));
        exceededTaegList = Arrays.asList(false, false);
        exceededAgentFeesList = Arrays.asList(false, false);

        checkSimulatorResultListByPayment(simulatorResultList, netAmountToCheckList, lenghtToCheckList, taegList, exceededTaegList, exceededAgentFeesList);

        //NEW TEST with max inquiry cost and without in percentage
        simulatorTableDetailsViewModelList.clear();
        simulatorTableViewModel.setInquiryCostFeeType(FeeType.PERCENTAGE);
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(120).mensilita(12).tan(new BigDecimal("5.23")).inquiryCost(new BigDecimal("3")).build());
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(120).mensilita(12).tan(new BigDecimal("5.23")).inquiryCost(new BigDecimal("3")).maxInquiryCost(new BigDecimal("200")).build());
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(120).mensilita(6).tan(new BigDecimal("5.23")).inquiryCost(new BigDecimal("3.34")).build());
        simulatorTableDetailsViewModelList.add(SimulatorTableDetailsViewModel.builder()
                .length(120).mensilita(6).tan(new BigDecimal("5.23")).inquiryCost(new BigDecimal("3.34")).maxInquiryCost(new BigDecimal("200")).build());

        payment = new BigDecimal("200.27");
        agentFees = new BigDecimal("2.69");
        uprightRemaining = new BigDecimal("458.69");

        simulatorResultList = simulatorUtil.calcByPayment(payment, agentFees, simulatorTableViewModel, uprightRemaining);
        assertEquals(simulatorTableDetailsViewModelList.size(), simulatorResultList.size());

        netAmountToCheckList = Arrays.asList(new BigDecimal("17327.97"), new BigDecimal("17848.94"), new BigDecimal("7889.31"), new BigDecimal("8491.99"));
        lenghtToCheckList = Arrays.asList(120, 120, 120, 120);
        taegList = Arrays.asList(new BigDecimal("7.12"), new BigDecimal("6.41"), new BigDecimal("9.33"), new BigDecimal("7.53"));
        exceededTaegList = Arrays.asList(false, false, false, false);
        exceededAgentFeesList = Arrays.asList(false, false, false, false);

        checkSimulatorResultListByPayment(simulatorResultList, netAmountToCheckList, lenghtToCheckList, taegList, exceededTaegList, exceededAgentFeesList);
    }
}
