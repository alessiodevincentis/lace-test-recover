package com.woonders.lacemsjsf.view.formcliente;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Cliente.Sesso;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.exception.ComuneMancanteException;
import com.woonders.lacemscommon.service.ClienteManagerService;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl;
import com.woonders.lacemscommon.util.CfGenerator;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.file.ArchiviazioneRendered;
import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Date;

import static com.woonders.lacemsjsf.view.formcliente.CheckCf.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class CheckCf implements Serializable {

	public static final String NAME = "checkCf";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	private String codiceF;
	private String tipoPratica;
	private String cognome;
	private String nome;
	private String luogoNascita;
	private Sesso sesso;
	private Date dataNascita;

	@ManagedProperty(ClienteManagerServiceImpl.JSF_NAME)
	private ClienteManagerService clienteManagerService;

	@ManagedProperty(DateConversionUtil.JSF_NAME)
	private DateConversionUtil dateConversionUtil;

	@ManagedProperty(CfGenerator.JSF_NAME)
	private CfGenerator cfGenerator;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;

	public String newClient() {
		ClienteViewModel clienteViewModel = clienteManagerService.findClienteByCodiceFiscale(codiceF);

		if (clienteViewModel != null) {
			if (Tipo.CLIENTE.toString().equalsIgnoreCase(clienteViewModel.getTipo())) {

				FacesUtil.addMessage("Il Cliente e' gia censito! Puoi Inserire una Nuova Pratica");

				passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.CLIENTE_ID,
						clienteViewModel.getId());
				passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.ARCHIVIAZIONE_RENDERED,
						ArchiviazioneRendered.builder().documentiClienteRendered(true).documentiPraticaRendered(false)
								.build());

				passaggioParametriUtils.getParametri()
						.put(PassaggioParametriUtils.Parametro.CENSITO_NUOVA_PRATICA_PARAMETER, true);
			} else {

				FacesUtil.addMessage("Il CF inserito risulta essere di un Nominativo Marketing!", FacesMessage.SEVERITY_WARN);
				return "";

			}

		} else {
			clienteViewModel = new ClienteViewModel();
			clienteViewModel.setCf(codiceF);
			clienteViewModel.setCognome(cognome);
			clienteViewModel.setNome(nome);
			clienteViewModel.setLuogoNascita(luogoNascita);
			clienteViewModel.setDataNascita(dataNascita);
			clienteViewModel.setSesso(sesso);

			passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.ARCHIVIAZIONE_RENDERED,
					ArchiviazioneRendered.builder().documentiClienteRendered(false).documentiPraticaRendered(false)
							.build());
			passaggioParametriUtils.getParametri()
					.put(PassaggioParametriUtils.Parametro.CENSITO_NUOVA_PRATICA_PARAMETER, false);

			FacesUtil.addMessage("Il Cliente non e' censito!");
		}
		final ClientePratica clienteNuovaPratica = new ClientePratica();
		clienteNuovaPratica.setClienteViewModel(clienteViewModel);
		// TODO FIX TEMPORANEO, DEVO VEDERE COME FARE
		clienteNuovaPratica.setPraticaViewModel(new PraticaViewModel());
		passaggioParametriUtils.getParametri().put(Parametro.TIPO_PRATICA_NUOVA_PRATICA_PARAMETER, tipoPratica);
		passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_NUOVA_PRATICA_PARAMETER, clienteNuovaPratica);
		return Constants.getFormsPath(true);

	}

	public void calcCF() {
		try {
			setCodiceF(cfGenerator.getCodiceFiscale(cognome, nome, dataNascita, luogoNascita, sesso.name()));
		} catch (ComuneMancanteException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgCfComuneErrato(), FacesMessage.SEVERITY_WARN);
		}
	}

}
