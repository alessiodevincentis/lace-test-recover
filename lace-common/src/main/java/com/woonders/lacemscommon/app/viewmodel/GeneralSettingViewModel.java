package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

/**
 * Created by ema98 on 9/21/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class GeneralSettingViewModel extends AbstractBaseViewModel {

    private long id;
    private boolean storageEnabled;
    private long usedStorageSpace;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
