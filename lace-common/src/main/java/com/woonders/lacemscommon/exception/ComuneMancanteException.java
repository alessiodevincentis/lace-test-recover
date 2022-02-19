package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 16/03/2017.
 */
public class ComuneMancanteException extends Exception {

	public ComuneMancanteException() {
	}

	public ComuneMancanteException(String message) {
		super(message);
	}

	public ComuneMancanteException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComuneMancanteException(Throwable cause) {
		super(cause);
	}

	public ComuneMancanteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
