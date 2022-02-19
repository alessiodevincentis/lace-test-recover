package com.woonders.lacemscommon.exception;

/**
 * Created by Emanuele on 26/02/2017.
 */
public class DatiFatturazioneToBeCompletedException extends Exception {

	public DatiFatturazioneToBeCompletedException() {
	}

	public DatiFatturazioneToBeCompletedException(String message) {
		super(message);
	}

	public DatiFatturazioneToBeCompletedException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatiFatturazioneToBeCompletedException(Throwable cause) {
		super(cause);
	}

	public DatiFatturazioneToBeCompletedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
