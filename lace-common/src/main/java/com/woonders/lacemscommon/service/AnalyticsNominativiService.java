package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.model.AnalyticsNominativiOperatore;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Provenienza;

import java.time.LocalDate;
import java.util.List;

/**
 * Manages analytics for leads to create analytics charts
 */
public interface AnalyticsNominativiService {


    /**
     * Counts how many leads given their referral
     */
    long countNominativiTotaliByProvenienza(Long currentAziendaId,
                                            long operatorId,
                                            Role.RoleName nominativiReadRoleName,
                                            Provenienza provenienza,
                                            LocalDate from, LocalDate to,
                                            long aziendaId,
                                            List<String> operatoriSelezionati,
                                            String fornitore);

    /**
     * Counts how many leads who became customers (signed a procedure) given their referral
     */
    long countNominativiDiventatiClientiByProvenienza(Long currentAziendaId,
                                                      long operatorId,
                                                      Role.RoleName nominativiReadRoleName,
                                                      Provenienza provenienza,
                                                      LocalDate from, LocalDate to,
                                                      long aziendaId,
                                                      List<String> operatoriSelezionati,
                                                      String fornitore);

    /**
     * TODO
     */
    List<AnalyticsNominativiOperatore> getListNominativiTotalAndBecomeClienti(Role.RoleName nominativiReadRoleName,
                                                                              long operatorId, long aziendaId,
                                                                              Long currentAziendaId,
                                                                              LocalDate from, LocalDate to,
                                                                              List<String> operatoriSelezionati);


    /**
     * Returns a list containing all referral names
     */
    List<String> getAllFornitoriLead(LocalDate from, LocalDate to);

    /**
     * TODO
     */
    long countNominativiProvenienzaLead(Role.RoleName nominativiReadRoleName,
                                        long operatorId, long aziendaId,
                                        Long currentAziendaId,
                                        LocalDate from, LocalDate to,
                                        List<String> operatoriSelezionati,
                                        String fornitoreLead);

    /**
     * TODO
     */
    long countNominativiProvenienzaLeadToCliente(Role.RoleName nominativiReadRoleName,
                                                 long operatorId, long aziendaId,
                                                 Long currentAziendaId,
                                                 LocalDate from, LocalDate to,
                                                 List<String> operatoriSelezionati,
                                                 String fornitoreLead);
}
