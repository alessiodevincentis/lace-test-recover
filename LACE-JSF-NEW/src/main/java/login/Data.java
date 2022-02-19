package login;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = "infoOperatore")
@ViewScoped
@Getter
@Setter
public class Data implements Serializable {

	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(OperatorServiceImpl.JSF_NAME)
	private OperatorService operatorService;
	private String nome;
	private String cognome;
	private String telefono;
	private String mail;

	// TODO migliorare con service
	public void saveData() {
		try {
			operatorService.changeData(nome, cognome, mail, telefono, httpSessionUtil.getOperatorId());
			FacesUtil.addMessage("Dati Personali Modificati con Successo");
		} catch (final Exception e) {
			FacesUtil.addMessage("Errore nella modifica dei dati personali", FacesMessage.SEVERITY_ERROR);
		}
	}

	@PostConstruct
	public void init() {
		OperatorViewModel operatorViewModel;
		operatorViewModel = operatorService.getOne(httpSessionUtil.getOperatorId());
		setCognome(operatorViewModel.getLastName());
		setNome(operatorViewModel.getFirstName());
		setTelefono(operatorViewModel.getPhoneNumber());
		setMail(operatorViewModel.getEmail());
	}
}
