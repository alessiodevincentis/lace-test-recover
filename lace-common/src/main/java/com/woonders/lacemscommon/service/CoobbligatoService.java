package com.woonders.lacemscommon.service;

import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.CoobbligatoViewModel;

/**
 * Created by emanuele on 03/01/17.
 * Manages procedures' guarantors info
 * [coobbligato] table
 */
public interface CoobbligatoService {

	/**
	 * Returns a list containing all the guarantors of a procedure by a procedure id
	 */
	List<CoobbligatoViewModel> findDistinctByPratica_CodicePratica(long codicePratica);

	/**
	 * Deletes a guarantor by their id
	 */
	void delete(CoobbligatoViewModel coobbligatoViewModel);
}
