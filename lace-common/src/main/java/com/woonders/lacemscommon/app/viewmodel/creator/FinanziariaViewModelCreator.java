package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;
import com.woonders.lacemscommon.db.entity.Finanziaria;

/**
 * Created by emanuele on 09/01/17.
 */
@Component
public class FinanziariaViewModelCreator extends AbstractBaseViewModelCreator<Finanziaria, FinanziariaViewModel> {

	@Override
	public Finanziaria createModel(FinanziariaViewModel finanziariaViewModel) {
		if (finanziariaViewModel != null) {
			return Finanziaria.builder().id(finanziariaViewModel.getId()).name(finanziariaViewModel.getName()).build();
		}
		return null;
	}

	@Override
	public FinanziariaViewModel createViewModel(Finanziaria finanziaria) {
		if (finanziaria != null) {
			return FinanziariaViewModel.builder().id(finanziaria.getId()).name(finanziaria.getName()).build();
		}
		return null;
	}
}
