package com.woonders.lacemscommon.service;

import java.util.Date;
import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;

/**
 * Created by emanuele on 04/01/17.
 * Utils service to retrieve customers/leads info
 */
public interface CheckNominativoService {

	/**
	 * Returns a list of possible customers/leads matches based on surname, name and date of birth
	 */
	List<ClienteViewModel> findNominativoByCognomeAndNomeAndDataNascita(String cognome, String nome, Date dataNascita);
}
