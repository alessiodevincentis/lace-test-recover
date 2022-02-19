package com.woonders.lacemscommon.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.exception.CannotSendSmsException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.service.CalendarioAppuntamentoFacade;
import com.woonders.lacemscommon.service.SmsService;
import com.woonders.lacemscommon.sms.MessageType;
import com.woonders.lacemscommon.sms.SmsUtil;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemscommon.util.RequestUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CalendarioAppuntamentoFacadeImpl implements CalendarioAppuntamentoFacade {

	public static final String NAME = "calendarioAppuntamentoFacadeImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";
	@Autowired
	private SmsService smsService;
	@Autowired
	private SmsUtil smsUtil;
	@Autowired
	private RequestUtil requestUtil;
	@Autowired
	private DateConversionUtil dateConversionUtil;

	@Override
	public void sendSmsAppuntamento(String mittente, String destinatario, String body, LocalDateTime inizioAppuntamento,
			long currentAziendaId) throws CannotSendSmsException, SmsNotSentException {

		if (StringUtils.isEmpty(mittente) || StringUtils.isEmpty(destinatario) || StringUtils.isEmpty(body)) {
			throw new CannotSendSmsException();
		}

		if (body.contains(SmsService.TIME_PLACEHOLDER)) {
			body = body.replace(SmsService.TIME_PLACEHOLDER,
					dateConversionUtil.formatTimeInRomeTimeZone(inizioAppuntamento));
		}
		if (body.contains(SmsService.DATE_PLACEHOLDER)) {
			body = body.replace(SmsService.DATE_PLACEHOLDER,
					dateConversionUtil.formatDateInRomeTimeZone(inizioAppuntamento));
		}

		List<SmsRequest> smsRequestList = new ArrayList<>();
		smsRequestList.add(smsUtil.buildSmsRequest(destinatario, body, mittente,
				requestUtil.getTenantName() + ":" + MessageType.SMS_CALENDARIO.toString()));

		smsService.sendSms(smsRequestList, MessageType.SMS_CALENDARIO, 0, currentAziendaId);
	}

}
