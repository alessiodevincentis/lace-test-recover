package com.woonders.lacemscommon.service;

import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.db.entity.Azienda;

/**
 * Created by emanuele on 03/01/17.
 * Manages the agency info
 * [azienda] table
 */
public interface AziendaService {

	AziendaViewModel save(AziendaViewModel aziendaViewModel);

	/**
	 * Returns a list of agency (if the agency has multiple sub agencies, there will be more, otherwise only one item)
	 */
	List<AziendaViewModel> findAll();

	AziendaViewModel getOne(long id);
	
	Azienda findOne(long id);

	long count();
}
