package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 02/10/2016.
 */
public class UnableToSaveException extends Exception {

	public UnableToSaveException() {
	}

	public UnableToSaveException(final String message) {
		super(message);
	}

	public UnableToSaveException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnableToSaveException(final Throwable cause) {
		super(cause);
	}

	public UnableToSaveException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
