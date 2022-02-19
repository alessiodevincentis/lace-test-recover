package com.woonders.lacemsjsf.ui.converter;

import com.woonders.lacemsjsf.util.BaseConverter;
import org.springframework.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Emanuele on 08/02/2017.
 */
@FacesConverter("com.woonders.lacemsjsf.ui.converter.LocalDateTimeConverter")
public class LocalDateTimeConverter extends BaseConverter {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String s) {
        if (!StringUtils.isEmpty(s)) {
            return LocalDateTime.parse(s, dateTimeFormatter);
        }
        return null;
    }

    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        if (o != null) {
            if (o instanceof LocalDateTime) {
                final LocalDateTime localDateTime = (LocalDateTime) o;
                return dateTimeFormatter.format(localDateTime);
            } else {
                getAsObject(facesContext, uiComponent, o.toString());
            }
        }
        return null;
    }
}
