package com.woonders.lacemscommon.exception;

/**
 * Created by ema98 on 8/18/2017.
 */
public class SimulatorTableInUseException extends Exception {

    public SimulatorTableInUseException() {
    }

    public SimulatorTableInUseException(String message) {
        super(message);
    }

    public SimulatorTableInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimulatorTableInUseException(Throwable cause) {
        super(cause);
    }

    public SimulatorTableInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
