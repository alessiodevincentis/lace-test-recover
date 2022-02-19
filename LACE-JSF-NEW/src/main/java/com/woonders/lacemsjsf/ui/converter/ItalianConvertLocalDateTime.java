package com.woonders.lacemsjsf.ui.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import org.springframework.util.StringUtils;

import com.woonders.lacemsjsf.util.BaseConverter;

/**
 * Created by Emanuele on 08/02/2017.
 */
@FacesConverter("com.woonders.lacemsjsf.ui.converter.ItalianConvertLocalDateTime")
public class ItalianConvertLocalDateTime extends BaseConverter {

	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		if (!StringUtils.isEmpty(s)) {
			return ZonedDateTime.parse(s, getFormatter(facesContext, uiComponent)).withZoneSameInstant(ZoneOffset.UTC)
					.toLocalDateTime();
		}
		return null;
	}

	private DateTimeFormatter getFormatter(FacesContext context, UIComponent component) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getPattern(component), getLocale(context, component));
		ZoneId zone = getZoneId(component);
		return (zone != null) ? formatter.withZone(zone) : formatter;
	}

	private ZoneId getZoneId(UIComponent component) {
		Object timeZone = component.getAttributes().get("timeZone");
		return (timeZone instanceof TimeZone) ? ((TimeZone) timeZone).toZoneId()
				: (timeZone instanceof String) ? ZoneId.of((String) timeZone) : null;
	}

	private Locale getLocale(FacesContext context, UIComponent component) {
		Object locale = component.getAttributes().get("locale");
		return (locale instanceof Locale) ? (Locale) locale
				: (locale instanceof String) ? new Locale((String) locale) : context.getViewRoot().getLocale();
	}

	private String getPattern(UIComponent component) {
		String pattern = (String) component.getAttributes().get("pattern");

		if (pattern == null) {
			throw new IllegalArgumentException("pattern attribute is required");
		}

		return pattern;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (o != null) {
			LocalDateTime localDateTime = (LocalDateTime) o;
			ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.of("GMT"));
			ZonedDateTime romeDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Rome"));
			return dateTimeFormatter.format(romeDateTime);
		}
		return null;
	}
}
