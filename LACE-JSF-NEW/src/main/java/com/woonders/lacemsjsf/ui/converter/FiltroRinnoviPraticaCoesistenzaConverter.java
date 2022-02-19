package com.woonders.lacemsjsf.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import com.woonders.lacemscommon.laceenum.FiltroRinnoviPraticaCoesistenza;
import com.woonders.lacemsjsf.util.BaseConverter;

@FacesConverter("filtroRinnoviPraticaCoesistenzaConverter")
public class FiltroRinnoviPraticaCoesistenzaConverter extends BaseConverter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		try {
			return FiltroRinnoviPraticaCoesistenza.valueOf(s);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		try {
			if (o != null) {
				return ((FiltroRinnoviPraticaCoesistenza) o).name();
			}
			return null;
		} catch (ClassCastException e) {
			return null;
		}
	}

}
