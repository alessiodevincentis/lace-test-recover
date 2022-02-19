package com.woonders.lacemscommon.exception;

import lombok.Getter;

/**
 * Created by ema98 on 3/5/2018.
 */
@Getter
public class UnableToSendEmailException extends Exception {

    private UnableToSendEmailError unableToSendEmailError;

    public UnableToSendEmailException(UnableToSendEmailError unableToSendEmailError) {
        this.unableToSendEmailError = unableToSendEmailError;
    }

    public UnableToSendEmailException(String message) {
        super(message);
    }

    public UnableToSendEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToSendEmailException(Throwable cause) {
        super(cause);
    }

    public UnableToSendEmailException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public enum UnableToSendEmailError {
        MISSING_RECIPIENT, MISSING_AGENCY_NAME, MISSING_REPLY_TO, MISSING_SUBJECT, MISSING_BODY
    }
}