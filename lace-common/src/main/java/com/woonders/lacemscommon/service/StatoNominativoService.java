package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;

import java.util.Map;

/**
 * Manages the status of leads
 */
public interface StatoNominativoService {

    /**
     * return the number of leads by a specific status
     */
    long countNominativiByStatoNominativo(Long currentAziendaId, long currentOperatorId,
                                          Role.RoleName nominativiReadRoleName, String statoNominativo);

    /**
     * return a paged list of leads details by a specific status
     */
    ViewModelPage<ClienteViewModel> getNominativiByStatoNominativo(Long currentAziendaId, long currentOperatorId,
                                                                   Role.RoleName nominativiReadRoleName, String statoNominativo, int firstElementIndex, int pageSize,
                                                                   String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * return a boolean util for checking if the reminder date for calling appointment is expired
     */
    boolean isContainsDataRecallNominativoLessToday(final String statoNominativo, final Role.RoleName nominativiReadRoleName, final long currentOperatorId, Long currentAziendaId);
}
