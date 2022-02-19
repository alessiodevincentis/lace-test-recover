package com.woonders.lacemsjsf.view.amministrazione;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.AmministrazioneService;
import com.woonders.lacemscommon.service.impl.AmministrazioneServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("amministrazioneConverter")
public class AmministrazioneConverter extends BaseConverter {

	private final AmministrazioneService amministrazioneService;

	public AmministrazioneConverter() {
		amministrazioneService = findBean(AmministrazioneServiceImpl.JSF_NAME);
	}

	@Override
	public Object getAsObject(final FacesContext fc, final UIComponent uic, final String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				return amministrazioneService.findOne(Integer.parseInt(value));
			} catch (final NumberFormatException e) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid amministration."));
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
			return String.valueOf(((AmministrazioneViewModel) object).getCodiceAmm());
		} else {
			return null;
		}
	}

}
