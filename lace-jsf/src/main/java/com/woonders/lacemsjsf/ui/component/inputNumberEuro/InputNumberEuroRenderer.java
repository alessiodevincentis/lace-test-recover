package com.woonders.lacemsjsf.ui.component.inputNumberEuro;

import javax.faces.render.FacesRenderer;

import org.primefaces.component.inputnumber.InputNumberRenderer;

@FacesRenderer(componentFamily = InputNumberEuro.COMPONENT_FAMILY, rendererType = InputNumberEuroRenderer.RENDERER_TYPE)
public class InputNumberEuroRenderer extends InputNumberRenderer {

	public static final String RENDERER_TYPE = "com.woonders.lacemsjsf.InputNumberEuroRenderer";
}
