package com.woonders.lacemsjsf.view.trattenuta;

import static com.woonders.lacemsjsf.view.trattenuta.TrattenutaView.NAME;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.db.entityutil.TrattenutaUtil;

import lombok.Setter;

/**
 * Created by Emanuele on 03/02/2017.
 */
@Setter
@ViewScoped
@ManagedBean(name = NAME)
public class TrattenutaView implements Serializable {

	public static final String NAME = "trattenutaView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	@ManagedProperty(TrattenutaUtil.JSF_NAME)
	private TrattenutaUtil trattenutaUtil;

	public void resetTrattenuta(TrattenuteViewModel trattenuteViewModel) {
		if (trattenuteViewModel != null && trattenuteViewModel.getCalcolaDat() != null
				&& !trattenuteViewModel.getCalcolaDat().isEmpty()) {
			trattenuteViewModel.setMontanteTrat(BigDecimal.ZERO);
			trattenuteViewModel.setBusta(null);
			trattenuteViewModel.setRateScadenza(0);
			trattenuteViewModel.setScadenzaTrat(null);
			trattenuteViewModel.setDataRinnovoTrat(null);
			trattenuteViewModel.setConteggioTrat(BigDecimal.ZERO);
		}
	}

	public void updateScadenzaTrattenuta(TrattenuteViewModel trattenuteViewModel) {
		trattenuteViewModel.setScadenzaTrat(trattenutaUtil.calcScadenzaTrat(trattenuteViewModel));
	}

	public void updateDataRinnovoTrattenuta(TrattenuteViewModel trattenuteViewModel) {
		trattenuteViewModel.setDataRinnovoTrat(trattenutaUtil.calcDataRinnovoTrat(trattenuteViewModel));
	}

	public void updateConteggioTrattenuta(TrattenuteViewModel trattenuteViewModel) {
		trattenuteViewModel.setConteggioTrat(trattenutaUtil.calcConteggioTrat(trattenuteViewModel));
	}

	public void updateRateScadenza(TrattenuteViewModel trattenuteViewModel) {
		trattenuteViewModel.setRateScadenza(trattenutaUtil.calcRateScadenza(trattenuteViewModel));
	}

	public void updateDatiTrattenutaFromScadenzaTrattenuta(TrattenuteViewModel trattenuteViewModel) {
		updateDataRinnovoTrattenuta(trattenuteViewModel);
		updateConteggioTrattenuta(trattenuteViewModel);
		updateRateScadenza(trattenuteViewModel);
	}

	public void updateDatiTrattenutaFromBustaPaga(TrattenuteViewModel trattenuteViewModel) {
		updateScadenzaTrattenuta(trattenuteViewModel);
		updateDataRinnovoTrattenuta(trattenuteViewModel);
		updateConteggioTrattenuta(trattenuteViewModel);
		updateRateScadenza(trattenuteViewModel);

	}

}
