package com.woonders.lacemsjsf.view.privacy;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.woonders.lacemscommon.app.viewmodel.PrivacyTemplateViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.exception.UnableToSaveException;
import com.woonders.lacemscommon.service.PrivacyTemplateService;
import com.woonders.lacemscommon.service.impl.PrivacyTemplateServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = PrivacyView.NAME)
@ViewScoped
@Getter
@Setter
public class PrivacyView implements Serializable {
	
	public static final String NAME = "privacyView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;

	
	@ManagedProperty(PrivacyTemplateServiceImpl.JSF_NAME)
    private PrivacyTemplateService privacyTemplateService;
	private PrivacyTemplateViewModel privacyTemplateViewModel;
	private Date dateCreate = new Date();
	private PrivacyTemplateViewModel privacyTemplateSelected;
	private List<PrivacyTemplateViewModel> privacyViewModelList;
	
	@PostConstruct
	public void init() {
		privacyTemplateViewModel = new PrivacyTemplateViewModel();
		privacyViewModelList = privacyTemplateService.findAll();
		
	}
	
	public void onRowSelect() throws IOException {
		privacyTemplateViewModel.setProvenienza(privacyTemplateSelected.getProvenienza());
		privacyTemplateViewModel.setFornitoreLead(privacyTemplateSelected.getFornitoreLead());
		privacyTemplateViewModel.setId(privacyTemplateSelected.getId());
		privacyTemplateViewModel.setPrivacyText(privacyTemplateSelected.getPrivacyText());
		privacyTemplateViewModel.setDateCreate(privacyTemplateSelected.getDateCreate());
    }
	

	
	public void nuovoTemplateAction() throws UnableToSaveException {
		
		try
		{
			privacyTemplateService.save(privacyTemplateViewModel);
			
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Insert Privacy Template", "L'inserimento della Privacy è avvenuto con seccesso.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		catch (Exception ex){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Errore nel Inserimento", "Error: " + ex.getMessage());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		finally
		{
			privacyTemplateViewModel = new PrivacyTemplateViewModel();
		}
		
	}
	
	
	
	public boolean hasPrivacyRole() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getPrivacyWriteRoles());
    }
}
