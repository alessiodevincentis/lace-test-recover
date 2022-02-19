package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 16/11/2016.
 */
public class NoFileFoundException extends Exception {

    public NoFileFoundException() {
    }

    public NoFileFoundException(final String message) {
        super(message);
    }

    public NoFileFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoFileFoundException(final Throwable cause) {
        super(cause);
    }

    public NoFileFoundException(final String message, final Throwable cause, final boolean enableSuppression,
                                final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
