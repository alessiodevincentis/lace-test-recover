package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.exception.UnableToDeleteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emanuele on 05/01/17.
 * Manages the leads information
 * [cliente] table
 */
public interface NominativoService extends AppService {

    /**
     * Saves a new lead
     */
    ClienteViewModel saveNominativo(final ClienteViewModel nominativoViewModel,
                                    final ResidenzaViewModel residenzaViewModel, final List<PraticaViewModel> preventivoViewModelList,
                                    final List<TrattenuteViewModel> trattenuteNominativoViewModelList,
                                    final List<AmministrazioneViewModel> amministrazioneViewModelList, final String commentoLog, long operatorId);

    /**
     * Deletes a quote from a lead
     */
    void deletePreventivo(final long preventivoId);

    /**
     * Deletes an another agency procedure from a lead
     */
    void deleteTrattenuta(final TrattenuteViewModel trattenuteViewModel);

    /**
     * Creates a new procedure for a lead who accepted a given quote
     */
    void creaPraticaDaPreventivoNominativo(final ClienteViewModel clienteViewModel,
                                           ResidenzaViewModel residenzaViewModel, final List<TrattenuteViewModel> trattenuteViewModelList,
                                           final List<TrattenuteViewModel> impegniSelezionati, final List<PraticaViewModel> preventivoViewModelList,
                                           final PraticaViewModel preventivoDaCreare, long operatorId, long aziendaId);

    /**
     * Deletes a lead given their id
     */
    void delete(final ClienteViewModel nominativoViewModel) throws UnableToDeleteException;

    /**
     * Retrieves info regarding the sms which was sent when the lead was acquired
     */
    NotificaLeadSmsViewModel getNotificaLeadSms(long nominativoId);

    /**
     * Update the lead operator
     */
    void updateNominativiOperator(final ArrayList<ClienteViewModel> nominativoViewModelList, long operatorId);
    
    /**
     * return list of provice
     */
    List<String> getAllProvince();
}
