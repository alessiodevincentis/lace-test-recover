package com.woonders.lacemscommon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.AziendaViewModelCreator;
import com.woonders.lacemscommon.db.entity.Azienda;
import com.woonders.lacemscommon.db.repository.AziendaRepository;
import com.woonders.lacemscommon.service.AziendaService;

/**
 * Created by emanuele on 03/01/17.
 */
@Service
@Transactional(readOnly = true)
public class AziendaServiceImpl implements AziendaService {

	public static final String NAME = "aziendaServiceImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Autowired
	private AziendaRepository aziendaRepository;
	@Autowired
	private AziendaViewModelCreator aziendaViewModelCreator;

	// @PreAuthorize("hasAuthority('DATI_AZIENDA_WRITE_SUPER') or
	// (hasAuthority('DATI_AZIENDA_WRITE') and
	// @authorizationConditionChecker.isSameAziendaId(#aziendaViewModel.id))")
	// per ora disabilitato perche se ho il permesso per modificare le
	// preferenze ma non azienda, questo fallisce perche salviamo anche azienda
	// quando salviamo le preferenze perche alcune sono qui! cazzo!
	@Override
	@Transactional
	public AziendaViewModel save(AziendaViewModel aziendaViewModel) {
		return aziendaViewModelCreator
				.createViewModel(aziendaRepository.save(aziendaViewModelCreator.createModel(aziendaViewModel)));
	}

	@Override
	public List<AziendaViewModel> findAll() {
		return aziendaViewModelCreator.createViewModelList(aziendaRepository.findAll());
	}

	@Override
	public AziendaViewModel getOne(long id) {
		return aziendaViewModelCreator.createViewModel(aziendaRepository.findOne(id));
	}
	
	@Override
	public Azienda findOne(long id) {
		return aziendaRepository.findOne(id);
	}

	@Override
	public long count() {
		return aziendaRepository.count();
	}
}
