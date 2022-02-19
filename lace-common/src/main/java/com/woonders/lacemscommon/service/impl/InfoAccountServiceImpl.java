package com.woonders.lacemscommon.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.viewmodel.InfoAccountViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.InfoAccountViewModelCreator;
import com.woonders.lacemscommon.db.entity.QOperator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.repository.InfoAccountRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.service.InfoAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;

@Service
@Transactional(readOnly = true)
@Slf4j
public class InfoAccountServiceImpl implements InfoAccountService {

    public static final String NAME = "infoAccountServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private InfoAccountRepository infoAccountRepository;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private InfoAccountViewModelCreator infoAccountViewModelCreator;

    @Override
    public InfoAccountViewModel getOne() {
        return infoAccountViewModelCreator.createViewModel(infoAccountRepository.findOne(1L));
    }

    @Override
    public long getOperatorsNumber() {
        final Collection<Role.RoleName> roleNameToAvoidCollection = new LinkedList<>();
        roleNameToAvoidCollection.add(Role.RoleName.ANTI_RICICLAGGIO_OUT);
        final BooleanExpression booleanExpression = QOperator.operator.role.any().roleName.notIn(roleNameToAvoidCollection);
        return operatorRepository.count(booleanExpression);
    }

}
