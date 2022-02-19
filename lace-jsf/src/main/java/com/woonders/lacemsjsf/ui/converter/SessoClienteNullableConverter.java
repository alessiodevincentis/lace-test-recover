package com.woonders.lacemsjsf.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemsjsf.util.BaseConverter;

/**
 * Created by Emanuele on 29/03/2017.
 */
@FacesConverter("sessoClienteNullableConverter")
public class SessoClienteNullableConverter extends BaseConverter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            return Cliente.Sesso.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        try {
            if(o != null) {
                return ((Cliente.Sesso) o).name();
            }
            return null;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
