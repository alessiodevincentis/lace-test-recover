package com.woonders.lacemsjsf.ui.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by Emanuele on 08/05/2017.
 */
@FacesConverter(value = "com.woonders.lacemsjsf.ui.converter.GenericEnumConverter")
public class GenericEnumConverter implements Converter {

	private static final String ATTRIBUTE_ENUM_TYPE = "GenericEnumConverter.enumType";

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof Enum) {
			component.getAttributes().put(ATTRIBUTE_ENUM_TYPE, value.getClass());
			return ((Enum<?>) value).name();
		} else if (value == null) {
			return null;
		} else {
			throw new ConverterException(new FacesMessage("Value is not an enum: " + value.getClass()));
		}
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Class<Enum> enumType = (Class<Enum>) component.getAttributes().get(ATTRIBUTE_ENUM_TYPE);
		try {
			return Enum.valueOf(enumType, value);
		} catch (IllegalArgumentException e) {
			throw new ConverterException(new FacesMessage("Value is not an enum of type: " + enumType));
		}
	}

}
