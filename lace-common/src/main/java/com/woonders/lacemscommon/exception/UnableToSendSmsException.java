package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 16/01/2017.
 */
public class UnableToSendSmsException extends Exception {

	public UnableToSendSmsException() {
	}

	public UnableToSendSmsException(String message) {
		super(message);
	}

	public UnableToSendSmsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnableToSendSmsException(Throwable cause) {
		super(cause);
	}

	public UnableToSendSmsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
