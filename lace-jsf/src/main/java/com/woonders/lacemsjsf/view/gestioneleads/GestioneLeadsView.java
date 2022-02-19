package com.woonders.lacemsjsf.view.gestioneleads;


import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.codec.binary.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.springframework.util.DigestUtils;

import com.woonders.lacemscommon.db.entity.FornitoriLead;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.service.FornitoriLeadService;
import com.woonders.lacemscommon.service.impl.FornitoriLeadServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = GestioneLeadsView.NAME)
@ViewScoped
@Getter
@Setter
public class GestioneLeadsView implements Serializable {
	
	public static final String NAME = "gestioneLeadsView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	
	@ManagedProperty(FornitoriLeadServiceImpl.JSF_NAME)
	private FornitoriLeadService fornitoriLeadsService;
	
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	

	private FornitoriLead fornitoriLead;
	private List<FornitoriLead> fornitoriLeads;
	
	private String dataRetention;
	
	@PostConstruct
    public void init() {
		
		
		fornitoriLeads = fornitoriLeadsService.findEntityAll();
	}
	
	public FornitoriLead getFornitoriLead() {
        return fornitoriLead;
    }

    public void setFornitoriLeads(FornitoriLead fornitoriLead) {
        this.fornitoriLead = fornitoriLead;
    }
    
    public List<FornitoriLead> getFornitoriLeads() {
        return fornitoriLeads;
    }

	
	public void setService(FornitoriLeadServiceImpl service) {
        this.fornitoriLeadsService = service;
    }

    public void onCellEdit(CellEditEvent event) {
		
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
       
        if (newValue != null && !newValue.equals(oldValue)) {
        	DataTable table = (DataTable) event.getSource();
    		List<?> elements = (List<?>) table.getValue();
    		
    		this.fornitoriLead = (FornitoriLead) elements.get(event.getRowIndex());
    		
    		final FornitoriLead fornitoriLeadUpd = fornitoriLeadsService.findOne(this.fornitoriLead.getId());
    		fornitoriLeadUpd.setDataRetention(this.fornitoriLead.getDataRetention());
    		fornitoriLeadUpd.setFornitoreLeadName(this.fornitoriLead.getFornitoreLeadName());
    		fornitoriLeadUpd.setProvenienza(this.fornitoriLead.getProvenienza());
    		fornitoriLeadUpd.setProvenienzaDesc(this.fornitoriLead.getProvenienzaDesc());
    		
        	fornitoriLeadsService.save(fornitoriLeadUpd);
        	
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
	

	public boolean hasGestionePermessiRole() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getGestionePermessiWriteRoles());
    }
	
}
