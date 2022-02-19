package com.woonders.lacemscommon.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.model.util.ClientePraticaUtil;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.PraticaViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityutil.PraticaUtil;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.exception.CannotUpdateDecorrenzaException;
import com.woonders.lacemscommon.exception.PermissionDeniedException;
import com.woonders.lacemscommon.service.FatturazioneService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.util.PredicateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
public class FatturazioneServiceImpl implements FatturazioneService {

    public static final String NAME = "fatturazioneServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PraticaUtil praticaUtil;
    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private OperatorService operatorService;
    @Autowired
    private ClientePraticaUtil clientePraticaUtil;

    @PreAuthorize("hasAnyAuthority('FATTURAZIONE_WRITE', 'FATTURAZIONE_WRITE_SUPER')")
    @Override
    public ViewModelPage<ClientePratica> searchPraticheDaPerfezionare(final String provenienza, final String provenienzaDesc, final Date dataInizio, final Date dataFine,
                                                                      final boolean liquidata, final boolean liquidazione, final boolean quietanzata,
                                                                      final boolean perfezionamentoSelected, final Long currentAziendaId, final int firstElementIndex, final int pageSize, final String sortField,
                                                                      final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters, final Role.RoleName fatturazioneWriteRoleName) {

        final BooleanExpression findPraticheDaPerfezionareExpression;
        try {
            findPraticheDaPerfezionareExpression = predicateHelper
                    .getPredicateForFindPraticheDaPerfezionare(provenienza, provenienzaDesc, dataInizio, dataFine, liquidazione, liquidata, quietanzata,
                            perfezionamentoSelected, fatturazioneWriteRoleName, currentAziendaId);
            final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

            final Page<Pratica> praticheDaPerfezionarePage = praticaRepository.findAll(findPraticheDaPerfezionareExpression,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePraticaList(praticheDaPerfezionarePage.getContent()),
                    praticheDaPerfezionarePage.getTotalPages(), praticheDaPerfezionarePage.getTotalElements());
        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }

    }

    @Override
    public BigDecimal sumTotaleMontantePraticheDaPerfezionare(final String provenienza, final String provenienzaDesc, final Date dataInizio, final Date dataFine, final boolean liquidata,
                                                              final boolean liquidazione, final boolean quietanzata, final boolean perfezionamentoSelected, 
                                                              final Long currentAziendaId, final Role.RoleName fatturazioneWriteRoleName) {
        BooleanExpression findPraticheDaPerfezionareExpression = null;
        try {
            findPraticheDaPerfezionareExpression = predicateHelper
                    .getPredicateForFindPraticheDaPerfezionare(provenienza, provenienzaDesc, dataInizio, dataFine, liquidazione, liquidata, quietanzata,
                            perfezionamentoSelected, fatturazioneWriteRoleName, currentAziendaId);
            final Iterable<Pratica> totalePerfezionateList = praticaRepository.findAll(findPraticheDaPerfezionareExpression);

            return praticaUtil.sumMontante(totalePerfezionateList);
        } catch (final PermissionDeniedException e) {
            return BigDecimal.ZERO;
        }

    }

    @Override
    public BigDecimal sumTotaleProvvigionePraticheDaPerfezionare(final String provenienza, final String provenienzaDesc, final Date dataInizio, final Date dataFine, final boolean liquidata,
                                                                 final boolean liquidazione, final boolean quietanzata, final boolean perfezionamentoSelected, final Long currentAziendaId, final Role.RoleName fatturazioneWriteRoleName) {
        BooleanExpression findPraticheDaPerfezionareExpression = null;
        try {
            findPraticheDaPerfezionareExpression = predicateHelper
                    .getPredicateForFindPraticheDaPerfezionare(provenienza, provenienzaDesc, dataInizio, dataFine, liquidazione, liquidata, quietanzata,
                            perfezionamentoSelected, fatturazioneWriteRoleName, currentAziendaId);
            final Iterable<Pratica> totalePerfezionateList = praticaRepository.findAll(findPraticheDaPerfezionareExpression);

            return praticaUtil.sumProvvigione(totalePerfezionateList);
        } catch (final PermissionDeniedException e) {
            return BigDecimal.ZERO;
        }

    }

    @Override
    public ViewModelPage<ClientePratica> searchPratichePerfezionate(final String provenienza, final String provenienzaDesc, final Date dataInizio, final Date dataFine,
                                                                    final String username, final Long currentAziendaId, final int firstElementIndex, final int pageSize, final String sortField,
                                                                    final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters, final long operatorId, final long aziendaId,
                                                                    final Role.RoleName fatturazioneReadRoleName) {
        Long operatorToSearchId = null;
        if (!StringUtils.isEmpty(username)) {
            operatorToSearchId = operatorService.findByUsernameViewModel(username).getId();
        }
        final BooleanExpression pratichePerfezionateExpression;
        try {
            pratichePerfezionateExpression = predicateHelper.getPredicateForFindPratichePerfezionate(
            		provenienza, provenienzaDesc, dataInizio, dataFine, operatorId, aziendaId, operatorToSearchId,
                    fatturazioneReadRoleName, currentAziendaId);
            final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

            final Page<Pratica> pratichePerfezionatePage = praticaRepository.findAll(pratichePerfezionateExpression,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePraticaList(pratichePerfezionatePage.getContent()),
                    pratichePerfezionatePage.getTotalPages(), pratichePerfezionatePage.getTotalElements());
        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }

    }

    @Override
    public BigDecimal sumTotaleMontantePratichePerfezionate(final String provenienza, final String provenienzaDesc, final Date dataInizio, final Date dataFine, 
    		final String username, final Long currentAziendaId, final long operatorId, final long aziendaId, 
    		final Role.RoleName fatturazioneReadRoleName) {
        Long operatorToSearchId = null;
        if (!StringUtils.isEmpty(username)) {
            operatorToSearchId = operatorService.findByUsernameViewModel(username).getId();
        }

        final BooleanExpression findPratichePerfezionateExpression;
        try {
            findPratichePerfezionateExpression = predicateHelper.getPredicateForFindPratichePerfezionate(
            		provenienza, provenienzaDesc, dataInizio, dataFine, operatorId, aziendaId, operatorToSearchId,
                    fatturazioneReadRoleName, currentAziendaId);
            final Iterable<Pratica> totalePerfezionateList = praticaRepository.findAll(findPratichePerfezionateExpression);

            return praticaUtil.sumMontante(totalePerfezionateList);
        } catch (final PermissionDeniedException e) {
            return BigDecimal.ZERO;
        }

    }

    @Override
    public BigDecimal sumTotaleProvvigionePratichePerfezionate(final String provenienza, final String provenienzaDesc, final Date dataInizio, final Date dataFine, 
    		final String username, final Long currentAziendaId, final long operatorId, final long aziendaId, 
    		final Role.RoleName fatturazioneReadRoleName) {
        Long operatorToSearchId = null;
        if (!StringUtils.isEmpty(username)) {
            operatorToSearchId = operatorService.findByUsernameViewModel(username).getId();
        }
        BooleanExpression findPratichePerfezionateExpression = null;
        try {
            findPratichePerfezionateExpression = predicateHelper.getPredicateForFindPratichePerfezionate(
            		provenienza, provenienzaDesc, dataInizio, dataFine, operatorId, aziendaId, operatorToSearchId,
                    fatturazioneReadRoleName, currentAziendaId);
            final Iterable<Pratica> totalePerfezionateList = praticaRepository.findAll(findPratichePerfezionateExpression);

            return praticaUtil.sumProvvigione(totalePerfezionateList);
        } catch (final PermissionDeniedException e) {
            return BigDecimal.ZERO;
        }

    }

    @Override
    @Transactional
    public void perfezionaPratiche(final List<ClientePratica> praticheDaPerfezionareViewModelList) {
        // TODO qua null non dovrebbe arrivare MAI o lanciamo eccezione
        if (praticheDaPerfezionareViewModelList != null) {
            for (final ClientePratica clientePratica : praticheDaPerfezionareViewModelList) {
                clientePratica.getPraticaViewModel().setPerfezionata(true);
                clientePratica.getPraticaViewModel().setDataPerf(new Date());
                clientePratica.getPraticaViewModel().setStatoPratica(StatoPratica.TERMINATA.getValue());
                final Pratica pratica = praticaViewModelCreator.createModel(clientePratica.getPraticaViewModel());
                pratica.setCliente(clienteViewModelCreator.createModel(clientePratica.getClienteViewModel()));
                praticaRepository.save(pratica);
            }
        }
    }

    @Override
    @Transactional
    public void perfezionaPratiche(final String provenienza, final String provenienzaDesc, final Date dataInizio, final Date dataFine, final boolean liquidata,
                                   final boolean liquidazione, final boolean quietanzata, final boolean perfezionamentoSelected, final Long currentAziendaId, final Role.RoleName fatturazioneWriteRoleName) {
        final BooleanExpression findPraticheDaPerfezionareExpression;
        try {
            findPraticheDaPerfezionareExpression = predicateHelper
                    .getPredicateForFindPraticheDaPerfezionare(provenienza, provenienzaDesc, dataInizio, dataFine, liquidazione, liquidata, quietanzata,
                            perfezionamentoSelected, fatturazioneWriteRoleName, currentAziendaId);
            final Iterable<Pratica> totalePerfezionateList = praticaRepository.findAll(findPraticheDaPerfezionareExpression);
            final List<ClientePratica> clientePraticaList = new LinkedList<>();
            totalePerfezionateList
                    .forEach(pratica -> clientePraticaList
                            .add(ClientePratica.builder().praticaViewModel(praticaViewModelCreator.createViewModel(pratica))
                                    .clienteViewModel(clienteViewModelCreator
                                            .createViewModel(clienteRepository.findOne(pratica.getCliente().getId())))
                                    .build()));
            perfezionaPratiche(clientePraticaList);
        } catch (final PermissionDeniedException e) {
            //da loggare
        }

    }

    @Override
    @Transactional
    public void updateDecorrenzaPratica(final long codicePratica, final Date decorrenzaDaModificare)
            throws CannotUpdateDecorrenzaException {
        try {
            final Pratica pratica = praticaRepository.findOne(codicePratica);
            pratica.setDecorrenza(decorrenzaDaModificare);
            pratica.setScadenzaPratica(praticaUtil.calcScadenzaPratica(pratica));
            pratica.setDataRinnovo(praticaUtil.calcDataRinnovo(pratica));
            praticaRepository.save(pratica);
        } catch (final DataAccessException e) {
            throw new CannotUpdateDecorrenzaException(e);
        }
    }

    @Override
    public ClientePratica getPratica(final long id) {
        final Pratica pratica = praticaRepository.findOne(id);
        return ClientePratica.builder().clienteViewModel(clienteViewModelCreator.createViewModel(pratica.getCliente()))
                .praticaViewModel(praticaViewModelCreator.createViewModel(pratica)).build();
    }
    
    @Override
	public List<String> getProvenienzaDescList() {
		final List<String> provenienzaDescList = clienteRepository.getDistinctProvenienzaDesc();

        return provenienzaDescList;
	}

}
