package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.service.FileService;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Emanuele on 16/11/2016.
 */
@Data
@Builder
public class FileViewModel {

    private String tenantId;
    private String clientePraticaId;
    private FileService.FileCategory fileCategory;
    private String fileName;
}
