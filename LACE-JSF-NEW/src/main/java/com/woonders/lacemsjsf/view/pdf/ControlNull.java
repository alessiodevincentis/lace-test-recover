package com.woonders.lacemsjsf.view.pdf;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class ControlNull {

	public void checkNull(Object obj) throws IllegalAccessException {
		for (Field f : getClass().getDeclaredFields()) {
			if (f.get(this) == null) {
				f.set(obj, getDefaultValueForType(f.getType()));
			}
		}
	}

	private Object getDefaultValueForType(Class<?> type) {
		Object defaultValue = null;
		if (type.isAssignableFrom(String.class)) {
			defaultValue = "";
		} else if (type.isAssignableFrom(int.class)) {
			defaultValue = 0;
		}
		return defaultValue;
	}

	public void setValueEmpty(PDAcroForm acroForm) throws IOException {
		for (PDField field : acroForm.getFields()) {
			if (field.getValueAsString().equalsIgnoreCase("0.00"))
				field.setValue("");
		}
	}

}
