package com.woonders.lacemscommon.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.QCliente;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.service.StatoNominativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StatoNominativoServiceImpl implements StatoNominativoService {

    public static final String NAME = "statoNominativoServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;

    //disabilitato per ora per la dashboard
    /*@PreAuthorize("hasAuthority('NOMINATIVI_READ_SUPER') or (hasAuthority('NOMINATIVI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('NOMINATIVI_READ_OWN')")*/
    @Override
    public long countNominativiByStatoNominativo(final Long currentAziendaId, final long currentOperatorId,
                                                 final Role.RoleName nominativiReadRoleName, final String statoNominativo) {
        final QCliente qCliente = QCliente.cliente;
        final BooleanExpression statoNominativoIsNotNull = qCliente.statoNominativo.isNotNull();
        final BooleanExpression countNominativiExpression = qCliente.tipo.eq(Tipo.NOMINATIVO.getValue())
                .and(statoNominativoIsNotNull).and(qCliente.statoNominativo.eq(statoNominativo));

        if (nominativiReadRoleName == null) {
            return 0;
        }

        switch (nominativiReadRoleName) {
            case NOMINATIVI_READ_OWN:
                return clienteRepository
                        .count(countNominativiExpression.and(qCliente.operatoreNominativo.id.eq(currentOperatorId)));
            case NOMINATIVI_READ_ALL:
                return clienteRepository
                        .count(countNominativiExpression.and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId)));
            case NOMINATIVI_READ_SUPER:
                if (currentAziendaId != null) {
                    return clienteRepository.count(
                            countNominativiExpression.and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId)));
                } else {
                    return clienteRepository.count(countNominativiExpression);
                }
            default:
                return 0;
        }

    }

    @PreAuthorize("hasAuthority('NOMINATIVI_READ_SUPER') or (hasAuthority('NOMINATIVI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('NOMINATIVI_READ_OWN')")
    @Override
    public ViewModelPage<ClienteViewModel> getNominativiByStatoNominativo(final Long currentAziendaId, final long currentOperatorId,
                                                                          final Role.RoleName nominativiReadRoleName, final String statoNominativo, final int firstElementIndex,
                                                                          final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder,
                                                                          final Map<QueryDSLHelper.TableField, Object> filters) {
        Page<Cliente> clientePage = null;

        final QCliente qCliente = QCliente.cliente;
        final BooleanExpression statoNominativoEquals = qCliente.statoNominativo.eq(statoNominativo);
        final BooleanExpression tipoNominativoEquals = qCliente.tipo.eq(Tipo.NOMINATIVO.getValue());

        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        final BooleanExpression statoNominativoIsNotNull = qCliente.statoNominativo.isNotNull();
        BooleanExpression nominativiDaLavorareExpression = statoNominativoIsNotNull.and(statoNominativoEquals).and(tipoNominativoEquals);
        nominativiDaLavorareExpression = nominativiDaLavorareExpression
                .and(queryDSLHelper.createFilterPredicate(filters));

        final PageRequest pageRequest = new PageRequest(firstElementIndex / pageSize, pageSize, qSort);

        switch (nominativiReadRoleName) {
            case NOMINATIVI_READ_OWN:
                clientePage = clienteRepository
                        .findAll(nominativiDaLavorareExpression.and(qCliente.operatoreNominativo.id.eq(currentOperatorId)), pageRequest);
                break;
            case NOMINATIVI_READ_ALL:
                clientePage = clienteRepository
                        .findAll(nominativiDaLavorareExpression.and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId)), pageRequest);
                break;
            case NOMINATIVI_READ_SUPER:
                if (currentAziendaId != null) {
                    clientePage = clienteRepository.findAll(
                            nominativiDaLavorareExpression.and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId)), pageRequest);
                } else {
                    clientePage = clienteRepository.findAll(nominativiDaLavorareExpression, pageRequest);
                }
                break;
        }

        if (clientePage != null) {
            return new ViewModelPageImpl<>(clienteViewModelCreator.createViewModelList(clientePage.getContent()),
                    clientePage.getTotalPages(), clientePage.getTotalElements());
        } else {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }
    }

    @Override
    public boolean isContainsDataRecallNominativoLessToday(final String statoNominativo, final Role.RoleName nominativiReadRoleName,
                                                           final long currentOperatorId, final Long currentAziendaId) {
        final String tipoNominativo = Tipo.NOMINATIVO.getValue();
        long result = 0;

        final Date today = new Date();
        final QCliente qCliente = QCliente.cliente;
        final BooleanExpression statoNominativoEquals = qCliente.statoNominativo.eq(statoNominativo);
        final BooleanExpression tipoNominativoEquals = qCliente.tipo.eq(tipoNominativo);
        final BooleanExpression statoNominativoIsNotNull = qCliente.statoNominativo.isNotNull();
        final BooleanExpression dataRecallIsNotNull = qCliente.dataRecallNominativo.isNotNull();
        final BooleanExpression dataRecallIsLessOrEquals = qCliente.dataRecallNominativo.loe(today);
        final BooleanExpression statoNominativoDataRecallExpired = tipoNominativoEquals
                .and(statoNominativoIsNotNull).and(statoNominativoEquals).and(dataRecallIsNotNull)
                .and(dataRecallIsLessOrEquals);


        switch (nominativiReadRoleName) {
            case NOMINATIVI_READ_OWN:
                result = clienteRepository
                        .count(statoNominativoDataRecallExpired.and(qCliente.operatoreNominativo.id.eq(currentOperatorId)));
                break;
            case NOMINATIVI_READ_ALL:
                result = clienteRepository
                        .count(statoNominativoDataRecallExpired.and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId)));
                break;
            case NOMINATIVI_READ_SUPER:
                if (currentAziendaId != null) {
                    result = clienteRepository.count(
                            statoNominativoDataRecallExpired.and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId)));
                    break;
                } else {
                    result = clienteRepository.count(statoNominativoDataRecallExpired);
                    break;
                }
        }
        return (result > 0);
    }

}
