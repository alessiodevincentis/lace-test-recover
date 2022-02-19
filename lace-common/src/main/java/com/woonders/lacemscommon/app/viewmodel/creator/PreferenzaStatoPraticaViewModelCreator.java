package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.PreferenzaStatoPraticaViewModel;
import com.woonders.lacemscommon.db.entity.PreferenzaStatoPratica;

@Component
public class PreferenzaStatoPraticaViewModelCreator
		extends AbstractBaseViewModelCreator<PreferenzaStatoPratica, PreferenzaStatoPraticaViewModel> {

	@Autowired
	private AziendaViewModelCreator aziendaViewModelCreator;

	@Override
	public PreferenzaStatoPratica createModel(PreferenzaStatoPraticaViewModel viewModel) {
		if (viewModel != null) {
			PreferenzaStatoPratica preferenzaStatoPratica = new PreferenzaStatoPratica();
			preferenzaStatoPratica.setId(viewModel.getId());
			preferenzaStatoPratica.setNomeStatoPratica(viewModel.getNomeStatoPratica());
			preferenzaStatoPratica.setGiorniNotifica(viewModel.getGiorniNotifica());
			preferenzaStatoPratica.setAzienda(aziendaViewModelCreator.createModel(viewModel.getAziendaViewModel()));
			return preferenzaStatoPratica;
		}
		return null;
	}

	@Override
	public PreferenzaStatoPraticaViewModel createViewModel(PreferenzaStatoPratica model) {
		if (model != null) {
			PreferenzaStatoPraticaViewModel preferenzaStatoPraticaViewModel = new PreferenzaStatoPraticaViewModel();
			preferenzaStatoPraticaViewModel.setId(model.getId());
			preferenzaStatoPraticaViewModel.setNomeStatoPratica(model.getNomeStatoPratica());
			preferenzaStatoPraticaViewModel.setGiorniNotifica(model.getGiorniNotifica());
			preferenzaStatoPraticaViewModel
					.setAziendaViewModel(aziendaViewModelCreator.createViewModel(model.getAzienda()));
			return preferenzaStatoPraticaViewModel;
		}
		return null;
	}

}
