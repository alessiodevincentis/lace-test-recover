package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.RoleViewModel;
import com.woonders.lacemscommon.db.entity.Role;

/**
 * Created by emanuele on 09/01/17.
 */
@Component
public class RoleViewModelCreator extends AbstractBaseViewModelCreator<Role, RoleViewModel> {

	@Override
	public Role createModel(RoleViewModel roleViewModel) {
		if (roleViewModel != null) {
			return Role.builder().id(roleViewModel.getId()).roleName(roleViewModel.getRoleName()).build();
		}
		return null;
	}

	@Override
	public RoleViewModel createViewModel(Role role) {
		if (role != null) {
			return RoleViewModel.builder().id(role.getId()).roleName(role.getRoleName()).build();
		}
		return null;
	}
}
