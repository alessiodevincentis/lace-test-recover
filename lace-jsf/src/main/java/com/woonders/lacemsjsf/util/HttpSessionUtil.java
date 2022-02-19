package com.woonders.lacemsjsf.util;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.security.CustomSecurityUser;
import com.woonders.lacemsjsf.exception.SessionExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class HttpSessionUtil {

	public static final String NAME = "httpSessionUtil";
	public static final String JSF_NAME = "#{" + NAME + "}";

	public long getAziendaId() {
		return getAttributeFromSession(SessionAttribute.AZIENDA_ID, Long.class);
	}

	public long getOperatorId() {
		return getAttributeFromSession(SessionAttribute.USERID, Long.class);
	}

	public String getUsername() {
		return getAttributeFromSession(SessionAttribute.USERNAME, String.class);
	}

	public boolean isStorageEnabled() {
		return getAttributeFromSession(SessionAttribute.STORAGE_ENABLED, Boolean.class);
	}

	public boolean isPrintPdfEnabled() {
		return getAttributeFromSession(SessionAttribute.PRINT_PDF_ENABLED, Boolean.class);
	}

	// TODO
	public List<Role.RoleName> getRoleNameList() {
		return getAttributeFromSession(SessionAttribute.ROLE_LIST, List.class);
	}

	private <T> T getAttributeFromSession(final SessionAttribute sessionAttribute, final Class<T> type) {
		final CustomSecurityUser customSecurityUser;
		try {
			customSecurityUser = (CustomSecurityUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		} catch (ClassCastException e) {
			//spring returns "anonymous" instead of principal if for some reason I didn't understand the session expired but it didn't log us out
			//we use this exception to then make the redirect in LaceExceptionHandler
			throw new SessionExpiredException();
		}
		final Object object;
		switch (sessionAttribute) {
		case ROLE_LIST:
			object = customSecurityUser.getAuthorities().stream()
					.map(grantedAuthority -> Role.RoleName.valueOf(grantedAuthority.getAuthority()))
					.collect(Collectors.toList());
			break;
		case USERNAME:
			object = customSecurityUser.getUsername();
			break;
		case USERID:
			object = customSecurityUser.getId();
			break;
		case STORAGE_ENABLED:
			object = customSecurityUser.isStorageEnabled();
			break;
		case PRINT_PDF_ENABLED:
			object = customSecurityUser.isPrintPdfEnabled();
			break;
		case AZIENDA_ID:
			object = customSecurityUser.getAziendaId();
			break;
		default:
			throw new IllegalStateException("No attribute " + sessionAttribute.getValue() + " in httpSession");
		}
		return type.cast(object);
	}

	public boolean hasAuthority(Role.RoleName roleName) {
		return getRoleNameList().contains(roleName);
	}

	public boolean hasAnyAuthority(Role.RoleName... roleNames) {
		List<Role.RoleName> roleNameList = getRoleNameList();
		for(Role.RoleName roleName : roleNames) {
			if(roleNameList.contains(roleName)) {
				return true;
			}
		}
		return false;
	}

	public Role.RoleName getAuthority(OperatorViewModel.MenuSection menuSection, OperatorViewModel.PermissionType permissionType) {
		List<Role.RoleName> roleNameList = getRoleNameList();
		for(Role.RoleName roleName : roleNameList) {
			if(roleName.toString().contains(menuSection.toString()) && roleName.toString().contains(permissionType.toString())) {
				return roleName;
			}
		}
		return null;
	}

    private enum SessionAttribute {
		ROLE_LIST("roleList"), USERNAME("username"), USERID("userid"), STORAGE_ENABLED(
				"storageEnabled"), PRINT_PDF_ENABLED("printPdfEnabled"), AZIENDA_ID("aziendaId");

		private final String value;

		SessionAttribute(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

}
