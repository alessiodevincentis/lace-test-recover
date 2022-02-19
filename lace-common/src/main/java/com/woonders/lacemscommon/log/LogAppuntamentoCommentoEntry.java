package com.woonders.lacemscommon.log;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Created by Emanuele on 16/04/2017. Usata per testare se l'ultimo appuntamento inserito nel log e lo stesso di quello che si vuole inserire,
 * quindi quando si apre modifica e si clicca salva senza aver modificato niente, in modo da non inserirlo.
 */
@Data
@EqualsAndHashCode
@Builder
public class LogAppuntamentoCommentoEntry {

    LocalDateTime dataOraInizioAppuntamento;
    LocalDateTime dataOraFineAppuntamento;
    String titolo;

}
