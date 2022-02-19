package com.woonders.lacemsjsf.ui.component.readonlyinputtext;

import javax.faces.component.FacesComponent;

import org.primefaces.component.inputtext.InputText;

@FacesComponent(ReadOnlyInputText.COMPONENT_TYPE)
public class ReadOnlyInputText extends InputText {

	public static final String COMPONENT_FAMILY = "com.woonders.lacemsjsf";
	public static final String COMPONENT_TYPE = "com.woonders.lacemsjsf.ReadOnlyInputText";

	public ReadOnlyInputText() {
		super();
		setReadonly(true);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ReadOnlyInputTextRenderer.RENDERER_TYPE;
	}

}
