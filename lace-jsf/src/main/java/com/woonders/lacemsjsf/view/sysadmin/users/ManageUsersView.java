package com.woonders.lacemsjsf.view.sysadmin.users;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.RoleViewModel;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.RoleServiceImpl;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emanuele on 22/09/2016.
 */
@ManagedBean
@ViewScoped
@Setter
@Getter
// http://stackoverflow.com/questions/15265433/how-and-when-is-a-viewscoped-bean-destroyed-in-jsf
public class ManageUsersView implements Serializable {

	@ManagedProperty(OperatorServiceImpl.JSF_NAME)
	private OperatorService operatorService;

	@ManagedProperty(RoleServiceImpl.JSF_NAME)
	private RoleServiceImpl roleServiceImpl;

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;

	private OperatorViewModel newOperatorViewModel;
	private OperatorViewModel selectedOperatorViewModel;
	private RoleViewModel newRoleViewModel;
	
	public void prepareNewLoginOperatorAction() {
		newOperatorViewModel = new OperatorViewModel();
	}

	public void addNewLoginOperatorAction() {
		/*
		 * try { newLoginOperatorViewModel.setDataSource(newDataSource);
		 * newLoginOperatorViewModel.setRole(newRole);
		 * operatorService.save(loginOperatorCreator.fromViewModel(
		 * newLoginOperatorViewModel));
		 * FacesUtil.closeDialog(FacesUtil.DialogType.OPERATOR);
		 * FacesUtil.addMessage(propertiesUtil.getMsgLoginOperatorSaved()); }
		 * catch (final UnableToSaveException e) {
		 * FacesUtil.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR); }
		 */
		// TODO da aggiustare quando rifacciamo la dashboard sysadmin
	}

	public void deleteLoginOperator() {
		/*
		 * try { operatorService.delete(selectedLoginOperator);
		 * selectedLoginOperator = null;
		 * FacesUtil.addMessage(propertiesUtil.getMsgLoginOperatorDeleted()); }
		 * catch (final UnableToDeleteException e) {
		 * FacesUtil.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR); }
		 */
		// TODO da aggiustare quando rifacciamo la dashboard sysadmin
	}

}
