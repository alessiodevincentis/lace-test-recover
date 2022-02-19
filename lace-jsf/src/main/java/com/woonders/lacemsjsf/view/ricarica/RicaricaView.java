package com.woonders.lacemsjsf.view.ricarica;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.RicaricaComunicazioneViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.RicaricaType;
import com.woonders.lacemscommon.db.entityutil.AziendaUtil;
import com.woonders.lacemscommon.exception.DatiFatturazioneToBeCompletedException;
import com.woonders.lacemscommon.exception.FatturaNonDisponibileException;
import com.woonders.lacemscommon.exception.PaymentException;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.RicaricaService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl.PdfCategory;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.RicaricaServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

import static com.woonders.lacemsjsf.view.ricarica.RicaricaView.NAME;

/**
 * Created by Emanuele on 15/02/2017.
 */
@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class RicaricaView implements Serializable {

	public static final String NAME = "ricaricaView";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@ManagedProperty(RicaricaServiceImpl.JSF_NAME)
	private RicaricaService ricaricaService;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	private Articolo.TipoArticolo tipoArticolo;
	@ManagedProperty(AziendaServiceImpl.JSF_NAME)
	private AziendaService aziendaService;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	@ManagedProperty(AziendaUtil.JSF_NAME)
	private AziendaUtil aziendaUtil;
	private LazyDataModel<RicaricaComunicazioneViewModel> ricaricaComunicazioneViewModelLazyDataModel;
	@ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
	private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
	private RicaricaType selectedRicaricaType;
	private OperatorViewModel selectedOperatorViewModel;
	@ManagedProperty(OperatorServiceImpl.JSF_NAME)
	private OperatorService operatorService;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;

	@PostConstruct
	public void init() {
		ricaricaComunicazioneViewModelLazyDataModel = new LazyDataModel<RicaricaComunicazioneViewModel>() {

			@Override
			public List<RicaricaComunicazioneViewModel> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
				for (Map.Entry<String, Object> entry : filters.entrySet()) {
					tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
				}

				ViewModelPage<RicaricaComunicazioneViewModel> viewModelPage = ricaricaService.getTopUpList(aziendaSelectionView.getCurrentAziendaId(),
						first,
						pageSize, sortField,
						com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters);

				// jsf di merda che non prendi i long!
				setRowCount((int) viewModelPage.getTotalElements());
				return viewModelPage.getContent();
			}
		};
	}

	public void setSelectedTipoArticolo() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String tipoArticoloString = params.get("selectedTipoArticolo");
		tipoArticolo = Articolo.TipoArticolo.valueOf(tipoArticoloString);
	}

	public void pay() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String tokenId = params.get("tokenId");
		try {
			if(tipoArticolo != null) {
				OperatorViewModel operatorViewModel = selectedOperatorViewModel;
				if(RicaricaType.SMS_AGENZIA.equals(selectedRicaricaType)) {
					operatorViewModel = null;
				}
				ricaricaService.makePayment(httpSessionUtil.getOperatorId(), tokenId, "eur", tipoArticolo, selectedRicaricaType, operatorViewModel, aziendaSelectionView.getCurrentAziendaId());
				FacesUtil.addMessage(propertiesUtil.getMsgSmsTopupPaymentSuccess());
				FacesUtil.updateSmsInfo();
			} else {
				FacesUtil.addMessage(propertiesUtil.getMsgSmsTopupInvalidChoice(), FacesMessage.SEVERITY_ERROR);
			}
		} catch (PaymentException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgSmsTopupPaymentFailed(), FacesMessage.SEVERITY_ERROR);
		} catch (DatiFatturazioneToBeCompletedException e) {
			FacesUtil.addMessage(propertiesUtil.getMsgDatiAziendaMancanti(), FacesMessage.SEVERITY_ERROR);
		}

	}

	public void showFattura(long ricaricaId) {
		try {
			String pdfLink = ricaricaService.findFatturaPdfLinkByRicaricaId(ricaricaId,
					requestTenantIdentifierResolver.getTenantIdentifier());
			passaggioParametriUtils.getParametri().put(Parametro.PDF_CATEGORY_PARAMETER, PdfCategory.FATTURA);
			passaggioParametriUtils.getParametri().put(Parametro.PDF_LINK, pdfLink);
			FacesUtil.openNewWindow(Constants.getPdfViewPath(false));
		} catch (FatturaNonDisponibileException e) {
			FacesUtil.addMessage("Fattura non disponibile, contatta il supporto", FacesMessage.SEVERITY_ERROR);
		}

	}

	public boolean isDatiFatturazioneToBeCompleted() {
		return aziendaUtil.isDatiFatturazioneToBeCompleted(aziendaService.getOne(aziendaSelectionView.getCurrentAziendaId()));
	}

	public boolean isRicaricaOperatoreInProgress() {
		return selectedRicaricaType != null && RicaricaType.SMS_MARKETING.equals(selectedRicaricaType);
	}

	public boolean isRicaricaTypeAndOperatorChosen() {
		return selectedRicaricaType != null;
	}

	public List<OperatorViewModel> getOperatorList() {
		Collection<Role.RoleName> roleNameCollection = new LinkedList<>();
		roleNameCollection.add(Role.RoleName.CAMPAGNE_MARKETING_WRITE);
		return operatorService.findByRoleNameInAndAzienda_Id(roleNameCollection, aziendaSelectionView.getCurrentAziendaId());
	}

	public String getRicaricaDescriptionText() {
		String description = selectedRicaricaType.getValue();
		if(RicaricaType.SMS_MARKETING.equals(selectedRicaricaType)) {
			description = description + " per " + selectedOperatorViewModel.getUsername();
		}
		return description;
	}

}
