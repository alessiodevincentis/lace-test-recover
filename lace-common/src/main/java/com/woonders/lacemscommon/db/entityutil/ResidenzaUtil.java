package com.woonders.lacemscommon.db.entityutil;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.util.DateToCalendar;

/**
 * Created by Emanuele on 04/02/2017.
 */
@Component
public class ResidenzaUtil {

	public static final String NAME = "residenzaUtil";
	public static final String JSF_NAME = "#{" + NAME + "}";

	public int anniResidenza(final Date dataInizioResidenza) {
		if (dataInizioResidenza != null) {
			return (DateToCalendar.diffeDateYear(dataInizioResidenza, new Date()));
		}
		return 0;
	}
}
