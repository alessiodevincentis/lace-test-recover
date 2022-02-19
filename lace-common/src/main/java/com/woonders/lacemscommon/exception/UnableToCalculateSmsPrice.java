package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 29/03/2017.
 */
public class UnableToCalculateSmsPrice extends Exception {

	public UnableToCalculateSmsPrice() {
	}

	public UnableToCalculateSmsPrice(String message) {
		super(message);
	}

	public UnableToCalculateSmsPrice(String message, Throwable cause) {
		super(message, cause);
	}

	public UnableToCalculateSmsPrice(Throwable cause) {
		super(cause);
	}

	public UnableToCalculateSmsPrice(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
