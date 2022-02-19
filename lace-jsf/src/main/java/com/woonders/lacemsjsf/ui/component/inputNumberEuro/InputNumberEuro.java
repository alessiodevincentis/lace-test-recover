package com.woonders.lacemsjsf.ui.component.inputNumberEuro;

import javax.faces.component.FacesComponent;

import org.primefaces.component.inputnumber.InputNumber;

@FacesComponent(InputNumberEuro.COMPONENT_TYPE)
public class InputNumberEuro extends InputNumber {

	public static final String COMPONENT_FAMILY = "com.woonders.lacemsjsf";
	public static final String COMPONENT_TYPE = "com.woonders.lacemsjsf.InputNumberEuro";

	public InputNumberEuro() {
		super();
		setDecimalSeparator(".");
		setSymbol("€");
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
		return InputNumberEuroRenderer.RENDERER_TYPE;
	}

}
