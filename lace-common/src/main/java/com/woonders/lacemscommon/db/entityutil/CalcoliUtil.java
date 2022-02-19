package com.woonders.lacemscommon.db.entityutil;

import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.laceenum.StringCalc;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Emanuele on 07/02/2017.
 */
@Component
public class CalcoliUtil {

    public BigDecimal calcPercentualeProvvigione(final String tipoProvvigione, final BigDecimal provvigione, final Integer durata,
                                                 final BigDecimal rata) {
        if (tipoProvvigione != null && provvigione != null && durata != null && rata != null) {
            if (StringCalc.EURO.getValue().equalsIgnoreCase(tipoProvvigione)) {
                final BigDecimal percProvParte1 = new BigDecimal(100);
                final BigDecimal percProvParte2 = rata.multiply(new BigDecimal(durata));
                if (!BigDecimal.ZERO.equals(percProvParte2)) {
                    return percProvParte1.divide(percProvParte2, 10, RoundingMode.HALF_UP).multiply(provvigione)
                            .setScale(6, RoundingMode.HALF_UP);
                }
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    // servono sia la provvigione che la provvigione percentuale perche poi
    // decide quale ritornare in base al tipo di calcolo
    public BigDecimal calcProvvigioneTotale(final String tipoProvvigione, final BigDecimal percProvvigione, final Integer durata,
                                            final BigDecimal rata, final BigDecimal provvigione, final BigDecimal importoErogato, final BigDecimal spese, final String tipoPratica) {


        if (!StringUtils.isEmpty(tipoPratica)) {
            if (StringCalc.PERC.getValue().equalsIgnoreCase(tipoProvvigione)) {

                if (!Pratica.TipoPratica.PRESTITO.getValue().equalsIgnoreCase(tipoPratica)) {
                    if (tipoProvvigione != null && percProvvigione != null && durata != null && rata != null) {
                        final BigDecimal provTotalPart1 = rata.multiply(new BigDecimal(durata));
                        final BigDecimal provTotalPart2 = provTotalPart1
                                .divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP).setScale(6, RoundingMode.HALF_UP);
                        return provTotalPart2.multiply(percProvvigione).setScale(2, RoundingMode.HALF_UP);
                    }
                } else if (spese != null && percProvvigione != null && durata != null && rata != null && importoErogato != null) {
                    final BigDecimal montante = rata.multiply(new BigDecimal(durata));
                    final BigDecimal interessi = montante.subtract(spese).subtract(importoErogato);
                    final BigDecimal provTotalPart2 = interessi
                            .divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP).setScale(6, RoundingMode.HALF_UP);
                    return provTotalPart2.multiply(percProvvigione).setScale(2, RoundingMode.HALF_UP);
                }
            } else if (StringCalc.EURO.getValue().equalsIgnoreCase(tipoProvvigione) && provvigione != null) {
                return provvigione;
            }
        }
        return BigDecimal.ZERO;

    }
}
