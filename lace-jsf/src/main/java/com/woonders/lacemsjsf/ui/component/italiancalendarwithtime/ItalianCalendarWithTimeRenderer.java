package com.woonders.lacemsjsf.ui.component.italiancalendarwithtime;

import javax.faces.render.FacesRenderer;

import org.primefaces.component.calendar.CalendarRenderer;

@FacesRenderer(componentFamily = ItalianCalendarWithTime.COMPONENT_FAMILY, rendererType = ItalianCalendarWithTimeRenderer.RENDERER_TYPE)
public class ItalianCalendarWithTimeRenderer extends CalendarRenderer {

	public static final String RENDERER_TYPE = "com.woonders.lacemsjsf.ItalianCalendarWithTimeRenderer";

}