package com.woonders.lacemsjsf.exception;

/**
 * Created by Emanuele on 10/10/2016.
 */
public class DataSourceNotFoundException extends Exception {

	public DataSourceNotFoundException() {
	}

	public DataSourceNotFoundException(final String message) {
		super(message);
	}

	public DataSourceNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DataSourceNotFoundException(final Throwable cause) {
		super(cause);
	}

	public DataSourceNotFoundException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
