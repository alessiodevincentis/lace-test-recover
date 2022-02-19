package com.woonders.lacemscommon.db.entityutil;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;

/**
 * Created by Emanuele on 26/02/2017.
 */
@Component
public class AziendaUtil {

	public static final String NAME = "aziendaUtil";
	public static final String JSF_NAME = "#{" + NAME + "}";

	public boolean isDatiFatturazioneToBeCompleted(AziendaViewModel aziendaViewModel) {
		return StringUtils.isEmpty(aziendaViewModel.getNomeAzienda())
				|| StringUtils.isEmpty(aziendaViewModel.getIndirizzo())
				|| StringUtils.isEmpty(aziendaViewModel.getLuogo()) || StringUtils.isEmpty(aziendaViewModel.getCap())
				|| StringUtils.isEmpty(aziendaViewModel.getProvincia())
				|| StringUtils.isEmpty(aziendaViewModel.getPiva()) || StringUtils.isEmpty(aziendaViewModel.getCf())
				|| StringUtils.isEmpty(aziendaViewModel.getTelefono());
	}

	public boolean isCellulareAziendaToBeCompleted(AziendaViewModel aziendaViewModel) {
		return StringUtils.isEmpty(aziendaViewModel.getCellulare());
	}
}