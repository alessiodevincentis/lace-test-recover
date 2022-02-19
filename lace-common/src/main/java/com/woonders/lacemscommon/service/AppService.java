package com.woonders.lacemscommon.service;

import java.util.Date;
import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.*;

public interface AppService {

	/**
	 * Returns the customer related to a procedure by a procedure id
	 */
	ClienteViewModel findClienteByIdPratica(final long praticaId);

	/**
	 * Returns a list of all the procedure related to a given customer by a customer id
	 */
	List<PraticaViewModel> findPraticheByIdCliente(final long clienteId);

	/**
	 * Returns a list of procedure opened by another agency and not yet settled related to a given customer by a customer id
	 */
	List<TrattenuteViewModel> findTrattenuteByIdCliente(final long clienteID);

	/**
	 * Returns the details of a customer by a customer id
	 */
	ClienteViewModel findClienteByIdCliente(final long clienteId);

	/**
	 * Returns residence info of a customer by a customer id
	 */
	ResidenzaViewModel findResidenzaByIdCliente(final long clienteId);

	/**
	 * Returns banking account info of a customer by a customer id
	 */
	ContoViewModel findContoByIdCliente(final long clienteId);

	/**
	 * Returns ID card info of a customer by a customer id
	 */
	DocumentoViewModel findDocumentoByIdCliente(final long clienteId);

	/**
	 * Returns a list of open quotes related to a customer by a customer id
	 */
	List<PreventivoViewModel> findPreventiviByIdCliente(final long clienteId);

	/**
	 * Returns a list of settled procedure related to a procedure by a procedure id
	 */
	List<EstinzioneViewModel> findEstinzioniByIdPratica(final long praticaId);

	/**
	 * Returns a list of guarantors related to a procedure by a procedure id
	 */
	List<CoobbligatoViewModel> findCoobbligatoByIdPratica(final long praticaId);

	/**
	 * Returns the procedure details by a procedure id
	 */
	PraticaViewModel findPraticaByCodicePratica(final long praticaId);

	/**
	 * TODO
	 */
	Date getDataNotificaByStatoPraticaAndAziendaId(String statoPratica, long aziendaId);
}
