package com.woonders.lacemscommon.service.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ClientePreventivo;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.model.util.ClientePraticaUtil;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.PraticaViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.PreventivoViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityutil.PraticaUtil;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.db.repository.PreventivoRepository;
import com.woonders.lacemscommon.db.repository.TrattenuteRepository;
import com.woonders.lacemscommon.exception.PermissionDeniedException;
import com.woonders.lacemscommon.laceenum.GraficoEnum;
import com.woonders.lacemscommon.service.DashboardService;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemscommon.util.PredicateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    public static final String NAME = "dashboardServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private PreventivoRepository preventivoRepository;
    @Autowired
    private TrattenuteRepository trattenuteRepository;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private PreventivoViewModelCreator preventivoViewModelCreator;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private PraticaUtil praticaUtil;
    @Autowired
    private ClientePraticaUtil clientePraticaUtil;

    @Override
    public long countByTipoPraticaInMeseCorrente(final String tipoPratica, final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId, final Role.RoleName clientiReadRoleName) {
        final Date inizioMeseDate = DateToCalendar.inizioMese(new Date(), 0);
        final Date fineMeseDate = DateToCalendar.fineMese(new Date(), 0);
        try {
            return praticaRepository.count(predicateHelper.getPredicateForFindPraticheCaricateWidget(tipoPratica, inizioMeseDate, fineMeseDate, currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    @Override
    public long countByTipoPraticaPerfezionataInAnnoCorrente(final String tipoPratica, final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId, final Role.RoleName fatturazioneReadRoleName) {
        final Date dataInizioAnno = DateToCalendar.getStarYear(new Date());
        final Date dataFineAnno = DateToCalendar.getEndYear(new Date());
        try {
            return praticaRepository.count(predicateHelper.getPredicateForFindPratichePerfezionateWidget(dataInizioAnno, dataFineAnno, tipoPratica, currentAziendaId, operatorAziendaId, currentOperatorId, fatturazioneReadRoleName));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    @Override
    public BigDecimal calcTotalValueStatistics(final Date monthDate, final Long selectedAziendaId,
                                               final Role.RoleName graficiReadRoleName, final long operatorId,
                                               final long aziendaId, final GraficoEnum graficoEnum) {
        final Date dataInizio = DateToCalendar.inizioMese(monthDate, 0);
        final Date dataFine = DateToCalendar.fineMese(monthDate, 0);

        final QPratica qPratica = QPratica.pratica;
        BooleanBuilder booleanBuilder = getBooleanBuilderGrafico(graficoEnum, dataInizio, dataFine);

        switch (graficiReadRoleName) {
            case GRAFICI_READ_OWN:
                booleanBuilder = booleanBuilder.and(qPratica.operatore.id.eq(operatorId));
                break;
            case GRAFICI_READ_ALL:
                booleanBuilder = booleanBuilder.and(qPratica.operatore.azienda.id.eq(aziendaId));
                break;
            case GRAFICI_READ_SUPER:
                if (selectedAziendaId != null) {
                    booleanBuilder = booleanBuilder.and(qPratica.operatore.azienda.id.eq(selectedAziendaId));
                }
                break;
        }

        switch (graficoEnum) {
            case MONTANTE:
            case MONTANTE_PERFEZIONATO:
                return Lists.newArrayList(praticaRepository.findAll(booleanBuilder)).stream()
                        .map(praticaUtil::calcMontante)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            case EROGATO:
                return Lists.newArrayList(praticaRepository.findAll(booleanBuilder)).stream()
                        .filter(pratica -> pratica.getImportoErogato() != null)
                        .map(Pratica::getImportoErogato)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return null;
    }

    private BooleanBuilder getBooleanBuilderGrafico(final GraficoEnum graficoEnum, final Date dataInizio, final Date dataFine) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        final QPratica qPratica = QPratica.pratica;
        booleanBuilder = booleanBuilder.and(qPratica.dataCaricamento.isNotNull());
        booleanBuilder = booleanBuilder.and(qPratica.statoPratica.isNotNull());
        booleanBuilder = booleanBuilder.and(qPratica.tipoPratica.isNotNull());
        switch (graficoEnum) {
            case MONTANTE_PERFEZIONATO:
                booleanBuilder = booleanBuilder.and(qPratica.perfezionata.isTrue());
                booleanBuilder = booleanBuilder.and(qPratica.dataPerf.between(dataInizio, dataFine));
                break;
            case MONTANTE:
                booleanBuilder = booleanBuilder.and(qPratica.dataCaricamento.between(dataInizio, dataFine));
                booleanBuilder = booleanBuilder.and(qPratica.statoPratica.in(StatoPratica.getStatiPraticheLiquidate()));
                booleanBuilder = booleanBuilder.and(qPratica.tipoPratica.ne(Pratica.TipoPratica.PRESTITO.getValue()));
                break;
            case EROGATO:
                booleanBuilder = booleanBuilder.and(qPratica.dataCaricamento.between(dataInizio, dataFine));
                booleanBuilder = booleanBuilder.and(qPratica.statoPratica.in(StatoPratica.getStatiPraticheLiquidate()));
                booleanBuilder = booleanBuilder.and(qPratica.tipoPratica.eq(Pratica.TipoPratica.PRESTITO.getValue()));
                break;
        }
        return booleanBuilder;
    }

    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public ViewModelPageImpl<ClientePratica> findByDataRinnovoBeforeThanDays(final Long currentAziendaId, final long operatorAziendaId,
                                                                             final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                                             final int days, final int firstElementIndex, final int pageSize,
                                                                             final String sortField,
                                                                             final QueryDSLHelper.SortOrder sortOrder,
                                                                             final Map<QueryDSLHelper.TableField, Object> filters) {

        final Date endDate = DateToCalendar.addDays(new Date(), days);

        try {
            final Page<Pratica> praticaPage;
            final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

            BooleanExpression predicate = predicateHelper.getPredicateForPraticheRinnovabili(currentAziendaId,
                    operatorAziendaId, currentOperatorId, clientiReadRoleName, endDate);

            predicate = predicate.and(queryDSLHelper.createFilterPredicate(filters));

            praticaPage = praticaRepository.findAll(predicate,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePraticaList(praticaPage.getContent()),
                    praticaPage.getTotalPages(), praticaPage.getTotalElements());

        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }
    }

    @Override
    public long countByDataRinnovoBeforeThanDays(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId, final Role.RoleName clientiReadRoleName, final int days) {
        final Date endDate = DateToCalendar.addDays(new Date(), days);
        try {
            return praticaRepository.count(predicateHelper.getPredicateForPraticheRinnovabili(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName, endDate));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }


    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public ViewModelPageImpl<ClientePratica> findByDataRinnovoTrattenutaBeforeThanDays(final Long currentAziendaId, final long operatorAziendaId,
                                                                                       final long currentOperatorId,
                                                                                       final Role.RoleName clientiReadRoleName, final int days,
                                                                                       final int firstElementIndex, final int pageSize,
                                                                                       final String sortField,
                                                                                       final QueryDSLHelper.SortOrder sortOrder,
                                                                                       final Map<QueryDSLHelper.TableField, Object> filters) {
        final Date endDate = DateToCalendar.addDays(new Date(), days);

        try {
            final Page<Trattenute> trattenutePage;
            final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

            BooleanExpression predicate = predicateHelper.getPredicateForTrattenuteByDataRinnovoTrattenutaBeforeThanDays(currentAziendaId,
                    operatorAziendaId, currentOperatorId, clientiReadRoleName, endDate);

            predicate = predicate.and(queryDSLHelper.createFilterPredicate(filters));

            trattenutePage = trattenuteRepository.findAll(predicate,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(clientePraticaUtil.generateClienteTrattenuteViewModel(trattenutePage.getContent(),
                    currentAziendaId, currentOperatorId, clientiReadRoleName),
                    trattenutePage.getTotalPages(), trattenutePage.getTotalElements());

        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }
    }

    @Override
    public long countByDataRinnovoTrattenutaBeforeThanDays(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                                           final Role.RoleName clientiReadRoleName, final int days) {
        final Date endDate = DateToCalendar.addDays(new Date(), days);
        try {
            return trattenuteRepository.count(predicateHelper.getPredicateForTrattenuteByDataRinnovoTrattenutaBeforeThanDays(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName, endDate));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }


    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public ViewModelPage<ClientePratica> findByDataRecallToday(final Long currentAziendaId, final long operatorAziendaId,
                                                               final long currentOperatorId,
                                                               final Role.RoleName clientiReadRoleName,
                                                               final int firstElementIndex, final int pageSize,
                                                               final String sortField,
                                                               final QueryDSLHelper.SortOrder sortOrder,
                                                               final Map<QueryDSLHelper.TableField, Object> filters) {

        final Date endDate = DateToCalendar.settaGiorno(new Date(), 23, 59, 59);
        try {

            final Page<Pratica> praticaPage;
            final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

            BooleanExpression predicate = predicateHelper.getPredicateForClientiDaRichiamare(
                    currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName, endDate);

            predicate = predicate.and(queryDSLHelper.createFilterPredicate(filters));

            praticaPage = praticaRepository.findAll(predicate,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));


            return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePraticaList(praticaPage.getContent()),
                    praticaPage.getTotalPages(), praticaPage.getTotalElements());

        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }
    }

    @Override
    public long countByDataRecallToday(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                       final Role.RoleName clientiReadRoleName) {
        final Date endDate = DateToCalendar.settaGiorno(new Date(), 23, 59, 59);
        try {
            return praticaRepository.count(predicateHelper.getPredicateForClientiDaRichiamare(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName, endDate));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }


    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public ViewModelPage<ClientePreventivo> findByPreventiviInCorso(final Long currentAziendaId, final long operatorAziendaId,
                                                                    final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                                    final int firstElementIndex, final int pageSize, final String sortField,
                                                                    final QueryDSLHelper.SortOrder sortOrder) {

        try {
            final Page<Preventivo> preventivoPage;
            final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

            final BooleanExpression preventiviClientiExpression = predicateHelper.getPredicateForPreventiviClientiInCorso(currentAziendaId,
                    operatorAziendaId, currentOperatorId, clientiReadRoleName);


            preventivoPage = preventivoRepository.findAll(preventiviClientiExpression,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePreventivoList(preventivoPage.getContent()),
                    preventivoPage.getTotalPages(), preventivoPage.getTotalElements());

        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }
    }

    @Override
    public long countByPreventiviInCorso(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                         final Role.RoleName clientiReadRoleName) {
        try {
            return preventivoRepository.count(predicateHelper.getPredicateForPreventiviClientiInCorso(currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    private List<Pratica> findAllPraticheByCliente_Id(final long clienteId) {
        return praticaRepository.findDistinctByCliente_Id(clienteId);
    }

    @Override
    public ClientePratica getPraticaOnPreventivoCliente(final ClientePreventivo selectedClientePreventivo, final Role.RoleName clientiReadRoleName, final long operatorId) {
        final ClientePratica selectedClientePratica = new ClientePratica();
        final List<Pratica> praticaList = findAllPraticheByCliente_Id(
                selectedClientePreventivo.getClienteViewModel().getId());
        selectedClientePratica.setClienteViewModel(selectedClientePreventivo.getClienteViewModel());

        //TODO questa pure secondo me e una schifezza, bisogna pensare ad una visualizzazione preventivo, senno che ti faccio vedere le pratiche a caso??
        Optional<Pratica> praticaOptional = Optional.empty();
        Pratica pratica = null;
        //TODO ho pensato come fare i diversi casi poi mi sono venuti tutti uguali....li lascio cosi e con il commento cosi ci ricordiamo subito che c-e qualcosa che non va...
        switch (clientiReadRoleName) {
            case CLIENTI_READ_SUPER:
                if (praticaList.size() > 1) {
                    praticaOptional = praticaList.stream().filter(p -> p.getOperatore().getId() == operatorId).findFirst();
                }
                pratica = praticaOptional.orElseGet(() -> praticaList.get(0));
                break;
            case CLIENTI_READ_ALL:
                if (praticaList.size() > 1) {
                    praticaOptional = praticaList.stream().filter(p -> p.getOperatore().getId() == operatorId).findFirst();
                }
                pratica = praticaOptional.orElseGet(() -> praticaList.get(0));
                break;
            case CLIENTI_READ_OWN:
                if (praticaList.size() > 1) {
                    praticaOptional = praticaList.stream().filter(p -> p.getOperatore().getId() == operatorId).findFirst();
                }
                pratica = praticaOptional.orElseGet(() -> praticaList.get(0));
                break;

        }

        selectedClientePratica.setPraticaViewModel(praticaViewModelCreator.createViewModel(pratica));

        return selectedClientePratica;
    }

    @Override
    @Transactional
    public void removeNotificaPreventivo(final PreventivoViewModel preventivoViewModel) {
        final Preventivo preventivo = preventivoRepository.findOne(preventivoViewModel.getIdPreventivo());
        preventivo.setNotificaPreventivo(true);
        preventivoRepository.save(preventivo);
    }

    @Override
    @Transactional
    public void removeNotificaRecall(final PraticaViewModel praticaViewModel) {
        final Pratica pratica = praticaRepository.findOne(praticaViewModel.getCodicePratica());
        pratica.setNotiRecall(praticaViewModel.getDataRecall());
        praticaRepository.save(pratica);
    }

    @Override
    @Transactional
    public void removeNotificaRinnovoPratica(final PraticaViewModel praticaViewModel) {
        final Pratica pratica = praticaRepository.findOne(praticaViewModel.getCodicePratica());
        pratica.setNotiRinnovo(praticaViewModel.getDataRinnovo());
        praticaRepository.save(pratica);
    }

    @Override
    @Transactional
    public void removeNotificaTrattenuta(final TrattenuteViewModel trattenutaViewModel) {
        final Trattenute trattenute = trattenuteRepository.findOne(trattenutaViewModel.getCodStip());
        trattenute.setNotiCoes(trattenutaViewModel.getDataRinnovoTrat());
        trattenuteRepository.save(trattenute);
    }

    @Override
    public ViewModelPage<ClientePratica> getLastPraticheClientiCaricati(final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                                                        final Role.RoleName clientiReadRoleName, final int firstElementIndex, final int pageSize,
                                                                        final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<String, Object> filters) {

        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        if (Role.RoleName.CLIENTI_READ_NOT.equals(clientiReadRoleName)) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        } else {
            final Date dataInizioMese = DateToCalendar.inizioMese(new Date(), -1);
            final Date dataFineMese = DateToCalendar.fineMese(new Date(), 0);
            Page<Pratica> praticaPage = null;
            try {
                praticaPage = praticaRepository.findAll(predicateHelper.getPredicateForFindPraticheCaricateWidget(null, dataInizioMese, dataFineMese, currentAziendaId, operatorAziendaId, currentOperatorId, clientiReadRoleName),
                        new PageRequest(firstElementIndex / pageSize, pageSize, qSort));
                return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePraticaList(praticaPage.getContent()),
                        praticaPage.getTotalPages(), praticaPage.getTotalElements());
            } catch (final PermissionDeniedException e) {
                return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
            }
        }
    }

    @Override
    public ClientePratica getPratica(final long id) {
        final Pratica pratica = praticaRepository.findOne(id);
        return ClientePratica.builder().clienteViewModel(clienteViewModelCreator.createViewModel(pratica.getCliente()))
                .praticaViewModel(praticaViewModelCreator.createViewModel(pratica)).build();
    }

    @Override
    public ClientePreventivo getClientePreventivo(final long id) {
        final Preventivo preventivo = preventivoRepository.findOne(id);
        return ClientePreventivo.builder().clienteViewModel(clienteViewModelCreator.createViewModel(preventivo.getCliente()))
                .preventivoViewModel(preventivoViewModelCreator.createViewModel(preventivo)).build();
    }

    public enum NotificationCategory {
        RECALL, PRATICA, COESISTENZA;

        public static NotificationCategory fromValue(final String value) {
            if (value == null) {
                return null;
            }
            for (final NotificationCategory category : NotificationCategory.values()) {
                if (category.toString().equalsIgnoreCase(value)) {
                    return category;
                }
            }
            throw new IllegalArgumentException("No constant with value " + value);
        }
    }
}
