package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.PreferenzaStatoPraticaViewModel;

import java.util.List;

/**
 * Manages logic useful to know how many days a procedure could be in it before operators should be warned (preferences)
 * [preferenza_stato_pratica] table
 */
public interface PreferenzaStatoPraticaService {

    /**
     * return list of preferences for procedure status by agency id
     */
    List<PreferenzaStatoPraticaViewModel> getAllPreferenzaStatoPraticaByAziendaId(long aziendaId);


    List<PreferenzaStatoPraticaViewModel> save(
            List<PreferenzaStatoPraticaViewModel> preferenzaStatoPraticaViewModelList);
}
