package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.ValutazioneAmministrazioneViewModel;

import java.util.List;

/**
 * Manages the reviews of amministrations (borrower employers, government organizations, public or private companies)
 * [valutazione_amministrazione] table
 */
public interface ValutazioneAmministrazioneService {

    void delete(long idValutazione);

    /**
     * Method used for searching [valutazione_amministrazione] entries by id [amministrazione] table
     */
    List<ValutazioneAmministrazioneViewModel> findDistinctByAmministrazione_CodiceAmm(long codiceAmm);

}
