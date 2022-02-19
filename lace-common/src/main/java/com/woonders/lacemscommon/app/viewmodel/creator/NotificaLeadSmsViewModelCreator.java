package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.NotificaLeadSmsViewModel;
import com.woonders.lacemscommon.db.entity.NotificaLeadSms;

@Component
public class NotificaLeadSmsViewModelCreator
		extends AbstractBaseViewModelCreator<NotificaLeadSms, NotificaLeadSmsViewModel> {

	@Override
	public NotificaLeadSms createModel(NotificaLeadSmsViewModel viewModel) {
		// non ci serve
		return null;
	}

	@Override
	public NotificaLeadSmsViewModel createViewModel(NotificaLeadSms model) {
		if (model != null) {
			return NotificaLeadSmsViewModel.builder().dataEsito(model.getDataEsito()).esito(model.getEsito())
					.id(model.getId()).build();
		}
		return null;
	}

}
