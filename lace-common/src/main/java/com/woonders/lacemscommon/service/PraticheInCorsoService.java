package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.PraticheInCorsoModel;

import java.util.List;

/**
 * Created by emanuele on 04/01/17.
 */

/**
 * Manages procedure still active
 */
public interface PraticheInCorsoService {

    /**
     * return list of procedure still active
     */
    List<PraticheInCorsoModel> getAltrePraticheInCorsoByClienteSenzaPraticaAttualmenteVisibile(final long idCliente,
                                                                                               final long idPratica);
}
