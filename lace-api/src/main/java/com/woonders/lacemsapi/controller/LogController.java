package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.NominativoLogViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by ema98 on 8/9/2017.
 */
//@Controller
//@RequestMapping("/log/*")
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping(value = {ControllerConstants.V1 + "/logNominativo"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NominativoLogViewModel logNominativo(final Cliente nominativoSalvato, final String commento, final long operatorId) {
        return logService.log(nominativoSalvato, commento, operatorId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getNominativoLog"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<NominativoLogViewModel> getNominativoLog(final long nominativoId, final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters) {
        return logService.getNominativoLog(nominativoId, firstElementIndex, pageSize, sortField, sortOrder, filters);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/logPratica"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NominativoLogViewModel logPratica(final Pratica praticaSalvata, final String commento, final long operatorId) {
        return logService.log(praticaSalvata, commento, operatorId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getPraticaLog"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<NominativoLogViewModel> getPraticaLog(final long nominativoId, final long praticaId, final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters) {
        return logService.getPraticaLog(nominativoId, praticaId, firstElementIndex, pageSize, sortField, sortOrder, filters);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/logCustomAction"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void logCustomAction(final long idClienteOrPratica, final LogService.TipoLog tipoLog, final String testo, final LogService.CustomAction customAction, final long operatorId) {
        logService.logCustomAction(idClienteOrPratica, tipoLog, testo, customAction, operatorId);
    }
}
