package com.woonders.lacemsjsf.view.preferenza;

import javax.faces.application.FacesMessage;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang3.StringUtils;

import com.woonders.lacemscommon.service.SmsService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 14/04/2017.
 */
@FacesValidator("com.woonders.lacemsjsf.view.preferenza.FillableSmsTextValidator")
@Slf4j
public class FillableSmsTextValidator extends SmsTextValidator{

    //la lunghezza di data e ora una volta sostituiti nel formato dd/MM/yyyy e HH:mm
    private static final int DATA_LENGTH = 10;
    private static final int ORA_LENGTH = 5;

    @Override
    public int calcSmsLength() {
        int dataPlaceHolderCount = StringUtils.countMatches(smsString, SmsService.DATE_PLACEHOLDER);
        int oraPlaceHolderCount = StringUtils.countMatches(smsString, SmsService.TIME_PLACEHOLDER);
        String smsStringWithoutPlaceholder = smsString.replace(SmsService.DATE_PLACEHOLDER, "").replace(SmsService.TIME_PLACEHOLDER, "");
        return smsStringWithoutPlaceholder.length() + dataPlaceHolderCount * DATA_LENGTH + oraPlaceHolderCount * ORA_LENGTH;
    }

    @Override
    public void validateSms() {
        super.validateSms();
        int sharpCount = StringUtils.countMatches(smsString, SmsService.SMS_SPECIAL_CHAR_PLACEHOLDER);
        if(sharpCount % 2 != 0) {
            final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    propertiesUtil.getMsgSmsFillableErrorSpecialCharacter(), null);
            throw new ValidatorException(msg);
        }
    }
}
