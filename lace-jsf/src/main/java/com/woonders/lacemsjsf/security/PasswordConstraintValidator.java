package com.woonders.lacemsjsf.security;

/**
 * Created by Emanuele on 10/10/2016.
 */

import java.util.Arrays;

import javax.faces.context.FacesContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.*;

import com.google.common.base.Joiner;
import com.woonders.lacemsjsf.config.PropertiesUtil;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	private PropertiesUtil propertiesUtil;

	@Override
	public void initialize(final ValidPassword arg0) {
		propertiesUtil = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(
				FacesContext.getCurrentInstance(), PropertiesUtil.JSF_NAME, PropertiesUtil.class);
	}

	@Override
	public boolean isValid(final String password, final ConstraintValidatorContext context) {
		final MessageResolver resolver = new PropertiesMessageResolver(propertiesUtil.getPasswordMessagesProperties());
		final PasswordValidator validator = new PasswordValidator(resolver,
				Arrays.asList(new LengthRule(8, 30), new CharacterRule(EnglishCharacterData.UpperCase, 1),
						new CharacterRule(EnglishCharacterData.Digit, 1),
						new CharacterRule(EnglishCharacterData.Special, 1), new WhitespaceRule(),
						new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
						new IllegalSequenceRule(EnglishSequenceData.Numerical),
						new IllegalSequenceRule(EnglishSequenceData.USQwerty)));
		final RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Joiner.on("\n").join(validator.getMessages(result)))
				.addConstraintViolation();
		return false;
	}

}