package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.PreferenzaStatoPraticaViewModel;
import com.woonders.lacemscommon.service.PreferenzaStatoPraticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by ema98 on 8/9/2017.
 */
//@Controller
//@RequestMapping("/preferenzastatopratica/*")
public class PreferenzaStatoPraticaController {

    @Autowired
    private PreferenzaStatoPraticaService preferenzaStatoPraticaService;

    @RequestMapping(value = {ControllerConstants.V1 + "/getAllPreferenzaStatoPraticaByAziendaId"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PreferenzaStatoPraticaViewModel> getAllPreferenzaStatoPraticaByAziendaId(final long aziendaId) {
        return preferenzaStatoPraticaService.getAllPreferenzaStatoPraticaByAziendaId(aziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/save"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PreferenzaStatoPraticaViewModel> save(final List<PreferenzaStatoPraticaViewModel> preferenzaStatoPraticaViewModelList) {
        return preferenzaStatoPraticaService.save(preferenzaStatoPraticaViewModelList);
    }
}
