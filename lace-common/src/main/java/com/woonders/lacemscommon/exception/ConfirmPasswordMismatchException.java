package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 10/10/2016.
 */
public class ConfirmPasswordMismatchException extends Exception {

    public ConfirmPasswordMismatchException() {
    }

    public ConfirmPasswordMismatchException(final String message) {
        super(message);
    }

    public ConfirmPasswordMismatchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConfirmPasswordMismatchException(final Throwable cause) {
        super(cause);
    }

    public ConfirmPasswordMismatchException(final String message, final Throwable cause,
                                            final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
