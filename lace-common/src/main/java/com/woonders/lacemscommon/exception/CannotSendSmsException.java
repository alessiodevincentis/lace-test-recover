package com.woonders.lacemscommon.exception;

public class CannotSendSmsException extends Exception {

	public CannotSendSmsException() {
		super();
	}

	public CannotSendSmsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CannotSendSmsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotSendSmsException(String message) {
		super(message);
	}

	public CannotSendSmsException(Throwable cause) {
		super(cause);
	}
}
