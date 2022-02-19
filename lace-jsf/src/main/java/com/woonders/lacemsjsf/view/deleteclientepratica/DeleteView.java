package com.woonders.lacemsjsf.view.deleteclientepratica;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.exception.CannotDeletePraticaException;
import com.woonders.lacemscommon.service.DeleteClienteService;
import com.woonders.lacemscommon.service.impl.DeleteClienteServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

import static com.woonders.lacemsjsf.view.deleteclientepratica.DeleteView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class DeleteView implements Serializable {

	public static final String NAME = "deleteView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	private static final Logger logger = LoggerFactory.getLogger(DeleteView.class);
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(DeleteClienteServiceImpl.JSF_NAME)
	private DeleteClienteService deleteClienteService;
	private String cfDelete;
	private String cfPraticaDelete;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	@ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
	private RequestTenantIdentifierResolver requestTenantIdentifierResolver;

	public void deleteRecordByCf() {

		try {
			final ClienteViewModel clienteViewModel = deleteClienteService.findByCf(cfDelete);

			if (clienteViewModel != null) {
				deleteClienteService.deleteByCf(requestTenantIdentifierResolver.getTenantIdentifier(), cfDelete);
				FacesUtil.addMessage(propertiesUtil.getMsgDeleteSuccess());
				logger.info(propertiesUtil.getMsgDeleteSuccess());
			} else {
				FacesUtil.addMessage(propertiesUtil.getMsgDeleteCfNotFound(), FacesMessage.SEVERITY_WARN);
			}
		} catch (final DataAccessException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgDeleteError(), FacesMessage.SEVERITY_ERROR);
			logger.error(propertiesUtil.getMsgDeleteError(), e);

		}
	}

	public String searchCfDelete() {
		try {
			passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.DELETE_PRATICA_LIST_PARAMETER,
					deleteClienteService.findPraticheToDeleteByCf(cfPraticaDelete, httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.DELETE),
							httpSessionUtil.getOperatorId()));
			return Constants.getDeleteDatatablePath(true);
		} catch (CannotDeletePraticaException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgPraticaCannotDelete(), FacesMessage.SEVERITY_ERROR);
			return "";
		}
	}

	public boolean isDeleteAllClienteByCfRendered() {
		return httpSessionUtil.hasAnyAuthority(RoleName.CLIENTI_DELETE_ALL, RoleName.CLIENTI_DELETE_SUPER);
	}
}
