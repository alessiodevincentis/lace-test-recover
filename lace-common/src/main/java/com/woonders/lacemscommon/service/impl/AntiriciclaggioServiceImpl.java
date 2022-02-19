package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.model.util.ClientePraticaUtil;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.PraticaViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.exception.PermissionDeniedException;
import com.woonders.lacemscommon.service.AntiriciclaggioService;
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
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AntiriciclaggioServiceImpl implements AntiriciclaggioService {

    public static final String NAME = "antiriciclaggioServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private ClientePraticaUtil clientePraticaUtil;

    @PreAuthorize("hasAuthority('ANTI_RICICLAGGIO_OUT') or hasAuthority('ANTI_RICICLAGGIO_READ')")
    @Override
    public ViewModelPage<ClientePratica> findPraticheAntiriciclaggio(String cf, Date dataInizio, Date dataFine,
                                                                     TipoRicercaAntiriciclaggio tipoRicercaAntiriciclaggio, int firstElementIndex, int pageSize,
                                                                     String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters,
                                                                     Long currentAziendaId, Role.RoleName antiriciclaggioReadRoleName, long operatorId) {
        // filters non usati per ora nella tabella

        QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        Page<Pratica> praticaPage;
        try {
            praticaPage = praticaRepository
                    .findAll(
                            predicateHelper.getPredicateForAntiriciclaggio(cf, dataInizio, dataFine,
                                    tipoRicercaAntiriciclaggio, antiriciclaggioReadRoleName, operatorId, currentAziendaId),
                            new PageRequest(firstElementIndex / pageSize, pageSize, qSort));
            return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePraticaList(praticaPage.getContent()),
                    praticaPage.getTotalPages(), praticaPage.getTotalElements());
        } catch (PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }

    }

}
