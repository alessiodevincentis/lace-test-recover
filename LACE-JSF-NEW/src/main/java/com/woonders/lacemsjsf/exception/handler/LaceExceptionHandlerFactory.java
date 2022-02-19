package com.woonders.lacemsjsf.exception.handler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Created by Emanuele on 13/04/2017.
 */
public class LaceExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory exceptionHandlerFactory;

    public LaceExceptionHandlerFactory(ExceptionHandlerFactory exceptionHandlerFactory) {
        this.exceptionHandlerFactory = exceptionHandlerFactory;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new LaceExceptionHandler(exceptionHandlerFactory.getExceptionHandler());
    }
}
