package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entityenum.AccountVersion;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class InfoAccountViewModel extends AbstractBaseViewModel {
    private long id;
    private AccountVersion version;
    private LocalDate expirationDate;
    private boolean simulator;
    private boolean multiagency;
    private boolean fileStorage;
    private int additionalUsers;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
