package com.woonders.lacemsjsf.ui.component.convertnumber;

import javax.faces.convert.FacesConverter;
import javax.faces.convert.NumberConverter;
import java.util.Locale;

@FacesConverter(ConvertNumberCurrencyEuro.COMPONENT_TYPE)
public class ConvertNumberCurrencyEuro extends NumberConverter {

    public static final String COMPONENT_FAMILY = "com.woonders.lacemsjsf";
    public static final String COMPONENT_TYPE = "com.woonders.lacemsjsf.ConvertNumberCurrencyEuro";

    public ConvertNumberCurrencyEuro() {
        setCurrencySymbol("€");
        setType("currency");
        setLocale(Locale.ITALY);
    }
}
