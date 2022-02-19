package com.woonders.lacemsjsf.view.azienda;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by ema98 on 8/18/2017.
 */
@FacesConverter("aziendaConverter")
public class AziendaConverter extends BaseConverter {

    private final AziendaService aziendaService;

    public AziendaConverter() {
        aziendaService = findBean(AziendaServiceImpl.JSF_NAME);
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s != null && s.trim().length() > 0 && !"qualsiasi".equalsIgnoreCase(s)) {
            try {
                return aziendaService.getOne(Integer.parseInt(s));
            } catch (final NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error",
                        "Not a valid azienda"));
            } catch (final ArrayIndexOutOfBoundsException e) {
                return null;
            }

        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o != null) {
            return String.valueOf(((AziendaViewModel) o).getId());
        } else {
            return null;
        }
    }
}
