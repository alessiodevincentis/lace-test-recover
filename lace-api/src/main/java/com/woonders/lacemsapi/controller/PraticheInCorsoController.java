package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.PraticheInCorsoModel;
import com.woonders.lacemscommon.service.PraticheInCorsoService;
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
//@RequestMapping("/praticheincorso/*")
public class PraticheInCorsoController {

    @Autowired
    private PraticheInCorsoService praticheInCorsoService;

    @RequestMapping(value = {ControllerConstants.V1 + "/getAltrePraticheInCorsoByClienteSenzaPraticaAttualmenteVisibile"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PraticheInCorsoModel> getAltrePraticheInCorsoByClienteSenzaPraticaAttualmenteVisibile(final long idCliente, final long idPratica) {
        return praticheInCorsoService.getAltrePraticheInCorsoByClienteSenzaPraticaAttualmenteVisibile(idCliente, idPratica);
    }
}
