package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.ResetPasswordViewModel;
import com.woonders.lacemscommon.db.entity.ResetPassword;

/**
 * Created by Cristian on 24/10/21.
 */
@Component
public class ResetPasswordViewModelCreator extends AbstractBaseViewModelCreator<ResetPassword, ResetPasswordViewModel> {

	@Autowired
    private OperatorViewModelCreator operatorViewModelCreator;
	
	@Override
	public ResetPassword createModel(ResetPasswordViewModel resetPasswordViewModel) {
		if (resetPasswordViewModel != null) {
            return ResetPassword.builder()
                    .operator(operatorViewModelCreator.createModel(resetPasswordViewModel.getOperatorViewModel()))
                    .modification_data(resetPasswordViewModel.getModification_data())
                    .duration_validity(resetPasswordViewModel.getDuration_validity())
                    .first_access(resetPasswordViewModel.isFirst_access())
                    .build();
        }
        return null;
	}

	@Override
	public ResetPasswordViewModel createViewModel(ResetPassword resetPassword) {
		if (resetPassword != null) {
			return ResetPasswordViewModel.builder()
						.operatorViewModel(operatorViewModelCreator.createViewModel(resetPassword.getOperator()))
						.modification_data(resetPassword.getModification_data())
						.duration_validity(resetPassword.getDuration_validity())
						.first_access(resetPassword.isFirst_access())
						.build();
		}
		return null;
		
	}

}
