package com.woonders.lacemsjsf.ui.converter;

import java.util.TimeZone;

import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("com.woonders.lacemsjsf.ui.converter.ItalianConvertDateTime")
public class ItalianConvertDateTime extends DateTimeConverter {

	public ItalianConvertDateTime() {
		super();
		setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
		setPattern("dd/MM/yyyy HH:mm");
	}

}
