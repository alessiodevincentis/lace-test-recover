package com.woonders.lacemsjsf.view.estinzione;

import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.db.entity.Trattenute;
import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import com.woonders.lacemscommon.db.entityutil.EstinzioneUtil;
import com.woonders.lacemscommon.db.entityutil.TrattenutaUtil;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Date;

import static com.woonders.lacemsjsf.view.estinzione.EstinzioneView.NAME;

/**
 * Created by Emanuele on 03/02/2017.
 */
@Setter
@ViewScoped
@ManagedBean(name = NAME)
public class EstinzioneView implements Serializable {

    public static final String NAME = "estinzioneView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @ManagedProperty(EstinzioneUtil.JSF_NAME)
    private EstinzioneUtil estinzioneUtil;
    @ManagedProperty(TrattenutaUtil.JSF_NAME)
    private TrattenutaUtil trattenutaUtil;

    public void updateConteggioEstinzione(final EstinzioneViewModel estinzioneViewModel) {
        estinzioneViewModel.setConteggioEstinzione(estinzioneUtil.calcConteggioEstinzione(estinzioneViewModel));
    }

    public void updateMesiScadenza(final EstinzioneViewModel estinzioneViewModel) {
        estinzioneViewModel.setMesiScadenza(estinzioneUtil.calcMesiScadenza(estinzioneViewModel));
    }

    public void updateScadenzaEstinzione(final EstinzioneViewModel estinzioneViewModel) {
        estinzioneViewModel.setScadenzaEstinzione(estinzioneUtil.calcScadenzaEstinzione(estinzioneViewModel));
    }

    //Qua uso trattenuta util per non ripetere il calcolo del rinnovo ma tocca mettere sti calcoli in una clase astratta PD
    public void updateRinnovoEstinzione(final EstinzioneViewModel estinzioneViewModel) {
        estinzioneViewModel.setDataRinnovoEstinzione(trattenutaUtil.calcDataRinnovoTrat(estinzioneViewModel.getScadenzaEstinzione(),
                estinzioneViewModel.getDurataEstinzione(), estinzioneViewModel.getTipoEstinzione()));
    }

    public void updateScadenzaAndMesiEstinzioneFromMontante(final EstinzioneViewModel estinzioneViewModel) {
        updateScadenzaEstinzione(estinzioneViewModel);
        updateMesiScadenza(estinzioneViewModel);
    }

    public boolean isDataNotificaConteggioDisabled(final Date dataRinnovoEstinzione,
                                                   final StatoConteggioEstinzione statoConteggioEstinzione,
                                                   final String tipoEstinzione) {

        if (Trattenute.TipoImpegno.PRESTITO.getValue().equalsIgnoreCase(tipoEstinzione)) {
            return !(!StringUtils.isEmpty(tipoEstinzione) && statoConteggioEstinzione != null &&
                    !StatoConteggioEstinzione.RICHIESTA_CONTEGGIO.equals(statoConteggioEstinzione));
        } else {
            return !(!StringUtils.isEmpty(tipoEstinzione) && dataRinnovoEstinzione != null && statoConteggioEstinzione != null
                    && !StatoConteggioEstinzione.RICHIESTA_CONTEGGIO.equals(statoConteggioEstinzione)
                    && !Trattenute.TipoImpegno.PIGNORAMENTO.getValue().equalsIgnoreCase(tipoEstinzione));
        }
    }

    public boolean isStatoConteggioDisabled(final Date dataRinnovoEstinzione, final String tipoEstinzione) {

        if (Trattenute.TipoImpegno.PRESTITO.getValue().equalsIgnoreCase(tipoEstinzione)) {
            return StringUtils.isEmpty(tipoEstinzione);
        } else {
            return !(!StringUtils.isEmpty(tipoEstinzione) && dataRinnovoEstinzione != null
                    && !Trattenute.TipoImpegno.PIGNORAMENTO.getValue().equalsIgnoreCase(tipoEstinzione));
        }
    }

    public void setDataNotificaConteggioEstinzione(final EstinzioneViewModel estinzioneViewModel) {
        estinzioneViewModel.setDataNotificaConteggioEstinzione(estinzioneUtil.getDataNotificaConteggioEstinzione(estinzioneViewModel.getStatoConteggioEstinzione()));
    }

}
