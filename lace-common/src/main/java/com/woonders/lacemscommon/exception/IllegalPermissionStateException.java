package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 11/05/2017.
 */
public class IllegalPermissionStateException extends Exception {

	public IllegalPermissionStateException() {
	}

	public IllegalPermissionStateException(String message) {
		super(message);
	}

	public IllegalPermissionStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalPermissionStateException(Throwable cause) {
		super(cause);
	}

	public IllegalPermissionStateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
