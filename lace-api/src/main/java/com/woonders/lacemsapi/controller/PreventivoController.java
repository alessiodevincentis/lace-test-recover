package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.exception.RinnovoNotSavedException;
import com.woonders.lacemscommon.service.PreventivoService;
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
//@RequestMapping("/preventivo/*")
public class PreventivoController extends AppController {

    @Autowired
    private PreventivoService preventivoService;

    @PostConstruct
    public void init() {
        appService = preventivoService;
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/deletePreventivo"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deletePreventivo(final long preventivoId) {
        preventivoService.deletePreventivo(preventivoId);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getAllPraticheAttiveEstinguibili"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PraticaViewModel> getAllPraticheAttiveEstinguibili(final long idCliente) {
        return preventivoService.getAllPraticheAttiveEstinguibili(idCliente);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getAllTrattenuteEstinguibili"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TrattenuteViewModel> getAllTrattenuteEstinguibili(final long idCliente) {
        return preventivoService.getAllTrattenuteEstinguibili(idCliente);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/creaPratica"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void creaPratica(final PreventivoViewModel preventivoViewModel, final List<PraticaViewModel> praticheDaEstinguereViewModelList, final List<TrattenuteViewModel> coesistenzeDaEstinguereViewModelList, final long clienteId, final long aziendaId, final long operatorId) throws RinnovoNotSavedException {
        preventivoService.creaPratica(preventivoViewModel, praticheDaEstinguereViewModelList, coesistenzeDaEstinguereViewModelList, clienteId, aziendaId, operatorId);
    }
}
