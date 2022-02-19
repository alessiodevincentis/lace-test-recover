package com.woonders.lacemsjsf.view.nominativo;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Cliente.Sesso;
import com.woonders.lacemscommon.exception.ComuneMancanteException;
import com.woonders.lacemscommon.service.CheckNominativoService;
import com.woonders.lacemscommon.service.impl.CheckNominativoServiceImpl;
import com.woonders.lacemscommon.util.CfGenerator;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ManagedBean(name = CheckNominativo.NAME)
@ViewScoped
@Getter
@Setter
public class CheckNominativo implements Serializable {

	public static final String NAME = "checkNominativo";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(CheckNominativoServiceImpl.JSF_NAME)
	private CheckNominativoService checkNominativoService;

	@ManagedProperty(CfGenerator.JSF_NAME)
	private CfGenerator cfGenerator;

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;

	private String cf;
	private String cognome;
	private String nome;
	private String luogoNascita;
	private Sesso sesso;
	private Date dataNascita;
	private String telefonoCellulare;
	private String telefonoFisso;
	private String email;

	public void getCalcCf() {
		if (StringUtils.isEmpty(cognome) || StringUtils.isEmpty(nome) || StringUtils.isEmpty(luogoNascita)
				|| dataNascita == null || sesso == null) {

			FacesUtil.addMessage(propertiesUtil.getMsgDatiCalcoloCfMancanti(), FacesMessage.SEVERITY_WARN);
			return;
		}
		try {
			setCf(cfGenerator.getCodiceFiscale(cognome, nome, dataNascita, luogoNascita, sesso.name()));
		} catch (ComuneMancanteException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgCfComuneErrato(), FacesMessage.SEVERITY_WARN);
		}
	}

	public void nominativoChecker() throws IOException {

		if (StringUtils.isEmpty(telefonoCellulare) && StringUtils.isEmpty(telefonoFisso)
				&& StringUtils.isEmpty(email)) {
			FacesUtil.addMessage(propertiesUtil.getMsgDatiNominativoMancanti(), FacesMessage.SEVERITY_WARN);
			return;
		}

		setDatiNominativoInPassaggioParametri();

		final List<ClienteViewModel> nominativiList = checkNominativoService
				.findNominativoByCognomeAndNomeAndDataNascita(cognome, nome, dataNascita);
		if (nominativiList != null && !nominativiList.isEmpty()) {
			passaggioParametriUtils.getParametri().put(Parametro.NOMINATIVI_CLIENTI_LIST_CHECK_NOMINATIVO_PARAMETER,
					nominativiList);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(Constants.getDatatableCheckNominativoPath(false));
		} else {
			passaggioParametriUtils.getParametri().put(Parametro.NUOVO_NOMINATIVO_PARAMETER, true);
			FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsNominativoPath(false));
		}

	}

	private void setDatiNominativoInPassaggioParametri() throws IOException {
		passaggioParametriUtils.getParametri().put(Parametro.COGNOME_CHECK_NOMINATIVO_PARAMETER, cognome);
		passaggioParametriUtils.getParametri().put(Parametro.NOME_CHECK_NOMINATIVO_PARAMETER, nome);
		passaggioParametriUtils.getParametri().put(Parametro.DATA_NASCITA_CHECK_NOMINATIVO_PARAMETER, dataNascita);
		passaggioParametriUtils.getParametri().put(Parametro.SESSO_CHECK_NOMINATIVO_PARAMETER, sesso);
		passaggioParametriUtils.getParametri().put(Parametro.LUOGO_CHECK_NOMINATIVO_PARAMETER, luogoNascita);
		passaggioParametriUtils.getParametri().put(Parametro.CELLULARE_CHECK_NOMINATIVO_PARAMETER, telefonoCellulare);
		passaggioParametriUtils.getParametri().put(Parametro.FISSO_CHECK_NOMINATIVO_PARAMETER, telefonoFisso);
		passaggioParametriUtils.getParametri().put(Parametro.EMAIL_CHECK_NOMINATIVO_PARAMETER, email);
		passaggioParametriUtils.getParametri().put(Parametro.CF_CHECK_NOMINATIVO_PARAMETER, cf);
	}
}
