package com.woonders.lacemscommon.sms;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.service.SmsService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 26/04/2017.
 */
@Slf4j
@Component
public class SmsUtil {

	private static final LocalTime smsIntervalLocalTimeStart = LocalTime.of(9, 0);
	private static final LocalTime smsIntervalLocalTimeEnd = LocalTime.of(19, 0);

	private ZonedDateTime getNowTime() {
		return ZonedDateTime.now(ZoneId.of("Europe/Rome"));
	}

	public Long calcScheduleTimeIfNeeded(ZonedDateTime nowZonedDateTime) {
		LocalTime localTime = nowZonedDateTime.toLocalTime();
		// se prima di start, aspetta start per l invio
		if (localTime.isBefore(smsIntervalLocalTimeStart)) {
			return nowZonedDateTime.withHour(smsIntervalLocalTimeStart.getHour())
					.withMinute(smsIntervalLocalTimeStart.getMinute()).withZoneSameInstant(ZoneId.of("UTC"))
					.toEpochSecond();
		}
		// se dopo di end, aspetta start del giorno seguente per l invio
		if (localTime.isAfter(smsIntervalLocalTimeEnd)) {
			return nowZonedDateTime.withHour(smsIntervalLocalTimeStart.getHour())
					.withMinute(smsIntervalLocalTimeStart.getMinute()).plusDays(1).withZoneSameInstant(ZoneId.of("UTC"))
					.toEpochSecond();
		}
		// nel caso in cui e compreso tra start ed end ritorna null, puo inviare
		// subito
		return null;
	}

	public SmsRequest buildSmsRequest(String numeroDestinatario, String body, String numeroMittente,
			String customString) {
		return buildSmsRequest(numeroDestinatario, body, numeroMittente, customString, false);
	}

	public SmsRequest buildSmsRequest(String numeroDestinatario, String body, String numeroMittente,
			String customString, boolean calcScheduleTime) {
		SmsRequest.SmsRequestBuilder smsRequestBuilder = SmsRequest.builder().source(SmsService.SOURCE)
				.country(SmsService.FIXED_IT_COUNTRY).body(body).customString(customString)
				.to(StringUtils.deleteWhitespace(numeroDestinatario))
				.from(StringUtils.deleteWhitespace(numeroMittente));
		if (calcScheduleTime) {
			Long scheduleTime = calcScheduleTimeIfNeeded(getNowTime());
			if (scheduleTime != null) {
				log.info(MessageFormat.format("Sms {0} {1} schedulato alle {2}", numeroDestinatario, customString,
						scheduleTime));
				smsRequestBuilder.schedule(scheduleTime);
			}
		}
		return smsRequestBuilder.build();
	}
}
