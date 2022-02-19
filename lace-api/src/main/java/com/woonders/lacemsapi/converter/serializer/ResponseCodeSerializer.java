package com.woonders.lacemsapi.converter.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.woonders.lacemsapi.model.app.BaseMessage;

/**
 * Created by Emanuele on 13/05/2017.
 */
public class ResponseCodeSerializer extends JsonSerializer<BaseMessage.ResponseCode> {

	@Override
	public void serialize(BaseMessage.ResponseCode responseCode, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		jsonGenerator.writeString(String.valueOf(responseCode.getValue()));
	}
}
