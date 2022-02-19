package com.woonders.lacemscommon.service;

import java.util.List;
import java.util.Map;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.CampagnaSmsViewModel;
import com.woonders.lacemscommon.app.viewmodel.CampagnaViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Campagna.CampagnaType;
import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;

/**
 * Created by Emanuele on 27/03/2017.
 * Manages marketing campaigns
 * [campagna] and [campagna_sms] tables
 */
public interface CampagnaMarketingService {

	/**
	 * Saves a marketing campaign which could be sent later
	 */
	CampagnaViewModel save(String nameCampagna, String descriptionCampagna, String messageToSend,
			CampagnaType campagnaType, boolean campagnaImport, long idCreator,
			FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel,
			List<ClienteViewModel> clienteViewModelList, int totaleMessaggi, boolean isClientiReadSuper,
			boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll,
			boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorAziendaId, Long currentAziendaId)
			throws NotEnoughCreditException;

	/**
	 * Starts sending all the sms of a marketing campaign
	 */
	void startSmsSendingCampagna(long id, long idSender, long currentAziendaId);

	/**
	 * Saves sent info details of a marketing campaign
	 */
	void writeCampagnaInfoSent(long id, long idSender) throws NotEnoughCreditException;

	/**
	 * Saves and start sending all the sms of a marketing campaign
	 */
	CampagnaViewModel saveAndSendCampagna(String nameCampagna, String descriptionCampagna, String messageToSend,
			CampagnaType campagnaType, boolean campagnaImport, long idCreator,
			FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel,
			List<ClienteViewModel> clienteViewModelList, int totaleMessaggi, boolean isClientiReadSuper,
			boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll,
			boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorAziendaId, Long currentAziendaId)
			throws NotEnoughCreditException;

	/**
	 * Returns a paged list of all the marketing campaign already saved or sent
	 */
	ViewModelPage<CampagnaViewModel> getCampagnaList(Long currentAziendaId, int firstElementIndex, int pageSize,
			String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);

	/**
	 * Returns a paged list of all the sms in a marketing campaign
	 */
	ViewModelPage<CampagnaSmsViewModel> getCampagnaSmsList(long idCampagna, int firstElementIndex, int pageSize,
			String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);

	/**
	 * Returns a paged list with the result details of a marketing campaign just sent
	 */
	ViewModelPage<ClienteViewModel> getNuovaCampagnaResultList(int firstElementIndex, int pageSize,
			FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel, boolean isClientiReadSuper,
			boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll,
			boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorId,
			long currentOperatorAziendaId, Long currentAziendaId);

	/**
	 * Returns a customer info
	 */
	ClienteViewModel getCliente(long id);

	/**
	 * Returns details of a marketing campaign by a marketing campaign id
	 */
	CampagnaViewModel findCampagna(long id);

	/**
	 * Returns details of an sms sent in a marketing campaign by the sms id
	 */
	CampagnaSmsViewModel findCampagnaSms(long id);

	/**
	 * Returns a count of sms status by marketing campaign id and sms status
	 */
	long countByEsitoCampagna(long idCampagna, EsitoSmsNotificaLead esito);

	/**
	 * Returns a count of sms status by marketing campaign id and sms status and customer/lead type
	 */
	long countByEsitoCampagnaAndTipo(long idCampagna, EsitoSmsNotificaLead esito, Tipo tipo);

	/**
	 * Imports as lead any recipient who successfully got an sms from a marketing campaign
	 */
	void importaNominativi(long idCampagna);

	/**
	 * Counts how many not sent yet sms of a marketing campaign by a marketing campaign id
	 */
	long countByListEsitoCampagnaSms(long idCampagna);
}
