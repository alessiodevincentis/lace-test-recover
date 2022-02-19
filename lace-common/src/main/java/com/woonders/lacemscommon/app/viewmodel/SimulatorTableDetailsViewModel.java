package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by ema98 on 8/10/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "viewId"}, callSuper = false)
@ToString
public class SimulatorTableDetailsViewModel extends AbstractBaseViewModel {

    private final String viewId = UUID.randomUUID().toString();
    private long id;
    private int length;
    private int mensilita;
    private BigDecimal tan;
    private BigDecimal maxTaeg;
    private BigDecimal maxAgentFees;
    private BigDecimal inquiryCost;
    private BigDecimal inquiryCostTaeg;
    private BigDecimal maxInquiryCost;
    private BigDecimal managementFees;
    private BigDecimal managementFeesTaeg;
    private BigDecimal maxManagementFees;
    private BigDecimal stampDutyCost;
    private BigDecimal stampDutyCostTaeg;
    private BigDecimal maxStampDutyCost;
    private BigDecimal otherCosts;
    private BigDecimal otherCostsTaeg;
    private BigDecimal maxOtherCosts;
    private BigDecimal insuranceCost;
    private BigDecimal insuranceCostTaeg;
    private BigDecimal maxInsuranceCost;
    private SimulatorTableViewModel simulatorTableViewModel;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
