package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;

import java.util.Date;
import java.util.Map;

/**
 * Manages the anti money laundering data
 */
public interface AntiriciclaggioService {

    /**
     * Returns a list of customers and procedures to be used to show a minimum set of data when needed to get info for anti money laundering
     */
    ViewModelPage<ClientePratica> findPraticheAntiriciclaggio(String cf, Date dataInizio, Date dataFine,
                                                              TipoRicercaAntiriciclaggio tipoRicercaAntiriciclaggio, int firstElementIndex, int pageSize,
                                                              String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters,
                                                              Long currentAziendaId, Role.RoleName antiriciclaggioReadRoleName, long operatorId);

    enum TipoRicercaAntiriciclaggio {
        CF, DATA
    }

}
