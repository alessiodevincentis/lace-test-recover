package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.UnableToUpdateException;
import com.woonders.lacemscommon.service.StatoPraticheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;

/**
 * Created by ema98 on 8/9/2017.
 */
//@Controller
//@RequestMapping("/statopratiche/*")
public class StatoPraticheController extends AppController {

    @Autowired
    private StatoPraticheService statoPraticheService;

    @PostConstruct
    public void init() {
        appService = statoPraticheService;
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/countByStatoPraticaAndTipoClienteAndUsername"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long countByStatoPraticaAndTipoClienteAndUsername(final Long currentAziendaId, final long currentOperatorId, final Role.RoleName clientiReadRoleName, final String statoPratica, final String tipoPratica) {
        return statoPraticheService.countByStatoPraticaAndTipoClienteAndUsername(currentAziendaId, currentOperatorId, clientiReadRoleName, statoPratica, tipoPratica);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/findByStatoPraticaAndTipoClienteAndUsername"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<ClientePratica> findByStatoPraticaAndTipoClienteAndUsername(final Long currentAziendaId, final long currentOperatorId, final Role.RoleName clientiReadRoleName, final String statoPratica, final String tipoPratica, final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters) {
        return statoPraticheService.findByStatoPraticaAndTipoClienteAndUsername(currentAziendaId, currentOperatorId, clientiReadRoleName, statoPratica, tipoPratica, firstElementIndex, pageSize, sortField, sortOrder, filters);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/updateStatoPraticaByCodicePratica"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PraticaViewModel updateStatoPraticaByCodicePratica(final String statoPratica, final long codicePratica) throws UnableToUpdateException {
        return statoPraticheService.updateStatoPraticaByCodicePratica(statoPratica, codicePratica);
    }

    @RequestMapping(value = {ControllerConstants.V1 + "/updateDataNotificaByCodicePratica"}, method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PraticaViewModel updateDataNotificaByCodicePratica(final Date dataNotifica, final long codicePratica) throws UnableToUpdateException {
        return statoPraticheService.updateDataNotificaByCodicePratica(dataNotifica, codicePratica);
    }

    /*@RequestMapping(value = {ControllerConstants.V1 + "/isContainsDatesToBeNotified"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean isContainsDatesToBeNotified(final String statoPratica, final Role.RoleName clientiReadRoleName, final long operatorId) {
        return statoPraticheService.isContainsDatesToBeNotified(statoPratica, clientiReadRoleName, operatorId);
    }*/

    @RequestMapping(value = {ControllerConstants.V1 + "/getClientePratica"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClientePratica getClientePratica(final long id) {
        return statoPraticheService.getClientePratica(id);
    }
}
