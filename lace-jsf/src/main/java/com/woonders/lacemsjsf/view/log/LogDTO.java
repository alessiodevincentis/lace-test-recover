package com.woonders.lacemsjsf.view.log;

import com.woonders.lacemscommon.service.LogService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 02/05/2017.
 */
@Getter
@Setter
@Builder
public class LogDTO {

    private long nominativoId;
    private long praticaId;
    private LogService.TipoLog tipoLog;

}
