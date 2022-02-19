package com.woonders.lacemsjsf.view.preferenza;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

/**
 * Created by Emanuele on 14/04/2017.
 * http://stackoverflow.com/questions/15420968/nosuchfieldexception-when-field-exists
 */
public class FillableSmsTextValidatorTest {

    @Test
    public void calcSmsLengthTest() throws NoSuchFieldException, IllegalAccessException {
        SmsTextValidator smsTextValidator = new FillableSmsTextValidator();
        Field smsString = SmsTextValidator.class.getDeclaredField("smsString");
        smsString.setAccessible(true);
        smsString.set(smsTextValidator, "Ciao ciao");

        assertEquals(9, smsTextValidator.calcSmsLength());

        smsString.set(smsTextValidator, "Ciao ciao #data#");

        assertEquals(20, smsTextValidator.calcSmsLength());

        smsString.set(smsTextValidator, "Appuntamento fissato in data #data# alle ore #ora#");

        assertEquals(54, smsTextValidator.calcSmsLength());
    }
}
