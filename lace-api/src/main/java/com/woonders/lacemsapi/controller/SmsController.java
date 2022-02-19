package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.app.clicksend.model.response.SendSmsMainResponse;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.exception.UnableToCalculateSmsPrice;
import com.woonders.lacemscommon.service.SmsService;
import com.woonders.lacemscommon.service.SmsServiceFacade;
import com.woonders.lacemscommon.sms.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by ema98 on 8/9/2017.
 */
@Controller
@RequestMapping("/sms/*")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private SmsServiceFacade smsServiceFacade;

    @RequestMapping(value = {ControllerConstants.V1 + "/sendSms"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SendSmsMainResponse sendSms(List<SmsRequest> smsRequestList, MessageType messageType, long idOperator, long currentAziendaId) throws SmsNotSentException {
        return smsService.sendSms(smsRequestList, messageType, idOperator, currentAziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/updateStatusNotificaSmsLead"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateStatusNotificaSmsLead(String smsId, int statusCode, long timestamp) throws ItemNotFoundException {
        smsService.updateStatusNotificaSmsLead(smsId, statusCode, timestamp);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/updateStatusNotificaSmsMarketing"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateStatusNotificaSmsMarketing(String id, int statusCode, long timestamp) throws ItemNotFoundException {
        smsService.updateStatusNotificaSmsMarketing(id, statusCode, timestamp);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/calcSmsPricingInfo"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CalcSmsInfo calcSmsPricingInfo(FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel, String messageText, boolean isClientiReadSuper, boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll, boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorId, long currentOperatorAziendaId, Long currentAziendaId) throws UnableToCalculateSmsPrice {
        return smsService.calcSmsPricingInfo(filtroCampagnaMarketingViewModel, messageText, isClientiReadSuper, isNominativiReadSuper, isClientiReadAll, isNominativiReadAll, isClientiReadOwn, isNominativiReadOwn, currentOperatorId, currentOperatorAziendaId, currentAziendaId);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/calcSmsPricingInfo"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CalcSmsInfo calcSmsPricingInfo(List<String> recipientNumberList, String messageText, long currentAziendaId) throws UnableToCalculateSmsPrice {
        return smsService.calcSmsPricingInfo(recipientNumberList, messageText, currentAziendaId);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/sendSms"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void sendSms(String recipientNumber, String body, MessageType messageType, long idOperator, long operatorAziendaId, String tenantName, long idClienteOrPratica, LogService.TipoLog tipoLog) throws SmsNotSentException, CannotSendSmsException {
        smsServiceFacade.sendSms(recipientNumber, body, messageType, idOperator, operatorAziendaId, tenantName, idClienteOrPratica, tipoLog);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/checkEnoughBalance"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean checkEnoughBalance(String recipientNumber, String body, long operatorId, long operatorAziendaId) throws UnableToCalculateSmsPrice {
        return smsServiceFacade.checkEnoughBalance(recipientNumber, body, operatorId, operatorAziendaId);
    }
}
