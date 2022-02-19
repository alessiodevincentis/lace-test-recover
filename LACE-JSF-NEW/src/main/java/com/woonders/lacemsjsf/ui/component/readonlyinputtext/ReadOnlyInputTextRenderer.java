package com.woonders.lacemsjsf.ui.component.readonlyinputtext;

import javax.faces.render.FacesRenderer;

import org.primefaces.component.inputtext.InputTextRenderer;

@FacesRenderer(componentFamily = ReadOnlyInputText.COMPONENT_FAMILY, rendererType = ReadOnlyInputTextRenderer.RENDERER_TYPE)
public class ReadOnlyInputTextRenderer extends InputTextRenderer {
	public static final String RENDERER_TYPE = "com.woonders.lacemsjsf.ReadOnlyInputTextRenderer";
}
