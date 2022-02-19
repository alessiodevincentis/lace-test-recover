package com.woonders.lacemsapi.converter;

import org.springframework.core.convert.converter.Converter;

import com.woonders.lacemscommon.sms.MessageType;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 23/01/2017.
 */
@Slf4j
public class MessageTypeConverter implements Converter<String, MessageType> {

	@Override
	public MessageType convert(String value) {
		MessageType messageType = null;
		try {
			messageType = MessageType.valueOf(value);
		} catch (final IllegalArgumentException e) {
			log.error("Unable to parse MessageType: " + value);
		}
		return messageType;
	}
}
