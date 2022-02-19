package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.AccessLogViewModel;
import com.woonders.lacemscommon.db.entity.AccessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessLogViewModelCreator extends AbstractBaseViewModelCreator<AccessLog, AccessLogViewModel> {

    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;

    @Override
    public AccessLog createModel(AccessLogViewModel viewModel) {
        //NEVER USED
        return null;
    }

    @Override
    public AccessLogViewModel createViewModel(AccessLog model) {
        if (model != null) {
            return AccessLogViewModel.builder()
                    .id(model.getId())
                    .operator(operatorViewModelCreator.createViewModel(model.getOperator()))
                    .loginDateTime(model.getLoginDateTime())
                    .ipCode(model.getIpCode())
                    .build();
        }
        return null;
    }
}
