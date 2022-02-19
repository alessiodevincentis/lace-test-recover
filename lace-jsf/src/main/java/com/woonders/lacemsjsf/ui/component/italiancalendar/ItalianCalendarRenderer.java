package com.woonders.lacemsjsf.ui.component.italiancalendar;

import javax.faces.render.FacesRenderer;

import org.primefaces.component.calendar.CalendarRenderer;

@FacesRenderer(componentFamily = ItalianCalendar.COMPONENT_FAMILY, rendererType = ItalianCalendarRenderer.RENDERER_TYPE)
public class ItalianCalendarRenderer extends CalendarRenderer {

	public static final String RENDERER_TYPE = "com.woonders.lacemsjsf.ItalianCalendarRenderer";

}
