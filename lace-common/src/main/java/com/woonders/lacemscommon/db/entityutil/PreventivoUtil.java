package com.woonders.lacemscommon.db.entityutil;

import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.db.entity.Preventivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Emanuele on 04/02/2017.
 */
@Component
public class PreventivoUtil {

    public static final String NAME = "preventivoUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";
    @Autowired
    private CalcoliUtil calcoliUtil;
    @Autowired
    private PraticaUtil praticaUtil;

    public BigDecimal calcPercentualeProvvigione(final PraticaViewModel preventivo) {
        return calcPercentualeProvvigione(preventivo.getTipoProvvigione(), preventivo.getProvvigione(),
                preventivo.getDurata(), preventivo.getRata());
    }

    public BigDecimal calcPercentualeProvvigione(final PreventivoViewModel preventivo) {
        return calcPercentualeProvvigione(preventivo.getTipoProvvigione(), preventivo.getProvvigione(),
                preventivo.getDurata(), preventivo.getRata());
    }

    private BigDecimal calcPercentualeProvvigione(final String tipoProvvigione, final BigDecimal provvigione, final Integer durata,
                                                  final BigDecimal rata) {
        return calcoliUtil.calcPercentualeProvvigione(tipoProvvigione, provvigione, durata, rata);
    }

    public BigDecimal calcProvvigioneTotale(final PraticaViewModel preventivo) {
        return calcProvvigioneTotale(preventivo.getTipoProvvigione(), preventivo.getPercProv(), preventivo.getDurata(),
                preventivo.getRata(), preventivo.getProvvigione(), preventivo.getImportoErogato(), preventivo.getSpese(),
                preventivo.getTipoPratica());
    }

    public BigDecimal calcProvvigioneTotale(final Preventivo preventivo) {
        return calcProvvigioneTotale(preventivo.getTipoProvvigione(), preventivo.getPercProvvigione(),
                preventivo.getDurata(), preventivo.getRata(), preventivo.getProvvigione(), preventivo.getImporto(),
                preventivo.getSpese(), preventivo.getTipoPratica());
    }

    public BigDecimal calcProvvigioneTotale(final PreventivoViewModel preventivo) {
        return calcProvvigioneTotale(preventivo.getTipoProvvigione(), preventivo.getPercProvvigione(),
                preventivo.getDurata(), preventivo.getRata(), preventivo.getProvvigione(), preventivo.getImporto(),
                preventivo.getSpese(), preventivo.getTipoPratica());
    }

    private BigDecimal calcProvvigioneTotale(final String tipoProvvigione, final BigDecimal percProvvigione, final Integer durata,
                                             final BigDecimal rata, final BigDecimal provvigione, final BigDecimal importoErogato, final BigDecimal spese, final String tipoPratica) {
        return calcoliUtil.calcProvvigioneTotale(tipoProvvigione, percProvvigione, durata, rata, provvigione, importoErogato, spese, tipoPratica);
    }

    public BigDecimal calcInteressi(final PreventivoViewModel preventivoViewModel) {
        return praticaUtil.calcInteressi(preventivoViewModel);
    }

    public BigDecimal calcInteressi(final Preventivo preventivo) {
        return praticaUtil.calcInteressi(preventivo);
    }
}
