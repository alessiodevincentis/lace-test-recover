package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ClienteTrattenuta;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;

import java.util.Date;
import java.util.Map;

/**
 * Created by emanuele on 06/01/17.
 * Manages the details of the leads dashboard
 */
public interface DashboardNominativoService extends AppService {

    /**
     * Returns a paged list of the leads who should be called in a given [date]
     */
    ViewModelPage<ClienteViewModel> getNominativiInDataRecall(Long currentAziendaId, long currentOperatorId,
                                                              Role.RoleName nominativiReadRoleName, final Date date, int firstElementIndex, int pageSize,
                                                              String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * Counts how many leads who should be called in a given [date]
     */
    long countNominativiInDataRecall(Long currentAziendaId, long currentOperatorId,
                                     Role.RoleName nominativiReadRoleName, final Date date);

    /**
     * Returns a paged list of all the another agency procedures available to be renewed
     */
    ViewModelPage<ClienteTrattenuta> findImpegniRinnovabili(Long currentAziendaId, long currentOperatorId,
                                                            Role.RoleName nominativiReadRoleName, final int days, int firstElementIndex, int pageSize, String sortField,
                                                            QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * Counts how many another agency procedures are available to be renewed
     */
    long countImpegniRinnovabili(Long currentAziendaId, long currentOperatorId, Role.RoleName nominativiReadRoleName,
                                 final int days);

    /**
     * TODO
     */
    void setRinnovoTrattenute(final TrattenuteViewModel trattenuteViewModel);

    /**
     * Counts how many new leads who are yet not processed
     */
    long countNewLead(Long currentAziendaId, long currentOperatorId, Role.RoleName nominativiReadRoleName);

    /**
     * Returns a lead details by their id
     */
    ClienteViewModel getNominativo(long id);

    /**
     * Returns an another agency procedure details by its id
     */
    ClienteTrattenuta getTrattenuta(long id);

    /**
     * Counts how many not called lead whose recall date is past the current date
     */
    long countNominativiOmessi(Long currentAziendaId, long currentOperatorId,
                               Role.RoleName nominativiReadRoleName);

    /**
     * Returns a paged list of all leads who have a recall date which is past the current date
     */
    ViewModelPage<ClienteViewModel> getNominativiWithDataRecallExpired(final Long currentAziendaId, final long currentOperatorId,
                                                                       final Role.RoleName nominativiReadRoleName, final int firstElementIndex,
                                                                       final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder,
                                                                       final Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * Removes the recall date from a lead
     */
    void setNullDataRecallNominativo(long id);
}
