package com.woonders.lacemsjsf.ui.component.inputnumberpercentage;

import org.primefaces.component.inputnumber.InputNumberRenderer;

import javax.faces.render.FacesRenderer;

@FacesRenderer(componentFamily = InputNumberPercentage.COMPONENT_FAMILY, rendererType = InputNumberPercentageRenderer.RENDERER_TYPE)
public class InputNumberPercentageRenderer extends InputNumberRenderer {

    public static final String RENDERER_TYPE = "com.woonders.lacemsjsf.InputNumberPercentageRenderer";
}
