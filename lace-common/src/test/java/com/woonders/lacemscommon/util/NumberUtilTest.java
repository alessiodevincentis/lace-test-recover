package com.woonders.lacemscommon.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ema98 on 8/15/2017.
 */
public class NumberUtilTest {

    private NumberUtil numberUtil = new NumberUtil();

    @Test
    public void isNullOrZeroTest() {
        assertTrue(numberUtil.isNullOrZero(null));
        assertTrue(numberUtil.isNullOrZero(BigDecimal.ZERO));
        assertFalse(numberUtil.isNullOrZero(BigDecimal.ONE));
    }
}
