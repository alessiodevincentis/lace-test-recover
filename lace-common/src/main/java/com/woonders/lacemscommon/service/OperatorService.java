package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder;
import com.woonders.lacemscommon.db.QueryDSLHelper.TableField;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by emanuele on 23/12/16.
 */

/**
 * Manages operators that can handle customers and procedures
 * [operator] table
 */
public interface OperatorService extends UserDetailsService {

    /**
     * return list of operators by agency id
     */
    List<OperatorViewModel> findAppOperatorListByAziendaId(Long aziendaId);

    /**
     * return all operators
     */
    List<OperatorViewModel> findAllAppOperator();

    /**
     * update details of operator
     */
    void changeData(final String nome, final String cognome, final String mail, final String telefono,
                    final long currentOperatorId) throws UnableToUpdateException;
    
    
    /**
     * update mail address of operator by username
     */
    String mail(final String user);

    /**
     * return list of mail address of operators
     */
    List<String> mailList();

    /**
     * return operator by username
     */
    OperatorViewModel findByUsernameViewModel(final String user);

    void save(Operator loginOperator) throws UnableToSaveException;

    void save(OperatorViewModel operatorViewModel) throws UnableToSaveException;

    void delete(Operator loginOperator);
    
    void delete(OperatorViewModel operatorViewModel) throws UnableToDeleteException;

    /**
     * change password of operator
     */
    void changePassword(final String oldPassword, final String newPassword, final String confirmNewPassword,
                        final long currentOperatorId)
            throws CannotChangePasswordException, InvalidCurrentPasswordException, ConfirmPasswordMismatchException, CannotChangePasswordException, InvalidCurrentPasswordException;

    /**
     * change password of operator
     */
    String newPasswordEncoded(final String newPassword, final String confirmNewPassword)
            throws CannotChangePasswordException, InvalidCurrentPasswordException, ConfirmPasswordMismatchException, CannotChangePasswordException, InvalidCurrentPasswordException;

    
    /**
     * return list of operators by a specific role name
     */
    List<OperatorViewModel> findByRoleName(final Role.RoleName roleName);

    /**
     * return list of operators by list of role name and agency id
     */
    List<OperatorViewModel> findByRoleNameInAndAzienda_Id(Collection<Role.RoleName> roleNameCollection, Long aziendaId);

    /**
     * return operator by username
     */
    OperatorViewModel findOperatorViewModelByUsername(String user) throws UnableToFindException, UnableToFindException;

    /**
     * return operator by id
     */
    OperatorViewModel getOne(long id);

    /**
     * return a paged list of operators
     */
    ViewModelPage<OperatorViewModel> findAll(int firstElementIndex, int pageSize, String sortField, SortOrder sortOrder,
                                             Map<TableField, Object> filters, long currentOperatorId, boolean isGestionePermessiWrite, Long currentAziendaId, boolean isGestionePermessiWriteSuper);

    /**
     * return list of operators that can receive third party lead
     */
    List<OperatorViewModel> getAllReceiveLeadEnabledOperatorByAziendaId(long aziendaId);

    /**
     * update list of operators that can receive third party lead
     */
    void updateReceiveLeadEnabledOperatorsByAziendaId(List<Long> selectedReceiveLeadOperatorIdsList, long aziendaId);
}
