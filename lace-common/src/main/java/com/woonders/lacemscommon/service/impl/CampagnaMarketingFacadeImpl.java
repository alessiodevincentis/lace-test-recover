package com.woonders.lacemscommon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woonders.lacemscommon.app.viewmodel.CampagnaViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.db.entity.Campagna;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;
import com.woonders.lacemscommon.service.CampagnaMarketingFacade;
import com.woonders.lacemscommon.service.CampagnaMarketingService;

/**
 * Created by Emanuele on 01/04/2017.
 */
@Service
public class CampagnaMarketingFacadeImpl implements CampagnaMarketingFacade {

	public static final String NAME = "campagnaMarketingFacadeImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Autowired
	private CampagnaMarketingService campagnaMarketingService;

	@Override
	public void saveAndSendCampagna(String nameCampagna, String descriptionCampagna, String messageToSend,
			Campagna.CampagnaType campagnaType, boolean campagnaImport, long idCreator,
			FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel,
			List<ClienteViewModel> clienteViewModelList, int totaleMessaggi, boolean isClientiReadSuper,
			boolean isNominativiReadSuper, boolean isClientiReadAll, boolean isNominativiReadAll,
			boolean isClientiReadOwn, boolean isNominativiReadOwn, long currentOperatorAziendaId, Long currentAziendaId)
			throws NotEnoughCreditException {
		CampagnaViewModel campagnaViewModel = campagnaMarketingService.saveAndSendCampagna(nameCampagna,
				descriptionCampagna, messageToSend, campagnaType, campagnaImport, idCreator,
				filtroCampagnaMarketingViewModel, clienteViewModelList, totaleMessaggi, isClientiReadSuper,
				isNominativiReadSuper, isClientiReadAll, isNominativiReadAll, isClientiReadOwn, isNominativiReadOwn,
				currentOperatorAziendaId, currentAziendaId);
		campagnaMarketingService.startSmsSendingCampagna(campagnaViewModel.getId(), idCreator, currentAziendaId);
	}

	@Override
	public void importaNominativi(long idCampagna) {
		campagnaMarketingService.importaNominativi(idCampagna);
	}

	@Override
	public void sendCampagna(long idCampagna, long idSender, long currentAziendaId) throws NotEnoughCreditException {
		campagnaMarketingService.writeCampagnaInfoSent(idCampagna, idSender);
		campagnaMarketingService.startSmsSendingCampagna(idCampagna, idSender, currentAziendaId);
	}
}
