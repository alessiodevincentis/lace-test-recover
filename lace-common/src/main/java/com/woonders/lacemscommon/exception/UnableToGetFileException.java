package com.woonders.lacemscommon.exception;

/**
 * Created by emanuele on 28/11/16.
 */
public class UnableToGetFileException extends Exception {

    public UnableToGetFileException() {
    }

    public UnableToGetFileException(final String message) {
        super(message);
    }

    public UnableToGetFileException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnableToGetFileException(final Throwable cause) {
        super(cause);
    }

    public UnableToGetFileException(final String message, final Throwable cause, final boolean enableSuppression,
                                    final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
