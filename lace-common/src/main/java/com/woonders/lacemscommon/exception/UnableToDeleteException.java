package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 02/10/2016.
 */
public class UnableToDeleteException extends Exception {

	public UnableToDeleteException() {
	}

	public UnableToDeleteException(final String message) {
		super(message);
	}

	public UnableToDeleteException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnableToDeleteException(final Throwable cause) {
		super(cause);
	}

	public UnableToDeleteException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
