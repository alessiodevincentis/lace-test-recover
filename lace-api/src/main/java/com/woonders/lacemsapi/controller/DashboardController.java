package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ClientePreventivo;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.laceenum.GraficoEnum;
import com.woonders.lacemscommon.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by diegopizzo on 09/08/17.
 */

//@Controller
//@RequestMapping("/dashboard/*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping(value = {ControllerConstants.V1 + "/countByTipoPraticaInMeseCorrente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countByTipoPraticaInMeseCorrente(final String tipoPratica, final Long currentAziendaId, final long operatorAziendaId,
                                                 final long currentOperatorId, final Role.RoleName clientiReadRoleName) {
        return dashboardService.countByTipoPraticaInMeseCorrente(tipoPratica, currentAziendaId, operatorAziendaId,
                currentOperatorId, clientiReadRoleName);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/countByTipoPraticaPerfezionataInAnnoCorrente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countByTipoPraticaPerfezionataInAnnoCorrente(final String tipoPratica, final Long currentAziendaId,
                                                             final long operatorAziendaId, final long currentOperatorId,
                                                             final Role.RoleName fatturazioneReadRoleName) {
        return dashboardService.countByTipoPraticaPerfezionataInAnnoCorrente(tipoPratica, currentAziendaId, operatorAziendaId,
                currentOperatorId, fatturazioneReadRoleName);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/findByDataRinnovoBeforeThanDays"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> findByDataRinnovoBeforeThanDays(final Long currentAziendaId, final long operatorAziendaId,
                                                                final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                                final int days) {
        return dashboardService.findByDataRinnovoBeforeThanDays(currentAziendaId, operatorAziendaId, currentOperatorId,
                clientiReadRoleName, days);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/countByDataRinnovoBeforeThanDays"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countByDataRinnovoBeforeThanDays(final Long currentAziendaId, final long operatorAziendaId,
                                                 final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                 final int days) {
        return dashboardService.countByDataRinnovoBeforeThanDays(currentAziendaId, operatorAziendaId, currentOperatorId,
                clientiReadRoleName, days);
    }

    /*@RequestMapping(value = {ControllerConstants.V1 + "/findByDataRinnovoTrattenutaBeforeThanDays"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> findByDataRinnovoTrattenutaBeforeThanDays(final Long currentAziendaId, final long operatorAziendaId,
                                                                          final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                                          final int days) {
        return dashboardService.findByDataRinnovoTrattenutaBeforeThanDays(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName,
                days);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/countByDataRinnovoTrattenutaBeforeThanDays"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countByDataRinnovoTrattenutaBeforeThanDays(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                                           final Role.RoleName clientiReadRoleName, final int days) {
        return dashboardService.countByDataRinnovoTrattenutaBeforeThanDays(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName, days);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/findByDataRecallToday"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePratica> findByDataRecallToday(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                                      final Role.RoleName clientiReadRoleName) {
        return dashboardService.findByDataRecallToday(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/countByDataRecallToday"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countByDataRecallToday(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                       final Role.RoleName clientiReadRoleName) {
        return dashboardService.countByDataRecallToday(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName);
    }

   /* @RequestMapping(value = {ControllerConstants.V1 + "/findByPreventiviInCorso"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClientePreventivo> findByPreventiviInCorso(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                                           final Role.RoleName clientiReadRoleName) {
        return dashboardService.findByPreventiviInCorso(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/countByPreventiviInCorso"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countByPreventiviInCorso(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                         final Role.RoleName clientiReadRoleName) {
        return dashboardService.countByPreventiviInCorso(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/removeNotificaPreventivo"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void removeNotificaPreventivo(@RequestBody final PreventivoViewModel preventivoViewModel) {
        dashboardService.removeNotificaPreventivo(preventivoViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/removeNotificaRecall"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void removeNotificaRecall(@RequestBody final PraticaViewModel praticaViewModel) {
        dashboardService.removeNotificaRecall(praticaViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/removeNotificaRinnovoPratica"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void removeNotificaRinnovoPratica(@RequestBody final PraticaViewModel praticaViewModel) {
        dashboardService.removeNotificaRinnovoPratica(praticaViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/removeNotificaTrattenuta"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void removeNotificaTrattenuta(@RequestBody final TrattenuteViewModel trattenuteViewModel) {
        dashboardService.removeNotificaTrattenuta(trattenuteViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getLastPraticheClientiCaricati"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<ClientePratica> getLastPraticheClientiCaricati(final Long currentAziendaId, final long operatorAziendaId,
                                                                        final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                                        final int firstElementIndex, final int pageSize, final String sortField,
                                                                        final QueryDSLHelper.SortOrder sortOrder, final Map<String, Object> filters) {
        return dashboardService.getLastPraticheClientiCaricati(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName, firstElementIndex,
                pageSize, sortField, sortOrder, filters);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getPratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClientePratica getPratica(final long id) {
        return dashboardService.getPratica(id);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/calcTotaleMontante"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BigDecimal calcTotaleMontante(final Date monthDate, final Long selectedAziendaId, final Role.RoleName graficiReadRoleName,
                                         final long operatorId, final long aziendaId, final GraficoEnum graficoEnum) {
        return dashboardService.calcTotalValueStatistics(monthDate, selectedAziendaId, graficiReadRoleName, operatorId, aziendaId, graficoEnum);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getPraticaOnPreventivoCliente"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClientePratica getPraticaOnPreventivoCliente(final ClientePreventivo selectedClientePreventivo, final Role.RoleName clientiReadRoleName,
                                                        final long operatorId) {
        return dashboardService.getPraticaOnPreventivoCliente(selectedClientePreventivo, clientiReadRoleName, operatorId);
    }
}