package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.CoobbligatoViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.CoobbligatoViewModelCreator;
import com.woonders.lacemscommon.db.entity.Coobbligato;
import com.woonders.lacemscommon.db.repository.CoobbligatoRepository;
import com.woonders.lacemscommon.service.CoobbligatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by emanuele on 03/01/17.
 */
// TODO da implementare e sostituire in giro!
@Service
@Transactional(readOnly = true)
public class CoobbligatoServiceImpl implements CoobbligatoService {

    public static final String NAME = "coobbligatoServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private CoobbligatoViewModelCreator coobbligatoViewModelCreator;
    @Autowired
    private CoobbligatoRepository coobbligatoRepository;

    @Override
    @Transactional
    public void delete(CoobbligatoViewModel coobbligatoViewModel) {
        coobbligatoRepository.delete(coobbligatoViewModel.getId());
    }

    @Override
    public List<CoobbligatoViewModel> findDistinctByPratica_CodicePratica(long codicePratica) {
        List<Coobbligato> coobbligatoList = coobbligatoRepository.findDistinctByPratica_CodicePratica(codicePratica);
        if (coobbligatoList != null) {
            return coobbligatoViewModelCreator.createViewModelList(coobbligatoList);
        }
        return null;
    }
}
