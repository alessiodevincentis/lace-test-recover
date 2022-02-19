package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.service.AziendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by ema98 on 8/8/2017.
 */
//@Controller
//@RequestMapping("/azienda/*")
public class AziendaController {

    @Autowired
    private AziendaService aziendaService;

    //TODO non funziona il save di nuove aziende
    @RequestMapping(value = {ControllerConstants.V1 + "/save"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AziendaViewModel save(@RequestBody final AziendaViewModel aziendaViewModel) {
        return aziendaService.save(aziendaViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findAll"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AziendaViewModel> findAll() {
        return aziendaService.findAll();
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getOne"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AziendaViewModel getOne(final long id) {
        return aziendaService.getOne(id);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/count"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long count() {
        return aziendaService.count();
    }
}
