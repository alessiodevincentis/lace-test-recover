package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class NoticeBoardViewModel extends AbstractBaseViewModel {

    private long id;
    private String title;
    private String body;
    private LocalDateTime dateTimeCreation;
    private OperatorViewModel creatorOperator;
    private String fileNameAttached;
    private AziendaViewModel aziendaAssigned;
    private List<OperatorViewModel> operatorViewModelList;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
