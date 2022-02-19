package com.woonders.lacemscommon.app.model.util;

import com.woonders.lacemscommon.app.model.PraticheInCorsoModel;
import com.woonders.lacemscommon.db.entity.Pratica;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class PraticheInCorsoModelUtil {

    public List<PraticheInCorsoModel> createModel(final List<Pratica> praticheInCorsoList) {
        final List<PraticheInCorsoModel> praticheInCorsoViewModelList = new LinkedList<>();
        for (final Pratica praticheInCorso : praticheInCorsoList) {
            final PraticheInCorsoModel praticheInCorsoModel = new PraticheInCorsoModel();
            praticheInCorsoModel.setTipoPratica(praticheInCorso.getTipoPratica());
            praticheInCorsoModel.setRata(praticheInCorso.getRata());
            praticheInCorsoModel.setDurata(praticheInCorso.getDurata());
            praticheInCorsoViewModelList.add(praticheInCorsoModel);
        }
        return praticheInCorsoViewModelList;
    }

}
