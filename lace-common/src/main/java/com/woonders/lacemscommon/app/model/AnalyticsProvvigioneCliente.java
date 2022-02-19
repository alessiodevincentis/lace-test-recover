package com.woonders.lacemscommon.app.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsProvvigioneCliente {

    private String username;
    private BigDecimal provvigioneTot;
    private BigDecimal provvigioneCqs;
    private BigDecimal provvigioneCqp;
    private BigDecimal ProvvigioneDlg;
    private BigDecimal provvigionePp;
}
