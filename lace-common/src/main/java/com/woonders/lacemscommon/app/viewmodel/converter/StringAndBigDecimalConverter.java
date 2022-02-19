package com.woonders.lacemscommon.app.viewmodel.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class StringAndBigDecimalConverter {

	private final Locale locale = Locale.US;
	private final DecimalFormat decimalFormat;

	public StringAndBigDecimalConverter() {
		decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		decimalFormat.setGroupingUsed(false);
		decimalFormat.setMaximumFractionDigits(2);
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setParseBigDecimal(true);
	}

	public String getStringFromBigDecimalValue(BigDecimal input) {
		if (input != null) {
			return decimalFormat.format(input);
		}
		return null;
	}

	public String getStringFromBigDecimalValue(BigDecimal input, int fractionDigits) {
		if (input != null) {
			decimalFormat.setMaximumFractionDigits(fractionDigits);
			return decimalFormat.format(input);
		}
		return null;
	}

	public BigDecimal getBigDecimalFromStringValue(String input) {
		try {
			if (input != null) {
				return (BigDecimal) decimalFormat.parse(input);
			}
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

}
