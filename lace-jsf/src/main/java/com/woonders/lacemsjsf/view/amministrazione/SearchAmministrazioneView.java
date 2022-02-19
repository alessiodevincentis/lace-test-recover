package com.woonders.lacemsjsf.view.amministrazione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.service.AmministrazioneService;
import com.woonders.lacemscommon.service.impl.AmministrazioneServiceImpl;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = SearchAmministrazioneView.NAME)
@ViewScoped
@Getter
@Setter
public class SearchAmministrazioneView implements Serializable {

	public static final String NAME = "searchAmministrazioneView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(AmministrazioneServiceImpl.JSF_NAME)
	private AmministrazioneService amministrazioneService;

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;

	private String ragioneSociale;
	private String piva;
	private String cf;
	private List<AmministrazioneViewModel> amministrazioneViewModelList = new ArrayList<>();
	private AmministrazioneViewModel selectedAmministrazione;

	public String searchRagSociale() {

		amministrazioneViewModelList = amministrazioneService.findDistinctByRagioneSocialeContaining(ragioneSociale);
		passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_LIST, amministrazioneViewModelList);
		return Constants.getDatatableAmministrazionePath(true);
	}

	public String searchPiva() {

		amministrazioneViewModelList.clear();
		amministrazioneViewModelList = amministrazioneService.findByPiva(piva);
		passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_LIST, amministrazioneViewModelList);
		return Constants.getDatatableAmministrazionePath(true);
	}

	public String searchCf() {

		amministrazioneViewModelList.clear();
		amministrazioneViewModelList = amministrazioneService.findByCodiceFiscale(cf);
		passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_LIST, amministrazioneViewModelList);
		return Constants.getDatatableAmministrazionePath(true);
	}

}
