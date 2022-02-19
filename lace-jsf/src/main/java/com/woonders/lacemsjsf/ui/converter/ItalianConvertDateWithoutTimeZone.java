package com.woonders.lacemsjsf.ui.converter;

import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("com.woonders.lacemsjsf.ui.converter.ItalianConvertDateWithoutTimeZone")
public class ItalianConvertDateWithoutTimeZone extends DateTimeConverter {

	public ItalianConvertDateWithoutTimeZone() {
		setPattern("dd/MM/yyyy");
	}

}
