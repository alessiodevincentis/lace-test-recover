package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.BackupViewModel;
import com.woonders.lacemscommon.db.entity.Backup;
import org.springframework.stereotype.Component;

@Component
public class BackupViewModelCreator extends AbstractBaseViewModelCreator<Backup, BackupViewModel> {

    @Override
    public Backup createModel(final BackupViewModel viewModel) {
        if (viewModel != null) {
            Backup.builder().id(viewModel.getId())
                    .dateLastBackup(viewModel.getDateLastBackup())
                    .passwordBackup(viewModel.getPasswordBackup()).build();
        }
        return null;
    }

    @Override
    public BackupViewModel createViewModel(final Backup model) {
        if (model != null) {
            BackupViewModel.builder().id(model.getId())
                    .dateLastBackup(model.getDateLastBackup())
                    .passwordBackup(model.getPasswordBackup()).build();
        }
        return null;
    }
}
