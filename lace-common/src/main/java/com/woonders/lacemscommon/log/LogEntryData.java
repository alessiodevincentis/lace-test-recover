package com.woonders.lacemscommon.log;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Created by Emanuele on 08/02/2017. Usata per testare se l'ultima entry del
 * log uguale a quella che si vuole inserire
 */
@Data
@EqualsAndHashCode
@Builder
public class LogEntryData {

    private long operatoreAssegnatoId;
    private LocalDateTime dataRecall;
    private String stato;
}