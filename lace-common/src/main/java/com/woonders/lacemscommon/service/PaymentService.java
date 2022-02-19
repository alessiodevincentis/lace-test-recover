package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entityenum.RicaricaType;
import com.woonders.lacemscommon.exception.DatiFatturazioneToBeCompletedException;
import com.woonders.lacemscommon.exception.PaymentException;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;

/**
 * Created by Emanuele on 15/02/2017.
 */

/**
 * Manages the payments through the chosen gateway
 */
public interface PaymentService {

    /**
     *
     */
    long makePayment(long payerOperatorId, String tokenId, Articolo.TipoArticolo tipoArticolo, String currency,
                     RicaricaType ricaricaType, OperatorViewModel operatoreRicaricato, long currentAziendaId)
            throws PaymentException, DatiFatturazioneToBeCompletedException;

    enum ProcessingCode {
        INVALID_NUMBER, INVALID_EXPIRY_MONTH, INVALID_EXPIRY_YEAR, INVALID_CVC, INVALID_SWIPE_DATA, INCORRECT_NUMBER, EXPIRED_CARD, INCORRECT_CVC, INCORRECT_ZIP, CARD_DECLINED, MISSING, PROCESSING_ERROR
    }
}
