package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.db.entity.Amministrazione;

@Component
public class AmministrazioneViewModelCreator
		extends AbstractBaseViewModelCreator<Amministrazione, AmministrazioneViewModel> {

	@Autowired
	private ValutazioneAmministrazioneViewModelCreator valutazioneAmministrazioneViewModelCreator;

	@Override
	public Amministrazione createModel(AmministrazioneViewModel viewModel) {
		if (viewModel != null) {
			final Amministrazione amministrazione = new Amministrazione();
			amministrazione.setCodiceAmm(viewModel.getCodiceAmm());
			amministrazione.setCodiceFiscale(viewModel.getCodiceFiscale());
			amministrazione.setCqs(viewModel.isCqs());
			amministrazione.setDlg(viewModel.isDlg());
			amministrazione.setEmail(viewModel.getEmail());
			amministrazione.setFax(viewModel.getFax());
			amministrazione.setIndirizzoPec(viewModel.getIndirizzoPec());
			amministrazione.setNote(viewModel.getNote());
			amministrazione.setNotificaPec(viewModel.isNotificaPec());
			amministrazione.setPiva(viewModel.getPiva());
			amministrazione.setRagioneSociale(viewModel.getRagioneSociale());
			amministrazione.setReferente(viewModel.getReferente());
			amministrazione.setIndirizzoSedeLegale(viewModel.getIndirizzoSedeLegale());
			amministrazione.setIndirizzoSedeNotifica(viewModel.getIndirizzoSedeNotifica());
			amministrazione.setProvSedeLegale(viewModel.getProvSedeLegale());
			amministrazione.setProvSedeNotifica(viewModel.getProvSedeNotifica());
			amministrazione.setCapSedeLegale(viewModel.getCapSedeLegale());
			amministrazione.setCapSedeNotifica(viewModel.getCapSedeNotifica());
			amministrazione.setLuogoSedeLegale(viewModel.getLuogoSedeLegale());
			amministrazione.setLuogoSedeNotifica(viewModel.getLuogoSedeNotifica());
			amministrazione.setCivicoSedeLegale(viewModel.getCivicoSedeLegale());
			amministrazione.setCivicoSedeNotifica(viewModel.getCivicoSedeNotifica());
			amministrazione.setNazioneSedeLegale(viewModel.getNazioneSedeLegale());
			amministrazione.setNazioneSedeNotifica(viewModel.getNazioneSedeNotifica());
			amministrazione.setRuolo(viewModel.getRuolo());
			amministrazione.setTelefono(viewModel.getTelefono());
			if (viewModel.getValutazioneAmministrazioneViewModelList() != null) {
				amministrazione.setValutazioneAmministrazione(valutazioneAmministrazioneViewModelCreator
						.fromList(viewModel.getValutazioneAmministrazioneViewModelList()));
			}
			return amministrazione;
		}
		return null;
	}

	@Override
	public AmministrazioneViewModel createViewModel(Amministrazione model) {
		if (model != null) {
			final AmministrazioneViewModel amministrazioneViewModel = new AmministrazioneViewModel();
			amministrazioneViewModel.setCodiceAmm(model.getCodiceAmm());
			amministrazioneViewModel.setCodiceFiscale(model.getCodiceFiscale());
			amministrazioneViewModel.setCqs(model.isCqs());
			amministrazioneViewModel.setDlg(model.isDlg());
			amministrazioneViewModel.setEmail(model.getEmail());
			amministrazioneViewModel.setFax(model.getFax());
			amministrazioneViewModel.setIndirizzoPec(model.getIndirizzoPec());
			amministrazioneViewModel.setNote(model.getNote());
			amministrazioneViewModel.setNotificaPec(model.isNotificaPec());
			amministrazioneViewModel.setPiva(model.getPiva());
			amministrazioneViewModel.setRagioneSociale(model.getRagioneSociale());
			amministrazioneViewModel.setReferente(model.getReferente());
			amministrazioneViewModel.setIndirizzoSedeLegale(model.getIndirizzoSedeLegale());
			amministrazioneViewModel.setIndirizzoSedeNotifica(model.getIndirizzoSedeNotifica());
			amministrazioneViewModel.setProvSedeLegale(model.getProvSedeLegale());
			amministrazioneViewModel.setProvSedeNotifica(model.getProvSedeNotifica());
			amministrazioneViewModel.setCapSedeLegale(model.getCapSedeLegale());
			amministrazioneViewModel.setCapSedeNotifica(model.getCapSedeNotifica());
			amministrazioneViewModel.setLuogoSedeLegale(model.getLuogoSedeLegale());
			amministrazioneViewModel.setLuogoSedeNotifica(model.getLuogoSedeNotifica());
			amministrazioneViewModel.setCivicoSedeLegale(model.getCivicoSedeLegale());
			amministrazioneViewModel.setCivicoSedeNotifica(model.getCivicoSedeNotifica());
			amministrazioneViewModel.setNazioneSedeLegale(model.getNazioneSedeLegale());
			amministrazioneViewModel.setNazioneSedeNotifica(model.getNazioneSedeNotifica());
			amministrazioneViewModel.setRuolo(model.getRuolo());
			amministrazioneViewModel.setTelefono(model.getTelefono());
			if (model.getValutazioneAmministrazione() != null) {
				amministrazioneViewModel.setValutazioneAmministrazioneViewModelList(
						valutazioneAmministrazioneViewModelCreator.fromSet(model.getValutazioneAmministrazione()));
			}
			return amministrazioneViewModel;
		}
		return null;
	}

}
