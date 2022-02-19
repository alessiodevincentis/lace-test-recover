package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.config.InternationalizationConfig;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.LogService;
import com.woonders.lacemscommon.service.SmsService;
import com.woonders.lacemscommon.sms.MessageType;
import com.woonders.lacemscommon.sms.SmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Emanuele on 21/01/2017.
 */
@Controller
@Slf4j
public class PushSmsDeliveryReceiptsController {

    public static final String PUSH_SMS_DELIVERY_RECEIPTS_URL = "/pushsmsdeliveryreceipt";
    // questa custom string la usiamo per settarci l'id del tenant che ha
    // mandato il messaggio! e ci settiamo anche il tipo di messaggio, se
    // notifica o campagna, nella forma
    // tenant:messageType (String:MessageType)
    public static final String CUSTOM_STRING = "custom_string";
    private final String TIMESTAMP_SEND = "timestamp_send";
    private final String TIMESTAMP = "timestamp";
    private final String MESSAGE_ID = "message_id";
    private final String STATUS = "status";
    private final String STATUS_CODE = "status_code";
    private final String STATUS_TEXT = "status_text";
    private final String ERROR_CODE = "error_code";
    private final String ERROR_TEXT = "error_text";
    private final String USER_ID = "user_id";
    private final String SUBACCOUNT_ID = "subaccount_id";
    // questa e una costante di clicksend, arriva sempre "sms"
    private final String MESSAGE_TYPE = "message_type";

    @Autowired
    private SmsService smsService;
    @Autowired
    private LogService logService;
    @Autowired
    private InternationalizationConfig.ItalyReloadableResourceBundleMessageSource italyReloadableResourceBundleMessageSource;

    @RequestMapping(value = {PUSH_SMS_DELIVERY_RECEIPTS_URL,
            "/v1" + PUSH_SMS_DELIVERY_RECEIPTS_URL}, method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String pushSmsDeliveryReceipt(@RequestParam(TIMESTAMP_SEND) long timestampSend,
                                         @RequestParam(TIMESTAMP) long timestamp, @RequestParam(MESSAGE_ID) String messageId,
                                         @RequestParam(STATUS) String status, @RequestParam(STATUS_CODE) int statusCode,
                                         @RequestParam(STATUS_TEXT) String statusText, @RequestParam(ERROR_CODE) String errorCode,
                                         @RequestParam(ERROR_TEXT) String errorText, @RequestParam(CUSTOM_STRING) String customString,
                                         @RequestParam(USER_ID) String userId, @RequestParam(SUBACCOUNT_ID) String subaccountId,
                                         @RequestParam(MESSAGE_TYPE) String clickSendMessageType) {

        try {
            // questo campo e nella forma tenant:MessageType
            MessageType messageType = MessageType.valueOf(customString.split(SmsClient.CUSTOM_STRING_SEPARATOR)[1]);
            switch (messageType) {
                case SMS_CAMPAGNA:
                    // qui gli passiamo la primarykey che ci siamo passati quando
                    // abbiamo spedito invece del messageId
                    smsService.updateStatusNotificaSmsMarketing(customString.split(SmsClient.CUSTOM_STRING_SEPARATOR)[2], statusCode, timestamp);
                    break;
                case SMS_NOTIFICA_LEAD:
                    smsService.updateStatusNotificaSmsLead(messageId, statusCode, timestamp);
                    break;
                //disabled at the moment, need to find a way how to keep trace of the messages sent
               /* case SMS_SINGLE:
                    long idClienteOrPratica = Long.parseLong(customString.split(SmsClient.CUSTOM_STRING_SEPARATOR)[2]);
                    LogService.TipoLog tipoLog = LogService.TipoLog.valueOf(customString.split(SmsClient.CUSTOM_STRING_SEPARATOR)[3]);
                    long idOperatore = Long.parseLong(customString.split(SmsClient.CUSTOM_STRING_SEPARATOR)[4]);
                    EsitoSmsNotificaLead esitoSmsNotificaLead = EsitoSmsNotificaLead.fromClickSendStatusCode(statusCode);
                    String smsBody = customString.split(SmsClient.CUSTOM_STRING_SEPARATOR)[5];
                    logService.logCustomAction(idClienteOrPratica, tipoLog,
                            MessageFormat.format(italyReloadableResourceBundleMessageSource.getMessage("msg.sms.logsmsreceived"), smsBody, esitoSmsNotificaLead.getValue()),
                            LogService.CustomAction.RECEIVED_SMS, idOperatore);
                    break;*/
            }
        } catch (IllegalArgumentException e) {
            log.error("Impossibile estrarre messageType da custom string: " + customString);
        } catch (ItemNotFoundException e) {
            log.error("Impossibile trovare notificaSms con messageId: " + messageId);
        }
        return null;
    }
}
