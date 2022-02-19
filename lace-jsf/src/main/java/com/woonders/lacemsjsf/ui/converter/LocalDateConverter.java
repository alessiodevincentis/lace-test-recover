package com.woonders.lacemsjsf.ui.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import org.springframework.util.StringUtils;

import com.woonders.lacemsjsf.util.BaseConverter;

@FacesConverter("com.woonders.lacemsjsf.ui.converter.LocalDateConverter")
public class LocalDateConverter extends BaseConverter {

	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		if (!StringUtils.isEmpty(s)) {
			return LocalDate.parse(s, dateFormatter);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (o != null) {
			LocalDate localDate = (LocalDate) o;
			return dateFormatter.format(localDate);
		}
		return null;
	}

}
