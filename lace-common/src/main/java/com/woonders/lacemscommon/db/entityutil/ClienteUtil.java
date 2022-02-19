package com.woonders.lacemscommon.db.entityutil;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.util.Age;
import com.woonders.lacemscommon.util.DateToCalendar;

/**
 * Created by Emanuele on 02/02/2017.
 */
@Component
public class ClienteUtil {

	public static final String NAME = "clienteUtil";
	public static final String JSF_NAME = "#{" + NAME + "}";

	public int calcAnniAssunzione(Date dataInizioLavoro) {
		if (dataInizioLavoro != null) {
			return DateToCalendar.calculateAge(dataInizioLavoro).getYears();
		}
		return 0;
	}

	public String calcEta(Date dataNascita) {
		if (dataNascita != null) {
			Age age = DateToCalendar.calculateAge(dataNascita);
			return "Anni: " + Integer.toString(age.getYears()) + " / Mesi: " + Integer.toString(age.getMonths());
		}
		return null;
	}
}
