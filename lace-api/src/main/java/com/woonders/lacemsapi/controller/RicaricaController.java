package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.RicaricaComunicazioneViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entityenum.RicaricaType;
import com.woonders.lacemscommon.exception.DatiFatturazioneToBeCompletedException;
import com.woonders.lacemscommon.exception.FatturaNonDisponibileException;
import com.woonders.lacemscommon.exception.PaymentException;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;
import com.woonders.lacemscommon.service.RicaricaService;
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
//@RequestMapping("/ricarica/*")
public class RicaricaController {

    @Autowired
    private RicaricaService ricaricaService;

    @RequestMapping(value = {ControllerConstants.V1 + "/makePayment"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void makePayment(final long payerOperatorId, final String tokenId, final String currency, final Articolo.TipoArticolo tipoArticolo, final RicaricaType ricaricaType, final OperatorViewModel operatoreRicaricato, final long currentAziendaId) throws PaymentException, DatiFatturazioneToBeCompletedException {
        ricaricaService.makePayment(payerOperatorId, tokenId, currency, tipoArticolo, ricaricaType, operatoreRicaricato, currentAziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getTopUpList"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<RicaricaComunicazioneViewModel> getTopUpList(final Long currentAziendaId, final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters) {
        return ricaricaService.getTopUpList(currentAziendaId, firstElementIndex, pageSize, sortField, sortOrder, filters);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findFatturaPdfLinkByRicaricaId"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findFatturaPdfLinkByRicaricaId(final long id, final String tenantName) throws FatturaNonDisponibileException {
        return ricaricaService.findFatturaPdfLinkByRicaricaId(id, tenantName);
    }
}
