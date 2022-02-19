package com.woonders.lacemsjsf.util;

import org.springframework.stereotype.Component;

@Component
public class CalculationUtils {

	public static final String NAME = "calculationUtils";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Deprecated
	public Double roundingDoubleTwoPlace(Double number) {
		number = (double) Math.round(number * 100);
		number = number / 100;
		return number;
	}

	@Deprecated
	public Double roundingDoubleSixPlace(Double number) {
		number = (double) Math.round(number * 1000000);
		number = number / 1000000;
		return number;
	}

}
