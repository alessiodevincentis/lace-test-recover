package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 23/02/2017.
 */
public class EmailFatturaNonInviataException extends RuntimeException {

	public EmailFatturaNonInviataException() {
	}

	public EmailFatturaNonInviataException(String message) {
		super(message);
	}

	public EmailFatturaNonInviataException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailFatturaNonInviataException(Throwable cause) {
		super(cause);
	}

	public EmailFatturaNonInviataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
