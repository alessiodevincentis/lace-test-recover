package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ClienteTrattenuta;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.DashboardNominativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;

/**
 * Created by diegopizzo on 09/08/17.
 */

//@Controller
//@RequestMapping("/dashboardnominativo/*")
public class DashboardNominativoController extends AppController {

    @Autowired
    private DashboardNominativoService dashboardNominativoService;

    @PostConstruct
    public void init() {
        appService = dashboardNominativoService;
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getNominativiInDataRecall"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<ClienteViewModel> getNominativiInDataRecall(final Long currentAziendaId, final long currentOperatorId,
                                                                     final Role.RoleName nominativiReadRoleName, final Date date,
                                                                     final int firstElementIndex, final int pageSize, final String sortField,
                                                                     final QueryDSLHelper.SortOrder sortOrder,
                                                                     final Map<QueryDSLHelper.TableField, Object> filters) {
        return dashboardNominativoService.getNominativiInDataRecall(currentAziendaId, currentOperatorId, nominativiReadRoleName, date, firstElementIndex,
                pageSize, sortField, sortOrder, filters);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/countNominativiInDataRecall"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countNominativiInDataRecall(final Long currentAziendaId, final long currentOperatorId, final Role.RoleName nominativiReadRoleName,
                                            final Date date) {
        return dashboardNominativoService.countNominativiInDataRecall(currentAziendaId, currentOperatorId, nominativiReadRoleName, date);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findImpegniRinnovabili"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<ClienteTrattenuta> findImpegniRinnovabili(final Long currentAziendaId, final long currentOperatorId,
                                                                   final Role.RoleName nominativiReadRoleName, final int days, final int firstElementIndex,
                                                                   final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder,
                                                                   final Map<QueryDSLHelper.TableField, Object> filters) {
        return dashboardNominativoService.findImpegniRinnovabili(currentAziendaId, currentOperatorId, nominativiReadRoleName, days, firstElementIndex, pageSize,
                sortField, sortOrder, filters);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/countImpegniRinnovabili"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countImpegniRinnovabili(final Long currentAziendaId, final long currentOperatorId, final Role.RoleName nominativiReadRoleName,
                                        final int days) {
        return dashboardNominativoService.countImpegniRinnovabili(currentAziendaId, currentOperatorId, nominativiReadRoleName, days);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/setRinnovoTrattenute"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void setRinnovoTrattenute(@RequestBody final TrattenuteViewModel trattenuteViewModel) {
        dashboardNominativoService.setRinnovoTrattenute(trattenuteViewModel);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/countNewLead"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countNewLead(final Long currentAziendaId, final long currentOperatorId, final Role.RoleName nominativiReadRoleName) {
        return dashboardNominativoService.countNewLead(currentAziendaId, currentOperatorId, nominativiReadRoleName);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getNominativo"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteViewModel getNominativo(final long id) {
        return dashboardNominativoService.getNominativo(id);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getTrattenuta"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteTrattenuta getTrattenuta(final long id) {
        return dashboardNominativoService.getTrattenuta(id);
    }
}
