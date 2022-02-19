package com.woonders.lacemsjsf.view.azienda;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.springframework.dao.DataAccessException;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "aziendaView")
@ViewScoped
@Getter
@Setter
@Slf4j
public class AziendaView implements Serializable {

	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(AziendaServiceImpl.JSF_NAME)
	private AziendaService aziendaService;
	private AziendaViewModel aziendaViewModel;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;

	@PostConstruct
	public void init() {
		//https://developer.jboss.org/thread/186179?_sscc=t link che spiega cache sui metodi, alla fine qua funziona tutto come cazzo je pare, ho fatto cosi col viewscope e il listener sul rendering della pagina
		refreshAziendaFromDb();
	}

	public void refreshAziendaFromDb() {
		if(FacesUtil.doesPageNeedToBeRefreshed()) {
			aziendaViewModel = aziendaService.getOne(aziendaSelectionView.getCurrentAziendaId());
		}
	}

	public void saveAzienda() {
		try {
			aziendaService.save(aziendaViewModel);
			FacesUtil.addMessage(propertiesUtil.getMsgSuccess());
		} catch (final DataAccessException e) {
			log.error(propertiesUtil.getMsgAziendaError(), e);
			FacesUtil.addMessage(propertiesUtil.getMsgAziendaError(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public boolean isSalvaDatiAziendaRendered() {
		return httpSessionUtil.hasAnyAuthority(Role.RoleName.getDatiAziendaWriteRoles());
	}

}
