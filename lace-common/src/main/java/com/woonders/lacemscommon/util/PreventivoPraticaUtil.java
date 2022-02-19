package com.woonders.lacemscommon.util;

import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityutil.TrattenutaUtil;
import com.woonders.lacemscommon.laceenum.StringCalc;
import com.woonders.lacemscommon.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by emanuele on 10/01/17.
 */
@Component
public class PreventivoPraticaUtil {

    @Autowired
    private OperatorService operatorService;
    @Autowired
    private TrattenutaUtil trattenutaUtil;

    public PraticaViewModel setPraticaDaPreventivo(final PreventivoViewModel preventivo, final long operatorId) {
        final PraticaViewModel pratica = new PraticaViewModel();
        pratica.setTipoPratica(preventivo.getTipoPratica());
        pratica.setRata(preventivo.getRata());
        pratica.setDurata(preventivo.getDurata());
        pratica.setImportoErogato(preventivo.getImporto());
        pratica.setTan(preventivo.getTan());
        pratica.setTaeg(preventivo.getTaeg());
        pratica.setTeg(preventivo.getTeg());
        pratica.setTipoProvvigione(preventivo.getTipoProvvigione());
        pratica.setProvvigione(preventivo.getProvvigione());
        pratica.setPercProv(preventivo.getPercProvvigione());
        pratica.setDataCaricamento(new Date());
        pratica.setSimulatorTableViewModel(preventivo.getSimulatorTableViewModel());
        pratica.setSpese(preventivo.getSpese());
        pratica.setInteressi(preventivo.getInteressi());
        //TODO WHY?
        pratica.getDataRinnovo();
        pratica.setStatoPratica(StatoPratica.PREISTRUTTORIA.getValue());
        pratica.setOperatore(operatorService.getOne(operatorId));
        return pratica;
    }

    public List<EstinzioneViewModel> setEstinzioneDaPratica(final List<PraticaViewModel> praticheDaEstinguere) {
        final List<EstinzioneViewModel> estinzioneList = new ArrayList<>();
        if (praticheDaEstinguere != null)
            for (final PraticaViewModel pratica : praticheDaEstinguere) {
                final EstinzioneViewModel estinzione = new EstinzioneViewModel();
                estinzione.setTipoEstinzione(pratica.getTipoPratica());
                estinzione.setRataEstinzione(pratica.getRata());
                estinzione.setDurataEstinzione(pratica.getDurata());
                estinzione.setCalcolaScadenza(StringCalc.DATA.getValue());
                if (pratica.getFinanziariaViewModel() != null && !StringUtils.isEmpty(pratica.getFinanziariaViewModel().getName())) {
                    estinzione.setIstitutoEst(pratica.getFinanziariaViewModel().getName());
                }
                if (pratica.getScadenzaPratica() != null) {
                    estinzione.setScadenzaEstinzione(pratica.getScadenzaPratica());
                    estinzione.setStatoConteggioEstinzione(StatoConteggioEstinzione.RICHIESTA_CONTEGGIO);
                    estinzione.setDataRinnovoEstinzione(trattenutaUtil.calcDataRinnovoTrat(pratica.getScadenzaPratica(),
                            pratica.getDurata(), pratica.getTipoPratica()));
                    estinzione.setDataNotificaConteggioEstinzione(null);
                }
                estinzioneList.add(estinzione);
            }
        return estinzioneList;
    }

    public List<EstinzioneViewModel> setEstinzioneDaTrattenuta(
            final List<TrattenuteViewModel> coesistenzeDaEstinguere) {
        final List<EstinzioneViewModel> estinzioneList = new ArrayList<>();
        if (coesistenzeDaEstinguere != null)
            for (final TrattenuteViewModel coesistenza : coesistenzeDaEstinguere) {
                final EstinzioneViewModel estinzione = new EstinzioneViewModel();
                estinzione.setTipoEstinzione(coesistenza.getTipoTrat());
                estinzione.setRataEstinzione(coesistenza.getRataTrat());
                estinzione.setDurataEstinzione(coesistenza.getDurataTrat());
                estinzione.setCalcolaScadenza(StringCalc.DATA.getValue());
                estinzione.setIstitutoEst(coesistenza.getIstitutoTrat());
                if (coesistenza.getScadenzaTrat() != null) {
                    estinzione.setScadenzaEstinzione(coesistenza.getScadenzaTrat());
                    estinzione.setStatoConteggioEstinzione(StatoConteggioEstinzione.RICHIESTA_CONTEGGIO);
                    estinzione.setDataRinnovoEstinzione(trattenutaUtil.calcDataRinnovoTrat(coesistenza.getScadenzaTrat(),
                            coesistenza.getDurataTrat(), coesistenza.getTipoTrat()));
                    estinzione.setDataNotificaConteggioEstinzione(null);
                }
                estinzioneList.add(estinzione);
            }
        return estinzioneList;
    }

}
