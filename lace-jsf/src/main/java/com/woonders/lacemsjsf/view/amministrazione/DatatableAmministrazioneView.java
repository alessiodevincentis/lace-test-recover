package com.woonders.lacemsjsf.view.amministrazione;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;


@ManagedBean(name = DatatableAmministrazioneView.NAME)
@ViewScoped
@Getter
@Setter
public class DatatableAmministrazioneView implements Serializable {

	public static final String NAME = "datatableAmministrazioneView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;

	private List<AmministrazioneViewModel> amministrazioneViewModelList;
	private AmministrazioneViewModel selectedAmministrazione;

	@PostConstruct
	public void init() {
		SetterListAmministrazione();
	}

	private void SetterListAmministrazione() {
		amministrazioneViewModelList = (List<AmministrazioneViewModel>) passaggioParametriUtils.getParametri()
				.get(Parametro.AMMINISTRAZIONE_LIST);
	}

	public void onRowSelect() throws IOException {
		passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_VIEWMODEL_PARAMETER,
				selectedAmministrazione);
		FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsAmministrazionePath(false));
	}

}
