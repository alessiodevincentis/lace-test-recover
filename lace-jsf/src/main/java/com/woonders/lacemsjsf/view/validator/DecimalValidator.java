package com.woonders.lacemsjsf.view.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.woonders.lacemsjsf.config.PropertiesUtil;

@ManagedBean
@RequestScoped
public class DecimalValidator implements Validator, Serializable {

	private final String regexNumber = "\\d+(\\.?\\d{0,2})";

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object param) throws ValidatorException {
		String value = (String) param;
		if (value != null && !value.isEmpty()) {
			if (!value.matches(regexNumber)) {
				final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
						propertiesUtil.getMsgErrorDecimalValue(), null);
				throw new ValidatorException(msg);
			}
		}
	}

	public void setPropertiesUtil(final PropertiesUtil propertiesUtil) {
		this.propertiesUtil = propertiesUtil;
	}

}
