package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 03/10/2016.
 */
public class ItemNotFoundException extends Exception {

	public ItemNotFoundException() {
	}

	public ItemNotFoundException(final String message) {
		super(message);
	}

	public ItemNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ItemNotFoundException(final Throwable cause) {
		super(cause);
	}

	public ItemNotFoundException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
