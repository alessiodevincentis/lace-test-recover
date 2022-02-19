package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.NominativoLogViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Pratica;

import java.util.Map;

/**
 * Created by Emanuele on 08/02/2017.
 * Manages logs for customers/leads/procedures
 * [nominativo_log] table
 */
public interface LogService {

    /**
     * Logs an operation done on a customer/lead
     */
    NominativoLogViewModel log(Cliente nominativoSalvato, String commento, long operatorId);

    /**
     * Returns a paged list of all the operations done on a given customer/lead by their id
     */
    ViewModelPage<NominativoLogViewModel> getNominativoLog(long nominativoId, int firstElementIndex, int pageSize,
                                                           String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * Logs an operation done on a procedure
     */
    NominativoLogViewModel log(Pratica praticaSalvata, String commento, long operatorId);

    /**
     * Returns a paged list of all the operations done on a given procedure by their id
     */
    ViewModelPage<NominativoLogViewModel> getPraticaLog(long nominativoId, long praticaId, int firstElementIndex,
                                                        int pageSize, String sortField, QueryDSLHelper.SortOrder sortOrder,
                                                        Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * Adds a custom log to a given customer/lead or procedure given their id
     */
    void logCustomAction(long idClienteOrPratica, TipoLog tipoLog, String testo, CustomAction customAction, long operatorId);

    enum TipoLog {
        NOMINATIVO, PRATICA
    }

    enum CustomAction {
        EMAIL, SMS, RECEIVED_SMS, INVIO_CONTESTAZIONE, RICEZ_CONTESTAZIONE_OK, RICEZ_CONTESTAZIONE_KO
    }
}
