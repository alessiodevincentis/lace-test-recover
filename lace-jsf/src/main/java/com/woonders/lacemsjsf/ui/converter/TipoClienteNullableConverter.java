package com.woonders.lacemsjsf.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemsjsf.util.BaseConverter;

/**
 * Created by Emanuele on 28/03/2017.
 */
@FacesConverter("tipoClienteNullableConverter")
public class TipoClienteNullableConverter extends BaseConverter{


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            return Tipo.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        try {
            if(o != null) {
                return ((Tipo) o).name();
            }
            return null;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
