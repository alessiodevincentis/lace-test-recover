package com.woonders.lacemscommon.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

/**
 * Created by Emanuele on 23/02/2017.
 */
@Component
public class IvaUtil {

	public BigDecimal fromLordoToNetto(BigDecimal lordo) {
		if (lordo == null) {
			return null;
		}
		return lordo.multiply(new BigDecimal("100")).divide(new BigDecimal("122"), 2, BigDecimal.ROUND_HALF_UP);
	}
}
