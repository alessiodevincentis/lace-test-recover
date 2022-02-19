package com.woonders.lacemsjsf.view.operatore;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.RoleViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.AziendaViewModelCreator;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.repository.AziendaRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.exception.CannotChangePasswordException;
import com.woonders.lacemscommon.exception.ConfirmPasswordMismatchException;
import com.woonders.lacemscommon.exception.InvalidCurrentPasswordException;
import com.woonders.lacemscommon.exception.UnableToSaveException;
import com.woonders.lacemscommon.laceenum.PermissionProfile;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.GestionePermessiService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.RoleService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.GestionePermessiServiceImpl;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.RoleServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.security.ValidPassword;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = NuovoOperatore.NAME)
@ViewScoped
@Getter
@Setter
public class NuovoOperatore implements Serializable {

	public static final String NAME = "nuovoOperatore";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(OperatorServiceImpl.JSF_NAME)
	private OperatorService operatorService;

	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;
	@ManagedProperty(AziendaServiceImpl.JSF_NAME)
	private AziendaService aziendaService;
	private AziendaViewModel aziendaViewModel;
	@Autowired
	private AziendaViewModelCreator aziendaViewModelCreator;
	
	@ManagedProperty(RoleServiceImpl.JSF_NAME)
    private RoleService roleService;

    private List<RoleViewModel> roleViewModelList;
    private PermissionProfile permissionProfile;
    private OperatorViewModel operatorToAssignProfile;
    @ManagedProperty(GestionePermessiServiceImpl.JSF_NAME)
    private GestionePermessiService gestionePermessiService;
	
	 @Autowired
	 private OperatorRepository operatorRepository;
	 
	 @Autowired
	 private AziendaRepository aziendaRepository;

	private String username;
	private String firstname;
	private String lastname;
	private String phonenumber;
	private String email;
	private Long aziendaid;
	
	@ValidPassword
	private String password;
	private String confirmpassword;
	
	@PostConstruct
	public void init() {
		username = "";
		password = "";
		
		roleViewModelList = roleService.findAll();
	}

	public void nuovoOperatoreAction() throws UnableToSaveException {

		if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password) && StringUtils.isEmpty(confirmpassword)) {
			FacesUtil.addMessage(propertiesUtil.getMsgDatiNominativoMancanti(), FacesMessage.SEVERITY_WARN);
			return;
		}
		
		try {
			final Operator operator = new Operator();
			//final OperatorViewModel operator = operatorService.getOne((long)75);
			operator.setUsername(username);
			operator.setPassword(operatorService.newPasswordEncoded(password, confirmpassword));
			operator.setFirstName(firstname);
			operator.setLastName(lastname);
			operator.setPhoneNumber(phonenumber);
			operator.setEmail(email);
			operator.setLeadRicevuti(0);
			operator.setPermissionProfile(null);
			operator.setReceiveLeadEnabled(false);
			operator.setSmsBalance(0);
			
			operator.setAzienda(aziendaService.findOne(aziendaSelectionView.getCurrentAziendaId()));
			
			Set<Role> role = new LinkedHashSet<Role>();
			Role r1 = new Role();
			r1.setId(7L);
			role.add(r1);
			operator.setRole(role);						
			
			//operator.setAzienda(aziendaViewModel);
			
			operatorService.save(operator);
			
			//operatorService.newOperatorSave(operator, aziendaid);
			FacesUtil.addMessage("Operatore Inserito con Successo");
		} catch (final CannotChangePasswordException e) {
			FacesUtil.addMessage("Errore " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		} catch (final InvalidCurrentPasswordException e) {
			FacesUtil.addMessage("Password Attuale Errata", FacesMessage.SEVERITY_ERROR);
		} catch (final ConfirmPasswordMismatchException e) {
			FacesUtil.addMessage("Nuova Password Diversa da Conferma Password!", FacesMessage.SEVERITY_ERROR);
		} catch (final UnableToSaveException e) {
			FacesUtil.addMessage("Errore " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		} catch (final Exception e) {
			FacesUtil.addMessage("Errore " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}

	}

	public boolean hasGestionePermessiRole() {
        return httpSessionUtil.hasAnyAuthority(Role.RoleName.getGestionePermessiWriteRoles());
    }
	
}
