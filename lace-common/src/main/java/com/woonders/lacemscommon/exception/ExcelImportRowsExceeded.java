package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 26/04/2017.
 */
public class ExcelImportRowsExceeded extends Exception {

	public ExcelImportRowsExceeded() {
	}

	public ExcelImportRowsExceeded(String message) {
		super(message);
	}

	public ExcelImportRowsExceeded(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcelImportRowsExceeded(Throwable cause) {
		super(cause);
	}

	public ExcelImportRowsExceeded(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
