package com.woonders.lacemsjsf.view.preferenza;

import java.text.MessageFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.BaseValidator;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 14/02/2017.
 */
@FacesValidator("com.woonders.lacemsjsf.view.preferenza.SmsSenderValidator")
@Slf4j
public class SmsSenderValidator extends BaseValidator {

	// controllare sempre SMSTextValidator
	//rivate static final String GSM_BASIC_CHARACTERS_REGEX_WITHOUT_SPACE = "^[A-Za-z0-9@£$àèéùìò_É!\"#$%&amp;'()*+,.:;&lt;=&gt;?]*$";
	private static final String GSM_BASIC_CHARACTERS_REGEX_WITHOUT_SPACE = "^(?=.*[a-zA-Z])(?=.*[a-zA-Z0-9])([a-zA-Z0-9]{1,11})$";
	private static final int MAX_SMS_SENDER_LENGTH = 11;
	private final PropertiesUtil propertiesUtil;

	public SmsSenderValidator() {
		propertiesUtil = findBean(PropertiesUtil.JSF_NAME);
	}

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
		String smsSenderString = (String) object;
		if (smsSenderString != null) {
			if (!smsSenderString.matches(GSM_BASIC_CHARACTERS_REGEX_WITHOUT_SPACE)) {
				final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						propertiesUtil.getMsgSmsSenderInvalidCharacter(), null);
				throw new ValidatorException(msg);
			}
			if (smsSenderString.length() > MAX_SMS_SENDER_LENGTH) {
				final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						MessageFormat.format(propertiesUtil.getMsgSmsSenderMaxLengthError(), MAX_SMS_SENDER_LENGTH),
						null);
				throw new ValidatorException(msg);
			}
		}
	}
}
