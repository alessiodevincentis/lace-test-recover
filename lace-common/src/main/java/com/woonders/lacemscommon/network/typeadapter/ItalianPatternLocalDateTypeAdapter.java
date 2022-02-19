package com.woonders.lacemscommon.network.typeadapter;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.springframework.util.StringUtils;

import com.google.gson.*;

/**
 * Created by Emanuele on 21/02/2017.
 */
public class ItalianPatternLocalDateTypeAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

	private final DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ofPattern("[dd/MM/yyyy]")).toFormatter();

	@Override
	public LocalDate deserialize(JsonElement jsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		if (!StringUtils.isEmpty(jsonElement.getAsString())) {
			return LocalDate.parse(jsonElement.getAsString(), dateFormatter);
		}
		return null;
	}

	@Override
	public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
		if (localDate != null) {
			return new JsonPrimitive(dateFormatter.format(localDate));
		}
		return null;
	}
}
