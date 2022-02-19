package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.CoobbligatoViewModel;
import com.woonders.lacemscommon.service.CoobbligatoService;
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
//@RequestMapping("/coobbligato/*")
public class CoobbligatoController {

    @Autowired
    private CoobbligatoService coobbligatoService;

    @RequestMapping(value = {ControllerConstants.V1 + "/findDistinctByPratica_CodicePratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CoobbligatoViewModel> findDistinctByPratica_CodicePratica(final long codicePratica) {
        return coobbligatoService.findDistinctByPratica_CodicePratica(codicePratica);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/delete"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void delete(@RequestBody final CoobbligatoViewModel coobbligatoViewModel) {
        coobbligatoService.delete(coobbligatoViewModel);
    }
}
