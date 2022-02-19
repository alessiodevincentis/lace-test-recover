package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;

/**
 * Manages part of the logic useful to send SMS
 */
public interface SmsLeadService {

    /**
     * send a SMS notification when we receive a third party lead
     */
    void sendLeadNotificaSms(ClienteViewModel clienteViewModel, String tenantId, long currentAziendaId);
}
