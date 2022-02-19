package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 11/02/2017.
 */
public class SmsNotSentException extends Exception {

    public SmsNotSentException() {
    }

    public SmsNotSentException(String message) {
        super(message);
    }

    public SmsNotSentException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsNotSentException(Throwable cause) {
        super(cause);
    }

    public SmsNotSentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
