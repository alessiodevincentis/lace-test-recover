package login;

import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.exception.CannotChangePasswordException;
import com.woonders.lacemscommon.exception.ConfirmPasswordMismatchException;
import com.woonders.lacemscommon.exception.InvalidCurrentPasswordException;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemsjsf.security.ValidPassword;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import lombok.Getter;
import lombok.Setter;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;

@ManagedBean(name = "changepsw")
@ViewScoped
@Getter
@Setter
public class ChangePsw implements Serializable {

	@ManagedProperty(OperatorServiceImpl.JSF_NAME)
	OperatorService operatorService;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	private String oldPassword;

	@ValidPassword
	private String newPassword;
	private String confirmNewPassword;

	public void changePasswordAction() throws Exception {
		try {
			operatorService.changePassword(oldPassword, newPassword, confirmNewPassword, httpSessionUtil.getOperatorId());
			FacesUtil.addMessage("Password Modificata con Successo");
		} catch (final CannotChangePasswordException e) {
			FacesUtil.addMessage("Errore " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		} catch (final InvalidCurrentPasswordException e) {
			FacesUtil.addMessage("Password Attuale Errata", FacesMessage.SEVERITY_ERROR);
		} catch (final ConfirmPasswordMismatchException e) {
			FacesUtil.addMessage("Nuova Password Diversa da Conferma Password!", FacesMessage.SEVERITY_ERROR);
		} finally {
			FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getLoginPath(false));
		}
	}

}
