package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.service.ClienteManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by diegopizzo on 09/08/17.
 */
//@Controller
//@RequestMapping("/clientemanager/*")
public class ClienteManagerController extends AppController {

    @Autowired
    private ClienteManagerService clienteManagerService;

    @PostConstruct
    public void init() {
        appService = clienteManagerService;
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/saveNewClienteWithPratica"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClientePratica saveNewClienteWithPratica(final ClienteViewModel clienteViewModel, final ContoViewModel contoViewModel,
                                                    final DocumentoViewModel documentoViewModel, final ResidenzaViewModel residenzaViewModel,
                                                    final List<TrattenuteViewModel> trattenuteViewModelList, final List<EstinzioneViewModel> estinzioneViewModelList,
                                                    final PraticaViewModel praticaViewModel, final List<CoobbligatoViewModel> coobbligatoViewModelList,
                                                    final List<AmministrazioneViewModel> amministrazioneViewModelList, final List<PreventivoViewModel> preventivoViewModelList,
                                                    final String commentoLog, final long operatorId) {
        return clienteManagerService.saveNewClienteWithPratica(clienteViewModel, contoViewModel, documentoViewModel, residenzaViewModel, trattenuteViewModelList,
                estinzioneViewModelList, praticaViewModel, coobbligatoViewModelList, amministrazioneViewModelList, preventivoViewModelList, commentoLog, operatorId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findClienteByCodiceFiscale"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel findClienteByCodiceFiscale(final String codiceFiscale) {
        return clienteManagerService.findClienteByCodiceFiscale(codiceFiscale);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/deleteEstinzione"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteEstinzione(@RequestBody final EstinzioneViewModel estinzioneViewModel) {
        clienteManagerService.deleteEstinzione(estinzioneViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/deleteTrattenuta"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteTrattenuta(@RequestBody final TrattenuteViewModel trattenuteViewModel) {
        clienteManagerService.deleteTrattenuta(trattenuteViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/deleteSecondaOccupazione"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteSecondaOccupazione(final long clienteId) {
        clienteManagerService.deleteSecondaOccupazione(clienteId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/setAntiriciclaggioFileCaricato"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void setAntiriciclaggioFileCaricato(@RequestBody final long praticaId, @RequestBody final boolean antiriciclaggioFileCaricato) {
        clienteManagerService.setAntiriciclaggioFileCaricato(praticaId, antiriciclaggioFileCaricato);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findClienteByIdPratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel findClienteByIdPratica(final long praticaId) {
        return clienteManagerService.findClienteByIdPratica(praticaId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findPraticheByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PraticaViewModel> findPraticheByIdCliente(final long clienteId) {
        return clienteManagerService.findPraticheByIdCliente(clienteId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findTrattenuteByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TrattenuteViewModel> findTrattenuteByIdCliente(final long clienteID) {
        return clienteManagerService.findTrattenuteByIdCliente(clienteID);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findClienteByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel findClienteByIdCliente(final long clienteId) {
        return clienteManagerService.findClienteByIdCliente(clienteId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findResidenzaByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResidenzaViewModel findResidenzaByIdCliente(final long clienteId) {
        return clienteManagerService.findResidenzaByIdCliente(clienteId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findContoByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ContoViewModel findContoByIdCliente(final long clienteId) {
        return clienteManagerService.findContoByIdCliente(clienteId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findDocumentoByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DocumentoViewModel findDocumentoByIdCliente(final long clienteId) {
        return clienteManagerService.findDocumentoByIdCliente(clienteId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findPreventiviByIdCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PreventivoViewModel> findPreventiviByIdCliente(final long clienteId) {
        return clienteManagerService.findPreventiviByIdCliente(clienteId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findEstinzioniByIdPratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<EstinzioneViewModel> findEstinzioniByIdPratica(final long praticaId) {
        return clienteManagerService.findEstinzioniByIdPratica(praticaId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findCoobbligatoByIdPratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CoobbligatoViewModel> findCoobbligatoByIdPratica(final long praticaId) {
        return clienteManagerService.findCoobbligatoByIdPratica(praticaId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/findPraticaByCodicePratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PraticaViewModel findPraticaByCodicePratica(final long praticaId) {
        return clienteManagerService.findPraticaByCodicePratica(praticaId);
    }

    @Override
    @RequestMapping(value = {ControllerConstants.V1 + "/getDataNotificaByStatoPraticaAndAziendaId"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Date getDataNotificaByStatoPraticaAndAziendaId(final String statoPratica, final long aziendaId) {
        return clienteManagerService.getDataNotificaByStatoPraticaAndAziendaId(statoPratica, aziendaId);
    }
}
