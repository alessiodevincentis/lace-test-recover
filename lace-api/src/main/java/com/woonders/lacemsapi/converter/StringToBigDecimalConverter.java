package com.woonders.lacemsapi.converter;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Emanuele on 12/11/2016.
 */
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

	private static final Logger logger = LoggerFactory.getLogger(StringToBigDecimalConverter.class);

	@Override
	public BigDecimal convert(final String value) {
		BigDecimal result;
		try {
			result = new BigDecimal(value);
		} catch (final NumberFormatException e) {
			logger.error("Unable to parse BigDecimal: " + value);
			result = null;
		}
		return result;
	}
}
