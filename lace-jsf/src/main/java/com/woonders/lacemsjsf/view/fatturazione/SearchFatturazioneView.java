package com.woonders.lacemsjsf.view.fatturazione;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.FatturazioneService;
import com.woonders.lacemscommon.service.impl.FatturazioneServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.woonders.lacemsjsf.view.fatturazione.SearchFatturazioneView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class SearchFatturazioneView implements Serializable {

	public static final String NAME = "searchFatturazioneView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	private final List<String> operators = new ArrayList<>();
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(FatturazioneServiceImpl.JSF_NAME)
	private FatturazioneService fatturazioneService;
	private Date dataInizio;
	private Date dataFine;
	private Date dataInizio2;
	private Date dataFine2;
	private String operatore;
	private String provenienza;
	private String provenienza2;
	private String provenienzaDesc;
	private String provenienzaDesc2;
	private boolean liquidazione;
	private boolean liquidata;
	private boolean quietanzata;
	private boolean perfezionamentoSelected;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;

	public String searchPerfeziona() {
		if ((dataInizio == null || dataFine == null) && liquidazione == false && liquidata == false
				&& quietanzata == false && perfezionamentoSelected == false) {
			FacesUtil.addMessage(propertiesUtil.getMsgDaPerfezionareDatiMancanti(), FacesMessage.SEVERITY_WARN);
			return "";
		}
		//*** 23-11-21 mod Cristian
		//*** passo anche come parametro la provenienza ***
		passaggioParametriUtils.getParametri().put(Parametro.PROVENIENZA, provenienza);
		passaggioParametriUtils.getParametri().put(Parametro.PROVENIENZA_DESC, provenienzaDesc);
		//********
		
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_DA_PERFEZIONARE_DATAINIZIO_PARAMETER, dataInizio);
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_DA_PERFEZIONARE_DATAFINE_PARAMETER, dataFine);
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_DA_PERFEZIONARE_LIQUIDAZIONE_PARAMETER,
				liquidazione);
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_DA_PERFEZIONARE_LIQUIDATA_PARAMETER, liquidata);
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_DA_PERFEZIONARE_QUIETANZATA_PARAMETER,
				quietanzata);
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_DA_PERFEZIONARE_PERFEZIONAMENTO_PARAMETER,
				perfezionamentoSelected);
		return Constants.getDatatablePraticheDaPerfezionarePath(true);
	}

	public String searchPerfezionate() {
		//*** 23-11-21 mod Cristian
		//*** passo anche come parametro la provenienza ***
		passaggioParametriUtils.getParametri().put(Parametro.PROVENIENZA, provenienza2);
		passaggioParametriUtils.getParametri().put(Parametro.PROVENIENZA_DESC, provenienzaDesc2);
		//********
		
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_PERFEZIONATE_DATAINIZIO_PARAMETER, dataInizio2);
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_PERFEZIONATE_DATAFINE_PARAMETER, dataFine2);
		String operatore;
		if (httpSessionUtil.hasAnyAuthority(Role.RoleName.FATTURAZIONE_READ_ALL, Role.RoleName.FATTURAZIONE_READ_SUPER)) {
			operatore = this.operatore;
		} else {
			operatore = httpSessionUtil.getUsername();
		}
		passaggioParametriUtils.getParametri().put(Parametro.PRATICHE_PERFEZIONATE_OPERATORE_PARAMETER, operatore);
		return Constants.getDatatablePratichePerfezionatePath(true);
	}

	public boolean isDaPerfezionareRendered() {
		return httpSessionUtil.hasAnyAuthority(Role.RoleName.FATTURAZIONE_WRITE, Role.RoleName.FATTURAZIONE_WRITE_SUPER);
	}

	public boolean isSelectOneMenuOperatoriRendered() {
		return httpSessionUtil.hasAnyAuthority(Role.RoleName.FATTURAZIONE_READ_ALL, Role.RoleName.FATTURAZIONE_READ_SUPER);
	}
	
	//*** Mod Cristian 11-11-2021 ***
    public List<String> getProvenienzaDescListClienti() {
        return fatturazioneService.getProvenienzaDescList();
    }

}
