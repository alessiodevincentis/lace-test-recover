package com.woonders.lacemsjsf.exception;

/**
 * Created by diegopizzo on 26/09/16.
 */
public class AmministrazioneNotExistsException extends Exception {

	private static final long serialVersionUID = -8876291651179762117L;

	public AmministrazioneNotExistsException() {
	}

	public AmministrazioneNotExistsException(String message) {
		super(message);
	}

	public AmministrazioneNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public AmministrazioneNotExistsException(Throwable cause) {
		super(cause);
	}

	public AmministrazioneNotExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
