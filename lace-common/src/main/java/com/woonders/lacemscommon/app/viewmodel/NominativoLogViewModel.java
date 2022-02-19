package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.service.LogService;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by Emanuele on 08/02/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class NominativoLogViewModel extends AbstractBaseViewModel {

    private long id;
    private LocalDateTime executionDateTime;
    private OperatorViewModel operator;
    private ClienteViewModel nominativo;
    private OperatorViewModel operatoreAssegnato;
    private LocalDateTime dataRecall;
    private String commento;
    private String shortCommento;
    private PraticaViewModel pratica;
    private LogService.TipoLog tipoLog;
    private String stato;

    @Override
    protected long getIdToCompare() {
        return id;
    }
}
