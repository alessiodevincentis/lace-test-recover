package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 21/02/2017.
 */
public class FatturaNonCreataException extends Exception {

	public FatturaNonCreataException() {
	}

	public FatturaNonCreataException(String message) {
		super(message);
	}

	public FatturaNonCreataException(String message, Throwable cause) {
		super(message, cause);
	}

	public FatturaNonCreataException(Throwable cause) {
		super(cause);
	}

	public FatturaNonCreataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
