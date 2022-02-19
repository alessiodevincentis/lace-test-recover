package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.CampagnaSmsViewModel;
import com.woonders.lacemscommon.db.entity.CampagnaSms;

/**
 * Created by Emanuele on 29/03/2017.
 */
@Component
public class CampagnaSmsViewModelCreator extends AbstractBaseViewModelCreator<CampagnaSms, CampagnaSmsViewModel> {

	@Autowired
	private CampagnaViewModelCreator campagnaViewModelCreator;
	@Autowired
	private ClienteViewModelCreator clienteViewModelCreator;

	@Override
	public CampagnaSms createModel(CampagnaSmsViewModel viewModel) {
		if (viewModel != null) {
			return CampagnaSms.builder().id(viewModel.getId()).cognomeDestinatario(viewModel.getCognomeDestinatario())
					.nomeDestinatario(viewModel.getNomeDestinatario())
					.numeroDestinatario(viewModel.getNumeroDestinatario())
					.campagna(campagnaViewModelCreator.createModel(viewModel.getCampagna()))
					.cliente(clienteViewModelCreator.createModel(viewModel.getCliente()))
					.dataEsito(viewModel.getDataEsito()).esito(viewModel.getEsito()).messageId(viewModel.getMessageId())
					.build();
		}
		return null;
	}

	@Override
	public CampagnaSmsViewModel createViewModel(CampagnaSms model) {
		if (model != null) {
			return CampagnaSmsViewModel.builder()
					.campagna(campagnaViewModelCreator.createViewModel(model.getCampagna()))
					.cliente(clienteViewModelCreator.createViewModel(model.getCliente()))
					.cognomeDestinatario(model.getCognomeDestinatario()).dataEsito(model.getDataEsito())
					.esito(model.getEsito()).id(model.getId()).messageId(model.getMessageId())
					.nomeDestinatario(model.getNomeDestinatario()).numeroDestinatario(model.getNumeroDestinatario())
					.build();
		}
		return null;
	}
}
