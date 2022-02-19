package com.woonders.lacemsapi.controller;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.AntiriciclaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * Created by diegopizzo on 09/08/17.
 */

//@Controller
//@RequestMapping("/antiriciclaggio/*")
public class AntiriciclaggioController {

    @Autowired
    private AntiriciclaggioService antiriciclaggioService;


    @RequestMapping(value = {ControllerConstants.V1 + "/findPraticheAntiriciclaggio"}, method = RequestMethod.GET, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ViewModelPage<ClientePratica> findPraticheAntiriciclaggio(final String cf, final Date dataInizio, final Date dataFine, final AntiriciclaggioService.TipoRicercaAntiriciclaggio tipoRicercaAntiriciclaggio,
                                                                     final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters,
                                                                     final Long currentAziendaId, final Role.RoleName antiriciclaggioReadRoleName, final long operatorId) {
        return antiriciclaggioService.findPraticheAntiriciclaggio(cf, dataInizio, dataFine, tipoRicercaAntiriciclaggio, firstElementIndex, pageSize, sortField, sortOrder,
                filters, currentAziendaId, antiriciclaggioReadRoleName, operatorId);
    }
}
