package com.woonders.lacemsapi.converter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Created by Emanuele on 23/10/2016.
 */
public class LocalDateConverter extends JsonDeserializer<LocalDate> implements Converter<String, LocalDate> {

	private static final Logger logger = LoggerFactory.getLogger(LocalDateConverter.class);
	private final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ofPattern("[yyyy-MM-dd]")).append(DateTimeFormatter.ofPattern("[dd/MM/yyyy]"))
			.toFormatter();

	@Override
	public LocalDate convert(final String value) {
		return parseDate(value);
	}

	private LocalDate parseDate(final String value) {
		if (value == null || value.isEmpty()) {
			return null;
		} else {
			try {
				return LocalDate.parse(value, dateTimeFormatter);
			} catch (final DateTimeParseException e) {
				logger.error("Unable to parse LocalDate: " + value);
				return null;
			}
		}
	}

	@Override
	public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		return parseDate(jsonParser.getValueAsString());
	}
}
