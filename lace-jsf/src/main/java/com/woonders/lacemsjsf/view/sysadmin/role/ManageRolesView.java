package com.woonders.lacemsjsf.view.sysadmin.role;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.woonders.lacemscommon.app.viewmodel.RoleViewModel;
import com.woonders.lacemscommon.service.impl.RoleServiceImpl;

/**
 * Created by Emanuele on 05/10/2016.
 */
@ManagedBean
@ViewScoped
public class ManageRolesView implements Serializable {

	@ManagedProperty(RoleServiceImpl.JSF_NAME)
	private RoleServiceImpl roleServiceImpl;

	public void setRoleService(final RoleServiceImpl roleService) {
		this.roleServiceImpl = roleService;
	}

	public List<RoleViewModel> getRoleList() {
		return roleServiceImpl.findAll();
	}
}
