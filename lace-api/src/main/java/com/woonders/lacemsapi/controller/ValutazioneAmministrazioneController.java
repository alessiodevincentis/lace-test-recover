package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.ValutazioneAmministrazioneViewModel;
import com.woonders.lacemscommon.service.ValutazioneAmministrazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by ema98 on 8/9/2017.
 */
//@Controller
//@RequestMapping("/valutazioneamministrazione/*")
public class ValutazioneAmministrazioneController {

    @Autowired
    private ValutazioneAmministrazioneService valutazioneAmministrazioneService;

    @RequestMapping(value = {ControllerConstants.V1 + "/delete"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody final long idValutazione) {
        valutazioneAmministrazioneService.delete(idValutazione);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findDistinctByAmministrazione_CodiceAmm"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ValutazioneAmministrazioneViewModel> findDistinctByAmministrazione_CodiceAmm(final long codiceAmm) {
        return valutazioneAmministrazioneService.findDistinctByAmministrazione_CodiceAmm(codiceAmm);
    }
}
