package com.woonders.lacemscommon.app.model;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimulatorResult {

    private int length;
    private BigDecimal payment;
    private BigDecimal interests;
    private BigDecimal amount;
    private BigDecimal amountTotal;
    private BigDecimal tan;
    private BigDecimal taeg;
    private BigDecimal upright;
    private boolean exceededTaeg;
    private boolean exceededAgentFees;
    private BigDecimal agentFeesTotal;
    private BigDecimal totalCosts;
}
