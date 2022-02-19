package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.TimeParametersViewModel;
import com.woonders.lacemscommon.db.entity.TimeParameters;

@Component
public class TimeParametersViewModelCreator extends AbstractBaseViewModelCreator<TimeParameters, TimeParametersViewModel> {

	@Override
	public TimeParameters createModel(TimeParametersViewModel viewModel) {
		// TODO Auto-generated method stub
		if (viewModel != null) {
			return TimeParameters.builder()
					.id(viewModel.getId())
					.dataType(viewModel.getDataType())
					.valueParameter(viewModel.getValueParameter())
					.codeIdentify(viewModel.getCodeIdentify())
					.description(viewModel.getDescription())
					.build();
		}
		return null;
	}

	@Override
	public TimeParametersViewModel createViewModel(TimeParameters model) {
		// TODO Auto-generated method stub
		if (model != null) {
			return TimeParametersViewModel.builder()
					.id(model.getId())
					.dataType(model.getDataType())
					.valueParameter(model.getValueParameter())
					.codeIdentify(model.getCodeIdentify())
					.description(model.getDescription())
					.build();
		}
		return null;
	}

}
