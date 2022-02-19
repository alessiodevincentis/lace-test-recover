package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.InfoAccountViewModel;
import com.woonders.lacemscommon.db.entity.InfoAccount;
import org.springframework.stereotype.Component;

@Component
public class InfoAccountViewModelCreator extends AbstractBaseViewModelCreator<InfoAccount, InfoAccountViewModel> {
    @Override
    public InfoAccount createModel(final InfoAccountViewModel viewModel) {
        if (viewModel != null) {
            return InfoAccount.builder().id(viewModel.getId())
                    .version(viewModel.getVersion())
                    .expirationDate(viewModel.getExpirationDate())
                    .simulator(viewModel.isSimulator())
                    .multiagency(viewModel.isMultiagency())
                    .fileStorage(viewModel.isFileStorage())
                    .additionalUsers(viewModel.getAdditionalUsers()).build();
        }
        return null;
    }

    @Override
    public InfoAccountViewModel createViewModel(final InfoAccount model) {
        if (model != null) {
            return InfoAccountViewModel.builder().id(model.getId())
                    .expirationDate(model.getExpirationDate())
                    .version(model.getVersion())
                    .simulator(model.isSimulator())
                    .multiagency(model.isMultiagency())
                    .fileStorage(model.isFileStorage())
                    .additionalUsers(model.getAdditionalUsers()).build();
        }
        return null;
    }
}
