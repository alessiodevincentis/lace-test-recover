package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.db.entity.Estinzione;
import com.woonders.lacemscommon.db.entityutil.EstinzioneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by emanuele on 09/01/17.
 */
@Component
public class EstinzioneViewModelCreator extends AbstractBaseViewModelCreator<Estinzione, EstinzioneViewModel> {

    @Autowired
    private EstinzioneUtil estinzioneUtil;

    @Override
    public Estinzione createModel(final EstinzioneViewModel estinzioneViewModel) {
        if (estinzioneViewModel != null) {

            return Estinzione.builder().calcolaScadenza(estinzioneViewModel.getCalcolaScadenza())
                    .dataBustapaga(estinzioneViewModel.getDataBustapaga())
                    .durataEstinzione(estinzioneViewModel.getDurataEstinzione())
                    .idEstinzione(estinzioneViewModel.getIdEstinzione())
                    .istitutoEst(estinzioneViewModel.getIstitutoEst())
                    .montanteEstinzione(estinzioneViewModel.getMontanteEstinzione())
                    .rataEstinzione(estinzioneViewModel.getRataEstinzione())
                    .scadenzaEstinzione(estinzioneViewModel.getScadenzaEstinzione())
                    .tipoEstinzione(estinzioneViewModel.getTipoEstinzione())
                    .dataRinnovoEstinzione(estinzioneViewModel.getDataRinnovoEstinzione())
                    .dataNotificaConteggioEstinzione(estinzioneViewModel.getDataNotificaConteggioEstinzione())
                    .statoConteggioEstinzione(estinzioneViewModel.getStatoConteggioEstinzione()).build();
        }

        return null;
    }

    @Override
    public EstinzioneViewModel createViewModel(final Estinzione estinzione) {
        if (estinzione != null) {

            return EstinzioneViewModel.builder().calcolaScadenza(estinzione.getCalcolaScadenza())
                    .conteggioEstinzione(estinzioneUtil.calcConteggioEstinzione(estinzione))
                    .dataBustapaga(estinzione.getDataBustapaga()).durataEstinzione(estinzione.getDurataEstinzione())
                    .idEstinzione(estinzione.getIdEstinzione()).istitutoEst(estinzione.getIstitutoEst())
                    .mesiScadenza(estinzioneUtil.calcMesiScadenza(estinzione))
                    .montanteEstinzione(estinzione.getMontanteEstinzione())
                    .rataEstinzione(estinzione.getRataEstinzione())
                    .scadenzaEstinzione(estinzione.getScadenzaEstinzione())
                    .tipoEstinzione(estinzione.getTipoEstinzione())
                    .dataRinnovoEstinzione(estinzione.getDataRinnovoEstinzione())
                    .dataNotificaConteggioEstinzione(estinzione.getDataNotificaConteggioEstinzione())
                    .statoConteggioEstinzione(estinzione.getStatoConteggioEstinzione()).build();
        }
        return null;
    }
}
