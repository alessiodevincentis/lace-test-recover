package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.model.AnalyticsProvvigioneCliente;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityutil.PraticaUtil;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.service.AnalyticsClientiService;
import com.woonders.lacemscommon.util.AnalyticsUtil;
import com.woonders.lacemscommon.util.PredicateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnalyticsClientiServiceImpl implements AnalyticsClientiService {

    public static final String NAME = "analyticsClientiServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private PraticaUtil praticaUtil;
    @Autowired
    private AnalyticsUtil analyticsUtil;

    /*@PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")*/
    @Override
    public BigDecimal sumByMontanteOrErogatoPraticheByStatoPraticheCollectionAndBetweenDateAndTipoPratica(final Role.RoleName clientiReadRoleName,
                                                                                                          final long operatorId, final long aziendaId,
                                                                                                          final Long currentAziendaId,
                                                                                                          final List<String> statoPraticheCollection,
                                                                                                          final Boolean isPratichePrestito,
                                                                                                          final Date from, final Date to,
                                                                                                          final boolean isSumMontante,
                                                                                                          final Pratica.TipoPratica tipoPratica,
                                                                                                          final List<String> operatoriSelezionati) {
        if (Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName) || Role.RoleName.CLIENTI_READ_ALL.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_SUPER.equals(clientiReadRoleName)) {
            if (from != null && to != null && from.compareTo(to) <= 0) {
                final Iterable<Pratica> praticaList = praticaRepository
                        .findAll(predicateHelper.getPredicateForTipoClienteAndStatoPraticaAndBetweenDateAndOperatorsSelectedAndTipoPratica(
                                statoPraticheCollection, clientiReadRoleName, operatorId, aziendaId, currentAziendaId,
                                isPratichePrestito, from, to, tipoPratica, operatoriSelezionati));
                return isSumMontante ? praticaUtil.sumMontante(praticaList) : praticaUtil.sumErogato(praticaList);
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public long countPraticheByStatoPraticheCollectionAndBetweenDateAndTipoPratica(final Role.RoleName clientiReadRoleName,
                                                                                   final long operatorId, final long aziendaId,
                                                                                   final Long currentAziendaId,
                                                                                   final List<String> statoPraticheCollection,
                                                                                   final Boolean isPratichePrestito,
                                                                                   final Date from, final Date to,
                                                                                   final Pratica.TipoPratica tipoPratica,
                                                                                   final List<String> operatoriSelezionati) {
        if (Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName) || Role.RoleName.CLIENTI_READ_ALL.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_SUPER.equals(clientiReadRoleName)) {
            if (from != null && to != null && from.compareTo(to) <= 0) {
                return praticaRepository
                        .count(predicateHelper.getPredicateForTipoClienteAndStatoPraticaAndBetweenDateAndOperatorsSelectedAndTipoPratica(
                                statoPraticheCollection, clientiReadRoleName, operatorId, aziendaId, currentAziendaId,
                                isPratichePrestito, from, to, tipoPratica, operatoriSelezionati));
            }
        }
        return 0;
    }

    private BigDecimal sumByProvvigioneTotalePraticheByStatoPraticheCollectionAndBetweenDate(final Role.RoleName clientiReadRoleName,
                                                                                             final long operatorId,
                                                                                             final Date from, final Date to,
                                                                                             final Pratica.TipoPratica tipoPratica) {
        if (Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName) || Role.RoleName.CLIENTI_READ_ALL.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_SUPER.equals(clientiReadRoleName)) {
            if (from != null && to != null && from.compareTo(to) <= 0) {
                final Iterable<Pratica> praticaList = praticaRepository
                        .findAll(predicateHelper.getPredicateForTipoClienteAndStatoPraticaAndBetweenDateAndOperatoreAndTipoPratica(
                                StatoPratica.getMainStatiPratica(), operatorId, from, to, tipoPratica));
                return praticaUtil.sumProvvigione(praticaList);
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<AnalyticsProvvigioneCliente> getAnalyticsProvvigioneClienteList(final Role.RoleName clientiReadRoleName,
                                                                                final long operatorId, final long aziendaId,
                                                                                final Long currentAziendaId,
                                                                                final Date from, final Date to,
                                                                                final List<String> operatoriSelezionati) {

        final List<AnalyticsProvvigioneCliente> analyticsProvvigioneClienteList = new ArrayList<>();
        if (from != null && to != null && from.compareTo(to) <= 0) {
            final List<OperatorViewModel> operatorViewModelList = analyticsUtil
                    .getOperatorListByOperatoriSelezionatiAndRolename(clientiReadRoleName,
                            operatoriSelezionati, operatorId, aziendaId, currentAziendaId);

            for (final OperatorViewModel operator : operatorViewModelList) {
                final AnalyticsProvvigioneCliente analyticsProvvigioneCliente = new AnalyticsProvvigioneCliente();

                final BigDecimal provvigioneTot = sumByProvvigioneTotalePraticheByStatoPraticheCollectionAndBetweenDate(
                        clientiReadRoleName, operator.getId(), from, to, null);

                final BigDecimal provvigioneCqs = sumByProvvigioneTotalePraticheByStatoPraticheCollectionAndBetweenDate(
                        clientiReadRoleName, operator.getId(), from, to, Pratica.TipoPratica.CESSIONE_S);

                final BigDecimal provvigioneCqp = sumByProvvigioneTotalePraticheByStatoPraticheCollectionAndBetweenDate(
                        clientiReadRoleName, operator.getId(), from, to, Pratica.TipoPratica.CESSIONE_P);

                final BigDecimal provvigioneDlg = sumByProvvigioneTotalePraticheByStatoPraticheCollectionAndBetweenDate(
                        clientiReadRoleName, operator.getId(), from, to, Pratica.TipoPratica.DELEGA);

                final BigDecimal provvigionePp = sumByProvvigioneTotalePraticheByStatoPraticheCollectionAndBetweenDate(
                        clientiReadRoleName, operator.getId(), from, to, Pratica.TipoPratica.PRESTITO);

                analyticsProvvigioneCliente.setUsername(operator.getUsername());
                analyticsProvvigioneCliente.setProvvigioneCqp(provvigioneCqp);
                analyticsProvvigioneCliente.setProvvigioneCqs(provvigioneCqs);
                analyticsProvvigioneCliente.setProvvigioneDlg(provvigioneDlg);
                analyticsProvvigioneCliente.setProvvigionePp(provvigionePp);
                analyticsProvvigioneCliente.setProvvigioneTot(provvigioneTot);
                analyticsProvvigioneClienteList.add(analyticsProvvigioneCliente);
            }
        }

        return analyticsProvvigioneClienteList;
    }

    @Override
    public BigDecimal sumProvvigioniAgenzia(final List<AnalyticsProvvigioneCliente> analyticsProvvigioneClienteList) {
        return analyticsProvvigioneClienteList.stream()
                .filter(analyticsProvvigioneCliente -> analyticsProvvigioneCliente.getProvvigioneTot() != null)
                .map(AnalyticsProvvigioneCliente::getProvvigioneTot)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}