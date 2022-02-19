package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.FinanziariaViewModelCreator;
import com.woonders.lacemscommon.db.repository.FinanziariaRepository;
import com.woonders.lacemscommon.service.FinanziariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by emanuele on 02/01/17.
 */
@Service
@Transactional(readOnly = true)
public class FinanziariaServiceImpl implements FinanziariaService {

    public static final String NAME = "finanziariaServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private FinanziariaRepository finanziariaRepository;
    @Autowired
    private FinanziariaViewModelCreator finanziariaViewModelCreator;

    @Override
    public List<FinanziariaViewModel> findAll() {
        return finanziariaViewModelCreator.createViewModelList(finanziariaRepository.findAll());
    }

    @Override
    public FinanziariaViewModel getOne(Long id) {
        return finanziariaViewModelCreator.createViewModel(finanziariaRepository.getOne(id));
    }
}
