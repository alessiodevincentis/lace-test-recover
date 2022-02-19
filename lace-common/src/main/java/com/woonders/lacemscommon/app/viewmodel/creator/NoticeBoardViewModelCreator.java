package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.NoticeBoardViewModel;
import com.woonders.lacemscommon.db.entity.NoticeBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoticeBoardViewModelCreator extends AbstractBaseViewModelCreator<NoticeBoard, NoticeBoardViewModel> {

    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;
    @Autowired
    private AziendaViewModelCreator aziendaViewModelCreator;

    @Override
    public NoticeBoard createModel(final NoticeBoardViewModel viewModel) {
        if (viewModel != null) {
            return NoticeBoard.builder()
                    .id(viewModel.getId())
                    .body(viewModel.getBody())
                    .creatorOperator(operatorViewModelCreator.createModel(viewModel.getCreatorOperator()))
                    .dateTimeCreation(viewModel.getDateTimeCreation())
                    .title(viewModel.getTitle())
                    .fileNameAttached(viewModel.getFileNameAttached())
                    .aziendaAssigned(aziendaViewModelCreator.createModel(viewModel.getAziendaAssigned()))
                    .operatorSet(operatorViewModelCreator.fromList(viewModel.getOperatorViewModelList())).build();
        }
        return null;
    }

    @Override
    public NoticeBoardViewModel createViewModel(final NoticeBoard model) {
        if (model != null) {
            return NoticeBoardViewModel.builder()
                    .id(model.getId())
                    .body(model.getBody())
                    .creatorOperator(operatorViewModelCreator.createViewModel(model.getCreatorOperator()))
                    .dateTimeCreation(model.getDateTimeCreation())
                    .title(model.getTitle())
                    .fileNameAttached(model.getFileNameAttached())
                    .aziendaAssigned(aziendaViewModelCreator.createViewModel(model.getAziendaAssigned()))
                    .operatorViewModelList(operatorViewModelCreator.fromSet(model.getOperatorSet())).build();
        }
        return null;
    }
}
