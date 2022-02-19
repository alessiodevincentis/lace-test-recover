package com.woonders.lacemscommon.exception;

/**
 * Created by ema98 on 8/15/2017.
 */
public class EmptyTanException extends Exception {

    public EmptyTanException() {
    }

    public EmptyTanException(String message) {
        super(message);
    }

    public EmptyTanException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyTanException(Throwable cause) {
        super(cause);
    }

    public EmptyTanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
