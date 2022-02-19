package com.woonders.lacemscommon.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ClienteTrattenuta;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.TrattenuteViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Trattenute;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.TrattenuteRepository;
import com.woonders.lacemscommon.exception.PermissionDeniedException;
import com.woonders.lacemscommon.service.DashboardNominativoService;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemscommon.util.PredicateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardNominativoServiceImpl extends AbstractAppServiceImpl implements DashboardNominativoService {

    public static final String NAME = "dashboardNominativoServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private TrattenuteRepository trattenuteRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private TrattenuteViewModelCreator trattenuteViewModelCreator;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private PredicateHelper predicateHelper;

    @Override
    public ViewModelPage<ClienteViewModel> getNominativiInDataRecall(final Long currentAziendaId, final long currentOperatorId,
                                                                     final Role.RoleName nominativiReadRoleName, final Date date, final int firstElementIndex,
                                                                     final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder,
                                                                     final Map<QueryDSLHelper.TableField, Object> filters) {
        final Page<Cliente> clientePage;

        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        try {
            clientePage = clienteRepository.findAll(
                    predicateHelper.getPredicateForClienteByDataRecallNominativo(currentAziendaId, currentOperatorId,
                            nominativiReadRoleName, date)
                            .and(queryDSLHelper.createFilterPredicate(filters)),
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(clienteViewModelCreator.createViewModelList(clientePage.getContent()),
                    clientePage.getTotalPages(), clientePage.getTotalElements());
        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }

    }

    @Override
    public long countNominativiInDataRecall(final Long currentAziendaId, final long currentOperatorId,
                                            final Role.RoleName nominativiReadRoleName, final Date date) {
        try {
            return clienteRepository.count(predicateHelper.getPredicateForClienteByDataRecallNominativo(currentAziendaId, currentOperatorId,
                    nominativiReadRoleName, date));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    @Override
    public ViewModelPage<ClienteTrattenuta> findImpegniRinnovabili(final Long currentAziendaId, final long currentOperatorId,
                                                                   final Role.RoleName nominativiReadRoleName, final int days, final int firstElementIndex, final int pageSize,
                                                                   final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters) {

        final Date dataRinnovoToCheck = DateToCalendar.addDays(new Date(), days);

        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        BooleanExpression impegniRinnovabiliWithFilterExpression = null;
        try {
            impegniRinnovabiliWithFilterExpression = predicateHelper.getPredicateForImpegniRinnovabili(currentAziendaId, currentOperatorId, nominativiReadRoleName, dataRinnovoToCheck)
                    .and(queryDSLHelper.createFilterPredicate(filters));
            final Page<Trattenute> trattenuteIterable = trattenuteRepository.findAll(impegniRinnovabiliWithFilterExpression,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(generateClienteTrattenuteViewModel(trattenuteIterable.getContent()),
                    trattenuteIterable.getTotalPages(), trattenuteIterable.getTotalElements());
        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }

    }

    private List<ClienteTrattenuta> generateClienteTrattenuteViewModel(final Iterable<Trattenute> trattenuteIterable) {
        final List<ClienteTrattenuta> clienteTrattenutaList = new LinkedList<>();
        trattenuteIterable.forEach(trattenute -> {
            final ClienteTrattenuta clienteTrattenuta = new ClienteTrattenuta();
            clienteTrattenuta.setClienteViewModel(clienteViewModelCreator.createViewModel(trattenute.getCliente()));
            clienteTrattenuta.setTrattenuteViewModel(trattenuteViewModelCreator.createViewModel(trattenute));
            clienteTrattenutaList.add(clienteTrattenuta);
        });

        return clienteTrattenutaList;
    }

    @Override
    public long countImpegniRinnovabili(final Long currentAziendaId, final long currentOperatorId,
                                        final Role.RoleName nominativiReadRoleName, final int days) {
        final Date dataRinnovoToCheck = DateToCalendar.addDays(new Date(), days);
        try {
            return trattenuteRepository.count(predicateHelper.getPredicateForImpegniRinnovabili(currentAziendaId, currentOperatorId, nominativiReadRoleName, dataRinnovoToCheck));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    @Override
    @Transactional
    public void setRinnovoTrattenute(final TrattenuteViewModel trattenuteViewModel) {
        final Trattenute trattenute = trattenuteRepository.findOne(trattenuteViewModel.getCodStip());
        trattenute.setNotiCoes(trattenute.getDataRinnovoTrat());
        trattenuteRepository.save(trattenute);
    }

    @Override
    public long countNewLead(final Long currentAziendaId, final long currentOperatorId,
                             final Role.RoleName nominativiReadRoleName) {
        try {
            return clienteRepository.count(predicateHelper.getPredicateForCountNewLead(currentAziendaId, currentOperatorId, nominativiReadRoleName));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    @Override
    public ClienteViewModel getNominativo(final long id) {
        final Cliente cliente = clienteRepository.findOne(id);
        return clienteViewModelCreator.createViewModel(cliente);
    }

    @Override
    public ClienteTrattenuta getTrattenuta(final long id) {
        final Trattenute trattenute = trattenuteRepository.findOne(id);
        final ClienteTrattenuta clienteTrattenuta = new ClienteTrattenuta();
        clienteTrattenuta.setClienteViewModel(clienteViewModelCreator.createViewModel(trattenute.getCliente()));
        clienteTrattenuta.setTrattenuteViewModel(trattenuteViewModelCreator.createViewModel(trattenute));
        return clienteTrattenuta;
    }

    @Override
    public long countNominativiOmessi(final Long currentAziendaId, final long currentOperatorId,
                                      final Role.RoleName nominativiReadRoleName) {
        try {
            return clienteRepository.count(predicateHelper.getPredicateForDataRecallNominativoExpired(currentAziendaId, currentOperatorId, nominativiReadRoleName));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    @PreAuthorize("hasAuthority('NOMINATIVI_READ_SUPER') or (hasAuthority('NOMINATIVI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('NOMINATIVI_READ_OWN')")
    @Override
    public ViewModelPage<ClienteViewModel> getNominativiWithDataRecallExpired(final Long currentAziendaId, final long currentOperatorId,
                                                                              final Role.RoleName nominativiReadRoleName, final int firstElementIndex,
                                                                              final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder,
                                                                              final Map<QueryDSLHelper.TableField, Object> filters) {
        final Page<Cliente> clientePage;

        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        try {
            clientePage = clienteRepository.findAll(
                    predicateHelper.getPredicateForDataRecallNominativoExpired(currentAziendaId, currentOperatorId,
                            nominativiReadRoleName)
                            .and(queryDSLHelper.createFilterPredicate(filters)),
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

            return new ViewModelPageImpl<>(clienteViewModelCreator.createViewModelList(clientePage.getContent()),
                    clientePage.getTotalPages(), clientePage.getTotalElements());
        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }
    }

    @Override
    @Transactional
    public void setNullDataRecallNominativo(final long id) {
        final Cliente cliente = clienteRepository.findOne(id);
        cliente.setDataRecallNominativo(null);
    }

}
