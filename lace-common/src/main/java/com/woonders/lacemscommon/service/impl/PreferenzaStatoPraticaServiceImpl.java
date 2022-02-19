package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.PreferenzaStatoPraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.PreferenzaStatoPraticaViewModelCreator;
import com.woonders.lacemscommon.db.repository.PreferenzaStatoPraticaRepository;
import com.woonders.lacemscommon.service.PreferenzaStatoPraticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PreferenzaStatoPraticaServiceImpl implements PreferenzaStatoPraticaService {

    public static final String NAME = "preferenzaStatoPraticaServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PreferenzaStatoPraticaRepository preferenzaStatoPraticaRepository;
    @Autowired
    private PreferenzaStatoPraticaViewModelCreator preferenzaStatoPraticaViewModelCreator;

    @Override
    public List<PreferenzaStatoPraticaViewModel> getAllPreferenzaStatoPraticaByAziendaId(long aziendaId) {
        return preferenzaStatoPraticaViewModelCreator.fromModelList(preferenzaStatoPraticaRepository.findByAzienda_Id(aziendaId));
    }

    @PreAuthorize("hasAuthority('PREFERENZE_WRITE_SUPER') or (hasAuthority('PREFERENZE_WRITE') and @authorizationConditionChecker.isSameAziendaId(#preferenzaStatoPraticaViewModelList.get(0).aziendaViewModel.id))")
    @Override
    @Transactional
    public List<PreferenzaStatoPraticaViewModel> save(
            List<PreferenzaStatoPraticaViewModel> preferenzaStatoPraticaViewModelList) {
        return preferenzaStatoPraticaViewModelCreator.fromModelList(preferenzaStatoPraticaRepository
                .save(preferenzaStatoPraticaViewModelCreator.createModelList(preferenzaStatoPraticaViewModelList)));
    }

}
