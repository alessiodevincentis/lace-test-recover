package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 15/02/2017.
 */
public class PaymentException extends Exception {

	public PaymentException() {
	}

	public PaymentException(String message) {
		super(message);
	}

	public PaymentException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentException(Throwable cause) {
		super(cause);
	}

	public PaymentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
