package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.ResidenzaViewModel;
import com.woonders.lacemscommon.db.entity.Residenza;

/**
 * Created by emanuele on 09/01/17.
 */
@Component
public class ResidenzaViewModelCreator extends AbstractBaseViewModelCreator<Residenza, ResidenzaViewModel> {

	@Override
	public Residenza createModel(ResidenzaViewModel residenzaViewModel) {
		if (residenzaViewModel != null) {
			return Residenza.builder().capDomicilio(residenzaViewModel.getCapDomicilio())
					.capPrecedente(residenzaViewModel.getCapPrecedente())
					.capResidenza(residenzaViewModel.getCapResidenza()).codiceRes(residenzaViewModel.getCodiceRes())
					.corrispondenza(residenzaViewModel.getCorrispondenza())
					.dataInizioResidenza(residenzaViewModel.getDataInizioResidenza())
					.indirizzoDomicilio(residenzaViewModel.getIndirizzoDomicilio())
					.indirizzoPrecedente(residenzaViewModel.getIndirizzoPrecedente())
					.indirizzoResidenza(residenzaViewModel.getIndirizzoResidenza())
					.luogoDomicilio(residenzaViewModel.getLuogoDomicilio())
					.luogoPrecedente(residenzaViewModel.getLuogoPrecedente())
					.luogoResidenza(residenzaViewModel.getLuogoResidenza()).mutuo(residenzaViewModel.isMutuo())
					.provDomicilio(residenzaViewModel.getProvDomicilio())
					.provinciaPrecedente(residenzaViewModel.getProvinciaPrecedente())
					.provResidenza(residenzaViewModel.getProvResidenza())
					.speseAbitazione(residenzaViewModel.getSpeseAbitazione())
					.tipoAbitazione(residenzaViewModel.getTipoAbitazione())
					.civicoResidenza(residenzaViewModel.getCivicoResidenza())
					.nazioneResidenza(residenzaViewModel.getNazioneResidenza())
					.civicoDomicilio(residenzaViewModel.getCivicoDomicilio())
					.nazioneDomicilio(residenzaViewModel.getNazioneDomicilio())
					.civicoPrecedente(residenzaViewModel.getCivicoPrecedente())
					.nazionePrecedente(residenzaViewModel.getNazionePrecedente()).build();
		}
		return null;
	}

	@Override
	public ResidenzaViewModel createViewModel(Residenza residenza) {
		if (residenza != null) {
			return ResidenzaViewModel.builder().capDomicilio(residenza.getCapDomicilio())
					.capPrecedente(residenza.getCapPrecedente()).capResidenza(residenza.getCapResidenza())
					.codiceRes(residenza.getCodiceRes()).corrispondenza(residenza.getCorrispondenza())
					.dataInizioResidenza(residenza.getDataInizioResidenza())
					.indirizzoDomicilio(residenza.getIndirizzoDomicilio())
					.indirizzoPrecedente(residenza.getIndirizzoPrecedente())
					.indirizzoResidenza(residenza.getIndirizzoResidenza()).luogoDomicilio(residenza.getLuogoDomicilio())
					.luogoPrecedente(residenza.getLuogoPrecedente()).luogoResidenza(residenza.getLuogoResidenza())
					.mutuo(residenza.isMutuo()).provDomicilio(residenza.getProvDomicilio())
					.provinciaPrecedente(residenza.getProvinciaPrecedente()).provResidenza(residenza.getProvResidenza())
					.speseAbitazione(residenza.getSpeseAbitazione()).tipoAbitazione(residenza.getTipoAbitazione())
					.civicoResidenza(residenza.getCivicoResidenza()).nazioneResidenza(residenza.getNazioneResidenza())
					.civicoDomicilio(residenza.getCivicoDomicilio()).nazioneDomicilio(residenza.getNazioneDomicilio())
					.civicoPrecedente(residenza.getCivicoPrecedente())
					.nazionePrecedente(residenza.getNazionePrecedente()).build();
		}
		return null;
	}
}
