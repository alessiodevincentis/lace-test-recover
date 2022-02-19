package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;
import com.woonders.lacemscommon.service.FinanziariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by diegopizzo on 09/08/17.
 */
//@Controller
//@RequestMapping("/finanziaria/*")
public class FinanziariaController {

    @Autowired
    private FinanziariaService finanziariaService;

    @RequestMapping(value = {ControllerConstants.V1 + "/findall"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FinanziariaViewModel> findAll() {
        return finanziariaService.findAll();
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getone"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public FinanziariaViewModel getOne(final Long id) {
        return finanziariaService.getOne(id);
    }
}
