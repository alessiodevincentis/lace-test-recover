package com.woonders.lacemsjsf.view.search;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.SearchService;
import com.woonders.lacemscommon.service.impl.SearchServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;

@FacesConverter("clientiNominativiConverter")
public class ClientiNominativiConverter extends BaseConverter {

	private final SearchService searchService;

	public ClientiNominativiConverter() {
		searchService = findBean(SearchServiceImpl.JSF_NAME);
	}

	@Override
	public Object getAsObject(final FacesContext fc, final UIComponent uic, final String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				return searchService.findOne(Integer.parseInt(value));
			} catch (final NumberFormatException e) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Cliente/Nominativo non valido."));
			} catch (final ArrayIndexOutOfBoundsException | ItemNotFoundException e) {
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	public String getAsString(final FacesContext fc, final UIComponent uic, final Object object) {
		if (object != null) {
			return String.valueOf(((ClienteViewModel) object).getId());
		} else {
			return null;
		}
	}

}