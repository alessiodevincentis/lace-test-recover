package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.EstinzionePraticaCliente;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import com.woonders.lacemscommon.exception.UnableToUpdateException;

import java.util.Date;
import java.util.Map;

/**
 * Created by emanuele on 03/01/17.
 */

/**
 * Manages status of procedures
 */
public interface StatoPraticheService extends AppService {

    /**
     * return the number of procedures with specific status (stato pratica)
     */
    long countByStatoPraticaAndTipoClienteAndUsername(Long currentAziendaId, long currentOperatorId,
                                                      Role.RoleName clientiReadRoleName, String statoPratica, String tipoPratica);

    /**
     * return a paged list of customer details and procedure by a specific status
     */
    ViewModelPage<ClientePratica> findByStatoPraticaAndTipoClienteAndUsername(Long currentAziendaId,
                                                                              long currentOperatorId, Role.RoleName clientiReadRoleName, String statoPratica, String tipoPratica,
                                                                              int firstElementIndex, int pageSize, String sortField, QueryDSLHelper.SortOrder sortOrder,
                                                                              Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * update value of procedure status by id
     */
    PraticaViewModel updateStatoPraticaByCodicePratica(String statoPratica, long codicePratica)
            throws UnableToUpdateException;

    /**
     * update value of notification reminder by procedure id
     */
    PraticaViewModel updateDataNotificaByCodicePratica(Date dataNotifica, long codicePratica)
            throws UnableToUpdateException;

    /**
     * update value of settled procedure status by id
     */
    EstinzioneViewModel updateStatoConteggioEstinzioneByIdEstinzione(StatoConteggioEstinzione statoConteggioEstinzione, long idEstinzione)
            throws UnableToUpdateException;

    /**
     * update value of notification reminder date by settled procedure id
     */
    EstinzioneViewModel updateDataNotificaConteggioEstinzioneByIdEstinzione(Date dataNotifica, long idEstinzione)
            throws UnableToUpdateException;

    /**
     * TODO
     */
    boolean isContainsDatesToBeNotified(String statoPratica, Role.RoleName clientiReadRoleName, long operatorId, long currentAzienda, String tipoPratica);

    boolean isContainsDatesToBeNotified(Long currentAziendaId,
                                        long currentOperatorId, Role.RoleName clientiReadRoleName,
                                        StatoConteggioEstinzione statoConteggioEstinzione, String tipoEstinzione);

    /**
     * return customer details and procedure by id
     */
    ClientePratica getClientePratica(long id);

    /**
     * return number of settled procedure by their status
     */
    long countEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(Long currentAziendaId,
                                                                            long currentOperatorId, Role.RoleName clientiReadRoleName,
                                                                            StatoConteggioEstinzione statoConteggioEstinzione, String tipoEstinzione);

    /**
     * return list of settled procedure by their status
     */
    ViewModelPage<EstinzionePraticaCliente> findEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(Long currentAziendaId,
                                                                                                              long currentOperatorId, Role.RoleName clientiReadRoleName,
                                                                                                              StatoConteggioEstinzione statoConteggioEstinzione,
                                                                                                              String tipoEstinzione,
                                                                                                              int firstElementIndex, int pageSize, String sortField,
                                                                                                              QueryDSLHelper.SortOrder sortOrder,
                                                                                                              Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * return value of settled procedure by id
     */
    EstinzioneViewModel getEstinzione(long id);

    /**
     * return value of settled procedure, customer details and procedure by id
     */
    EstinzionePraticaCliente getEstinzionePraticaCliente(long id);
}
