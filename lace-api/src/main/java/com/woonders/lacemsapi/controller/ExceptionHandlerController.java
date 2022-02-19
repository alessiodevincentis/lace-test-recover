package com.woonders.lacemsapi.controller;

import com.woonders.lacemsapi.model.app.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Emanuele on 11/11/2016.
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseBody
    public Object defaultErrorHandler(final HttpServletRequest request, final Exception e) {
        final String errorMessage = "DEFAULT ERROR: KO " + e.getMessage();
        log.error(errorMessage, e);
        if (request.getServletPath().endsWith(DevisProxPushLeadController.PUSH_LEAD_URL) || request.getServletPath().endsWith(FormLacePushLeadController.LACE_PUSH_LEAD_FORM_URL)) {
            return new ResponseEntity<>(errorMessage, null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(BaseMessage.builder().message(errorMessage)
                    .responseCode(BaseMessage.ResponseCode.UNEXPECTED).build(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
