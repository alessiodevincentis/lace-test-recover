package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.*;
import com.woonders.lacemscommon.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by ema98 on 8/9/2017.
 */
//@Controller
//@RequestMapping("/operator/*")
public class OperatorController {

    @Autowired
    private OperatorService operatorService;

    @RequestMapping(value = {ControllerConstants.V1 + "/findAppOperatorListByAziendaId"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<OperatorViewModel> findAppOperatorListByAziendaId(final Long aziendaId) {
        return operatorService.findAppOperatorListByAziendaId(aziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findAllAppOperator"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<OperatorViewModel> findAllAppOperator() {
        return operatorService.findAllAppOperator();
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/changeData"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void changeData(final String nome, final String cognome, final String mail, final String telefono, final long currentOperatorId) throws UnableToUpdateException {
        operatorService.changeData(nome, cognome, mail, telefono, currentOperatorId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/mail"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String mail(final String user) {
        return operatorService.mail(user);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/mailList"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> mailList() {
        return operatorService.mailList();
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findByUsernameViewModel"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperatorViewModel findByUsernameViewModel(final String user) {
        return operatorService.findByUsernameViewModel(user);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/save"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void save(Operator loginOperator) throws UnableToSaveException {
        operatorService.save(loginOperator);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/save"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void save(final OperatorViewModel operatorViewModel) throws UnableToSaveException {
        operatorService.save(operatorViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/delete"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void delete(final Operator loginOperator) {
        operatorService.delete(loginOperator);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/changePassword"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void changePassword(final String oldPassword, final String newPassword, final String confirmNewPassword, final long currentOperatorId) throws CannotChangePasswordException, InvalidCurrentPasswordException, ConfirmPasswordMismatchException, CannotChangePasswordException, InvalidCurrentPasswordException {
        operatorService.changePassword(oldPassword, newPassword, confirmNewPassword, currentOperatorId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findByRoleName"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<OperatorViewModel> findByRoleName(final Role.RoleName roleName) {
        return operatorService.findByRoleName(roleName);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findByRoleNameInAndAzienda_Id"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<OperatorViewModel> findByRoleNameInAndAzienda_Id(final Collection<Role.RoleName> roleNameCollection, final Long aziendaId) {
        return operatorService.findByRoleNameInAndAzienda_Id(roleNameCollection, aziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findOperatorViewModelByUsername"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperatorViewModel findOperatorViewModelByUsername(final String user) throws UnableToFindException, UnableToFindException {
        return operatorService.findOperatorViewModelByUsername(user);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getOne"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperatorViewModel getOne(final long id) {
        return operatorService.getOne(id);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findAll"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<OperatorViewModel> findAll(final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters, final long currentOperatorId, final boolean isGestionePermessiWrite, final Long currentAziendaId, final boolean isGestionePermessiWriteSuper) {
        return operatorService.findAll(firstElementIndex, pageSize, sortField, sortOrder, filters, currentOperatorId, isGestionePermessiWrite, currentAziendaId, isGestionePermessiWriteSuper);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getAllReceiveLeadEnabledOperatorByAziendaId"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<OperatorViewModel> getAllReceiveLeadEnabledOperatorByAziendaId(final long aziendaId) {
        return operatorService.getAllReceiveLeadEnabledOperatorByAziendaId(aziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/updateReceiveLeadEnabledOperatorsByAziendaId"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateReceiveLeadEnabledOperatorsByAziendaId(final List<Long> selectedReceiveLeadOperatorIdsList, final long aziendaId) {
        operatorService.updateReceiveLeadEnabledOperatorsByAziendaId(selectedReceiveLeadOperatorIdsList, aziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/loadUserByUsername"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {
        return operatorService.loadUserByUsername(s);
    }
}
