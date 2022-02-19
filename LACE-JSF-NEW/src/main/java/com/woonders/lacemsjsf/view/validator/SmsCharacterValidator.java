package com.woonders.lacemsjsf.view.validator;

import com.woonders.lacemsjsf.view.preferenza.SmsTextValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

/**
 * Created by ema98 on 7/19/2017.
 */
@FacesValidator("com.woonders.lacemsjsf.view.validator.SmsCharacterValidator")
@Slf4j
public class SmsCharacterValidator extends SmsTextValidator {

    //NOT USED
    @Override
    public int calcSmsLength() {
        return 0;
    }

    @Override
    public void validateSms() {
        if (StringUtils.isEmpty(smsString) || !smsString.matches(GSM_BASIC_CHARACTERS_REGEX)) {
            final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    propertiesUtil.getMsgSmsValidationError(), null);
            throw new ValidatorException(msg);
        }
    }
}
