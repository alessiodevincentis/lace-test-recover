package com.woonders.lacemsjsf.ui.component.inputnumberpercentage;

import org.primefaces.component.inputnumber.InputNumber;

import javax.faces.component.FacesComponent;

@FacesComponent(InputNumberPercentage.COMPONENT_TYPE)
public class InputNumberPercentage extends InputNumber {

    public static final String COMPONENT_FAMILY = "com.woonders.lacemsjsf";
    public static final String COMPONENT_TYPE = "com.woonders.lacemsjsf.InputNumberPercentage";

    public InputNumberPercentage() {
        super();
        setDecimalSeparator(".");
        setSymbol("%");
        setThousandSeparator("");
        setDecimalPlaces("2");
        setMinValue("0.00");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return InputNumberPercentageRenderer.RENDERER_TYPE;
    }

}
