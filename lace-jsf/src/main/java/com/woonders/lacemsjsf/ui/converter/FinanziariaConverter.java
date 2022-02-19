package com.woonders.lacemsjsf.ui.converter;

import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;
import com.woonders.lacemscommon.service.FinanziariaService;
import com.woonders.lacemscommon.service.impl.FinanziariaServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by emanuele on 26/11/16.
 */
@FacesConverter("finanziariaConverter")
public class FinanziariaConverter extends BaseConverter {

	private final FinanziariaService finanziariaService;

	public FinanziariaConverter() {
		finanziariaService = findBean(FinanziariaServiceImpl.JSF_NAME);
	}

	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				return finanziariaService.getOne(Long.parseLong(value));
			} catch (final NumberFormatException e) {
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Finanziaria"));
			} catch (final ArrayIndexOutOfBoundsException e) {
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
		return String.valueOf(((FinanziariaViewModel) o).getId());
	}
}
