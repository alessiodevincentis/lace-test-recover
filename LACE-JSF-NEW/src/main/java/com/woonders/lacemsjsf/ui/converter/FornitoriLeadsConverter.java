package com.woonders.lacemsjsf.ui.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.woonders.lacemscommon.app.viewmodel.FornitoriLeadViewModel;
import com.woonders.lacemscommon.service.FornitoriLeadService;
import com.woonders.lacemscommon.service.impl.FornitoriLeadServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;

@FacesConverter("fornitoriLeadsConverter")
public class FornitoriLeadsConverter extends BaseConverter {

	private final FornitoriLeadService fornitoriLeadsService;

	public FornitoriLeadsConverter() {
		fornitoriLeadsService = findBean(FornitoriLeadServiceImpl.JSF_NAME);
	}
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				return fornitoriLeadsService.getOne(Long.parseLong(value));
			} catch (final NumberFormatException e) {
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Operator Not Valid"));
			} catch (final ArrayIndexOutOfBoundsException e) {
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return String.valueOf(((FornitoriLeadViewModel) value).getId());
	}

}
