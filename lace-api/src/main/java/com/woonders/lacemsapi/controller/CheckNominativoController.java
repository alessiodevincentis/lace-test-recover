package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.service.CheckNominativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by diegopizzo on 09/08/17.
 */
//@Controller
//@RequestMapping("/checknominativo/*")
public class CheckNominativoController {

    @Autowired
    private CheckNominativoService checkNominativoService;

    @RequestMapping(value = {ControllerConstants.V1 + "/findClienteByIdPratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClienteViewModel> findNominativoByCognomeAndNomeAndDataNascita(final String cognome, final String nome, final Date dataNascita) {
        return checkNominativoService.findNominativoByCognomeAndNomeAndDataNascita(cognome, nome, dataNascita);
    }
}
