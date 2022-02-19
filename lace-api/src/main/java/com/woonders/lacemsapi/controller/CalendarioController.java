package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.CalendarioAppuntamentoViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.CannotSendSmsException;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.service.CalendarioAppuntamentoFacade;
import com.woonders.lacemscommon.service.CalendarioAppuntamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by diegopizzo on 09/08/17.
 */
//@Controller
//@RequestMapping("/calendario/*")
public class CalendarioController {

    @Autowired
    private CalendarioAppuntamentoService calendarioAppuntamentoService;
    @Autowired
    private CalendarioAppuntamentoFacade calendarioAppuntamentoFacade;

    @RequestMapping(value = {ControllerConstants.V1 + "/sendSmsAppuntamento"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void sendSmsAppuntamento(final String mittente, final String destinatario, final String body, final LocalDateTime inizioAppuntamento, final long currentAziendaId)
            throws CannotSendSmsException, SmsNotSentException {
        calendarioAppuntamentoFacade.sendSmsAppuntamento(mittente, destinatario, body, inizioAppuntamento, currentAziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findPraticheByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PraticaViewModel> findPraticheByIdCliente(final long id, final long operatorId, final Role.RoleName clientiReadRoleName) {
        return calendarioAppuntamentoService.findPraticheByIdCliente(id, operatorId, clientiReadRoleName);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/saveAppuntamento"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long saveAppuntamento(@RequestBody final CalendarioAppuntamentoViewModel calendarioAppuntamentoViewModel, @RequestBody final long operatorId) {
        return calendarioAppuntamentoService.saveAppuntamento(calendarioAppuntamentoViewModel, operatorId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getAppuntamenti"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CalendarioAppuntamentoViewModel> getAppuntamenti(final Date start, final Date end, final List<String> operatoriSelezionati, final Long currentAziendaId,
                                                                 final long id, final String operatoreAssegnatoForCheckAppuntamentiPresenti, final long aziendaId,
                                                                 final long operatorId, final Role.RoleName calendarioReadRoleName) {
        return calendarioAppuntamentoService.getAppuntamenti(start, end, operatoriSelezionati, currentAziendaId, id,
                operatoreAssegnatoForCheckAppuntamentiPresenti, aziendaId, operatorId, calendarioReadRoleName);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getAppuntamenti"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CalendarioAppuntamentoViewModel> getAppuntamenti(final LocalDateTime startLocalDateTime, final LocalDateTime endLocalDateTime,
                                                                 final List<String> operatoriSelezionati, final Long currentAziendaId, final long id,
                                                                 final String operatoreAssegnatoForCheckAppuntamentiPresenti, final long aziendaId,
                                                                 final long operatorId, final Role.RoleName calendarioReadRoleName) {
        return calendarioAppuntamentoService.getAppuntamenti(startLocalDateTime, endLocalDateTime, operatoriSelezionati, currentAziendaId, id,
                operatoreAssegnatoForCheckAppuntamentiPresenti, aziendaId, operatorId, calendarioReadRoleName);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getOne"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CalendarioAppuntamentoViewModel getOne(final long id) {
        return calendarioAppuntamentoService.getOne(id);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/getOne"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteAppuntamento(@RequestBody long id, @RequestBody long operatorId) {
        calendarioAppuntamentoService.deleteAppuntamento(id, operatorId);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/changeTimeOne"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CalendarioAppuntamentoViewModel changeTimeOne(@RequestBody final long id, @RequestBody final Date newStartDate, @RequestBody final Date newEndDate) {
        return calendarioAppuntamentoService.changeTimeOne(id, newStartDate, newEndDate);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/sendSmsAppuntamento"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void sendSmsAppuntamento(final long idAppuntamento, final CalendarioAppuntamentoService.SMSType smsType, final long aziendaId) throws CannotSendSmsException, SmsNotSentException, NotEnoughCreditException {
        calendarioAppuntamentoService.sendSmsAppuntamento(idAppuntamento, smsType, aziendaId);
    }
}
