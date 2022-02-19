package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.CalendarioAppuntamentoViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.CannotSendSmsException;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;
import com.woonders.lacemscommon.exception.SmsNotSentException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Manages appointments of the calendar
 * [calendario_appuntamento] table
 */
public interface CalendarioAppuntamentoService {

    /**
     * Returns a list of procedures related to a customer by a customer id
     */
    List<PraticaViewModel> findPraticheByIdCliente(final long id, final long operatorId, final Role.RoleName clientiReadRoleName);

    /**
     * Saves the appointment to the calendar
     */
    long saveAppuntamento(CalendarioAppuntamentoViewModel calendarioAppuntamentoViewModel, long operatorId);

    /**
     * Returns a list of appointment within the given filters
     */
    List<CalendarioAppuntamentoViewModel> getAppuntamenti(Date start, Date end, List<String> operatoriSelezionati,
                                                          Long currentAziendaId, long id, String operatoreAssegnatoForCheckAppuntamentiPresenti, final long aziendaId, final long operatorId, final Role.RoleName calendarioReadRoleName);

    /**
     * Returns a list of appointment within the given filters
     */
    List<CalendarioAppuntamentoViewModel> getAppuntamenti(final LocalDateTime startLocalDateTime,
                                                          final LocalDateTime endLocalDateTime, final List<String> operatoriSelezionati, final Long currentAziendaId, final long id,
                                                          final String operatoreAssegnatoForCheckAppuntamentiPresenti, final long aziendaId, final long operatorId, final Role.RoleName calendarioReadRoleName);

    CalendarioAppuntamentoViewModel getOne(long id);

    /**
     * Deletes an appointment from the calendar
     */
    void deleteAppuntamento(long id, long operatorId);

    /**
     * Changes appointments startDate and endDate
     */
    CalendarioAppuntamentoViewModel changeTimeOne(long id, Date newStartDate, Date newEndDate);

    /**
     * Sends an sms with the details of the appointment
     */
    void sendSmsAppuntamento(final long idAppuntamento, final SMSType smsType, final long aziendaId) throws CannotSendSmsException, SmsNotSentException, NotEnoughCreditException;

    enum SMSType {OPERATOR, CLIENT}
}
