package com.woonders.lacemsjsf.view.calendarioappuntamenti;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.viewmodel.CalendarioAppuntamentoViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.exception.CannotSendSmsException;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.service.CalendarioAppuntamentoService;
import com.woonders.lacemscommon.service.ClienteManagerService;
import com.woonders.lacemscommon.service.SearchService;
import com.woonders.lacemscommon.service.SettingService;
import com.woonders.lacemscommon.service.impl.CalendarioAppuntamentoServiceImpl;
import com.woonders.lacemscommon.service.impl.SettingServiceImpl;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl;
import com.woonders.lacemscommon.service.impl.SearchServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@ManagedBean(name = CalendarioAppuntamentiView.NAME)
@ViewScoped
@Getter
@Setter
@Slf4j
public class CalendarioAppuntamentiView implements Serializable {

	public static final String NAME = "calendarioAppuntamentiView";
	public static final String JSF_NAME = "#{" + NAME + "}";
	public static final String GENERALI_TAB = "generaliTab";
	public static final String SPECIFICI_TAB = "specificiTab";
	public static final String RIEPILOGO_TAB = "riepilogoTab";
	public static final String FROM_DASHBOARD = "dashboard";
	public static final String FROM_PRATICA_NOMINATIVO = "praticaNominativo";

	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
	private ScheduleModel lazyEventModel;
	private CalendarioAppuntamentoViewModel calendarioAppuntamentoViewModel;
	@ManagedProperty(CalendarioAppuntamentoServiceImpl.JSF_NAME)
	private CalendarioAppuntamentoService calendarioAppuntamentoService;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;
	@ManagedProperty(SearchServiceImpl.JSF_NAME)
	private SearchService searchService;
	private PraticaViewModel selectedPratica;
	@ManagedProperty(ClienteManagerServiceImpl.JSF_NAME)
	private ClienteManagerService clienteManagerService;
	@ManagedProperty(DateConversionUtil.JSF_NAME)
	private DateConversionUtil dateConversionUtil;
	private String saveAppuntamentoFrom;
	private List<String> operatoriSelezionati;
	private boolean sendSmsClienteNominativo;
	private boolean sendSmsOperatoreAssegnato;
	@ManagedProperty(HttpSessionUtil.JSF_NAME)
	private HttpSessionUtil httpSessionUtil;
	@ManagedProperty(AziendaSelectionView.JSF_NAME)
	private AziendaSelectionView aziendaSelectionView;
	@ManagedProperty(SettingServiceImpl.JSF_NAME)
	private SettingService settingService;

	@PostConstruct
	public void init() {
		calendarioAppuntamentoViewModel = new CalendarioAppuntamentoViewModel();
		lazyEventModel = new LazyScheduleModel() {

			@Override
			public void loadEvents(Date start, Date end) {
				List<CalendarioAppuntamentoViewModel> appuntamentiList = calendarioAppuntamentoService
						.getAppuntamenti(start, end, operatoriSelezionati, aziendaSelectionView.getCurrentAziendaId(), 0, null,
								httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(),
								httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CALENDARIO, OperatorViewModel.PermissionType.READ));
				if (appuntamentiList != null) {
					for (CalendarioAppuntamentoViewModel appuntamento : appuntamentiList) {

						DefaultScheduleEvent event = new DefaultScheduleEvent(appuntamento.getTitolo(),
								dateConversionUtil.calcDateFromLocalDateTime(appuntamento.getInizioAppuntamento()),
								dateConversionUtil.calcDateFromLocalDateTime(appuntamento.getFineAppuntamento()),
								appuntamento.getId());

						if (appuntamento.getNominativo() != null) {
							if (Tipo.CLIENTE.getValue().equalsIgnoreCase(appuntamento.getNominativo().getTipo())) {
								event.setStyleClass("event-cliente");
							} else {
								event.setStyleClass("event-nominativo");
							}
						} else {
							event.setStyleClass("event-libero");
						}

						lazyEventModel.addEvent(event);
					}
				}
			}
		};
	}

	private boolean hasAuthorityReadAppuntamento(CalendarioAppuntamentoViewModel calendarioAppuntamentoViewModel) {
		return httpSessionUtil.hasAuthority(RoleName.CALENDARIO_READ_SUPER)
				|| (httpSessionUtil.hasAnyAuthority(RoleName.CALENDARIO_READ_ALL) && calendarioAppuntamentoViewModel != null
				&& calendarioAppuntamentoViewModel.getOperatorAssegnato() != null && calendarioAppuntamentoViewModel.getOperatorAssegnato().getAzienda().getId() == httpSessionUtil.getAziendaId())
				|| (httpSessionUtil.hasAnyAuthority(RoleName.CALENDARIO_READ_OWN)
						&& calendarioAppuntamentoViewModel != null
						&& calendarioAppuntamentoViewModel.getOperatorAssegnato() != null && httpSessionUtil
								.getOperatorId() == calendarioAppuntamentoViewModel.getOperatorAssegnato().getId());
	}

	public void onEventSelect(SelectEvent selectEvent) {
		ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
		calendarioAppuntamentoViewModel = calendarioAppuntamentoService.getOne((long) event.getData());

		if (!hasAuthorityReadAppuntamento(calendarioAppuntamentoViewModel)) {
			FacesUtil.addMessage(propertiesUtil.getMsgReadAppuntamentoUnauthorized(), FacesMessage.SEVERITY_WARN);
			return;
		}

		calendarioAppuntamentoViewModel.setInizioAppuntamento(dateConversionUtil
				.convertToLocalDateTimeInRome(calendarioAppuntamentoViewModel.getInizioAppuntamento()));
		calendarioAppuntamentoViewModel.setFineAppuntamento(
				dateConversionUtil.convertToLocalDateTimeInRome(calendarioAppuntamentoViewModel.getFineAppuntamento()));
		FacesUtil.execute("PF('appuntamentoSelected').show();");
		FacesUtil.update("form:dlgAppuntamentoView");
	}

	public void onDateSelect(SelectEvent selectEvent) {
		Date date = (Date) selectEvent.getObject();
		calendarioAppuntamentoViewModel.setInizioAppuntamento(dateConversionUtil.calcLocalDateTimeFromDate(date));
		calendarioAppuntamentoViewModel.setFineAppuntamento(
				dateConversionUtil.calcLocalDateTimeFromDate(date).plusMinutes(Constants.SLOT_MINUTES_SCHEDULE));
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		ScheduleEvent scheduleEvent = event.getScheduleEvent();
		onEventResizeOrMove(scheduleEvent, propertiesUtil.getMsgScheduleEventMovedSuccess(),
				event.getScheduleEvent().getStartDate(), event.getDayDelta(), event.getMinuteDelta());
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		ScheduleEvent scheduleEvent = event.getScheduleEvent();
		onEventResizeOrMove(scheduleEvent, propertiesUtil.getMsgScheduleEventMovedSuccess(),
				event.getScheduleEvent().getEndDate(), event.getDayDelta(), event.getMinuteDelta());
	}

	private void onEventResizeOrMove(ScheduleEvent scheduleEvent, String messageToShow, Date date, int dayDelta,
			int minuteDelta) {
		calendarioAppuntamentoService.changeTimeOne((long) scheduleEvent.getData(), scheduleEvent.getStartDate(),
				scheduleEvent.getEndDate());

		String message = MessageFormat.format(messageToShow,
				dateConversionUtil.calcStringFromLocalDateTime(dateConversionUtil.calcLocalDateTimeFromDate(date)
						.minusDays(dayDelta).minusMinutes(minuteDelta)),
				dateConversionUtil.calcStringFromLocalDateTime(dateConversionUtil.calcLocalDateTimeFromDate(date)));

		FacesUtil.addMessage(message, FacesMessage.SEVERITY_INFO);
	}

	private void saveAppuntamentoFromPraticaOrNominativo(ClienteViewModel clienteViewModel,
			PraticaViewModel praticaViewModel) {

			calendarioAppuntamentoViewModel.setNominativo(clienteViewModel);
			calendarioAppuntamentoViewModel.setPratica(praticaViewModel);

			saveAppuntamentoAndSendSms(calendarioAppuntamentoViewModel);
	}

	private void saveAppuntamentoAndCheckResultFromPraticaOrNominativo() {

		ClienteViewModel clienteViewModel = (ClienteViewModel) passaggioParametriUtils.getParametri()
				.get(Parametro.CLIENTE_VIEWMODEL_APPUNTAMENTO_PARAMETER);
		PraticaViewModel praticaViewModel = (PraticaViewModel) passaggioParametriUtils.getParametri()
				.get(Parametro.PRATICA_VIEWMODEL_APPUNTAMENTO_PARAMETER);

		saveAppuntamentoFromPraticaOrNominativo(clienteViewModel, praticaViewModel);
		FacesUtil.execute("PF('newAppuntamentoFromNominativoCliente').hide();");

		passaggioParametriUtils.getParametri().remove(Parametro.CLIENTE_VIEWMODEL_APPUNTAMENTO_PARAMETER);
		passaggioParametriUtils.getParametri().remove(Parametro.PRATICA_VIEWMODEL_APPUNTAMENTO_PARAMETER);
		resetAppuntamento();

		if (clienteViewModel != null) {
			FacesUtil.update("form:logNominativoPanel");
		}
	}

	private void saveAppuntamentoFromDashboard() {
		saveAppuntamentoAndSendSms(calendarioAppuntamentoViewModel);
	}

	private void saveAppuntamentoAndSendSms(CalendarioAppuntamentoViewModel calendarioAppuntamentoViewModel) {
			long appuntamentoId = calendarioAppuntamentoService.saveAppuntamento(
					calendarioAppuntamentoViewModel, httpSessionUtil.getOperatorId());
			FacesUtil.addMessage(propertiesUtil.getMsgAddAppuntamentoSuccess());

			if(sendSmsClienteNominativo) {
				try {
					calendarioAppuntamentoService.sendSmsAppuntamento(appuntamentoId, CalendarioAppuntamentoService.SMSType.CLIENT, httpSessionUtil.getAziendaId());
					FacesUtil.addMessage(propertiesUtil.getMsgAppuntamentoClienteNominativoNotificaSmsSuccess());
				} catch (CannotSendSmsException e) {
					FacesUtil.addMessage(propertiesUtil.getMsgAppuntamentoClienteNominativoNotificaSmsError(),
							FacesMessage.SEVERITY_ERROR);
				} catch (SmsNotSentException e) {
					FacesUtil.addMessage(propertiesUtil.getMsgAppuntamentoClienteNominativoNotificaSmsSendError(),
							FacesMessage.SEVERITY_ERROR);
				} catch (NotEnoughCreditException e) {
                    FacesUtil.addMessage(propertiesUtil.getMsgSmsErrorNotEnoughBalance(),
                            FacesMessage.SEVERITY_ERROR);
                }
			}

			if(sendSmsOperatoreAssegnato) {
				try {
					calendarioAppuntamentoService.sendSmsAppuntamento(appuntamentoId, CalendarioAppuntamentoService.SMSType.OPERATOR,
							httpSessionUtil.getAziendaId());
					FacesUtil.addMessage(propertiesUtil.getMsgAppuntamentoOperatoreNotificaSmsSuccess());
				} catch (CannotSendSmsException e) {
					FacesUtil.addMessage(propertiesUtil.getMsgAppuntamentoOperatoreNotificaSmsError(),
							FacesMessage.SEVERITY_ERROR);
				} catch (SmsNotSentException e) {
					FacesUtil.addMessage(propertiesUtil.getMsgAppuntamentoOperatoreNotificaSmsSendError(),
							FacesMessage.SEVERITY_ERROR);
				} catch (NotEnoughCreditException e) {
                    FacesUtil.addMessage(propertiesUtil.getMsgSmsErrorNotEnoughBalance(),
                            FacesMessage.SEVERITY_ERROR);
                }
            }
	}

	private void saveAppuntamentoAndSendSmsFromDashboard() {
		saveAppuntamentoFromDashboard();
		FacesUtil.execute("PF('newAppuntamentoWizard').hide();PF('appuntamentoSelected').hide();");
		resetAppuntamentoPraticaSelezionataAndWizard();
	}

	/**
	 * Chiamiamo questo metodo dalle pagine che valida i dati ed informa nel
	 * caso ci siano gia appuntamenti in quella fascia oraria chiedendo conferma
	 * di inserimento tramite la dialog
	 *
	 * @param saveAppuntamentoFrom
	 */
	public void checkAppuntamentiPresentiBeforeSave(String saveAppuntamentoFrom) {

		if (StringUtils.isEmpty(calendarioAppuntamentoViewModel.getTitolo())
				|| calendarioAppuntamentoViewModel.getInizioAppuntamento() == null
				|| calendarioAppuntamentoViewModel.getFineAppuntamento() == null) {
			FacesUtil.addMessage(propertiesUtil.getMsgAppuntamentoDatiMancanti(), FacesMessage.SEVERITY_ERROR);
			return;
		}

		LocalDateTime inizioAppuntamentoUTC = dateConversionUtil
				.convertToLocalDateTimeInUtc(calendarioAppuntamentoViewModel.getInizioAppuntamento());
		LocalDateTime fineAppuntamentoUTC = dateConversionUtil
				.convertToLocalDateTimeInUtc(calendarioAppuntamentoViewModel.getFineAppuntamento());
		List<CalendarioAppuntamentoViewModel> calendarioAppuntamentoViewModelList = calendarioAppuntamentoService
				.getAppuntamenti(inizioAppuntamentoUTC, fineAppuntamentoUTC, null, aziendaSelectionView.getCurrentAziendaId(),
						calendarioAppuntamentoViewModel.getId(),
						getOperatoreAssegnato(calendarioAppuntamentoViewModel, saveAppuntamentoFrom), httpSessionUtil.getAziendaId(), httpSessionUtil.getOperatorId(),
						httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CALENDARIO, OperatorViewModel.PermissionType.READ));
		this.saveAppuntamentoFrom = saveAppuntamentoFrom;
		if (calendarioAppuntamentoViewModelList != null && !calendarioAppuntamentoViewModelList.isEmpty()) {
			FacesUtil.openDialog(FacesUtil.DialogType.CONFIRM_SAVE_CALENDARIO_ALTRI_APPUNTAMENTI_PRESENTI);
		} else {
			saveAppuntamento();
		}
	}

	private String getOperatoreAssegnato(CalendarioAppuntamentoViewModel calendarioAppuntamentoViewModel,
			String saveAppuntamentoFrom) {
		ClienteViewModel clienteViewModel = (ClienteViewModel) passaggioParametriUtils.getParametri()
				.get(Parametro.CLIENTE_VIEWMODEL_APPUNTAMENTO_PARAMETER);
		PraticaViewModel praticaViewModel = (PraticaViewModel) passaggioParametriUtils.getParametri()
				.get(Parametro.PRATICA_VIEWMODEL_APPUNTAMENTO_PARAMETER);

		if (saveAppuntamentoFrom.equalsIgnoreCase(FROM_DASHBOARD)) {
			clienteViewModel = calendarioAppuntamentoViewModel.getNominativo();
			praticaViewModel = calendarioAppuntamentoViewModel.getPratica();
		}

		if (clienteViewModel != null) {
			if (Tipo.CLIENTE.getValue().equalsIgnoreCase(clienteViewModel.getTipo())) {
				return praticaViewModel.getOperatore().getUsername();
			} else {
				return clienteViewModel.getOperatoreNominativo().getUsername();
			}
		} else {
			return calendarioAppuntamentoViewModel.getOperatorAssegnato().getUsername();
		}
	}

	/**
	 * Viene chiamato direttamente dalla dialog di conferma, perche ormai
	 * abbiamo gia controllato che sia tutto ok (campi validati e no altri
	 * appuntamenti)
	 */
	public void saveAppuntamento() {
		switch (saveAppuntamentoFrom) {
		case FROM_DASHBOARD:
			saveAppuntamentoAndSendSmsFromDashboard();
			break;
		case FROM_PRATICA_NOMINATIVO:
			saveAppuntamentoAndCheckResultFromPraticaOrNominativo();
			break;
		}
		FacesUtil.closeDialog(FacesUtil.DialogType.CONFIRM_SAVE_CALENDARIO_ALTRI_APPUNTAMENTI_PRESENTI);
		FacesUtil.update("form:schedule");
		FacesUtil.update("form:scheduleGrowl");
	}

	public void resetAppuntamento() {
		calendarioAppuntamentoViewModel = new CalendarioAppuntamentoViewModel();
		sendSmsClienteNominativo = false;
		sendSmsOperatoreAssegnato = false;
	}

	public List<ClienteViewModel> completeClientiNominativi(final String query) {
		return searchService.completeClientiNominativi(query, httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ),
				httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.NOMINATIVI, OperatorViewModel.PermissionType.READ),
				httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId());
	}

	public List<PraticaViewModel> getPraticheListByCliente() {
		if (calendarioAppuntamentoViewModel.getNominativo() != null) {
			return calendarioAppuntamentoService
					.findPraticheByIdCliente(calendarioAppuntamentoViewModel.getNominativo().getId(), httpSessionUtil.getOperatorId(),
							httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.READ));
		}
		return null;
	}

	public void onRowSelect(SelectEvent event) {
		long id = ((PraticaViewModel) event.getObject()).getCodicePratica();
		setPraticaIntoCalendarioAppuntamenti(id);
	}

	private void setPraticaIntoCalendarioAppuntamenti(long id) {
		calendarioAppuntamentoViewModel.setPratica(clienteManagerService.findPraticaByCodicePratica(id));
	}

	public boolean isNominativoRendered() {
		return calendarioAppuntamentoViewModel.getNominativo() != null && Tipo.NOMINATIVO.getValue()
				.equalsIgnoreCase(calendarioAppuntamentoViewModel.getNominativo().getTipo());
	}

	public boolean isPraticaRendered() {
		return calendarioAppuntamentoViewModel.getNominativo() != null
				&& Tipo.CLIENTE.getValue().equalsIgnoreCase(calendarioAppuntamentoViewModel.getNominativo().getTipo());
	}

	public boolean isAppuntamentoLiberoRendered() {
		return calendarioAppuntamentoViewModel.getNominativo() == null;
	}

	// TODO codio il delirio qua poi lo aggiusto
	public String onFlowProcess(FlowEvent event) {
		// se nuovo e vecchio step sono uguali significa che ho chiuso la
		// dialog, quindi torno all inizio
		// sulla chiusura dialog nell ajax ci vuole true nella chiamata a
		// loadStep altrimenti questo metodo non viene chiamato e va
		// al tab successivo causando la validazione del titolo
		// purtroppo non ho trovato documentazione per capire cosa succede
		// realmente
		if (event.getOldStep().equals(event.getNewStep())) {
			return GENERALI_TAB;
		}

		if (GENERALI_TAB.equals(event.getOldStep())) {
			if (calendarioAppuntamentoViewModel.getNominativo() == null) {
				return RIEPILOGO_TAB;
			} else if (Tipo.NOMINATIVO.getValue()
					.equalsIgnoreCase(calendarioAppuntamentoViewModel.getNominativo().getTipo())) {
				return RIEPILOGO_TAB;
			} else {
				if (getPraticheListByCliente().size() > 1) {
					return SPECIFICI_TAB;
				} else {
					setPraticaIntoCalendarioAppuntamenti(getPraticheListByCliente().get(0).getCodicePratica());
					return RIEPILOGO_TAB;
				}
			}
		} else if (RIEPILOGO_TAB.equals(event.getOldStep())) {
			if (calendarioAppuntamentoViewModel.getNominativo() == null) {
				return GENERALI_TAB;
			} else if (Tipo.NOMINATIVO.getValue()
					.equalsIgnoreCase(calendarioAppuntamentoViewModel.getNominativo().getTipo())) {
				return GENERALI_TAB;
			} else {
				if (getPraticheListByCliente().size() == 1) {
					return GENERALI_TAB;
				} else {
					return SPECIFICI_TAB;
				}
			}
		} else if (SPECIFICI_TAB.equals(event.getOldStep()) && RIEPILOGO_TAB.equals(event.getNewStep())
				&& selectedPratica == null) {
			FacesUtil.addMessage(propertiesUtil.getMsgAppuntamentoDatiMancanti2());
			return SPECIFICI_TAB;
		} else if (SPECIFICI_TAB.equals(event.getOldStep())) {
			return event.getNewStep();
		}
		return GENERALI_TAB;
	}

	public void resetAppuntamentoPraticaSelezionataAndWizard() {
		calendarioAppuntamentoViewModel = new CalendarioAppuntamentoViewModel();
		selectedPratica = null;
		sendSmsClienteNominativo = false;
		sendSmsOperatoreAssegnato = false;
	}

	public void deleteAppuntamento() {
		try {
			calendarioAppuntamentoService.deleteAppuntamento(calendarioAppuntamentoViewModel.getId(), httpSessionUtil.getOperatorId());
			RequestContext.getCurrentInstance().execute("PF('appuntamentoSelected').hide();");
			FacesUtil.addMessage(propertiesUtil.getMsgDeleteAppuntamentoSuccess(), FacesMessage.SEVERITY_INFO);
			FacesUtil.update("form:schedule");
			FacesUtil.update("form:scheduleGrowl");
		} catch (DataAccessException e) {
			RequestContext.getCurrentInstance().execute("PF('appuntamentoSelected').hide();");
			log.error("Errore nell'eliminazione dell'Appuntamento " + e.getMessage());
			FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public String concatNomeCognome() {
		if (calendarioAppuntamentoViewModel != null && calendarioAppuntamentoViewModel.getNominativo() != null
				&& !StringUtils.isEmpty(calendarioAppuntamentoViewModel.getNominativo().getNome())
				&& !StringUtils.isEmpty(calendarioAppuntamentoViewModel.getNominativo().getCognome())) {
			return calendarioAppuntamentoViewModel.getNominativo().getCognome().concat(" ")
					.concat(calendarioAppuntamentoViewModel.getNominativo().getNome());
		}
		return "";
	}

	public void redirectToClienteOrNominativoForm() throws IOException {
		if (calendarioAppuntamentoViewModel.getNominativo() != null) {
			if (Tipo.CLIENTE.getValue().equalsIgnoreCase(calendarioAppuntamentoViewModel.getNominativo().getTipo())) {
				ClientePratica clientePratica = new ClientePratica();
				clientePratica.setClienteViewModel(calendarioAppuntamentoViewModel.getNominativo());
				clientePratica.setPraticaViewModel(calendarioAppuntamentoViewModel.getPratica());
				passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_SELECTED_ON_SEARCH, clientePratica);
				FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getFormsPath(false));
			} else {
				passaggioParametriUtils.getParametri().put(Parametro.NOMINATIVO_SELECTED_ON_SEARCH,
						calendarioAppuntamentoViewModel.getNominativo());
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(Constants.getFormsNominativoPath(false));
			}
		}
	}

	public boolean isRenderedNotificaSmsInFormsCliente() {
		ClienteViewModel clienteViewModel = (ClienteViewModel) passaggioParametriUtils.getParametri()
				.get(Parametro.CLIENTE_VIEWMODEL_APPUNTAMENTO_PARAMETER);
		return clienteViewModel != null && Tipo.CLIENTE.getValue().equalsIgnoreCase(clienteViewModel.getTipo());
	}

	public boolean isRenderedNotificaSmsInFormsNominativo() {
		ClienteViewModel clienteViewModel = (ClienteViewModel) passaggioParametriUtils.getParametri()
				.get(Parametro.CLIENTE_VIEWMODEL_APPUNTAMENTO_PARAMETER);
		return clienteViewModel != null && Tipo.NOMINATIVO.getValue().equalsIgnoreCase(clienteViewModel.getTipo());
	}

	public void changeToDate() {
		if (calendarioAppuntamentoViewModel.getFineAppuntamento() == null || calendarioAppuntamentoViewModel
				.getFineAppuntamento().isBefore(calendarioAppuntamentoViewModel.getInizioAppuntamento())) {
			calendarioAppuntamentoViewModel.setFineAppuntamento(calendarioAppuntamentoViewModel.getInizioAppuntamento()
					.plusMinutes(Constants.SLOT_MINUTES_SCHEDULE));
		}
	}

	public void changeFromDate() {
		if (calendarioAppuntamentoViewModel.getInizioAppuntamento() == null || calendarioAppuntamentoViewModel
				.getInizioAppuntamento().isAfter(calendarioAppuntamentoViewModel.getFineAppuntamento())) {
			calendarioAppuntamentoViewModel.setInizioAppuntamento(calendarioAppuntamentoViewModel.getFineAppuntamento()
					.minusMinutes(Constants.SLOT_MINUTES_SCHEDULE));
		}
	}

	public boolean isSelectCheckboxMenuRendered() {
		return httpSessionUtil.hasAnyAuthority(RoleName.CALENDARIO_READ_ALL, RoleName.CALENDARIO_READ_SUPER);
	}

	public boolean isClienteNominativoRendered() {
		return httpSessionUtil.hasAnyAuthority(RoleName.CLIENTI_WRITE_OWN, RoleName.CLIENTI_WRITE_ALL, RoleName.CLIENTI_WRITE_SUPER,
				RoleName.NOMINATIVI_WRITE_OWN, RoleName.NOMINATIVI_WRITE_ALL, RoleName.NOMINATIVI_WRITE_SUPER);
	}

	private boolean isAppuntamentiButtonRendered(RoleName roleNameSuper, RoleName roleNameAll, RoleName roleNameOwn) {
		if(httpSessionUtil.hasAuthority(roleNameSuper)) {
			return true;
		} else if (httpSessionUtil.hasAuthority(roleNameAll)) {
			if (calendarioAppuntamentoViewModel != null
					&& calendarioAppuntamentoViewModel.getOperatorAssegnato() != null && httpSessionUtil
					.getAziendaId() == calendarioAppuntamentoViewModel.getOperatorAssegnato().getAzienda().getId()) {
				return true;
			}
		} else if (httpSessionUtil.hasAuthority(roleNameOwn)) {
			if (calendarioAppuntamentoViewModel != null
					&& calendarioAppuntamentoViewModel.getOperatorAssegnato() != null && httpSessionUtil
							.getOperatorId() == calendarioAppuntamentoViewModel.getOperatorAssegnato().getId()) {
				return true;
			}
		}
		return false;
	}

	public boolean isSaveAppuntamentiButtonRendered() {
		return isAppuntamentiButtonRendered(RoleName.CALENDARIO_WRITE_SUPER, RoleName.CALENDARIO_WRITE_ALL, RoleName.CALENDARIO_WRITE_OWN);
	}

	public boolean isDeleteAppuntamentiButtonRendered() {
		return isAppuntamentiButtonRendered(RoleName.CALENDARIO_DELETE_SUPER, RoleName.CALENDARIO_DELETE_ALL, RoleName.CALENDARIO_DELETE_OWN);
	}

	public boolean canViewPraticaOrNominativo() {
		if (isNominativoRendered()) {
			return httpSessionUtil.hasAuthority(RoleName.NOMINATIVI_READ_SUPER)
					|| (httpSessionUtil.hasAuthority(RoleName.NOMINATIVI_READ_ALL)
						&& calendarioAppuntamentoViewModel.getNominativo() != null
						&& calendarioAppuntamentoViewModel.getNominativo().getOperatoreNominativo().getAzienda()
						.getId() == httpSessionUtil.getAziendaId())
					|| (httpSessionUtil.hasAuthority(RoleName.NOMINATIVI_READ_OWN)
							&& calendarioAppuntamentoViewModel.getNominativo() != null
							&& calendarioAppuntamentoViewModel.getNominativo().getOperatoreNominativo()
									.getId() == httpSessionUtil.getOperatorId());
		}
		if (isPraticaRendered()) {
			return httpSessionUtil.hasAuthority(RoleName.CLIENTI_READ_SUPER)
					|| (httpSessionUtil.hasAuthority(RoleName.CLIENTI_READ_ALL)
						&& calendarioAppuntamentoViewModel.getPratica() != null && calendarioAppuntamentoViewModel
						.getPratica().getOperatore().getAzienda().getId() == httpSessionUtil.getAziendaId())
					|| (httpSessionUtil.hasAuthority(RoleName.CLIENTI_READ_OWN)
							&& calendarioAppuntamentoViewModel.getPratica() != null && calendarioAppuntamentoViewModel
									.getPratica().getOperatore().getId() == httpSessionUtil.getOperatorId());
		}
		return false;
	}

	public boolean canAddAppuntamentoFromClienteNominativo(OperatorViewModel operatorViewModel) {
		return httpSessionUtil.hasAuthority(RoleName.CALENDARIO_WRITE_SUPER)
				|| (httpSessionUtil.hasAuthority(RoleName.CALENDARIO_WRITE_ALL)
					&& operatorViewModel.getAzienda().getId() == httpSessionUtil.getAziendaId())
				|| (httpSessionUtil.hasAnyAuthority(RoleName.CALENDARIO_WRITE_OWN)
						&& operatorViewModel.getId() == httpSessionUtil.getOperatorId());
	}

}
