package com.woonders.lacemsjsf.exception;

/**
 * Created by emanuele on 26/11/16.
 */
public class FunctionalityNotEnabledException extends Exception {

	public FunctionalityNotEnabledException() {
	}

	public FunctionalityNotEnabledException(final String message) {
		super(message);
	}

	public FunctionalityNotEnabledException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public FunctionalityNotEnabledException(final Throwable cause) {
		super(cause);
	}

	public FunctionalityNotEnabledException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
