package com.woonders.lacemscommon.exception;

public class CannotImportExcelException extends Exception {

	public CannotImportExcelException() {
		super();
	}

	public CannotImportExcelException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CannotImportExcelException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotImportExcelException(String message) {
		super(message);
	}

	public CannotImportExcelException(Throwable cause) {
		super(cause);
	}
}
