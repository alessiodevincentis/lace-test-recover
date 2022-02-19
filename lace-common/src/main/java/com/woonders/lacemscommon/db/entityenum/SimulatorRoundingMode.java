package com.woonders.lacemscommon.db.entityenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.RoundingMode;

/**
 * Created by ema98 on 8/10/2017.
 */
@AllArgsConstructor
@Getter
public enum SimulatorRoundingMode {

    CLASSIC(RoundingMode.HALF_UP, "Classico"), UP(RoundingMode.UP, "Sempre eccesso"), DOWN(RoundingMode.DOWN, "Sempre difetto");

    private final RoundingMode roundingMode;
    private final String value;


}
