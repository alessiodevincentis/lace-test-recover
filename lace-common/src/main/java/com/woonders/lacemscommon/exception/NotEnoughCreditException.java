package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 16/01/2017.
 */
public class NotEnoughCreditException extends Exception {

	public NotEnoughCreditException() {
	}

	public NotEnoughCreditException(String message) {
		super(message);
	}

	public NotEnoughCreditException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughCreditException(Throwable cause) {
		super(cause);
	}

	public NotEnoughCreditException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
