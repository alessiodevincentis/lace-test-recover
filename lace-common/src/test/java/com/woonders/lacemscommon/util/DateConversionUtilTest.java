package com.woonders.lacemscommon.util;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emanuele on 12/02/2017.
 */
public class DateConversionUtilTest {

    private DateConversionUtil dateConversionUtil = new DateConversionUtil();

    @Test
    public void calcUTCLocalDateTimeFromTimestampTest() {
        LocalDateTime localDateTimeFromManualValue = LocalDateTime.of(2017,2, 12, 14, 48, 42);
        LocalDateTime localDateTimeFromLongTimestamp = dateConversionUtil.calcUTCLocalDateTimeFromTimestamp(1486910922);
        assertEquals(localDateTimeFromManualValue, localDateTimeFromLongTimestamp);
    }
}
