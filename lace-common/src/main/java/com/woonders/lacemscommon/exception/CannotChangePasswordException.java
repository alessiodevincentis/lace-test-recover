package com.woonders.lacemscommon.exception;

public class CannotChangePasswordException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -5059930921499710059L;

    public CannotChangePasswordException() {
        super();
    }

    public CannotChangePasswordException(final String arg0, final Throwable arg1, final boolean arg2, final boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public CannotChangePasswordException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public CannotChangePasswordException(final String arg0) {
        super(arg0);
    }

    public CannotChangePasswordException(final Throwable arg0) {
        super(arg0);
    }

}
