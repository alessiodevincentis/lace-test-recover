package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.CannotDeletePraticaException;

import java.util.List;

/**
 * Manages customers/leads/procedures deletion
 * [cliente, pratica] tables
 */
public interface DeleteClienteService {

    /**
     * Returns a customer by their tax id
     */
    ClienteViewModel findByCf(String cf);

    /**
     * Deletes a customer by their tax id
     */
    void deleteByCf(String tenantId, String cf);

    /**
     * Deletes all the procedures of a given customer by their tax id
     */
    List<ClientePratica> findPraticheToDeleteByCf(String cf, Role.RoleName clientiDeleteRoleName, long operatorId) throws CannotDeletePraticaException;

    /**
     * Deletes a procedure by its code
     */
    void deletePratica(String tenantId, long codicePratica);
}
