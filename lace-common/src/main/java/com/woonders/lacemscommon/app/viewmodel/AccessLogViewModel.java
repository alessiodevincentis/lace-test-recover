package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class AccessLogViewModel extends AbstractBaseViewModel {

    private long id;
    private OperatorViewModel operator;
    private LocalDateTime loginDateTime;
    private String ipCode;
    private String note;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
