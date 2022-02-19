package com.woonders.lacemscommon.service;

import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.exception.ItemNotFoundException;

/**
 * Manages the list of borrower employers, government organizations, public or private companies
 * [amministrazione] table
 */
public interface AmministrazioneService {

	AmministrazioneViewModel save(AmministrazioneViewModel amministrazioneViewModel);

	/**
	 * Returns a list of [amministrazione] based on their name when ragioneSociale is contained into it
	 */
	List<AmministrazioneViewModel> findDistinctByRagioneSocialeContaining(String ragioneSociale);

	/**
	 * Returns a list of [amministrazione] based on their name when ragioneSociale is exactly it
	 */
	AmministrazioneViewModel findByRagioneSociale(String ragioneSociale) throws ItemNotFoundException;

	/**
	 * Returns a list of [amministrazione] based on their vat code
	 */
	List<AmministrazioneViewModel> findByPiva(String piva);

	/**
	 * Returns a list of [amministrazione] based on their tax code
	 */
	List<AmministrazioneViewModel> findByCodiceFiscale(String codiceFiscale);

	AmministrazioneViewModel findOne(long id) throws ItemNotFoundException;

	/**
	 * Returns a list of [amministrazione] based on the customer who uses it
	 */
	List<AmministrazioneViewModel> findDistinctByCliente_Id(long clienteId);

	/**
	 * TODO
	 */
	List<AmministrazioneViewModel> completeAmministrazione(final String query);
}
