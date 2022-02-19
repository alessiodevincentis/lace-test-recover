package com.woonders.lacemscommon.exception;

/**
 * Created by emanuele on 28/11/16.
 */
public class UnableToSaveFileException extends Exception {

    public UnableToSaveFileException() {
    }

    public UnableToSaveFileException(final String message) {
        super(message);
    }

    public UnableToSaveFileException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnableToSaveFileException(final Throwable cause) {
        super(cause);
    }

    public UnableToSaveFileException(final String message, final Throwable cause, final boolean enableSuppression,
                                     final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
