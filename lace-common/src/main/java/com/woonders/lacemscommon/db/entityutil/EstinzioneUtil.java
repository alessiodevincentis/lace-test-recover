package com.woonders.lacemscommon.db.entityutil;

import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.db.entity.Estinzione;
import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import com.woonders.lacemscommon.laceenum.StringCalc;
import com.woonders.lacemscommon.util.DateToCalendar;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Emanuele on 02/02/2017.
 */
@Component
public class EstinzioneUtil {

    public static final String NAME = "estinzioneUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";

    public BigDecimal calcConteggioEstinzione(final Estinzione estinzione) {
        return calcConteggioEstinzione(estinzione.getScadenzaEstinzione(), estinzione.getRataEstinzione());
    }

    public BigDecimal calcConteggioEstinzione(final EstinzioneViewModel estinzioneViewModel) {
        return calcConteggioEstinzione(estinzioneViewModel.getScadenzaEstinzione(),
                estinzioneViewModel.getRataEstinzione());
    }

    private BigDecimal calcConteggioEstinzione(final Date scadenzaEstinzione, final BigDecimal rataEstinzione) {
        BigDecimal conteggioEstinzione = BigDecimal.ZERO;
        if (scadenzaEstinzione != null && rataEstinzione != null) {
            final int diff = DateToCalendar.diffeDateMonth(new Date(), scadenzaEstinzione);
            conteggioEstinzione = (rataEstinzione.multiply(new BigDecimal(diff)))
                    .subtract(rataEstinzione.multiply(new BigDecimal(diff)).multiply(BigDecimal.TEN)
                            .divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP));
        }
        return conteggioEstinzione.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // questo non e chiamato perche lo salviamo anche nel db...
    public Date calcScadenzaEstinzione(final Estinzione estinzione) {
        return calcScadenzaEstinzione(estinzione.getScadenzaEstinzione(), estinzione.getDataBustapaga(),
                estinzione.getCalcolaScadenza(), estinzione.getRataEstinzione(), estinzione.getMontanteEstinzione());
    }

    public Date calcScadenzaEstinzione(final EstinzioneViewModel estinzioneViewModel) {
        return calcScadenzaEstinzione(estinzioneViewModel.getScadenzaEstinzione(),
                estinzioneViewModel.getDataBustapaga(), estinzioneViewModel.getCalcolaScadenza(),
                estinzioneViewModel.getRataEstinzione(), estinzioneViewModel.getMontanteEstinzione());
    }

    // TODO molto simile a TrattenutaUtil::calcScadenzaTrat
    private Date calcScadenzaEstinzione(final Date scadenzaEstinzione, final Date dataBustaPaga, final String calcolaScadenza,
                                        final BigDecimal rataEstinzione, final BigDecimal montanteEstinzione) {
        if (StringCalc.RATE.getValue().equalsIgnoreCase(calcolaScadenza) && rataEstinzione != null
                && !rataEstinzione.equals(BigDecimal.ZERO) && montanteEstinzione != null
                && !montanteEstinzione.equals(BigDecimal.ZERO) && dataBustaPaga != null) {
            final Calendar date = DateToCalendar.dateToCalendar(dataBustaPaga);
            // TODO ho messo round down perche prima dividendo gli int anche se
            // veniva 7.8 il risultato diventava 7. Ma deve essere cosi??
            final int rateScadenza = montanteEstinzione.divide(rataEstinzione, BigDecimal.ROUND_DOWN).intValue();
            date.add(Calendar.MONTH, rateScadenza);
            return date.getTime();
        } else if (StringCalc.DATA.getValue().equalsIgnoreCase(calcolaScadenza)) {
            return scadenzaEstinzione;
        }
        return null;
    }

    public int calcMesiScadenza(final Estinzione estinzione) {
        return calcMesiScadenza(estinzione.getRataEstinzione(), estinzione.getScadenzaEstinzione());
    }

    public int calcMesiScadenza(final EstinzioneViewModel estinzioneViewModel) {
        return calcMesiScadenza(estinzioneViewModel.getRataEstinzione(), estinzioneViewModel.getScadenzaEstinzione());
    }

    // TODO mi sfugge a che serve la rata
    private int calcMesiScadenza(final BigDecimal rataEstinzione, final Date scadenzaEstinzione) {
        if (rataEstinzione != null && !rataEstinzione.equals(BigDecimal.ZERO) && scadenzaEstinzione != null) {
            return DateToCalendar.diffeDateMonth(new Date(), scadenzaEstinzione);
        }
        return 0;
    }

    public Date getDataNotificaConteggioEstinzione(final StatoConteggioEstinzione statoConteggioEstinzione) {

        if (statoConteggioEstinzione != null) {
            switch (statoConteggioEstinzione) {
                case RICHIESTA_CONTEGGIO:
                    return null;
                case ATTESA_CONTEGGIO:
                    //10 giorni è il tempo di attesa massimo del conteggio
                    return DateToCalendar.addDays(new Date(), 10);
                case CONTEGGIO_RICEVUTO:
                    return DateToCalendar.fineMese(new Date(), 0);
            }
        }
        return null;
    }

}
