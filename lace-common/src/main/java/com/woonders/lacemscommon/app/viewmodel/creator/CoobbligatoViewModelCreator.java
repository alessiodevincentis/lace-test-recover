package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.CoobbligatoViewModel;
import com.woonders.lacemscommon.db.entity.Coobbligato;

@Component
public class CoobbligatoViewModelCreator extends AbstractBaseViewModelCreator<Coobbligato, CoobbligatoViewModel> {

	@Override
	public Coobbligato createModel(final CoobbligatoViewModel viewModel) {
		if (viewModel != null) {
			Coobbligato coobbligato = new Coobbligato();
			coobbligato.setId(viewModel.getId());
			coobbligato.setCap(viewModel.getCap());
			coobbligato.setCat(viewModel.getCat());
			coobbligato.setCodiceFiscale(viewModel.getCodiceFiscale());
			coobbligato.setCognome(viewModel.getCognome());
			coobbligato.setDataAssunzione(viewModel.getDataAssunzione());
			coobbligato.setDataNascita(viewModel.getDataNascita());
			coobbligato.setDataRilascioDocumento(viewModel.getDataRilascioDocumento());
			coobbligato.setEmail(viewModel.getEmail());
			coobbligato.setEnte(viewModel.getEnte());
			coobbligato.setImpiego(viewModel.getImpiego());
			coobbligato.setIndirizzo(viewModel.getIndirizzo());
			coobbligato.setLuogo(viewModel.getLuogo());
			coobbligato.setLuogoNascita(viewModel.getLuogoNascita());
			coobbligato.setLuogoRilascioDocumento(viewModel.getLuogoRilascioDocumento());
			coobbligato.setNettoStipendio(viewModel.getNettoStipendio());
			coobbligato.setNome(viewModel.getNome());
			coobbligato.setNrComponentiFamiliari(viewModel.getNrComponentiFamiliari());
			coobbligato.setNumeroDocumento(viewModel.getNumeroDocumento());
			coobbligato.setOccupazione(viewModel.getOccupazione());
			coobbligato.setProvincia(viewModel.getProvincia());
			coobbligato.setProvinciaNascita(viewModel.getProvinciaNascita());
			coobbligato.setScadenzaDocumento(viewModel.getScadenzaDocumento());
			coobbligato.setSesso(viewModel.getSesso());
			coobbligato.setTelefono(viewModel.getTelefono());
			coobbligato.setTelefonoLavoro(viewModel.getTelefonoLavoro());
			coobbligato.setTipoContrattoLavoro(viewModel.getTipoContrattoLavoro());
			coobbligato.setTipoDocumento(viewModel.getTipoDocumento());
			coobbligato.setCivicoAbitazione(viewModel.getCivicoAbitazione());
			coobbligato.setNazioneAbitazione(viewModel.getNazioneAbitazione());
			coobbligato.setProvinciaRilascioDocumento(viewModel.getProvinciaRilascioDocumento());
			coobbligato.setNazioneRilascioDocumento(viewModel.getNazioneRilascioDocumento());
			coobbligato.setNazioneNascita(viewModel.getNazioneNascita());
			coobbligato.setCittadinanza(viewModel.getCittadinanza());
			return coobbligato;
		}
		return null;
	}

	@Override
	public CoobbligatoViewModel createViewModel(Coobbligato model) {
		if (model != null) {
			final CoobbligatoViewModel coobbligatoViewModel = new CoobbligatoViewModel();
			coobbligatoViewModel.setId(model.getId());
			coobbligatoViewModel.setCap(model.getCap());
			coobbligatoViewModel.setCat(model.getCat());
			coobbligatoViewModel.setCodiceFiscale(model.getCodiceFiscale());
			coobbligatoViewModel.setCognome(model.getCognome());
			coobbligatoViewModel.setDataAssunzione(model.getDataAssunzione());
			coobbligatoViewModel.setDataNascita(model.getDataNascita());
			coobbligatoViewModel.setDataRilascioDocumento(model.getDataRilascioDocumento());
			coobbligatoViewModel.setEmail(model.getEmail());
			coobbligatoViewModel.setEnte(model.getEnte());
			coobbligatoViewModel.setImpiego(model.getImpiego());
			coobbligatoViewModel.setIndirizzo(model.getIndirizzo());
			coobbligatoViewModel.setLuogo(model.getLuogo());
			coobbligatoViewModel.setLuogoNascita(model.getLuogoNascita());
			coobbligatoViewModel.setLuogoRilascioDocumento(model.getLuogoRilascioDocumento());
			coobbligatoViewModel.setNettoStipendio(model.getNettoStipendio());
			coobbligatoViewModel.setNome(model.getNome());
			coobbligatoViewModel.setNrComponentiFamiliari(model.getNrComponentiFamiliari());
			coobbligatoViewModel.setNumeroDocumento(model.getNumeroDocumento());
			coobbligatoViewModel.setOccupazione(model.getOccupazione());
			coobbligatoViewModel.setProvincia(model.getProvincia());
			coobbligatoViewModel.setProvinciaNascita(model.getProvinciaNascita());
			coobbligatoViewModel.setScadenzaDocumento(model.getScadenzaDocumento());
			coobbligatoViewModel.setSesso(model.getSesso());
			coobbligatoViewModel.setTelefono(model.getTelefono());
			coobbligatoViewModel.setTelefonoLavoro(model.getTelefonoLavoro());
			coobbligatoViewModel.setTipoContrattoLavoro(model.getTipoContrattoLavoro());
			coobbligatoViewModel.setTipoDocumento(model.getTipoDocumento());
			coobbligatoViewModel.setCivicoAbitazione(model.getCivicoAbitazione());
			coobbligatoViewModel.setNazioneAbitazione(model.getNazioneAbitazione());
			coobbligatoViewModel.setProvinciaRilascioDocumento(model.getProvinciaRilascioDocumento());
			coobbligatoViewModel.setNazioneRilascioDocumento(model.getNazioneRilascioDocumento());
			coobbligatoViewModel.setNazioneNascita(model.getNazioneNascita());
			coobbligatoViewModel.setCittadinanza(model.getCittadinanza());
			return coobbligatoViewModel;
		}
		return null;
	}

}
