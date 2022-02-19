package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.exception.UnableToDeleteException;
import com.woonders.lacemscommon.service.NominativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by ema98 on 8/9/2017.
 */
//@Controller
//@RequestMapping("/nominativo/*")
public class NominativoController extends AppController {

    @Autowired
    private NominativoService nominativoService;

    @PostConstruct
    public void init() {
        appService = nominativoService;
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/saveNominativo"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel saveNominativo(final ClienteViewModel nominativoViewModel, final ResidenzaViewModel residenzaViewModel, final List<PraticaViewModel> preventivoViewModelList, final List<TrattenuteViewModel> trattenuteNominativoViewModelList, final List<AmministrazioneViewModel> amministrazioneViewModelList, final String commentoLog, final long operatorId) {
        return nominativoService.saveNominativo(nominativoViewModel, residenzaViewModel, preventivoViewModelList, trattenuteNominativoViewModelList, amministrazioneViewModelList, commentoLog, operatorId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/deletePreventivo"}, method = RequestMethod.DELETE, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deletePreventivo(final PraticaViewModel preventivoViewModel) {
        nominativoService.deletePreventivo(preventivoViewModel.getCodicePratica());
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/deleteTrattenuta"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteTrattenuta(final TrattenuteViewModel trattenuteViewModel) {
        nominativoService.deleteTrattenuta(trattenuteViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/creaPraticaDaPreventivoNominativo"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void creaPraticaDaPreventivoNominativo(final ClienteViewModel clienteViewModel, final ResidenzaViewModel residenzaViewModel, final List<TrattenuteViewModel> trattenuteViewModelList, final List<TrattenuteViewModel> impegniSelezionati, final List<PraticaViewModel> preventivoViewModelList, final PraticaViewModel preventivoDaCreare, final long operatorId, final long aziendaId) {
        nominativoService.creaPraticaDaPreventivoNominativo(clienteViewModel, residenzaViewModel, trattenuteViewModelList, impegniSelezionati, preventivoViewModelList, preventivoDaCreare, operatorId, aziendaId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/delete"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void delete(final ClienteViewModel nominativoViewModel) throws UnableToDeleteException {
        nominativoService.delete(nominativoViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getNotificaLeadSms"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NotificaLeadSmsViewModel getNotificaLeadSms(final long nominativoId) {
        return nominativoService.getNotificaLeadSms(nominativoId);
    }

}
