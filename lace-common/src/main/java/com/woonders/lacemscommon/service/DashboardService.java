package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ClientePreventivo;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.laceenum.GraficoEnum;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Manages the details of the main dashboard
 */
public interface DashboardService {

    /**
     * Counts procedure in a given state within the current month
     */
    long countByTipoPraticaInMeseCorrente(String tipoPratica, Long currentAziendaId, long operatorAziendaId,
                                          long currentOperatorId, Role.RoleName clientiReadRoleName);

    /**
     * Counts procedure in a settled state within the current year
     */
    long countByTipoPraticaPerfezionataInAnnoCorrente(String tipoPratica, Long currentAziendaId, long operatorAziendaId,
                                                      long currentOperatorId, Role.RoleName fatturazioneReadRoleName);

    /**
     * Returns the amount of money related to procedures for a given type [GraficoEnum]
     */
    BigDecimal calcTotalValueStatistics(Date monthDate, Long selectedAziendaId,
                                        Role.RoleName graficiReadRoleName, long operatorId,
                                        long aziendaId, GraficoEnum graficoEnum);


    /**
     * Returns a paged list of all the procedures which can be renewed
     */
    ViewModelPageImpl<ClientePratica> findByDataRinnovoBeforeThanDays(Long currentAziendaId, long operatorAziendaId,
                                                                      long currentOperatorId, Role.RoleName clientiReadRoleName,
                                                                      int days, int firstElementIndex, int pageSize,
                                                                      String sortField,
                                                                      QueryDSLHelper.SortOrder sortOrder,
                                                                      Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * Counts how many the procedures which can be renewed
     */
    long countByDataRinnovoBeforeThanDays(Long currentAziendaId, long operatorAziendaId, long currentOperatorId,
                                          Role.RoleName clientiReadRoleName, int days);


    /**
     * Returns a paged list of all the procedures which have a related another agency procedure which can be renewed
     */
    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    ViewModelPageImpl<ClientePratica> findByDataRinnovoTrattenutaBeforeThanDays(Long currentAziendaId, long operatorAziendaId,
                                                                                long currentOperatorId,
                                                                                Role.RoleName clientiReadRoleName, int days,
                                                                                int firstElementIndex, int pageSize,
                                                                                String sortField,
                                                                                QueryDSLHelper.SortOrder sortOrder,
                                                                                Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * Counts how many procedures which have a related another agency procedure which can be renewed
     */
    long countByDataRinnovoTrattenutaBeforeThanDays(Long currentAziendaId, long operatorAziendaId,
                                                    long currentOperatorId, Role.RoleName clientiReadRoleName, int days);


    /**
     * Returns a paged list of all the customers who should be called today
     */
    ViewModelPage<ClientePratica> findByDataRecallToday(Long currentAziendaId, long operatorAziendaId,
                                                        long currentOperatorId,
                                                        Role.RoleName clientiReadRoleName,
                                                        int firstElementIndex, int pageSize,
                                                        String sortField,
                                                        QueryDSLHelper.SortOrder sortOrder,
                                                        Map<QueryDSLHelper.TableField, Object> filters);

    /**
     * Counts how many customers who should be called today
     */
    long countByDataRecallToday(Long currentAziendaId, long operatorAziendaId, long currentOperatorId,
                                Role.RoleName clientiReadRoleName);


    /**
     * Returns a paged list of all the customers who have an opened quote
     */
    ViewModelPage<ClientePreventivo> findByPreventiviInCorso(Long currentAziendaId, long operatorAziendaId,
                                                             long currentOperatorId, Role.RoleName clientiReadRoleName,
                                                             int firstElementIndex, int pageSize, String sortField,
                                                             QueryDSLHelper.SortOrder sortOrder);

    /**
     * Counts how many customers who have an opened quote
     */
    long countByPreventiviInCorso(Long currentAziendaId, long operatorAziendaId, long currentOperatorId,
                                  Role.RoleName clientiReadRoleName);

    /**
     * Removes the flag used to have a notification on open quotes
     */
    void removeNotificaPreventivo(PreventivoViewModel preventivoViewModel);

    /**
     * Removes the flag used to have a notification on recall date
     */
    void removeNotificaRecall(PraticaViewModel praticaViewModel);

    /**
     * Removes the flag used to have a notification on a renewable procedure
     */
    void removeNotificaRinnovoPratica(PraticaViewModel praticaViewModel);

    /**
     * Removes the flag used to have a notification on an another agency procedure
     */
    void removeNotificaTrattenuta(TrattenuteViewModel trattenuteViewModel);

    /**
     * Returns a paged list of the last opened procedures and related customers
     */
    ViewModelPage<ClientePratica> getLastPraticheClientiCaricati(Long currentAziendaId, long operatorAziendaId, long currentOperatorId,
                                                                 Role.RoleName clientiReadRoleName, int firstElementIndex, int pageSize, String sortField,
                                                                 QueryDSLHelper.SortOrder sortOrder, Map<String, Object> filters);

    /**
     * Returns a procedure details by its id
     */
    ClientePratica getPratica(long id);

    /**
     * Returns the procedure info related to a given quote
     */
    ClientePratica getPraticaOnPreventivoCliente(final ClientePreventivo selectedClientePreventivo, Role.RoleName clientiReadRoleName, long operatorId);

    /**
     * Returns the customer info related to a given quote id
     */
    ClientePreventivo getClientePreventivo(long id);
}
