package com.woonders.lacemsjsf.ui.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;

@FacesConverter("operatorConverter")
public class OperatorConverter extends BaseConverter {

	private final OperatorService operatorService;

	public OperatorConverter() {
		operatorService = findBean(OperatorServiceImpl.JSF_NAME);
	}

	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				return operatorService.getOne(Long.parseLong(value));
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
	public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
		return String.valueOf(((OperatorViewModel) o).getId());
	}
}
