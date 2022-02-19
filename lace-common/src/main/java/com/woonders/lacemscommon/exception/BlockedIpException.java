package com.woonders.lacemscommon.exception;

/**
 * Created by ema98 on 8/8/2017.
 */
public class BlockedIpException extends RuntimeException {

    public BlockedIpException() {
    }

    public BlockedIpException(String message) {
        super(message);
    }

    public BlockedIpException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockedIpException(Throwable cause) {
        super(cause);
    }

    public BlockedIpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
