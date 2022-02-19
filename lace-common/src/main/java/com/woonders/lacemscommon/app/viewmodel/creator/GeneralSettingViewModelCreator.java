package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.GeneralSettingViewModel;
import com.woonders.lacemscommon.db.entity.GeneralSetting;
import org.springframework.stereotype.Component;

/**
 * Created by ema98 on 9/21/2017.
 */
@Component
public class GeneralSettingViewModelCreator extends AbstractBaseViewModelCreator<GeneralSetting, GeneralSettingViewModel> {

    @Override
    public GeneralSetting createModel(GeneralSettingViewModel viewModel) {
        if (viewModel != null) {
            return GeneralSetting.builder()
                    .id(viewModel.getId())
                    .storageEnabled(viewModel.isStorageEnabled())
                    .usedStorageSpace(viewModel.getUsedStorageSpace())
                    .build();
        }
        return null;
    }

    @Override
    public GeneralSettingViewModel createViewModel(GeneralSetting model) {
        if (model != null) {
            return GeneralSettingViewModel.builder()
                    .id(model.getId())
                    .storageEnabled(model.isStorageEnabled())
                    .usedStorageSpace(model.getUsedStorageSpace())
                    .build();
        }
        return null;
    }
}
