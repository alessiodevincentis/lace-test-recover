package com.woonders.lacemscommon.service.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.service.CheckNominativoService;
import com.woonders.lacemscommon.util.PredicateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CheckNominativoServiceImpl implements CheckNominativoService {

    public static final String NAME = "checkNominativoServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private PredicateHelper predicateHelper;

    @Override
    public List<ClienteViewModel> findNominativoByCognomeAndNomeAndDataNascita(final String cognome, final String nome,
                                                                               final Date dataNascita) {

        BooleanExpression nominativiExpression = predicateHelper
                .getPredicateForMatchNominativiByCognomeAndNomeAndDataNascita(cognome, nome, dataNascita);

        return clienteViewModelCreator
                .createViewModelList(Lists.newArrayList(clienteRepository.findAll(nominativiExpression)));
    }

}
