package com.woonders.lacemsjsf.ui.component.readonlyselectonemenu;

import javax.faces.render.FacesRenderer;

import org.primefaces.component.selectonemenu.SelectOneMenuRenderer;

@FacesRenderer(componentFamily = ReadOnlySelectOneMenu.COMPONENT_FAMILY, rendererType = ReadOnlySelectOneMenuRenderer.RENDERER_TYPE)
public class ReadOnlySelectOneMenuRenderer extends SelectOneMenuRenderer {

	public static final String RENDERER_TYPE = "com.woonders.lacemsjsf.ReadOnlySelectOneMenuRenderer";

}
