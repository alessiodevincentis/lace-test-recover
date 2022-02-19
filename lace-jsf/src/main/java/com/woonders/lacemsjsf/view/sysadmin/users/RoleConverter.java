package com.woonders.lacemsjsf.view.sysadmin.users;

import com.woonders.lacemscommon.app.viewmodel.RoleViewModel;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.RoleService;
import com.woonders.lacemscommon.service.impl.RoleServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by Emanuele on 06/10/2016.
 */
@FacesConverter("roleConverter")
public class RoleConverter extends BaseConverter {

	private RoleService roleService;

	public RoleConverter() {
		roleService = findBean(RoleServiceImpl.JSF_NAME);
	}

	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				return roleService.findOne(Long.valueOf(value));
			} catch (final ItemNotFoundException | NumberFormatException e) {
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Role."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object object) {
		if (object != null) {
			return String.valueOf(((RoleViewModel) object).getId());
		} else {
			return null;
		}
	}
}
