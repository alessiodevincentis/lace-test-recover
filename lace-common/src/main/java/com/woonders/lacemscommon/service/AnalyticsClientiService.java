package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.AnalyticsProvvigioneCliente;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Role;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Manages analytics for customers and procedure to create analytics charts
 */
public interface AnalyticsClientiService {


    /**
     * TODO
     */
    BigDecimal sumByMontanteOrErogatoPraticheByStatoPraticheCollectionAndBetweenDateAndTipoPratica(Role.RoleName clientiReadRoleName,
                                                                                                   long operatorId, long aziendaId,
                                                                                                   Long currentAziendaId,
                                                                                                   List<String> statoPraticheCollection,
                                                                                                   Boolean isPratichePrestito,
                                                                                                   Date from, Date to,
                                                                                                   boolean isSumMontante,
                                                                                                   Pratica.TipoPratica tipoPratica,
                                                                                                   List<String> operatoriSelezionati);

    /**
     * TODO
     */
    long countPraticheByStatoPraticheCollectionAndBetweenDateAndTipoPratica(Role.RoleName clientiReadRoleName,
                                                                            long operatorId, long aziendaId,
                                                                            Long currentAziendaId,
                                                                            List<String> statoPraticheCollection,
                                                                            Boolean isPratichePrestito,
                                                                            Date from, Date to,
                                                                            Pratica.TipoPratica tipoPratica,
                                                                            List<String> operatoriSelezionati);


    /**
     * TODO
     */
    List<AnalyticsProvvigioneCliente> getAnalyticsProvvigioneClienteList(Role.RoleName clientiReadRoleName,
                                                                         long operatorId, long aziendaId,
                                                                         Long currentAziendaId,
                                                                         Date from, Date to,
                                                                         List<String> operatoriSelezionati);

    /**
     * Returns the total amount of commission gained by the agency
     */
    BigDecimal sumProvvigioniAgenzia(List<AnalyticsProvvigioneCliente> analyticsProvvigioneClienteList);
}
