package com.woonders.lacemsjsf.exception;

/**
 * Created by Emanuele on 14/11/2016.
 */
public class OutOfStorageException extends Exception {

	public OutOfStorageException() {
	}

	public OutOfStorageException(final String message) {
		super(message);
	}

	public OutOfStorageException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public OutOfStorageException(final Throwable cause) {
		super(cause);
	}

	public OutOfStorageException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
