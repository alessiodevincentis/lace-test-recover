package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 26/02/2017.
 */
public class FatturaNonDisponibileException extends Exception {

	public FatturaNonDisponibileException() {
	}

	public FatturaNonDisponibileException(String message) {
		super(message);
	}

	public FatturaNonDisponibileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FatturaNonDisponibileException(Throwable cause) {
		super(cause);
	}

	public FatturaNonDisponibileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
