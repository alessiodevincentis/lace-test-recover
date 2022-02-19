package com.woonders.lacemsjsf.view.calendarioappuntamenti;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.BaseValidator;

import lombok.Getter;
import lombok.Setter;

@FacesValidator("com.woonders.lacemsjsf.view.calendarioappuntamenti.DateRangeValidator")
@RequestScoped
@Getter
@Setter
public class DateRangeValidator extends BaseValidator {

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;

	public DateRangeValidator() {
		propertiesUtil = findBean(PropertiesUtil.JSF_NAME);
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		LocalDateTime startDate = (LocalDateTime) ((UIInput)component.getAttributes().get("startDate")).getValue();
		LocalDateTime endDate = (LocalDateTime) ((UIInput)component.getAttributes().get("endDate")).getValue();

		if(startDate == null || endDate == null || endDate.minusMinutes(Constants.SLOT_MINUTES_SCHEDULE).isBefore(startDate)) {
			final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					MessageFormat.format(propertiesUtil.getMsgAppuntamentoValidateError(), Constants.SLOT_MINUTES_SCHEDULE), null);
			throw new ValidatorException(msg);
		}
	}
}
