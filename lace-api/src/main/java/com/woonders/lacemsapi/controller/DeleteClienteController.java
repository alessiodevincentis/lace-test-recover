package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.CannotDeletePraticaException;
import com.woonders.lacemscommon.service.DeleteClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by diegopizzo on 09/08/17.
 */

//@Controller
//@RequestMapping("/deletecliente/*")
public class DeleteClienteController {

    @Autowired
    private DeleteClienteService deleteClienteService;

    @RequestMapping(value = {ControllerConstants.V1 + "/findbycf"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel findByCf(final String cf) {
        return deleteClienteService.findByCf(cf);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/deletebycf"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteByCf(@RequestBody final String cf) {
        deleteClienteService.deleteByCf(null, cf);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findpratichetodeletebycf"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> findPraticheToDeleteByCf(final String cf, final Role.RoleName clientiDeleteRoleName,
                                                         final long operatorId) throws CannotDeletePraticaException {
        return deleteClienteService.findPraticheToDeleteByCf(cf, clientiDeleteRoleName, operatorId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/deletepratica"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deletePratica(@RequestBody final long codicePratica) {
        deleteClienteService.deletePratica(null, codicePratica);
    }
}
