package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.RicaricaComunicazioneViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entityenum.RicaricaType;
import com.woonders.lacemscommon.exception.DatiFatturazioneToBeCompletedException;
import com.woonders.lacemscommon.exception.FatturaNonDisponibileException;
import com.woonders.lacemscommon.exception.PaymentException;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;

import java.util.Map;

/**
 * Created by Emanuele on 25/02/2017.
 */

/**
 * Manages topup logic
 * [ricarica_comunicazione] table
 */
public interface RicaricaService {

    /**
     * Method used to make a topup for send SMSs
     */
    void makePayment(long payerOperatorId, String tokenId, String currency, Articolo.TipoArticolo tipoArticolo,
                     RicaricaType ricaricaType, OperatorViewModel operatoreRicaricato, long currentAziendaId)
            throws PaymentException, DatiFatturazioneToBeCompletedException;

    /**
     * return paged list of topup history
     */
    ViewModelPage<RicaricaComunicazioneViewModel> getTopUpList(Long currentAziendaId, int firstElementIndex,
                                                               int pageSize, String sortField, QueryDSLHelper.SortOrder sortOrder,
                                                               Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * TenantName is used ONLY to send notification on slack, it is NOT good for anything else!
     * One day, when we move HttpSessionUtil on this project, we can remove the parameter
     * and do it directly in the method!
     */
    String findFatturaPdfLinkByRicaricaId(long id, String tenantName) throws FatturaNonDisponibileException;
}
