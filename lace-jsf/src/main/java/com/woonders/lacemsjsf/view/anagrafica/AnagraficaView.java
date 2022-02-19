package com.woonders.lacemsjsf.view.anagrafica;

import static com.woonders.lacemsjsf.view.anagrafica.AnagraficaView.NAME;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.springframework.util.StringUtils;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.db.entityutil.ClienteUtil;
import com.woonders.lacemscommon.exception.ComuneMancanteException;
import com.woonders.lacemscommon.util.CfGenerator;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;

import lombok.Setter;

/**
 * Created by Emanuele on 03/02/2017.
 */
@Setter
@ViewScoped
@ManagedBean(name = NAME)
public class AnagraficaView implements Serializable {

	public static final String NAME = "anagraficaView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(ClienteUtil.JSF_NAME)
	private ClienteUtil clienteUtil;

	@ManagedProperty(CfGenerator.JSF_NAME)
	private CfGenerator cfGenerator;

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;

	public void calcEta(ClienteViewModel clienteViewModel) {
		clienteViewModel.setEta(clienteUtil.calcEta(clienteViewModel.getDataNascita()));
	}

	public void calcAnniAssunzione(ClienteViewModel clienteViewModel) {
		clienteViewModel.setAnniAssunzione(clienteUtil.calcAnniAssunzione(clienteViewModel.getDataInizio()));
	}

	public void calcAnniAssunzione2(ClienteViewModel clienteViewModel) {
		clienteViewModel.setAnniAssunzione2(clienteUtil.calcAnniAssunzione(clienteViewModel.getDataInizio2()));
	}

	public void calcInversoCF(ClienteViewModel clienteViewModel) {
		if (clienteViewModel.getCf() != null && !clienteViewModel.getCf().isEmpty()) {
			clienteViewModel.setDataNascita(cfGenerator.elaboraDataNascitaDaCf(clienteViewModel.getCf()));
			clienteViewModel.setLuogoNascita(cfGenerator.elaboraComuneDaCodiceComune(clienteViewModel.getCf()));
			clienteViewModel.setSesso(cfGenerator.elaboraSessoDaCf(clienteViewModel.getCf()));
			clienteViewModel.setEta(clienteUtil.calcEta(clienteViewModel.getDataNascita()));
		}
	}

	public void calcCF(ClienteViewModel clienteViewModel) {
		if (StringUtils.isEmpty(clienteViewModel.getCognome()) || StringUtils.isEmpty(clienteViewModel.getNome())
				|| StringUtils.isEmpty(clienteViewModel.getLuogoNascita()) || clienteViewModel.getDataNascita() == null
				|| clienteViewModel.getSesso() == null) {
			FacesUtil.addMessage(propertiesUtil.getMsgDatiCalcoloCfMancanti(), FacesMessage.SEVERITY_WARN);
			return;
		}

		try {
			clienteViewModel.setCf(cfGenerator.getCodiceFiscale(clienteViewModel.getCognome(), clienteViewModel.getNome(),
                    clienteViewModel.getDataNascita(), clienteViewModel.getLuogoNascita(),
                    clienteViewModel.getSesso().name()));
		} catch (ComuneMancanteException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgCfComuneErrato(), FacesMessage.SEVERITY_WARN);
		}
	}
}
