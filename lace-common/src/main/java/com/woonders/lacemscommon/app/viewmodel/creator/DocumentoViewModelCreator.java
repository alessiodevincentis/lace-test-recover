package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.DocumentoViewModel;
import com.woonders.lacemscommon.db.entity.Documento;

@Component
public class DocumentoViewModelCreator extends AbstractBaseViewModelCreator<Documento, DocumentoViewModel> {

	@Override
	public Documento createModel(DocumentoViewModel viewModel) {
		if (viewModel != null) {
			Documento documento = new Documento();
			documento.setCodiceDoc(viewModel.getCodiceDoc());
			documento.setLuogoRilascioDoc(viewModel.getLuogoRilascioDoc());
			documento.setProvinciaRilascio(viewModel.getProvinciaRilascio());
			documento.setNazioneRilascio(viewModel.getNazioneRilascio());
			documento.setNumeroDoc(viewModel.getNumeroDoc());
			documento.setRilascioDoc(viewModel.getRilascioDoc());
			documento.setScadenzaDoc(viewModel.getScadenzaDoc());
			documento.setTipoDocumento(viewModel.getTipoDocumento());
			return documento;
		}
		return null;
	}

	@Override
	public DocumentoViewModel createViewModel(Documento model) {
		if (model != null) {
			DocumentoViewModel documentoViewModel = new DocumentoViewModel();
			documentoViewModel.setCodiceDoc(model.getCodiceDoc());
			documentoViewModel.setLuogoRilascioDoc(model.getLuogoRilascioDoc());
			documentoViewModel.setProvinciaRilascio(model.getProvinciaRilascio());
			documentoViewModel.setNazioneRilascio(model.getNazioneRilascio());
			documentoViewModel.setNumeroDoc(model.getNumeroDoc());
			documentoViewModel.setRilascioDoc(model.getRilascioDoc());
			documentoViewModel.setScadenzaDoc(model.getScadenzaDoc());
			documentoViewModel.setTipoDocumento(model.getTipoDocumento());
			return documentoViewModel;
		}
		return null;
	}

}
