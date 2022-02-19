package com.woonders.lacemscommon.db.entityutil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.db.entity.Trattenute;
import com.woonders.lacemscommon.laceenum.StringCalc;
import com.woonders.lacemscommon.util.DateToCalendar;

/**
 * Created by Emanuele on 03/02/2017.
 */
@Component
public class TrattenutaUtil {

	public static final String NAME = "trattenutaUtil";
	public static final String JSF_NAME = "#{" + NAME + "}";

	// TODO non chiamato perche lo salviamo nel db
	public Date calcScadenzaTrat(Trattenute trattenute) {
		return calcScadenzaTrat(trattenute.getScadenzaTrat(), trattenute.getBusta(), trattenute.getCalcolaDat(),
				trattenute.getRataTrat(), trattenute.getMontanteTrat());
	}

	public Date calcScadenzaTrat(TrattenuteViewModel trattenute) {
		return calcScadenzaTrat(trattenute.getScadenzaTrat(), trattenute.getBusta(), trattenute.getCalcolaDat(),
				trattenute.getRataTrat(), trattenute.getMontanteTrat());
	}

	// TODO molto simile a EstinzioneUtil::calcScadenzaEstinzione
	private Date calcScadenzaTrat(Date scadenzaTrattenuta, Date busta, String calcolaDat, BigDecimal rataTrattenuta,
			BigDecimal montanteTrattenuta) {
		if (StringCalc.RATE.getValue().equalsIgnoreCase(calcolaDat) && busta != null && montanteTrattenuta != null
				&& !montanteTrattenuta.equals(BigDecimal.ZERO) && rataTrattenuta != null
				&& !rataTrattenuta.equals(BigDecimal.ZERO)) {
			final Calendar date = DateToCalendar.dateToCalendar(busta);
			final Integer rateScadenza = montanteTrattenuta.intValue() / rataTrattenuta.intValue();
			date.add(Calendar.MONTH, rateScadenza);
			return DateToCalendar.fineMese(date.getTime(), 0);
		} else if (StringCalc.DATA.getValue().equalsIgnoreCase(calcolaDat)) {
			return scadenzaTrattenuta;
		}
		return null;
	}

	// TODO non chiamato perche lo salviamo nel db
	public Date calcDataRinnovoTrat(Trattenute trattenute) {
		return calcDataRinnovoTrat(trattenute.getScadenzaTrat(), trattenute.getDurataTrat(), trattenute.getTipoTrat());
	}

	public Date calcDataRinnovoTrat(TrattenuteViewModel trattenuteViewModel) {
		return calcDataRinnovoTrat(trattenuteViewModel.getScadenzaTrat(), trattenuteViewModel.getDurataTrat(),
				trattenuteViewModel.getTipoTrat());
	}

	public Date calcDataRinnovoTrat(Date scadenzaTrattenuta, Integer durataTrattenuta, String tipoTrattenuta) {
		if (scadenzaTrattenuta != null && durataTrattenuta != null && durataTrattenuta != 0
				&& !(Trattenute.TipoImpegno.PIGNORAMENTO.getValue().equalsIgnoreCase(tipoTrattenuta))
				&& !(Trattenute.TipoImpegno.PRESTITO.getValue().equalsIgnoreCase(tipoTrattenuta))) {
			final BigDecimal annirinnovo = ((new BigDecimal(durataTrattenuta).multiply(new BigDecimal("0.6"))));
			final Calendar cal = DateToCalendar.dateToCalendar(scadenzaTrattenuta);
			cal.add(Calendar.MONTH, -annirinnovo.intValue());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
		}
		return null;
	}

	public int calcRateScadenza(Trattenute trattenute) {
		return calcRateScadenza(trattenute.getRataTrat(), trattenute.getScadenzaTrat());
	}

	public int calcRateScadenza(TrattenuteViewModel trattenuteViewModel) {
		return calcRateScadenza(trattenuteViewModel.getRataTrat(), trattenuteViewModel.getScadenzaTrat());
	}

	public int calcRateScadenza(BigDecimal rataTrat, Date scadenzaTrat) {
		// TODO perche controlliamo rataTrat???
		if (rataTrat != null && !rataTrat.equals(BigDecimal.ZERO) && scadenzaTrat != null) {
			return DateToCalendar.diffeDateMonth(new Date(), scadenzaTrat);
		}
		return 0;
	}

	public BigDecimal calcConteggioTrat(Trattenute trattenute) {
		return calcConteggioTrat(trattenute.getScadenzaTrat(), trattenute.getRataTrat());
	}

	public BigDecimal calcConteggioTrat(TrattenuteViewModel trattenuteViewModel) {
		return calcConteggioTrat(trattenuteViewModel.getScadenzaTrat(), trattenuteViewModel.getRataTrat());
	}

	private BigDecimal calcConteggioTrat(Date scadenzaTrattenuta, BigDecimal rataTrattenuta) {
		if (scadenzaTrattenuta != null && rataTrattenuta != null) {
			final BigDecimal diff = new BigDecimal(DateToCalendar.diffeDateMonth(new Date(), scadenzaTrattenuta));
			return (diff.multiply(rataTrattenuta)).subtract((diff.multiply(rataTrattenuta).multiply(BigDecimal.TEN))
					.divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return BigDecimal.ZERO;
	}
}
