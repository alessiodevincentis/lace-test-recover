package com.woonders.lacemsjsf.exception;

/**
 * Created by Emanuele on 12/06/2017.
 */
public class SessionExpiredException extends RuntimeException {

    public SessionExpiredException() {
    }

    public SessionExpiredException(String message) {
        super(message);
    }

    public SessionExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionExpiredException(Throwable cause) {
        super(cause);
    }

    public SessionExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
