package com.woonders.lacemsapi.converter;

import java.io.IOException;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.woonders.lacemscommon.db.entityenum.Provenienza;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 05/05/2017.
 */
@Slf4j
public class ProvenienzaConverter extends JsonDeserializer<Provenienza> implements Converter<String, Provenienza> {

	@Override
	public Provenienza convert(String value) {
		return parseProvenienza(value);
	}

	@Override
	public Provenienza deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		return parseProvenienza(jsonParser.getValueAsString());
	}

	private Provenienza parseProvenienza(String value) {
		// manteniamo LEAD se non riusciamo a parsare
		Provenienza provenienza = Provenienza.LEAD;
		try {
			provenienza = Provenienza.valueOf(value);
		} catch (final IllegalArgumentException e) {
			log.error("Unable to parse Provenienza: " + value);
		}
		return provenienza;
	}
}
