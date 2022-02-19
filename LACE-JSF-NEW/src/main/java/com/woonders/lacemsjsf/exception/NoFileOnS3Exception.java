package com.woonders.lacemsjsf.exception;

import java.io.ByteArrayOutputStream;

/**
 * Created by Emanuele on 13/03/2017.
 */
public class NoFileOnS3Exception extends Exception {

    private ByteArrayOutputStream byteArrayOutputStreamDefaultContent;

    public NoFileOnS3Exception() {
    }

    public NoFileOnS3Exception(String message) {
        super(message);
    }

    public NoFileOnS3Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFileOnS3Exception(Throwable cause) {
        super(cause);
    }

    public NoFileOnS3Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoFileOnS3Exception(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStreamDefaultContent = byteArrayOutputStream;
    }

    public ByteArrayOutputStream getByteArrayOutputStreamDefaultContent() {
        return byteArrayOutputStreamDefaultContent;
    }
}
