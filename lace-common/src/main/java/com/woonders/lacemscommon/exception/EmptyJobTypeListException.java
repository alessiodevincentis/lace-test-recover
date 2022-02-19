package com.woonders.lacemscommon.exception;

/**
 * Created by ema98 on 8/28/2017.
 */
public class EmptyJobTypeListException extends Exception {

    public EmptyJobTypeListException() {
    }

    public EmptyJobTypeListException(String message) {
        super(message);
    }

    public EmptyJobTypeListException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyJobTypeListException(Throwable cause) {
        super(cause);
    }

    public EmptyJobTypeListException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
