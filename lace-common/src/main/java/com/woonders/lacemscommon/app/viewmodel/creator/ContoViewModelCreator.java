package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.ContoViewModel;
import com.woonders.lacemscommon.db.entity.Conto;

@Component
public class ContoViewModelCreator extends AbstractBaseViewModelCreator<Conto, ContoViewModel> {

	@Override
	public Conto createModel(ContoViewModel viewModel) {
		if (viewModel != null) {
			Conto conto = new Conto();
			conto.setBanca(viewModel.getBanca());
			conto.setCodiceConto(viewModel.getCodiceConto());
			conto.setDataConto(viewModel.getDataConto());
			conto.setIban(viewModel.getIban());
			conto.setSportello(viewModel.getSportello());
			conto.setStipendio(viewModel.isStipendio());
			return conto;
		}
		return null;
	}

	@Override
	public ContoViewModel createViewModel(Conto model) {
		if (model != null) {
			ContoViewModel contoViewModel = new ContoViewModel();
			contoViewModel.setBanca(model.getBanca());
			contoViewModel.setCodiceConto(model.getCodiceConto());
			contoViewModel.setDataConto(model.getDataConto());
			contoViewModel.setIban(model.getIban());
			contoViewModel.setSportello(model.getSportello());
			contoViewModel.setStipendio(model.isStipendio());
			return contoViewModel;
		}
		return null;
	}

}
