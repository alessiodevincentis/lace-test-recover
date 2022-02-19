package com.woonders.lacemsjsf.ui.component.italiancalendarwithtime;

import javax.faces.component.FacesComponent;

import org.primefaces.component.calendar.Calendar;

@FacesComponent(ItalianCalendarWithTime.COMPONENT_TYPE)
public class ItalianCalendarWithTime extends Calendar {

	public static final String COMPONENT_FAMILY = "com.woonders.lacemsjsf";
	public static final String COMPONENT_TYPE = "com.woonders.lacemsjsf.ItalianCalendarWithTime";

	public ItalianCalendarWithTime() {
		super();
		setTimeZone("Europe/Rome");
		setPattern("dd/MM/yyyy HH:mm");
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ItalianCalendarWithTimeRenderer.RENDERER_TYPE;
	}

}
