package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.AmministrazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@Controller
//@RequestMapping("/amministrazione/*")
public class AmministrazioneController {

    @Autowired
    private AmministrazioneService amministrazioneService;


    @RequestMapping(value = {ControllerConstants.V1 + "/save"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AmministrazioneViewModel save(@RequestBody final AmministrazioneViewModel amministrazioneViewModel) {
        return amministrazioneService.save(amministrazioneViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findDistinctByRagioneSocialeContaining"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AmministrazioneViewModel> findDistinctByRagioneSocialeContaining(final String ragioneSociale) {
        return amministrazioneService.findDistinctByRagioneSocialeContaining(ragioneSociale);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findByRagioneSociale"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AmministrazioneViewModel findByRagioneSociale(final String ragioneSociale) throws ItemNotFoundException {
        return amministrazioneService.findByRagioneSociale(ragioneSociale);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findByPiva"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AmministrazioneViewModel> findByPiva(final String piva) {
        return amministrazioneService.findByPiva(piva);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findByCodiceFiscale"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AmministrazioneViewModel> findByCodiceFiscale(final String codiceFiscale) {
        return amministrazioneService.findByCodiceFiscale(codiceFiscale);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findOne"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AmministrazioneViewModel findOne(final long id) throws ItemNotFoundException {
        return amministrazioneService.findOne(id);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findDistinctByCliente_Id"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AmministrazioneViewModel> findDistinctByCliente_Id(final long clienteId) {
        return amministrazioneService.findDistinctByCliente_Id(clienteId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/completeAmministrazione"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AmministrazioneViewModel> completeAmministrazione(final String query) {
        return amministrazioneService.completeAmministrazione(query);
    }


}
