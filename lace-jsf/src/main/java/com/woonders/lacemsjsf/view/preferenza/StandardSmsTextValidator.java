package com.woonders.lacemsjsf.view.preferenza;

import javax.faces.validator.FacesValidator;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 14/04/2017.
 */
@FacesValidator("com.woonders.lacemsjsf.view.preferenza.StandardSmsTextValidator")
@Slf4j
public class StandardSmsTextValidator extends SmsTextValidator{

    @Override
    public int calcSmsLength() {
        return smsString.length();
    }
}
