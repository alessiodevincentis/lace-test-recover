package com.woonders.lacemsjsf.ui.component.readonlyselectonemenu;

import javax.faces.component.FacesComponent;

import org.primefaces.component.selectonemenu.SelectOneMenu;

@FacesComponent(ReadOnlySelectOneMenu.COMPONENT_TYPE)
public class ReadOnlySelectOneMenu extends SelectOneMenu {

	public static final String COMPONENT_FAMILY = "com.woonders.lacems";
	public static final String COMPONENT_TYPE = "com.woonders.lacemsjsf.ReadOnlySelectOneMenu";

	public ReadOnlySelectOneMenu() {
		super();
		setReadonly(true);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ReadOnlySelectOneMenuRenderer.RENDERER_TYPE;
	}

}
