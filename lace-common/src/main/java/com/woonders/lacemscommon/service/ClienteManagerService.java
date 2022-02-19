package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.*;

import java.util.List;

/**
 * Created by emanuele on 05/01/17.
 * Manages customers
 * [cliente] table
 */
public interface ClienteManagerService extends AppService {

    /**
     * Save a new customer and a new procedure with all their details
     */
    ClientePratica saveNewClienteWithPratica(final ClienteViewModel clienteViewModel,
                                             final ContoViewModel contoViewModel, final DocumentoViewModel documentoViewModel,
                                             final ResidenzaViewModel residenzaViewModel, final List<TrattenuteViewModel> trattenuteViewModelList,
                                             final List<EstinzioneViewModel> estinzioneViewModelList, final PraticaViewModel praticaViewModel,
                                             final List<CoobbligatoViewModel> coobbligatoViewModelList,
                                             final List<AmministrazioneViewModel> amministrazioneViewModelList,
                                             final List<PreventivoViewModel> preventivoViewModelList, String commentoLog, long operatorId);

    /**
     * Returns a customer by their tax code
     */
    ClienteViewModel findClienteByCodiceFiscale(final String codiceFiscale);

    /**
     * Deletes a settled procedure by its id
     */
    void deleteEstinzione(final EstinzioneViewModel estinzioneViewModel);

    /**
     * Deletes a still active procedure created by another agency
     */
    void deleteTrattenuta(final TrattenuteViewModel trattenuteViewModel);

    /**
     * Deletes the alternative job of a customer
     */
    void deleteSecondaOccupazione(final long clienteId);

    /**
     * Sets a flag which indicates the anti money laundering file was uploaded
     */
    void setAntiriciclaggioFileCaricato(final long praticaId, final boolean antiriciclaggioFileCaricato);

    /**
     * Returns a procedure by its id
     */
    PraticaViewModel getPraticaById(final long praticaId);

    /**
     * Returns a list of files related to a customer by a customer id
     */
    List<ClientePraticaFileViewModel> findByCliente_Id(Long clienteId);

    /**
     * Returns a list of files related to a procedure by a procedure id
     */
    List<ClientePraticaFileViewModel> findByPratica_CodicePratica(Long codicePratica);

}
