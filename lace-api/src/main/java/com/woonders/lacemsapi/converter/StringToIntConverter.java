package com.woonders.lacemsapi.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Emanuele on 12/11/2016.
 */
public class StringToIntConverter implements Converter<String, Integer> {

	private static final Logger logger = LoggerFactory.getLogger(StringToIntConverter.class);

	@Override
	public Integer convert(final String value) {
		Integer result;
		try {
			result = Integer.valueOf(value);
		} catch (final NumberFormatException e) {
			logger.error("Unable to parse Integer: " + value);
			result = null;
		}
		return result;
	}
}
