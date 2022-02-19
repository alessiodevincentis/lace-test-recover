package com.woonders.lacemscommon.service;

import java.time.LocalDateTime;

import com.woonders.lacemscommon.exception.CannotSendSmsException;
import com.woonders.lacemscommon.exception.SmsNotSentException;

/**
 * Facade to manage appointments of the calendar
 * [calendario_appuntamento] table
 */
public interface CalendarioAppuntamentoFacade {

	/**
	 * Send a sms to [destinatario] recipient number with info related to the appointment
	 */
	void sendSmsAppuntamento(String mittente, String destinatario, String body, LocalDateTime inizioAppuntamento,
			long currentAziendaId) throws CannotSendSmsException, SmsNotSentException;
}
