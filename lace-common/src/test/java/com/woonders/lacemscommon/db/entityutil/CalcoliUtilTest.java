package com.woonders.lacemscommon.db.entityutil;

import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.laceenum.StringCalc;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 07/02/2017.
 */
public class CalcoliUtilTest {

    private CalcoliUtil calcoliUtil;

    @Before
    public void setUp() {
        calcoliUtil = new CalcoliUtil();
    }

    @Test
    public void calcPercentualProvvigioneTest() {
        String tipoProvvigione = StringCalc.EURO.getValue();
        BigDecimal provvigione = new BigDecimal("1743.31");
        Integer durata = 24;
        BigDecimal rata = new BigDecimal("123.45");

        assertEquals(new BigDecimal("58.839949"),
                calcoliUtil.calcPercentualeProvvigione(tipoProvvigione, provvigione, durata, rata));

        tipoProvvigione = StringCalc.EURO.getValue();
        provvigione = new BigDecimal("7690.68");
        durata = 60;
        rata = new BigDecimal("145.69");

        assertEquals(new BigDecimal("87.979957"),
                calcoliUtil.calcPercentualeProvvigione(tipoProvvigione, provvigione, durata, rata));

        tipoProvvigione = StringCalc.PERC.getValue();
        provvigione = new BigDecimal("10");
        durata = 0;
        rata = BigDecimal.ZERO;

        assertEquals(BigDecimal.ZERO,
                calcoliUtil.calcPercentualeProvvigione(tipoProvvigione, provvigione, durata, rata));

    }

    @Test
    public void calcProvvigioneTotaleTest() {
        String tipoProvvigione = StringCalc.PERC.getValue();
        BigDecimal percProvvigione = new BigDecimal("58.84");
        Integer durata = 24;
        BigDecimal rata = new BigDecimal("123.45");
        BigDecimal provvigione = BigDecimal.ZERO;

        assertEquals(new BigDecimal("1743.31"),
                calcoliUtil.calcProvvigioneTotale(tipoProvvigione, percProvvigione, durata, rata, provvigione, BigDecimal.ZERO, BigDecimal.ZERO, Pratica.TipoPratica.CESSIONE_P.getValue()));

        tipoProvvigione = StringCalc.PERC.getValue();
        percProvvigione = new BigDecimal("87.98");
        durata = 60;
        rata = new BigDecimal("145.69");
        provvigione = BigDecimal.ZERO;

        assertEquals(new BigDecimal("7690.68"),
                calcoliUtil.calcProvvigioneTotale(tipoProvvigione, percProvvigione, durata, rata, provvigione, BigDecimal.ZERO, BigDecimal.ZERO, Pratica.TipoPratica.CESSIONE_P.getValue()));

        tipoProvvigione = StringCalc.EURO.getValue();
        percProvvigione = BigDecimal.ZERO;
        durata = 0;
        rata = BigDecimal.ZERO;
        provvigione = new BigDecimal("111.11");

        assertEquals(new BigDecimal("111.11"),
                calcoliUtil.calcProvvigioneTotale(tipoProvvigione, percProvvigione, durata, rata, provvigione, BigDecimal.ZERO, BigDecimal.ZERO, Pratica.TipoPratica.CESSIONE_P.getValue()));
    }
}
