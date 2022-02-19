package com.woonders.lacemsjsf.ui.converter;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemsjsf.util.BaseConverter;
import lombok.extern.slf4j.Slf4j;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@Slf4j
@FacesConverter("aziendaConverter")
public class AziendaConverter extends BaseConverter {

    private final AziendaService aziendaService;

    public AziendaConverter() {
        aziendaService = findBean(AziendaServiceImpl.JSF_NAME);
    }

    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                return aziendaService.getOne(Long.parseLong(value));
            } catch (final NumberFormatException e) {
                throw new ConverterException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Azienda"));
            } catch (final ArrayIndexOutOfBoundsException e) {
                return null;
            }

        } else {
            return null;
        }
    }

    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        if (o != null) {
            return String.valueOf(((AziendaViewModel) o).getId());
        } else {
            return null;
        }
    }
}
