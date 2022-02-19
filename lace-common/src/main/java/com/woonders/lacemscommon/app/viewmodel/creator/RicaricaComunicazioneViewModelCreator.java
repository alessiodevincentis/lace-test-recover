package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.RicaricaComunicazioneViewModel;
import com.woonders.lacemscommon.db.entity.RicaricaComunicazione;

/**
 * Created by Emanuele on 16/02/2017.
 */
@Component
public class RicaricaComunicazioneViewModelCreator
		extends AbstractBaseViewModelCreator<RicaricaComunicazione, RicaricaComunicazioneViewModel> {

	@Autowired
	private OperatorViewModelCreator operatorViewModelCreator;

	@Override
	public RicaricaComunicazione createModel(RicaricaComunicazioneViewModel viewModel) {
		if (viewModel != null) {
			return RicaricaComunicazione.builder().id(viewModel.getId()).dateTime(viewModel.getDateTime())
					.payer(operatorViewModelCreator.createModel(viewModel.getPayer())).amount(viewModel.getAmount())
					.paymentMethod(viewModel.getPaymentMethod()).quantity(viewModel.getQuantity())
					.ricaricaType(viewModel.getRicaricaType()).fatturaId(viewModel.getFatturaId())
					.operatoreRicaricato(operatorViewModelCreator.createModel(viewModel.getOperatoreRicaricato()))
					.build();
		}
		return null;
	}

	@Override
	public RicaricaComunicazioneViewModel createViewModel(RicaricaComunicazione model) {
		if (model != null) {
			return RicaricaComunicazioneViewModel.builder().id(model.getId()).dateTime(model.getDateTime())
					.payer(operatorViewModelCreator.createViewModel(model.getPayer())).amount(model.getAmount())
					.paymentMethod(model.getPaymentMethod()).quantity(model.getQuantity())
					.ricaricaType(model.getRicaricaType()).fatturaId(model.getFatturaId())
					.operatoreRicaricato(operatorViewModelCreator.createViewModel(model.getOperatoreRicaricato()))
					.build();
		}
		return null;
	}
}
