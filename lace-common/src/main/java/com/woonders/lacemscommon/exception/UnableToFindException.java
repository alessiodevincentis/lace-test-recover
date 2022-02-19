package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 10/10/2016.
 */
public class UnableToFindException extends Exception {

    public UnableToFindException() {
    }

    public UnableToFindException(final String message) {
        super(message);
    }

    public UnableToFindException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnableToFindException(final Throwable cause) {
        super(cause);
    }

    public UnableToFindException(final String message, final Throwable cause, final boolean enableSuppression,
                                 final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
