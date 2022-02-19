package com.woonders.lacemscommon.service;

import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.db.entity.Campagna.CampagnaType;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;

/**
 * Created by Emanuele on 01/04/2017.
 * Facade to manage marketing campaigns
 * [campagna] and [campagna_sms] tables
 */
public interface CampagnaMarketingFacade {

	/**
	 * Saves and start sending a marketing campaign
	 */
	void saveAndSendCampagna(String nameCampagna, String descriptionCampagna, String messageToSend,
			CampagnaType campagnaType, boolean campagnaImport, long idCreator,
			FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel,
			List<ClienteViewModel> clienteViewModelList, int totaleMessaggi, boolean isClientiReadSuper,
			boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll,
			boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorAziendaId, Long currentAziendaId)
			throws NotEnoughCreditException;

	/**
	 * Imports as lead any recipient who successfully got an sms from a marketing campaign
	 */
	void importaNominativi(long idCampagna);

	/**
	 * Start sending all the sms of a marketing campaign
	 */
	void sendCampagna(long idCampagna, long idSender, long currentAziendaId) throws NotEnoughCreditException;
}
