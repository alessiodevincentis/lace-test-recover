package com.woonders.lacemsjsf.ui.component.Inputnumberdefault;

import org.primefaces.component.inputnumber.InputNumber;

import javax.faces.component.FacesComponent;

@FacesComponent(InputNumberDefault.COMPONENT_TYPE)
public class InputNumberDefault extends InputNumber {

    public static final String COMPONENT_FAMILY = "com.woonders.lacemsjsf";
    public static final String COMPONENT_TYPE = "com.woonders.lacemsjsf.InputNumberDefault";

    public InputNumberDefault() {
        super();
        setDecimalSeparator(".");
        setThousandSeparator("");
        setDecimalPlaces("4");
        setMinValue("0.00");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return InputNumberDefaultRenderer.RENDERER_TYPE;
    }

}
