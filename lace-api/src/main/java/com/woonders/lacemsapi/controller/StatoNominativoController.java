package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.StatoNominativoService;
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
//@RequestMapping("/statonominativo/*")
public class StatoNominativoController {

    @Autowired
    private StatoNominativoService statoNominativoService;

    @RequestMapping(value = {ControllerConstants.V1 + "/countNominativiByStatoNominativo"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countNominativiByStatoNominativo(final Long currentAziendaId, final long currentOperatorId, final Role.RoleName nominativiReadRoleName, final String statoNominativo) {
        return statoNominativoService.countNominativiByStatoNominativo(currentAziendaId, currentOperatorId, nominativiReadRoleName, statoNominativo);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/getNominativiByStatoNominativo"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<ClienteViewModel> getNominativiByStatoNominativo(final Long currentAziendaId, final long currentOperatorId, final Role.RoleName nominativiReadRoleName, final String statoNominativo, final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters) {
        return statoNominativoService.getNominativiByStatoNominativo(currentAziendaId, currentOperatorId, nominativiReadRoleName, statoNominativo, firstElementIndex, pageSize, sortField, sortOrder, filters);
    }

    /*@RequestMapping(value = {ControllerConstants.V1 + "/isContainsDataRecallNominativoLessToday"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean isContainsDataRecallNominativoLessToday(final String statoNominativo, final Role.RoleName nominativiReadRoleName, final long operatorId) {
        return statoNominativoService.isContainsDataRecallNominativoLessToday(statoNominativo, nominativiReadRoleName, operatorId);
    }*/
}
