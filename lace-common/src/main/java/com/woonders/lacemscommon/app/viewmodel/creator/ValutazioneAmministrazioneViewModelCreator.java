package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.ValutazioneAmministrazioneViewModel;
import com.woonders.lacemscommon.db.entity.ValutazioneAmministrazione;

@Component
public class ValutazioneAmministrazioneViewModelCreator
		extends AbstractBaseViewModelCreator<ValutazioneAmministrazione, ValutazioneAmministrazioneViewModel> {

	@Override
	public ValutazioneAmministrazione createModel(
			ValutazioneAmministrazioneViewModel valutazioneAmministrazioneViewModel) {
		if (valutazioneAmministrazioneViewModel != null) {
			final ValutazioneAmministrazione valutazioneAmministrazione = new ValutazioneAmministrazione();
			valutazioneAmministrazione.setId(valutazioneAmministrazioneViewModel.getId());
			valutazioneAmministrazione.setCompagnia(valutazioneAmministrazioneViewModel.getCompagnia());
			valutazioneAmministrazione.setValutazione(valutazioneAmministrazioneViewModel.getValutazione());
			valutazioneAmministrazione.setDataValutazione(valutazioneAmministrazioneViewModel.getDataValutazione());
			valutazioneAmministrazione.setInfoAggiuntive(valutazioneAmministrazioneViewModel.getInfoAggiuntive());
			return valutazioneAmministrazione;
		}
		return null;
	}

	@Override
	public ValutazioneAmministrazioneViewModel createViewModel(ValutazioneAmministrazione valutazioneAmministrazione) {
		if (valutazioneAmministrazione != null) {
			final ValutazioneAmministrazioneViewModel valutazioneAmministrazioneViewModel = new ValutazioneAmministrazioneViewModel();
			valutazioneAmministrazioneViewModel.setId(valutazioneAmministrazione.getId());
			valutazioneAmministrazioneViewModel.setCompagnia(valutazioneAmministrazione.getCompagnia());
			valutazioneAmministrazioneViewModel.setValutazione(valutazioneAmministrazione.getValutazione());
			valutazioneAmministrazioneViewModel.setDataValutazione(valutazioneAmministrazione.getDataValutazione());
			valutazioneAmministrazioneViewModel.setInfoAggiuntive(valutazioneAmministrazione.getInfoAggiuntive());
			return valutazioneAmministrazioneViewModel;
		}
		return null;
	}

}
