package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableDetailsViewModel;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.exception.EmptyJobTypeListException;
import com.woonders.lacemscommon.exception.EmptyTanException;
import com.woonders.lacemscommon.exception.SimulatorTableInUseException;

import java.util.List;
import java.util.Map;

/**
 * Created by ema98 on 8/10/2017.
 */

/**
 * Manages simulator tables for create quote
 * [simulator_table]
 */
public interface SimulatorTableService {

    /**
     * return a paged list of simulator tables created
     */
    ViewModelPage<SimulatorTableViewModel> findAll(Long aziendaId, long operatorId, Role.RoleName simulatorReadRoleName, int firstElementIndex, int pageSize,
                                                   String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * return a simulator table by id
     */
    SimulatorTableViewModel findOne(long simulatorTableId);

    /**
     * return a list of simulator table by id operator assigned
     */
    List<SimulatorTableViewModel> findAssigned(long operatorId, Pratica.TipoPratica simulatorTableType, List<Impiego> jobTypeList);

    /**
     * return details of simulator table
     */
    List<SimulatorTableDetailsViewModel> getDetails(long simulatorTableId);

    SimulatorTableViewModel save(long creatorOperatorId, long creatorOperatorAziendaId, SimulatorTableViewModel simulatorTableViewModel, List<SimulatorTableDetailsViewModel> simulatorTableDetailsViewModelList) throws EmptyTanException, EmptyJobTypeListException;

    void delete(long simulatorTableId) throws SimulatorTableInUseException;

    /**
     * return a boolean for enable/disable simulator table
     */
    boolean enable(long simulatorTableId, boolean enabled);

    /**
     * Used to clone a SimulatorTable. Returns the id of the newly created table to be edited
     */
    long clone(long simulatorTableId);

    /**
     * check if a specific table is used by some operator
     */
    boolean isTableUsedSomewhere(long simulatorTableId);
}
