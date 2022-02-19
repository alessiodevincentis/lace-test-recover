package com.woonders.lacemsjsf.view.ricarica;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.util.StringUtils;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;

/**
 * Created by Emanuele on 28/03/2017.
 */
@FacesConverter("operatorConverter")
public class OperatorConverter extends BaseConverter {

	private final OperatorService operatorService;

	public OperatorConverter() {
		operatorService = findBean(OperatorServiceImpl.JSF_NAME);
	}

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		if (!StringUtils.isEmpty(s)) {
			OperatorViewModel operatorViewModel = operatorService.findByUsernameViewModel(s);
			if (operatorViewModel != null) {
				return operatorViewModel;
			} else {
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", "Username non trovato"));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (o != null) {
			return String.valueOf(((OperatorViewModel) o).getUsername());
		} else {
			return null;
		}
	}
}
