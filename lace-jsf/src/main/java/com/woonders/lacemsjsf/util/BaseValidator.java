package com.woonders.lacemsjsf.util;

import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

/**
 * Created by Emanuele on 11/02/2017.
 */
public abstract class BaseValidator implements Validator {

    // http://stackoverflow.com/questions/2633112/get-jsf-managed-bean-by-name-in-any-servlet-related-class
    public <T> T findBean(final String beanName) {
        final FacesContext context = FacesContext.getCurrentInstance();
        return (T) context.getApplication().evaluateExpressionGet(context, beanName, Object.class);
    }
}
