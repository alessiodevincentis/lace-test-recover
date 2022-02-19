package com.woonders.lacemsjsf.db.app.model.util;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemsjsf.app.model.Occupazione;

@Component
public class ClienteViewModelUtil {

	public static final String NAME = "clienteViewModelUtil";
	public static final String JSF_NAME = "#{" + NAME + "}";

	public Occupazione getOccupazionePredefinita(final ClienteViewModel clienteViewModel) {
		final Occupazione occupazione = new Occupazione();
		if (clienteViewModel.isSecondaOccupazionePredefinita()) {
			occupazione.setImpiego(clienteViewModel.getImpiego2());
			occupazione.setOccupazione(clienteViewModel.getOccupazione2());
			occupazione.setQualifica(clienteViewModel.getQualifica2());
			occupazione.setEnte(clienteViewModel.getEnte2());
			occupazione.setCategoria(clienteViewModel.getCat2());
			occupazione.setInizioOccupazioneAssunzione(clienteViewModel.getDataInizio2());
		} else {
			occupazione.setImpiego(clienteViewModel.getImpiego());
			occupazione.setOccupazione(clienteViewModel.getOccupazione());
			occupazione.setQualifica(clienteViewModel.getQualifica());
			occupazione.setEnte(clienteViewModel.getEnte());
			occupazione.setCategoria(clienteViewModel.getCat());
			occupazione.setInizioOccupazioneAssunzione(clienteViewModel.getDataInizio());
		}
		return occupazione;
	}

}
