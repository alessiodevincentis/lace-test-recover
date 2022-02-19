package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.ValutazioneAmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ValutazioneAmministrazioneViewModelCreator;
import com.woonders.lacemscommon.db.repository.ValutazioneAmministrazioneRepository;
import com.woonders.lacemscommon.service.ValutazioneAmministrazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ValutazioneAmministrazioneServiceImpl implements ValutazioneAmministrazioneService {

    public static final String NAME = "valutazioneAmministrazioneServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ValutazioneAmministrazioneViewModelCreator valutazioneAmministrazioneViewModelCreator;
    @Autowired
    private ValutazioneAmministrazioneRepository valutazioneAmministrazioneRepository;

    @Override
    @Transactional
    public void delete(long idValutazione) {
        valutazioneAmministrazioneRepository.delete(idValutazione);
    }

    @Override
    public List<ValutazioneAmministrazioneViewModel> findDistinctByAmministrazione_CodiceAmm(long amministrazioneId) {
        return valutazioneAmministrazioneViewModelCreator.createViewModelList(
                valutazioneAmministrazioneRepository.findDistinctByAmministrazione_CodiceAmm(amministrazioneId));
    }

}
