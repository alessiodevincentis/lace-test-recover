package com.woonders.lacemscommon.service;

import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;

/**
 * Created by emanuele on 02/01/17.
 * Manages the list of financial companies which actually lends the money to the borrower
 * [finanziaria] table
 */
public interface FinanziariaService {

	/**
	 * Returns all the financial companies currently available
	 */
	List<FinanziariaViewModel> findAll();

	FinanziariaViewModel getOne(final Long id);

}
