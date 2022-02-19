package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 09/10/2016.
 */
public class UnableToUpdateException extends Exception {

	public UnableToUpdateException() {
	}

	public UnableToUpdateException(final String message) {
		super(message);
	}

	public UnableToUpdateException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnableToUpdateException(final Throwable cause) {
		super(cause);
	}

	public UnableToUpdateException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
