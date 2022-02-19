package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.CampagnaViewModel;
import com.woonders.lacemscommon.db.entity.Campagna;

/**
 * Created by Emanuele on 29/03/2017.
 */
@Component
public class CampagnaViewModelCreator extends AbstractBaseViewModelCreator<Campagna, CampagnaViewModel> {

	@Autowired
	private OperatorViewModelCreator operatorViewModelCreator;

	@Override
	public Campagna createModel(CampagnaViewModel viewModel) {
		if (viewModel != null) {
			return Campagna.builder().id(viewModel.getId()).name(viewModel.getName())
					.description(viewModel.getDescription()).creationDateTime(viewModel.getCreationDateTime())
					.sentDateTime(viewModel.getSentDateTime()).text(viewModel.getText())
					.campagnaType(viewModel.getCampagnaType()).campagnaImport(viewModel.isCampagnaImport())
					.creatorOperator(operatorViewModelCreator.createModel(viewModel.getCreatorOperatorViewModel()))
					.senderOperator(operatorViewModelCreator.createModel(viewModel.getSenderOperatorViewModel()))
					.filtroCampagnaMarketingViewModel(viewModel.getFiltroCampagnaMarketingViewModel())
					.totaleDestinatari(viewModel.getTotaleDestinatari()).totaleMessaggi(viewModel.getTotaleMessaggi())
					.build();
		}
		return null;
	}

	@Override
	public CampagnaViewModel createViewModel(Campagna model) {
		if (model != null) {
			return CampagnaViewModel.builder().id(model.getId()).name(model.getName())
					.description(model.getDescription()).creationDateTime(model.getCreationDateTime())
					.sentDateTime(model.getSentDateTime()).text(model.getText()).campagnaType(model.getCampagnaType())
					.campagnaImport(model.isCampagnaImport())
					.creatorOperatorViewModel(operatorViewModelCreator.createViewModel(model.getCreatorOperator()))
					.senderOperatorViewModel(operatorViewModelCreator.createViewModel(model.getSenderOperator()))
					.filtroCampagnaMarketingViewModel(model.getFiltroCampagnaMarketingViewModel())
					.totaleDestinatari(model.getTotaleDestinatari()).totaleMessaggi(model.getTotaleMessaggi()).build();
		}
		return null;
	}
}
