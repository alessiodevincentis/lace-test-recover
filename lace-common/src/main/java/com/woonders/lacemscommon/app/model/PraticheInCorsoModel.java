package com.woonders.lacemscommon.app.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PraticheInCorsoModel {
	private String tipoPratica;
	private BigDecimal rata = BigDecimal.ZERO;
	private Integer durata = new Integer(0);
}
