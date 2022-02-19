package com.woonders.lacemsjsf.ui.component.Inputnumberdefault;

import org.primefaces.component.inputnumber.InputNumberRenderer;

import javax.faces.render.FacesRenderer;

@FacesRenderer(componentFamily = InputNumberDefault.COMPONENT_FAMILY, rendererType = InputNumberDefaultRenderer.RENDERER_TYPE)
public class InputNumberDefaultRenderer extends InputNumberRenderer {

    public static final String RENDERER_TYPE = "com.woonders.lacemsjsf.InputNumberDefaultRenderer";
}
