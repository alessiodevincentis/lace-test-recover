package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.CannotUpdateDecorrenzaException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by emanuele on 05/01/17.
 * Manages the invoicing stuff
 */
public interface FatturazioneService {


    /**
     * Settle a list of given procedures
     */
    void perfezionaPratiche(final List<ClientePratica> praticheDaPerfezionareViewModelList);

    /**
     * Settle a list of procedures given start/end date and their statuses
     */
    void perfezionaPratiche(final Date dataInizio, final Date dataFine, final boolean liquidata,
                            final boolean liquidazione, final boolean quietanzata, boolean perfezionamentoSelected,
                            Long currentAziendaId, Role.RoleName fatturazioneWriteRoleName);

    /**
     * Updates the date when the customer starts to repay back their debt
     */
    void updateDecorrenzaPratica(final long codicePratica, final Date decorrenzaDaModificare)
            throws CannotUpdateDecorrenzaException;

    /**
     * Returns a paged list of settled procedures filtered by dates
     */
    ViewModelPage<ClientePratica> searchPratichePerfezionate(Date dataInizio, Date dataFine, String username,
                                                             Long currentAziendaId, int firstElementIndex, int pageSize, String sortField, SortOrder sortOrder,
                                                             Map<QueryDSLHelper.TableField, Object> filters, long operatorId, long aziendaId, Role.RoleName fatturazioneReadRoleName);

    /**
     * Returns a procedure by its id
     */
    ClientePratica getPratica(long id);

    /**
     * Returns a list of procedures which could be settled
     */
    @PreAuthorize("hasAnyAuthority('FATTURAZIONE_WRITE', 'FATTURAZIONE_WRITE_SUPER')")
    ViewModelPage<ClientePratica> searchPraticheDaPerfezionare(Date dataInizio, Date dataFine,
                                                               boolean liquidata, boolean liquidazione, boolean quietanzata,
                                                               boolean perfezionamentoSelected, Long currentAziendaId, int firstElementIndex, int pageSize, String sortField,
                                                               SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters, Role.RoleName fatturazioneWriteRoleName);

    /**
     * Returns the sum of principal and interests for procedures which could be settled in given dates
     */
    BigDecimal sumTotaleMontantePraticheDaPerfezionare(final Date dataInizio, final Date dataFine,
                                                       final boolean liquidata, final boolean liquidazione, final boolean quietanzata,
                                                       boolean perfeziomanentoSelected, Long currentAziendaId, Role.RoleName fatturazioneWriteRoleName);

    /**
     * Returns the sum of commissions for procedures which could be settled in given dates
     */
    BigDecimal sumTotaleProvvigionePraticheDaPerfezionare(final Date dataInizio, final Date dataFine,
                                                          final boolean liquidata, final boolean liquidazione, final boolean quietanzata,
                                                          boolean perfeziomanentoSelected, Long currentAziendaId, Role.RoleName fatturazioneWriteRoleName);

    /**
     * Returns the sum of commissions for settled procedures in given dates
     */
    BigDecimal sumTotaleProvvigionePratichePerfezionate(Date dataInizio, Date dataFine, String username,
                                                        Long currentAziendaId, long operatorId, long aziendaId, Role.RoleName fatturazioneReadRoleName);

    /**
     * Returns the sum of principal and interests for settled procedures in given dates
     */
    BigDecimal sumTotaleMontantePratichePerfezionate(Date dataInizio, Date dataFine, String username,
                                                     Long currentAziendaId, long operatorId, long aziendaId, Role.RoleName fatturazioneReadRoleName);
}
