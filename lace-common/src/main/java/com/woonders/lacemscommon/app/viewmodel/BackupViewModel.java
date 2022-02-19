package com.woonders.lacemscommon.app.viewmodel;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class BackupViewModel extends AbstractBaseViewModel {

    private Long id;
    private LocalDateTime dateLastBackup;
    private String passwordBackup;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
