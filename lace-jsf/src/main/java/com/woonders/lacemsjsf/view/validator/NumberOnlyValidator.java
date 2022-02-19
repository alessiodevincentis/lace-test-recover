package com.woonders.lacemsjsf.view.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import org.springframework.util.StringUtils;

import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.BaseValidator;

@FacesValidator("com.woonders.lacemsjsf.view.validator.NumberOnlyValidator")
public class NumberOnlyValidator extends BaseValidator {

	private static final String ONLY_NUMBER_REGEX = "\\d+";
	private final PropertiesUtil propertiesUtil;

	public NumberOnlyValidator() {
		propertiesUtil = findBean(PropertiesUtil.JSF_NAME);
	}

	@Override
	public void validate(FacesContext arg0, UIComponent uiComponent, Object object) throws ValidatorException {
		String value = (String) object;
		if (!StringUtils.isEmpty(value)) {
			if (!value.matches(ONLY_NUMBER_REGEX)) {
				final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						propertiesUtil.getMsgNumberOnly(), null);
				throw new ValidatorException(msg);
			}
		}
	}

}
