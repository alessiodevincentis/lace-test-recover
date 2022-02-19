package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class ResetPasswordViewModel extends AbstractBaseViewModel {

	private long id;
    private OperatorViewModel operatorViewModel;
    private Date modification_data;
    private int duration_validity;
    private boolean first_access;

    @Override
    protected long getIdToCompare() {
        return id;
    }
    
}
