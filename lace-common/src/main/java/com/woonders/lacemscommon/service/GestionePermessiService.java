package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.exception.IllegalPermissionStateException;
import com.woonders.lacemscommon.laceenum.PermissionProfile;

/**
 * Created by Emanuele on 10/05/2017.
 * Manages operators permissions
 * [operator_role] table
 */
public interface GestionePermessiService {

    /**
     * Saves new permissions for a given operator with their new profile permissions
     * @throws IllegalPermissionStateException
     */
    void saveNewPermission(OperatorViewModel operatorViewModel, PermissionProfile permissionProfile) throws IllegalPermissionStateException;
}
