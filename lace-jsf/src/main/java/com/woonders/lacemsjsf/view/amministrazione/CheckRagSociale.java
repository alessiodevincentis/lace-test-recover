package com.woonders.lacemsjsf.view.amministrazione;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.AmministrazioneService;
import com.woonders.lacemscommon.service.impl.AmministrazioneServiceImpl;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = CheckRagSociale.NAME)
@ViewScoped
@Getter
@Setter
public class CheckRagSociale implements Serializable {

	public static final String NAME = "checkRagSociale";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(AmministrazioneServiceImpl.JSF_NAME)
	private AmministrazioneService amministrazioneService;

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;

	private String ragioneSociale;

	public String newAmministration() {

		try {
			passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_VIEWMODEL_PARAMETER,
					amministrazioneService.findByRagioneSociale(ragioneSociale));
			FacesUtil.addMessage("Amministrazione censita!");
		} catch (final ItemNotFoundException e) {
			passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_VIEWMODEL_PARAMETER,
					AmministrazioneViewModel.builder().ragioneSociale(ragioneSociale).build());
			FacesUtil.addMessage("Amministrazione non censita!");
		}

		return Constants.getFormsAmministrazionePath(true);
	}

}
