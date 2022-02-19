package com.woonders.lacemsjsf.ui.component.italiancalendar;

import org.primefaces.component.calendar.Calendar;

import javax.faces.component.FacesComponent;

//http://stackoverflow.com/questions/10568633/adding-custom-attributes-to-primefaces-autocomplete-component-in-jsf
//http://memorynotfound.com/jsf-custom-input-facescomponent-example/ pare si può fare senza taglib
@FacesComponent(ItalianCalendar.COMPONENT_TYPE)
public class ItalianCalendar extends Calendar {

    public static final String COMPONENT_FAMILY = "com.woonders.lacemsjsf";
    public static final String COMPONENT_TYPE = "com.woonders.lacemsjsf.ItalianCalendar";

    public ItalianCalendar() {
        super();
        setPattern("dd/MM/yyyy");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return ItalianCalendarRenderer.RENDERER_TYPE;
    }

}
