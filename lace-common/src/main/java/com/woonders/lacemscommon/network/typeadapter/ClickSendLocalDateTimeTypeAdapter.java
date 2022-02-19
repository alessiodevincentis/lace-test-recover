package com.woonders.lacemscommon.network.typeadapter;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.google.gson.*;

/**
 * Created by Emanuele on 25/02/2017.
 */
public class ClickSendLocalDateTimeTypeAdapter implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDate> {

	@Override
	public LocalDateTime deserialize(JsonElement jsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		if (jsonElement != null) {
			return LocalDateTime.ofInstant(Instant.ofEpochSecond(jsonElement.getAsLong()), ZoneId.of("GMT"));
		}
		return null;
	}

	@Override
	public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
		return null;
	}
}
