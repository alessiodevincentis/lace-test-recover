package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.service.AppService;
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
//@RequestMapping("/app/*")
public class AppController {

    AppService appService;

    @RequestMapping(value = {ControllerConstants.V1 + "/findClienteByIdPratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel findClienteByIdPratica(final long praticaId) {
        return appService.findClienteByIdPratica(praticaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findPraticheByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PraticaViewModel> findPraticheByIdCliente(final long clienteId) {
        return appService.findPraticheByIdCliente(clienteId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findTrattenuteByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TrattenuteViewModel> findTrattenuteByIdCliente(final long clienteID) {
        return appService.findTrattenuteByIdCliente(clienteID);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findClienteByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel findClienteByIdCliente(final long clienteId) {
        return appService.findClienteByIdCliente(clienteId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findResidenzaByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResidenzaViewModel findResidenzaByIdCliente(final long clienteId) {
        return appService.findResidenzaByIdCliente(clienteId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findContoByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ContoViewModel findContoByIdCliente(final long clienteId) {
        return appService.findContoByIdCliente(clienteId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findDocumentoByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DocumentoViewModel findDocumentoByIdCliente(final long clienteId) {
        return appService.findDocumentoByIdCliente(clienteId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findPreventiviByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PreventivoViewModel> findPreventiviByIdCliente(final long clienteId) {
        return appService.findPreventiviByIdCliente(clienteId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findEstinzioniByIdPratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<EstinzioneViewModel> findEstinzioniByIdPratica(final long praticaId) {
        return appService.findEstinzioniByIdPratica(praticaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findCoobbligatoByIdPratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CoobbligatoViewModel> findCoobbligatoByIdPratica(final long praticaId) {
        return appService.findCoobbligatoByIdPratica(praticaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findPraticaByCodicePratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PraticaViewModel findPraticaByCodicePratica(final long praticaId) {
        return appService.findPraticaByCodicePratica(praticaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getDataNotificaByStatoPraticaAndAziendaId"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Date getDataNotificaByStatoPraticaAndAziendaId(final String statoPratica, final long aziendaId) {
        return appService.getDataNotificaByStatoPraticaAndAziendaId(statoPratica, aziendaId);
    }
}
