package com.woonders.lacemscommon.db.entityutil;

import com.google.common.collect.Lists;
import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.db.entity.Estinzione;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Preventivo;
import com.woonders.lacemscommon.util.DateToCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Emanuele on 02/02/2017.
 */
@Component
public class PraticaUtil {

    public static final String NAME = "praticaUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private EstinzioneUtil estinzioneUtil;
    @Autowired
    private CalcoliUtil calcoliUtil;

    public BigDecimal calcNettoRicavo(final BigDecimal importoErogato, final BigDecimal conteggioEstinzioneTotale) {
        if (importoErogato != null && conteggioEstinzioneTotale != null) {
            return (importoErogato.subtract(conteggioEstinzioneTotale)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    // TODO questi sono duplicati, cazzo
    public BigDecimal calcConteggioEstinzioneTotale(final PraticaViewModel praticaViewModel,
                                                    final List<EstinzioneViewModel> estinzioneViewModelList) {
        // praticaViewModel non e usato, non serve, non toglierlo altrimenti i
        // metodi si incastrano, e la sparo!
        return calcConteggioEstinzioneTotale(estinzioneViewModelList);
    }

    // TODO questi sono duplicati, cazzo
    private BigDecimal calcConteggioEstinzioneTotale(final List<EstinzioneViewModel> estinzioneViewModelList) {
        if (estinzioneViewModelList != null) {
            return estinzioneViewModelList.stream()
                    .map(estinzioneViewModel -> estinzioneUtil.calcConteggioEstinzione(estinzioneViewModel))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    // TODO questi sono duplicati, cazzo
    private BigDecimal calcConteggioEstinzioneTotale(final Set<Estinzione> estinzioneSet) {
        if (estinzioneSet != null) {
            return estinzioneSet.stream().map(estinzione -> estinzioneUtil.calcConteggioEstinzione(estinzione))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    // TODO questi sono duplicati, cazzo
    public BigDecimal calcConteggioEstinzioneTotale(final Pratica pratica) {
        return calcConteggioEstinzioneTotale(pratica.getEstinzione());
    }

    public BigDecimal calcNettoRicavo(final PraticaViewModel praticaViewModel,
                                      final List<EstinzioneViewModel> estinzioneViewModelList) {
        return calcNettoRicavo(praticaViewModel.getImportoErogato(), estinzioneViewModelList);
    }

    public BigDecimal calcNettoRicavo(final Pratica pratica) {
        return calcNettoRicavo(pratica.getImportoErogato(), pratica.getEstinzione());
    }

    // TODO questi sono duplicati, cazzo
    private BigDecimal calcNettoRicavo(final BigDecimal importoErogato, final Set<Estinzione> estinzioneSet) {
        if (importoErogato != null && estinzioneSet != null) {
            return importoErogato.subtract(calcConteggioEstinzioneTotale(estinzioneSet)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    // TODO questi sono duplicati, cazzo
    private BigDecimal calcNettoRicavo(final BigDecimal importoErogato, final List<EstinzioneViewModel> estinzioneViewModelList) {
        if (importoErogato != null && estinzioneViewModelList != null) {
            return importoErogato.subtract(calcConteggioEstinzioneTotale(estinzioneViewModelList)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calcNettoMensile(final PraticaViewModel praticaViewModel) {
        return calcNettoMensile(praticaViewModel.getQuintoMax());
    }

    public BigDecimal calcNettoMensile(final Pratica pratica) {
        return calcNettoMensile(pratica.getQuintoMax());
    }

    public BigDecimal calcNettoMensile(final BigDecimal quintoMax) {
        // TODO perche moltiplichiamo per 5??
        if (quintoMax != null) {
            return quintoMax.multiply(new BigDecimal("5")).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calcInteressi(final Pratica pratica) {
        return calcInteressi(pratica.getSpese(), pratica.getImportoErogato(), pratica.getRata(), pratica.getDurata(),
                pratica.getCapitaleFinanziato());
    }

    public BigDecimal calcInteressi(final PreventivoViewModel preventivo) {
        return calcInteressi(preventivo.getSpese(), preventivo.getImporto(), preventivo.getRata(), preventivo.getDurata(),
                null);
    }

    public BigDecimal calcInteressi(final Preventivo preventivo) {
        return calcInteressi(preventivo.getSpese(), preventivo.getImporto(), preventivo.getRata(), preventivo.getDurata(),
                null);
    }

    public BigDecimal calcInteressi(final PraticaViewModel praticaViewModel) {
        BigDecimal capitaleFinanziatoBd = null;
        if (praticaViewModel.getCapitaleFinanziato() != null && !praticaViewModel.getCapitaleFinanziato().isEmpty()) {
            capitaleFinanziatoBd = new BigDecimal(praticaViewModel.getCapitaleFinanziato());
        }
        return calcInteressi(praticaViewModel.getSpese(), praticaViewModel.getImportoErogato(),
                praticaViewModel.getRata(), praticaViewModel.getDurata(), capitaleFinanziatoBd);
    }

    private BigDecimal calcInteressi(final BigDecimal spese, final BigDecimal importoErogato, final BigDecimal rata, final Integer durata,
                                     final BigDecimal capitaleFinanziato) {

        if (capitaleFinanziato != null && rata != null && durata != null && capitaleFinanziato != BigDecimal.ZERO) {
            return ((calcMontante(rata, durata).subtract(capitaleFinanziato))).setScale(2, BigDecimal.ROUND_HALF_UP);

        } else if (spese != null && importoErogato != null && rata != null && durata != null) {
            return ((calcMontante(rata, durata).subtract(spese)).subtract(importoErogato)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calcProvTotale(final PraticaViewModel praticaViewModel) {
        return calcProvTotale(praticaViewModel.getProvvigione(), praticaViewModel.getPercProv(),
                praticaViewModel.getRata(), praticaViewModel.getDurata(), praticaViewModel.getTipoProvvigione(), praticaViewModel.getImportoErogato(),
                praticaViewModel.getSpese(), praticaViewModel.getTipoPratica());
    }

    public BigDecimal calcProvTotale(final Pratica pratica) {
        return calcProvTotale(pratica.getProvvigione(), pratica.getPercProv(), pratica.getRata(), pratica.getDurata(),
                pratica.getTipoProvvigione(), pratica.getImportoErogato(), pratica.getSpese(), pratica.getTipoPratica());
    }

    private BigDecimal calcProvTotale(final BigDecimal provvigione, final BigDecimal percProv, final BigDecimal rata, final Integer durata,
                                      final String tipoProvvigione, final BigDecimal importoErogato, final BigDecimal spese, final String tipoPratica) {
        return calcoliUtil.calcProvvigioneTotale(tipoProvvigione, percProv, durata, rata, provvigione, importoErogato, spese, tipoPratica);
    }

    public BigDecimal calcPercProv(final Pratica pratica) {
        return calcPercProv(pratica.getProvvigione(), pratica.getRata(), pratica.getDurata(),
                pratica.getTipoProvvigione());
    }

    public BigDecimal calcPercProv(final PraticaViewModel praticaViewModel) {
        return calcPercProv(praticaViewModel.getProvvigione(), praticaViewModel.getRata(), praticaViewModel.getDurata(),
                praticaViewModel.getTipoProvvigione());
    }

    public BigDecimal calcPercProv(final BigDecimal provvigione, final BigDecimal rata, final Integer durata, final String tipoProvvigione) {
        return calcoliUtil.calcPercentualeProvvigione(tipoProvvigione, provvigione, durata, rata);
    }

    public Date calcDataRinnovo(final PraticaViewModel praticaViewModel) {
        return calcDataRinnovo(praticaViewModel.getDecorrenza(), praticaViewModel.getDurata(),
                praticaViewModel.getTipoPratica());
    }

    public Date calcDataRinnovo(final Pratica pratica) {
        return calcDataRinnovo(pratica.getDecorrenza(), pratica.getDurata(), pratica.getTipoPratica());
    }

    private Date calcDataRinnovo(final Date decorrenza, final Integer durata, final String tipoPratica) {
        if (decorrenza != null && durata != null && durata != 0) {
            if (!Pratica.TipoPratica.PRESTITO.getValue().equalsIgnoreCase(tipoPratica)) {
                final BigDecimal annirinnovo = (new BigDecimal(durata).multiply(new BigDecimal("0.4")))
                        .setScale(0, RoundingMode.CEILING).subtract(new BigDecimal("1"));
                final Calendar cal = DateToCalendar.dateToCalendar(decorrenza);
                cal.add(Calendar.MONTH, annirinnovo.intValue());
                cal.set(Calendar.DAY_OF_MONTH, 1);
                return cal.getTime();
            }
        }
        return null;
    }

    public BigDecimal calcMontante(final PraticaViewModel praticaViewModel) {
        return calcMontante(praticaViewModel.getRata(), praticaViewModel.getDurata());
    }

    public BigDecimal calcMontante(final Pratica pratica) {
        return calcMontante(pratica.getRata(), pratica.getDurata());
    }

    private BigDecimal calcMontante(final BigDecimal rata, final Integer durata) {
        if (rata != null && durata != null) {
            return rata.multiply(new BigDecimal(durata)).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    // non usato perche lo salviamo nel db
    public Date calcScadenzaPratica(final Pratica pratica) {
        return calcScadenzaPratica(pratica.getDecorrenza(), pratica.getDurata());
    }

    public Date calcScadenzaPratica(final PraticaViewModel praticaViewModel) {
        return calcScadenzaPratica(praticaViewModel.getDecorrenza(), praticaViewModel.getDurata());
    }

    /*
     * Viene messo il meno uno perchè con la data di decorrenza la prima rata e'
     * gia pagata quindi la scadenza e le rate a scadere iniziano già con un
     * mese/rata in meno
     */
    private Date calcScadenzaPratica(final Date decorrenza, final Integer durata) {
        if (decorrenza != null && durata != null && durata != 0) {
            return DateToCalendar.fineMese(DateToCalendar.setMonth(decorrenza, durata), -1);
        }
        return null;
    }

    public int calcRateScadenza(final Pratica pratica) {
        return calcRateScadenza(pratica.getDecorrenza(), pratica.getDurata());
    }

    public int calcRateScadenza(final PraticaViewModel praticaViewModel) {
        return calcRateScadenza(praticaViewModel.getDecorrenza(), praticaViewModel.getDurata());
    }

    private int calcRateScadenza(final Date decorrenza, final Integer durata) {
        if (decorrenza != null && durata != null) {
            final Integer rateScadenza = durata - (DateToCalendar.diffeDateMonth(decorrenza, new Date())) - 1;
            if (rateScadenza < 0) {
                return 0;
            } else if (rateScadenza > durata) {
                return durata;
            } else {
                return rateScadenza;
            }
        }
        return 0;
    }

    public BigDecimal calcDebitoResiduo(final Pratica pratica) {
        return calcDebitoResiduo(pratica.getRata(), calcRateScadenza(pratica));
    }

    public BigDecimal calcDebitoResiduo(final PraticaViewModel praticaViewModel) {
        return calcDebitoResiduo(praticaViewModel.getRata(), praticaViewModel.getRateScadenza());
    }

    private BigDecimal calcDebitoResiduo(final BigDecimal rata, final int rateScadenza) {
        if (rata != null) {
            final BigDecimal dieciPercento = BigDecimal.TEN.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
            final BigDecimal debitoTotale = rata.multiply(new BigDecimal(rateScadenza));
            final BigDecimal percentuale = debitoTotale.multiply(dieciPercento);
            return debitoTotale.subtract(percentuale).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal sumMontante(final Iterable<Pratica> praticaList) {
        if (praticaList != null) {
            return Lists.newArrayList(praticaList).stream().map(this::calcMontante).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    // I love lambda expressions XD
    public BigDecimal sumProvvigione(final Iterable<Pratica> praticaList) {
        if (praticaList != null) {
            return Lists.newArrayList(praticaList).stream().map(this::calcProvTotale).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal sumErogato(final Iterable<Pratica> praticaList) {
        BigDecimal totaleErogato = BigDecimal.ZERO;
        if (praticaList != null) {
            for (final Pratica pratica : praticaList) {
                if (pratica.getImportoErogato() != null) {
                    totaleErogato = totaleErogato.add(pratica.getImportoErogato()).setScale(2, RoundingMode.HALF_UP);
                }
            }
        }
        return totaleErogato;
    }
}
