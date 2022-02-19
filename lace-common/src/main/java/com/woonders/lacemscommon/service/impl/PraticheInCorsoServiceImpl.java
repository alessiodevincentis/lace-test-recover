package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.model.PraticheInCorsoModel;
import com.woonders.lacemscommon.app.model.util.PraticheInCorsoModelUtil;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.service.PraticheInCorsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PraticheInCorsoServiceImpl implements PraticheInCorsoService {

    public static final String NAME = "praticheInCorsoServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private PraticheInCorsoModelUtil praticheInCorsoModelUtil;

    // TODO controllare se calcola la scadenza pratica
    @Override
    public List<PraticheInCorsoModel> getAltrePraticheInCorsoByClienteSenzaPraticaAttualmenteVisibile(
            final long idCliente, final long idPratica) {

        final Collection<String> statoPraticaStringCollection = new LinkedList<>();
        statoPraticaStringCollection.add(StatoPratica.ESTINTA.getValue());
        statoPraticaStringCollection.add(StatoPratica.ANNULATA.getValue());
        statoPraticaStringCollection.add(StatoPratica.RESPINTA.getValue());
        statoPraticaStringCollection.add(StatoPratica.NON_ISTRUITA.getValue());
        final List<Pratica> praticaList = praticaRepository
                .findDistinctByCliente_IdAndRinnovataFalseAndOrDecorrenzaNullOrScadenzaAndNotInStatoPratica(idCliente,
                        new Date(), statoPraticaStringCollection);

        rimuoviPraticaSelezionata(idPratica, praticaList);

        return praticheInCorsoModelUtil.createModel(praticaList);
    }

    private void rimuoviPraticaSelezionata(final long codicePraticaDaRimuovere, final List<Pratica> praticaList) {
        for (final Pratica pratica : praticaList) {
            if (pratica.getCodicePratica() == codicePraticaDaRimuovere) {
                praticaList.remove(pratica);
                break;
            }
        }
    }

}
