package com.woonders.lacemscommon.db.entityutil;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;

@Component
public class SettingUtil {

	public static final String NAME = "settingUtil";
	public static final String JSF_NAME = "#{" + NAME + "}";

	public String getAliasMittente(SettingViewModel settingViewModel, AziendaViewModel aziendaViewModel) {
		return settingViewModel.getAlias();
	}
}
