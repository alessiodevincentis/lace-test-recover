package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.model.AnalyticsNominativiOperatore;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Provenienza;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.service.AnalyticsNominativiService;
import com.woonders.lacemscommon.util.AnalyticsUtil;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemscommon.util.PredicateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnalyticsNominativiServiceImpl implements AnalyticsNominativiService {

    public static final String NAME = "analyticsNominativiServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DateConversionUtil dateConversionUtil;
    @Autowired
    private AnalyticsUtil analyticsUtil;


    private long countNominativiTotaliByProvenienza(final Long currentAziendaId,
                                                    final long operatorId,
                                                    final Role.RoleName nominativiReadRoleName,
                                                    final Provenienza provenienza,
                                                    final LocalDate from, final LocalDate to,
                                                    final long aziendaId,
                                                    final List<String> operatoriSelezionati,
                                                    final String fornitore,
                                                    final boolean isEnabledToDoSwitch) {

        if (Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName) || Role.RoleName.NOMINATIVI_READ_ALL.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_SUPER.equals(nominativiReadRoleName)) {
            if (from != null && to != null && from.compareTo(to) <= 0) {
                return clienteRepository.count(predicateHelper.getPredicateForTipoAndDataClienteNotNullAndProvenienzaAndBetweenDataInsAndStatoNominativoNotNullAndOperatorIn
                        (currentAziendaId, operatorId, nominativiReadRoleName, provenienza, dateConversionUtil.calcDateFromLocalDate(from),
                                dateConversionUtil.calcDateFromLocalDate(to), aziendaId, operatoriSelezionati, isEnabledToDoSwitch,
                                false, fornitore));
            }
        }
        return 0;
    }

    private long countNominativiDiventatiClientiByProvenienza(final Long currentAziendaId,
                                                              final long operatorId,
                                                              final Role.RoleName nominativiReadRoleName,
                                                              final Provenienza provenienza,
                                                              final LocalDate from, final LocalDate to,
                                                              final long aziendaId,
                                                              final List<String> operatoriSelezionati,
                                                              final String fornitore,
                                                              final boolean isEnabledToDoSwitch) {

        if (Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName) || Role.RoleName.NOMINATIVI_READ_ALL.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_SUPER.equals(nominativiReadRoleName)) {
            if (from != null && to != null && from.compareTo(to) <= 0) {
                return clienteRepository.count(predicateHelper.getPredicateForTipoAndDataClienteNotNullAndProvenienzaAndBetweenDataInsAndStatoNominativoNotNullAndOperatorIn
                        (currentAziendaId, operatorId, nominativiReadRoleName, provenienza, dateConversionUtil.calcDateFromLocalDate(from),
                                dateConversionUtil.calcDateFromLocalDate(to), aziendaId, operatoriSelezionati, isEnabledToDoSwitch,
                                true, fornitore));
            }
        }
        return 0;
    }

    @Override
    public long countNominativiTotaliByProvenienza(final Long currentAziendaId,
                                                   final long operatorId,
                                                   final Role.RoleName nominativiReadRoleName,
                                                   final Provenienza provenienza,
                                                   final LocalDate from, final LocalDate to,
                                                   final long aziendaId,
                                                   final List<String> operatoriSelezionati,
                                                   final String fornitore) {
        return countNominativiTotaliByProvenienza(currentAziendaId, operatorId, nominativiReadRoleName,
                provenienza, from, to, aziendaId, operatoriSelezionati, fornitore, true);
    }


    @Override
    public long countNominativiDiventatiClientiByProvenienza(final Long currentAziendaId,
                                                             final long operatorId,
                                                             final Role.RoleName nominativiReadRoleName,
                                                             final Provenienza provenienza,
                                                             final LocalDate from, final LocalDate to,
                                                             final long aziendaId,
                                                             final List<String> operatoriSelezionati,
                                                             final String fornitore) {

        return countNominativiDiventatiClientiByProvenienza(currentAziendaId, operatorId, nominativiReadRoleName,
                provenienza, from, to, aziendaId, operatoriSelezionati, fornitore, true);
    }


    @Override
    public List<AnalyticsNominativiOperatore> getListNominativiTotalAndBecomeClienti(final Role.RoleName nominativiReadRoleName,
                                                                                     final long operatorId, final long aziendaId,
                                                                                     final Long currentAziendaId,
                                                                                     final LocalDate from, final LocalDate to,
                                                                                     final List<String> operatoriSelezionati) {


        final List<AnalyticsNominativiOperatore> analyticsNominativiOperatoreList = new ArrayList<>();
        final List<OperatorViewModel> operatorViewModelList = analyticsUtil
                .getOperatorListByOperatoriSelezionatiAndRolename(nominativiReadRoleName,
                        operatoriSelezionati, operatorId, aziendaId, currentAziendaId);

        for (final OperatorViewModel operator : operatorViewModelList) {
            final AnalyticsNominativiOperatore analyticsNominativiOperatore = new AnalyticsNominativiOperatore();

            final long nominativiTotali = countNominativiTotaliByProvenienza(currentAziendaId, operator.getId(),
                    nominativiReadRoleName, null, from, to, aziendaId, operatoriSelezionati,
                    null, false);

            final long nominativiToClienti = countNominativiDiventatiClientiByProvenienza(currentAziendaId,
                    operator.getId(), nominativiReadRoleName, null, from, to, aziendaId,
                    operatoriSelezionati, null, false);

            analyticsNominativiOperatore.setUsername(operator.getUsername());
            analyticsNominativiOperatore.setNominativiTot(nominativiTotali);
            analyticsNominativiOperatore.setNominativiToClienti(nominativiToClienti);
            analyticsNominativiOperatoreList.add(analyticsNominativiOperatore);
        }
        return analyticsNominativiOperatoreList;
    }

    @Override
    public List<String> getAllFornitoriLead(final LocalDate from, final LocalDate to) {
        if (from != null && to != null && from.compareTo(to) <= 0) {
            return clienteRepository.getAllFornitoriLead();
        }
        return null;
    }

    @Override
    public long countNominativiProvenienzaLead(final Role.RoleName nominativiReadRoleName,
                                               final long operatorId, final long aziendaId,
                                               final Long currentAziendaId,
                                               final LocalDate from, final LocalDate to,
                                               final List<String> operatoriSelezionati,
                                               final String fornitoreLead) {

        if (Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName) || Role.RoleName.NOMINATIVI_READ_ALL.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_SUPER.equals(nominativiReadRoleName)) {
            if (from != null && to != null && from.compareTo(to) <= 0) {

                return clienteRepository.count(predicateHelper.getPredicateForTipoAndDataClienteNotNullAndProvenienzaAndBetweenDataInsAndStatoNominativoNotNullAndOperatorIn(
                        currentAziendaId, operatorId, nominativiReadRoleName, Provenienza.LEAD,
                        dateConversionUtil.calcDateFromLocalDate(from), dateConversionUtil.calcDateFromLocalDate(to),
                        aziendaId, operatoriSelezionati, true, false, fornitoreLead));
            }
        }
        return 0;
    }

    @Override
    public long countNominativiProvenienzaLeadToCliente(final Role.RoleName nominativiReadRoleName,
                                                        final long operatorId, final long aziendaId,
                                                        final Long currentAziendaId,
                                                        final LocalDate from, final LocalDate to,
                                                        final List<String> operatoriSelezionati,
                                                        final String fornitoreLead) {

        if (Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName) || Role.RoleName.NOMINATIVI_READ_ALL.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_SUPER.equals(nominativiReadRoleName)) {
            if (from != null && to != null && from.compareTo(to) <= 0) {
                return clienteRepository.count(predicateHelper.getPredicateForTipoAndDataClienteNotNullAndProvenienzaAndBetweenDataInsAndStatoNominativoNotNullAndOperatorIn(
                        currentAziendaId, operatorId, nominativiReadRoleName, Provenienza.LEAD,
                        dateConversionUtil.calcDateFromLocalDate(from), dateConversionUtil.calcDateFromLocalDate(to),
                        aziendaId, operatoriSelezionati, true, true, fornitoreLead));
            }
        }
        return 0;
    }
}
