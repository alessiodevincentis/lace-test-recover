package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.CalendarioAppuntamentoViewModel;
import com.woonders.lacemscommon.db.entity.CalendarioAppuntamento;

@Component
public class CalendarioAppuntamentoViewModelCreator
		extends AbstractBaseViewModelCreator<CalendarioAppuntamento, CalendarioAppuntamentoViewModel> {

	@Autowired
	private OperatorViewModelCreator operatorViewModelCreator;
	@Autowired
	private ClienteViewModelCreator clienteViewModelCreator;
	@Autowired
	private PraticaViewModelCreator praticaViewModelCreator;

	@Override
	public CalendarioAppuntamento createModel(CalendarioAppuntamentoViewModel viewModel) {
		if (viewModel != null) {
			return CalendarioAppuntamento.builder().descrizione(viewModel.getDescrizione()).id(viewModel.getId())
					.inizioAppuntamento(viewModel.getInizioAppuntamento())
					.nominativo(clienteViewModelCreator.createModel(viewModel.getNominativo()))
					.operator(operatorViewModelCreator.createModel(viewModel.getOperator()))
					.pratica(praticaViewModelCreator.createModel(viewModel.getPratica())).titolo(viewModel.getTitolo())
					.operatoreAssegnato(operatorViewModelCreator.createModel(viewModel.getOperatorAssegnato()))
					.fineAppuntamento(viewModel.getFineAppuntamento())
					.invioSmsClienteNominativo(viewModel.isInvioSmsClienteNominativo())
					.invioSmsOperatoreAssegnato(viewModel.isInvioSmsOperatoreAssegnato()).build();
		}
		return null;
	}

	@Override
	public CalendarioAppuntamentoViewModel createViewModel(CalendarioAppuntamento model) {
		if (model != null) {
			return CalendarioAppuntamentoViewModel.builder().descrizione(model.getDescrizione()).id(model.getId())
					.inizioAppuntamento(model.getInizioAppuntamento())
					.nominativo(clienteViewModelCreator.createViewModel(model.getNominativo()))
					.operator(operatorViewModelCreator.createViewModel(model.getOperator()))
					.pratica(praticaViewModelCreator.createViewModel(model.getPratica())).titolo(model.getTitolo())
					.operatorAssegnato(operatorViewModelCreator.createViewModel(model.getOperatoreAssegnato()))
					.fineAppuntamento(model.getFineAppuntamento())
					.invioSmsClienteNominativo(model.isInvioSmsClienteNominativo())
					.invioSmsOperatoreAssegnato(model.isInvioSmsOperatoreAssegnato()).build();

		}
		return null;
	}

}
