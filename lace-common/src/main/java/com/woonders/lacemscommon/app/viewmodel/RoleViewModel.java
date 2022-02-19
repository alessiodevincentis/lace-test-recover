package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entity.Role;

import lombok.*;

/**
 * Created by emanuele on 09/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class RoleViewModel extends AbstractBaseViewModel {

	private long id;
	private Role.RoleName roleName;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
