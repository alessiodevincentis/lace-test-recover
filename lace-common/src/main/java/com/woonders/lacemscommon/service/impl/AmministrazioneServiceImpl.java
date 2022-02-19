package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.AmministrazioneViewModelCreator;
import com.woonders.lacemscommon.db.entity.Amministrazione;
import com.woonders.lacemscommon.db.repository.AmministrazioneRepository;
import com.woonders.lacemscommon.db.repository.ValutazioneAmministrazioneRepository;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.AmministrazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AmministrazioneServiceImpl implements AmministrazioneService {

    public static final String NAME = "amministrazioneServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private AmministrazioneViewModelCreator amministrazioneViewModelCreator;
    @Autowired
    private AmministrazioneRepository amministrazioneRepository;
    @Autowired
    private ValutazioneAmministrazioneRepository valutazioneAmministrazioneRepository;

    @PreAuthorize("hasAuthority('AMMINISTRAZIONI_WRITE')")
    @Override
    @Transactional
    public AmministrazioneViewModel save(AmministrazioneViewModel amministrazioneViewModel) {
        Amministrazione amministrazione = amministrazioneViewModelCreator.createModel(amministrazioneViewModel);
        if (amministrazione.getValutazioneAmministrazione() != null) {
            amministrazione.getValutazioneAmministrazione().forEach(
                    valutazioneAmministrazione -> valutazioneAmministrazione.setAmministrazione(amministrazione));
        }
        Amministrazione amministrazioneSalvata = amministrazioneRepository.save(amministrazione);
        amministrazioneSalvata.setValutazioneAmministrazione(valutazioneAmministrazioneRepository
                .save(amministrazione.getValutazioneAmministrazione()).stream().collect(Collectors.toSet()));
        return amministrazioneViewModelCreator.createViewModel(amministrazioneSalvata);
    }

    @Override
    public List<AmministrazioneViewModel> findDistinctByRagioneSocialeContaining(String ragioneSociale) {
        return amministrazioneViewModelCreator.createViewModelList(
                amministrazioneRepository.findDistinctByRagioneSocialeContainingIgnoreCase(ragioneSociale));
    }

    // TODO controllare se serve lancia l'eccezione
    @Override
    public AmministrazioneViewModel findByRagioneSociale(String ragioneSociale) throws ItemNotFoundException {
        Amministrazione amministrazione = amministrazioneRepository.findByRagioneSocialeIgnoreCase(ragioneSociale);
        if (amministrazione != null) {
            return amministrazioneViewModelCreator.createViewModel(amministrazione);
        } else {
            throw new ItemNotFoundException();
        }
    }

    // TODO controllare exception
    @Override
    public List<AmministrazioneViewModel> findByPiva(String piva) {
        return amministrazioneViewModelCreator
                .createViewModelList(amministrazioneRepository.findByPivaIgnoreCase(piva));
    }

    // TODO controllare exception
    @Override
    public List<AmministrazioneViewModel> findByCodiceFiscale(String codiceFiscale) {
        return amministrazioneViewModelCreator
                .createViewModelList(amministrazioneRepository.findByCodiceFiscaleIgnoreCase(codiceFiscale));
    }

    @Override
    public AmministrazioneViewModel findOne(long id) throws ItemNotFoundException {
        Amministrazione amministrazione = amministrazioneRepository.findOne(id);
        if (amministrazione != null) {
            return amministrazioneViewModelCreator.createViewModel(amministrazioneRepository.findOne(id));
        } else {
            throw new ItemNotFoundException();
        }
    }

    @Override
    public List<AmministrazioneViewModel> completeAmministrazione(final String query) {
        return amministrazioneViewModelCreator
                .createViewModelList(amministrazioneRepository.findDistinctByRagioneSocialeContainingIgnoreCase(query));
    }

    @Override
    public List<AmministrazioneViewModel> findDistinctByCliente_Id(long clienteId) {
        return amministrazioneViewModelCreator
                .fromModelList(amministrazioneRepository.findDistinctByCliente_Id(clienteId));
    }

}
