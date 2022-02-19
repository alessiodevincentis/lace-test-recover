package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 23/02/2017.
 */
public class FatturaNonEliminataException extends Exception {

	public FatturaNonEliminataException() {
	}

	public FatturaNonEliminataException(String message) {
		super(message);
	}

	public FatturaNonEliminataException(String message, Throwable cause) {
		super(message, cause);
	}

	public FatturaNonEliminataException(Throwable cause) {
		super(cause);
	}

	public FatturaNonEliminataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
