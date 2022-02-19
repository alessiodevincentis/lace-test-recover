package com.woonders.lacemscommon.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ema98 on 8/15/2017.
 */
@Component
public class NumberUtil {

    public static final String NAME = "numberUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";

    public boolean isNullOrZero(final BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.equals(BigDecimal.ZERO);
    }

    public BigDecimal ifNullToZero(final BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        return bigDecimal;
    }

    public String italianCurrencyFormat(final BigDecimal value) {
        return NumberFormat.getCurrencyInstance(Locale.ITALY)
                .format(value);
    }
}
