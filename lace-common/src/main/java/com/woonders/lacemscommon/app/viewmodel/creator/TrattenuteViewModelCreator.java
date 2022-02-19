package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.db.entity.Trattenute;
import com.woonders.lacemscommon.db.entityutil.TrattenutaUtil;

/**
 * Created by emanuele on 09/01/17.
 */
@Component
public class TrattenuteViewModelCreator extends AbstractBaseViewModelCreator<Trattenute, TrattenuteViewModel> {

	@Autowired
	private TrattenutaUtil trattenutaUtil;

	@Override
	public Trattenute createModel(TrattenuteViewModel trattenuteViewModel) {
		if (trattenuteViewModel != null) {

			return Trattenute.builder().busta(trattenuteViewModel.getBusta())
					.calcolaDat(trattenuteViewModel.getCalcolaDat()).codStip(trattenuteViewModel.getCodStip())
					.dataRinnovoTrat(trattenuteViewModel.getDataRinnovoTrat())
					.durataTrat(trattenuteViewModel.getDurataTrat()).istitutoTrat(trattenuteViewModel.getIstitutoTrat())
					.montanteTrat(trattenuteViewModel.getMontanteTrat()).notiCoes(trattenuteViewModel.getNotiCoes())
					.rataTrat(trattenuteViewModel.getRataTrat()).scadenzaTrat(trattenuteViewModel.getScadenzaTrat())
					.tipoTrat(trattenuteViewModel.getTipoTrat()).build();
		}
		return null;
	}

	@Override
	public TrattenuteViewModel createViewModel(Trattenute trattenute) {
		if (trattenute != null) {
			TrattenuteViewModel trattenuteViewModel = TrattenuteViewModel.builder().busta(trattenute.getBusta())
					.calcolaDat(trattenute.getCalcolaDat()).codStip(trattenute.getCodStip())
					.conteggioTrat(trattenutaUtil.calcConteggioTrat(trattenute))
					.dataRinnovoTrat(trattenute.getDataRinnovoTrat()).durataTrat(trattenute.getDurataTrat())
					.istitutoTrat(trattenute.getIstitutoTrat()).montanteTrat(trattenute.getMontanteTrat())
					.notiCoes(trattenute.getNotiCoes()).rataTrat(trattenute.getRataTrat())
					.rateScadenza(trattenutaUtil.calcRateScadenza(trattenute))
					.scadenzaTrat(trattenute.getScadenzaTrat()).tipoTrat(trattenute.getTipoTrat()).build();

			return trattenuteViewModel;
		}
		return null;
	}

}
