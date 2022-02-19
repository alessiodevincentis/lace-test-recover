package com.woonders.lacemscommon.sms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

/**
 * Created by Emanuele on 15/05/2017.
 */
public class SmsUtilTest {

	private SmsUtil smsUtil = new SmsUtil();

	@Test
	public void calcScheduleTimeIfNeededTest() {
		ZonedDateTime twelveZoneDateTime = ZonedDateTime.of(2017, 5, 1, 12, 0, 0, 0, ZoneId.of("Europe/Rome"));

		Long scheduleTime = smsUtil.calcScheduleTimeIfNeeded(twelveZoneDateTime);
		assertNull(scheduleTime);

		ZonedDateTime eightZonedDateTime = twelveZoneDateTime.withHour(8);
		scheduleTime = smsUtil.calcScheduleTimeIfNeeded(eightZonedDateTime);
		// Mon, 01 May 2017 07:00:00 GMT
		assertEquals(1493622000, scheduleTime.longValue());

		ZonedDateTime twentyZonedDateTime = twelveZoneDateTime.withHour(20);
		scheduleTime = smsUtil.calcScheduleTimeIfNeeded(twentyZonedDateTime);
		// Tue, 02 May 2017 07:00:00 GMT
		assertEquals(1493708400, scheduleTime.longValue());

		ZonedDateTime nineZonedDateTime = twelveZoneDateTime.withHour(9);
		scheduleTime = smsUtil.calcScheduleTimeIfNeeded(nineZonedDateTime);
		assertNull(scheduleTime);

		ZonedDateTime nineteenZonedDateTime = twelveZoneDateTime.withHour(19);
		scheduleTime = smsUtil.calcScheduleTimeIfNeeded(nineteenZonedDateTime);
		assertNull(scheduleTime);

	}

}
