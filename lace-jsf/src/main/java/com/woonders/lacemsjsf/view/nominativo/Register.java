package com.woonders.lacemsjsf.view.nominativo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.db.entityenum.Provenienza;
import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.entityutil.ClienteUtil;
import com.woonders.lacemscommon.exception.*;
import com.woonders.lacemscommon.service.*;
import com.woonders.lacemscommon.service.impl.*;
import com.woonders.lacemscommon.sms.MessageType;
import com.woonders.lacemscommon.util.*;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.*;
import com.woonders.lacemsjsf.util.FacesUtil.DialogType;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.file.ArchiviazioneRendered;
import com.woonders.lacemsjsf.view.log.LogDTO;
import com.woonders.lacemsjsf.view.simulator.SimulatorUtilsView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mail.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.hibernate.result.NoMoreReturnsException;
import org.primefaces.context.RequestContext;
import org.springframework.dao.DataAccessException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "register")
@ViewScoped
@Getter
@Setter
@Slf4j
public class Register implements Serializable {

    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    private List<TrattenuteViewModel> impegniSelezionati;
    private List<PraticaViewModel> preventivoList;
    
    private List<PrivacyTemplateViewModel> privacyTemplateList;
    @ManagedProperty(PrivacyTemplateServiceImpl.JSF_NAME)
    private PrivacyTemplateService privacyTemplateService;
    
    private List<TrattenuteViewModel> trattenuteNominativoList;
    private ClienteViewModel nominativo;
    private NotificaLeadSmsViewModel notificaLeadSmsViewModel;
    @ManagedProperty(NominativoServiceImpl.JSF_NAME)
    private NominativoService nominativoService;
    private PraticaViewModel preventivoDaCreare;
    @ManagedProperty(MailClient.JSF_NAME)
    private MailClient mailClient;
    private List<AmministrazioneViewModel> amministrazioneViewModelList;
    @ManagedProperty(AmministrazioneServiceImpl.JSF_NAME)
    private AmministrazioneService amministrazioneService;
    
    @ManagedProperty(ClientePrivacyTemplateServiceImpl.JSF_NAME)
    private ClientePrivacyTemplateService clientePrivacyTemplateService;
    
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    private ResidenzaViewModel residenzaViewModel;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(ClienteUtil.JSF_NAME)
    private ClienteUtil clienteUtil;
    private String commentoLog;
    @ManagedProperty(CfGenerator.JSF_NAME)
    private CfGenerator cfGenerator;
    @ManagedProperty(LaceMailSender.JSF_NAME)
    private LaceMailSender laceMailSender;
    @ManagedProperty(LogServiceImpl.JSF_NAME)
    private LogService logService;
    @ManagedProperty(SmsServiceFacadeImpl.JSF_NAME)
    private SmsServiceFacade smsServiceFacade;
    @ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
    private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
    private String smsText;
    @ManagedProperty(ExceptionResolverUtil.JSF_NAME)
    private ExceptionResolverUtil exceptionResolverUtil;
    @ManagedProperty(SimulatorUtilsView.JSF_NAME)
    private SimulatorUtilsView simulatorUtilsView;
    @ManagedProperty(ErrorConverter.JSF_NAME)
    private ErrorConverter errorConverter;
    private ArchiviazioneRendered archiviazioneRendered;
    private String contestaNominativoCommento;
    private int motivoContestazione;
    @ManagedProperty(SettingServiceImpl.JSF_NAME)
    private SettingService settingService;
    private ClientePrivacyTemplateViewModel clientePrivacyTemplateViewModel;
    
    @PostConstruct
    public void init() {
        nominativo = new ClienteViewModel();
        residenzaViewModel = new ResidenzaViewModel();
        preventivoDaCreare = new PraticaViewModel();
        amministrazioneViewModelList = new ArrayList<>();
        trattenuteNominativoList = new ArrayList<>();
        preventivoList = new ArrayList<>();
        impegniSelezionati = new ArrayList<>();
        
        privacyTemplateList = privacyTemplateService.findAll();
        
        //initialized at false (hidden file upload). Where needed values will be changed to true
        archiviazioneRendered = ArchiviazioneRendered.builder().documentiClienteRendered(false)
                .documentiPraticaRendered(false).build();
        if (passaggioParametriUtils.getParametri().get(Parametro.NUOVO_NOMINATIVO_PARAMETER) != null) {
            setNominativoFromCheckNominativo();
        } else {
            setNominativoSelectedOnSearch();
        }
        notificaLeadSmsViewModel = nominativoService.getNotificaLeadSms(nominativo.getId());
        passaggioParametriUtils.getParametri().put(Parametro.ID_NOMINATIVO_ON_FORM_NOMINATIVO_PARAMETER,
                LogDTO.builder().nominativoId(nominativo.getId()).tipoLog(LogService.TipoLog.NOMINATIVO).build());
        log.info("ABLE -------------------------------- init nominativo" + nominativo.getStatoNominativo());
    }

    public void add() {
        final PraticaViewModel prev = new PraticaViewModel();
        prev.setOperatore(operatorService.getOne(httpSessionUtil.getOperatorId()));
        preventivoList.add(prev);
    }

    public void remove(final PraticaViewModel prev) {
        try {
            if (prev.getCodicePratica() != 0) {
                nominativoService.deletePreventivo(prev.getCodicePratica());
            }
            preventivoList.remove(prev);
        } catch (final DataAccessException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addTratt() {
        final TrattenuteViewModel trattenutaNominativo = new TrattenuteViewModel();
        trattenuteNominativoList.add(trattenutaNominativo);
    }

    public void removeTratt(final TrattenuteViewModel trattenutaNominativo) {
        try {
            if (trattenutaNominativo.getCodStip() != 0) {
                nominativoService.deleteTrattenuta(trattenutaNominativo);
            }
            trattenuteNominativoList.remove(trattenutaNominativo);
        } catch (final DataAccessException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void clearPassaggioParametriAmministrazione() {
        passaggioParametriUtils.getParametri().remove(Parametro.AMMINISTRAZIONE_VIEWMODEL_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.RAGIONE_SOCIALE_PARAMETER);
    }

    public void setPreventiviDaCreare(final PraticaViewModel prev) {
        preventivoDaCreare = prev;
    }

    public void saveNominativo(){ 
    	saveNominativo(true);
    	//FacesUtil.execute("window.location.reload();");
    	}

    public void saveNominativo(boolean showAlert) {
        
    	nominativo.setTipo(Tipo.NOMINATIVO.getValue());
        if (nominativo.getCf().isEmpty()) {
            nominativo.setCf(null);
        }
        if (nominativo.getStatoNominativo() != null && nominativo.getStatoNominativo().length() > 0) {
            log.info("ABLE -------------------------------- save nominativo " + nominativo.getStatoNominativo());
        }
        else {
            nominativo.setStatoNominativo(StatoNominativo.DA_LAVORARE.getValue());
        }
        if (!nominativo.getProvenienza().equals("Lead"))
        {	
        	nominativo.setFornitoreLead("");        
        }
        nominativo = nominativoService.saveNominativo(nominativo, residenzaViewModel, preventivoList,
                trattenuteNominativoList, amministrazioneViewModelList, commentoLog, httpSessionUtil.getOperatorId());
        setResidenzaViewModel(nominativo.getResidenzaViewModel());
        setAmministrazioneViewModelList(nominativo.getAmministrazioneViewModelList());
        setTrattenuteNominativoList(nominativo.getTrattenutaViewModelList());
        setPreventivoList(nominativo.getPraticaViewModelList());
        passaggioParametriUtils.getParametri().put(Parametro.ID_NOMINATIVO_ON_FORM_NOMINATIVO_PARAMETER,
                LogDTO.builder().nominativoId(nominativo.getId()).tipoLog(LogService.TipoLog.NOMINATIVO).build());
        setCommentoLog(null);
        // Add message
        if (showAlert) { FacesUtil.addMessage("Nominativo salvato con Successo"); }
        archiviazioneRendered = ArchiviazioneRendered.builder().documentiClienteRendered(true)
                .documentiPraticaRendered(true).build();
        saveClienteNominativoIdIntoPassaggioParametri(nominativo.getId());
    }

    public void creaPratica() throws IOException {
        try {

            if (impegniSelezionati.size() > 2) {
                FacesUtil.showMessageInDialog(FacesMessage.SEVERITY_WARN, "Attenzione!",
                        "Puoi Estinguere al massimo 2 Impegni");
                return;
            }

            final ClienteViewModel clienteViewModel = nominativoService
                    .findClienteByIdPratica(preventivoDaCreare.getCodicePratica());

            if (clienteViewModel.getCf() == null || clienteViewModel.getCf().isEmpty()) {
                FacesUtil.showMessageInDialog(FacesMessage.SEVERITY_WARN, "CF non Presente",
                        "Attenzione! per creare la pratica inserire il Codice Fiscale");
                return;
            }

            nominativoService.creaPraticaDaPreventivoNominativo(clienteViewModel,
                    clienteViewModel.getResidenzaViewModel(), trattenuteNominativoList, impegniSelezionati,
                    preventivoList, preventivoDaCreare, httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId());

            FacesUtil.addMessage("Pratica Creata con Successo");
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(Constants.getDashboardNominativoPath(false));

        } catch (final DataAccessException e) {
            log.error("Errore creazione pratica da nominativo", e);
            FacesUtil.addMessage("Errore nella creazione della Pratica", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void sendSms() {
        try {
            if (smsServiceFacade.checkEnoughBalance(nominativo.getTelefono(), smsText, httpSessionUtil.getAziendaId())) {
                try {
                    smsServiceFacade.sendSms(nominativo.getTelefono(), smsText, MessageType.SMS_SINGLE,
                            httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId(),
                            requestTenantIdentifierResolver.getTenantIdentifier(), nominativo.getId(), LogService.TipoLog.NOMINATIVO);
                    logService.logCustomAction(nominativo.getId(), LogService.TipoLog.NOMINATIVO, smsText,
                            LogService.CustomAction.SMS, httpSessionUtil.getOperatorId());
                    FacesUtil.addMessage(propertiesUtil.getMsgSmsSentSuccess());
                    FacesUtil.updateSmsInfo();
                } catch (final SmsNotSentException e) {
                    FacesUtil.addMessage(propertiesUtil.getMsgErrorSendSmsGeneric(), FacesMessage.SEVERITY_ERROR);
                } catch (final CannotSendSmsException e) {
                    FacesUtil.addMessage(propertiesUtil.getMsgErrorSmsMissingFields(), FacesMessage.SEVERITY_ERROR);
                }
            } else {
                FacesUtil.addMessage(propertiesUtil.getMsgSmsErrorNotEnoughBalance(), FacesMessage.SEVERITY_ERROR);
            }
        } catch (final UnableToCalculateSmsPrice unableToCalculateSmsPrice) {
            // i don-t want to show an error saying we are calculating the price
            // since it-s already shown in the view,
            // so let-s show a generic error message
            FacesUtil.addMessage(propertiesUtil.getMsgErrorSendSmsGeneric(), FacesMessage.SEVERITY_ERROR);
        }
    }


    private void logInvioContestazione(){
        logService.logCustomAction(nominativo.getId(), LogService.TipoLog.NOMINATIVO, "Invio contestazione",
                LogService.CustomAction.INVIO_CONTESTAZIONE, httpSessionUtil.getOperatorId());
    }
    private void logContestazioneAccettata(){
        logService.logCustomAction(nominativo.getId(), LogService.TipoLog.NOMINATIVO, "Contestazione Accettata",
                LogService.CustomAction.RICEZ_CONTESTAZIONE_OK, httpSessionUtil.getOperatorId());
    }
    private void logContestazioneRifiutata(String testo){
        logService.logCustomAction(nominativo.getId(), LogService.TipoLog.NOMINATIVO, "Causa: "+testo,
                LogService.CustomAction.RICEZ_CONTESTAZIONE_KO, httpSessionUtil.getOperatorId());
    }


    public void sendContestaNominativo() {
        ViewModelPage<NominativoLogViewModel> viewModelPage;
        viewModelPage = logService.getNominativoLog(nominativo.getId(), 0, 100, "executionDateTime",
                com.woonders.lacemscommon.db.QueryDSLHelper.SortOrder.valueOf("DESCENDING"),
                null);
        ArrayList<LeadContestatoLog> logArray = new ArrayList<LeadContestatoLog>();

        for (NominativoLogViewModel nominativoLog : viewModelPage.getContent()) {
			
            ZonedDateTime utcDateTime = nominativoLog.getExecutionDateTime().atZone(ZoneId.of("GMT"));
            ZonedDateTime romeDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Rome"));

            logArray.add(LeadContestatoLog.builder().executionDateTime(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(romeDateTime))
                    .commento(nominativoLog.getCommento())
                    .stato(nominativoLog.getStato()).build());
        }
        
        LeadContestato lead = LeadContestato.builder().laceLeadID(nominativo.getId()).laceTenantName(requestTenantIdentifierResolver.getTenantIdentifier())
                .devisProxTenantId(aziendaService.getOne(httpSessionUtil.getAziendaId()).getDevisProxId()).devisProxLeadId(String.valueOf(nominativo.getLeadId()))
                .requestReason(motivoContestazione).requestNote(contestaNominativoCommento).build();

        lead.setLogs(logArray);    
        String response = LeadContestatoService.sendPOST(lead);
        
        int status = -1;
        String msg = "";
        try {
            JsonObject responseJson = new JsonParser().parse(response).getAsJsonObject();
            status = responseJson.get("STATUS").getAsInt();
            msg = responseJson.get("MSG").getAsString();
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("ERROR: on parsing response " + response);
		}
        
        boolean needRefresh = false;
        switch (status) {
		case 0://ERROR
			log.error("ERROR: on dispute " + msg);
			FacesUtil.addMessage("ERRORE nella contestazione", FacesMessage.SEVERITY_ERROR);
			break;
		case 1://DISPUTE SENT
			nominativo.setStatoNominativo(StatoNominativo.CONTESTATO.getValue());
	        saveNominativo(false);
	        logInvioContestazione();
	        needRefresh = true;
	        FacesUtil.addMessage("Contestazione inviata con Successo");
			break;
		case 2://DISPUTE ACCEPTED
	        nominativo.setStatoNominativo(StatoNominativo.CONTESTAZIONE_ACCETTATA.getValue());
	        saveNominativo(false);
            logContestazioneAccettata();
	        needRefresh = true;
	        FacesUtil.addMessage("Contestazione Accettata");
			break;
		case 3://DISPUTE REFUSED
			nominativo.setStatoNominativo(StatoNominativo.CONTESTAZIONE_RIFIUTATA.getValue());
	        saveNominativo(false);
            logContestazioneRifiutata(msg);
	        needRefresh = true;
	        FacesUtil.addMessage("Contestazione rifiutata", FacesMessage.SEVERITY_WARN);	       
			break;			
		default://UNKNOW
			log.error("ERROR: UNKNOW STATUS <"+ status +"> IN RESPONSE " + msg);
			FacesUtil.addMessage("Errore nella contestazione", FacesMessage.SEVERITY_ERROR);
			break;
		}
        FacesUtil.closeDialog(DialogType.DIALOG_CONTESTA_NOMINATIVO);
        
        if (needRefresh)
        	FacesUtil.refresh();
        	//FacesUtil.refreshAfter(4000);
        
//        if (response.startsWith("KO")) {
//            log.error(response);
//            return;
//        }
//        
//        //RETRIVE TH ACTUAL NOMINATIVO MAYBE THE STATO IS CHANGED
//        Boolean alreadyContestato = false;
//        if (nominativoService != null)
//        {
//	        ClienteViewModel nominativoContestato = nominativoService.findClienteByIdCliente(nominativo.getId());
//	        if (nominativoContestato.getStatoNominativo().contentEquals(StatoNominativo.CONTESTAZIONE_ACCETTATA.getValue()))
//	        		alreadyContestato = true;
//	        else if (nominativoContestato.getStatoNominativo().contentEquals(StatoNominativo.CONTESTAZIONE_RIFIUTATA.getValue()))
//        		alreadyContestato = true;	
//	    }
//        else
//        	log.error("--------------------------------------------------------------nominativoService NULL ");
//        
//        if (!alreadyContestato)
//        {
//	        nominativo.setStatoNominativo(StatoNominativo.CONTESTATO.getValue());
//	        saveNominativo(false);
//        }
//        logInvioContestazione();
    }

    public void sendEmail() {
        try {
        	//*** converto l'accapo \n in <br/>
        	mailClient.setBody(mailClient.getBody().replace("\n", "<br/>"));
            
        	laceMailSender.sendEmail(nominativo.getEmail(), nominativo.getOperatoreNominativo().getAzienda().getNomeAzienda(),
                    nominativo.getOperatoreNominativo().getAzienda().getEmail(), mailClient.getSubject(),
                    mailClient.getBody());
            logService.logCustomAction(nominativo.getId(), LogService.TipoLog.NOMINATIVO, mailClient.getBody(),
                    LogService.CustomAction.EMAIL, httpSessionUtil.getOperatorId());
            FacesUtil.addMessage(propertiesUtil.getMsgSendMailOk());
            
            
        } catch (final UnableToSendEmailException e) {
            FacesUtil.addMessage(errorConverter.convertSendEmailError(e.getUnableToSendEmailError()), FacesMessage.SEVERITY_ERROR);
        }
        finally {
        	if (clientePrivacyTemplateViewModel != null)
        		clientePrivacyTemplateService.save(clientePrivacyTemplateViewModel);
        }
    }

    // inserisco i preventivi nel body della mail
    public void addPreventiviToBody() {
        final AziendaViewModel aziendaViewModel = aziendaService.getOne(httpSessionUtil.getAziendaId());
        final String preventivi = mailClient.textPreventiviNominativo(nominativo, preventivoList, aziendaViewModel);
        mailClient.setBody(preventivi);
    }
    
    
    
    
    
    //*** Add By Cristian 16-11-2021
    // inserisco privacy nel body della mail
    public void addPrivacyToBody() {
        //final AziendaViewModel aziendaViewModel = aziendaService.getOne(httpSessionUtil.getAziendaId());
        try
        {
        	 final Map<Long, StringBuilder> privacyTemplateMap = mailClient.textPrivacyTemplate(nominativo, privacyTemplateList);
            
        	 Map.Entry<Long, StringBuilder> entry = privacyTemplateMap.entrySet().iterator().next();
             Long privacy_id  = entry.getKey();
             String privacy = entry.getValue().toString();
             
             mailClient.setBody(privacy);
             mailClient.setSubject("INFORMATIVA SUL TRATTAMENTO DEI SUOI DATI");
             
             if (privacy_id > 0)
             {
            	//*** Registro l'invio del privacy template al determinato cliente con la data 
                 clientePrivacyTemplateViewModel = new ClientePrivacyTemplateViewModel();
                 clientePrivacyTemplateViewModel.setClienteId(nominativo.getId());
                 clientePrivacyTemplateViewModel.setPrivacyTemplatesId(privacy_id);
             }
             else
             {
            	 FacesUtil.addMessage("Non è stato trovato nessun Testo della Privacy Associato al Lead!", FacesMessage.SEVERITY_ERROR);
             }
             
        }
        catch (final Exception ex)
        {
        	FacesUtil.addMessage(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
       
    }
    
    
    
    

    // inserisco i preventivi nel body degli sms
    public void addPreventiviToBodySMS() {
        final String preventivi = mailClient.textPreventiviNominativoSMS(nominativo, preventivoList);
        smsText = preventivi;
    }

    public void viewAmministrazioneSelezionata() throws ItemNotFoundException {
        if (amministrazioneViewModelList != null) {
            passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_VIEWMODEL_PARAMETER,
                    amministrazioneService
                            .findByRagioneSociale(amministrazioneViewModelList.get(0).getRagioneSociale()));
            RequestContext.getCurrentInstance().execute("PF('dlgnuovaamministrazione').show();");
        }
    }

    public void viewAmministrazioneAssegnata(final long idCliente) {
        setAmministrazioneViewModelList(amministrazioneService.findDistinctByCliente_Id(idCliente));
    }

    public void deleteNominativo() throws IOException {
        try {
            nominativoService.delete(nominativo);
            FacesUtil.addMessage(propertiesUtil.getMsgNominativoDeleted());
            FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getDashboardNominativoPath(true));
        } catch (final UnableToDeleteException e) {
            FacesUtil.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public boolean isDisabledButtonDeleteNominativo() {
        // dobbiamo averlo salvato altrimenti non puoi eliminarlo
        // per essere abilitato devi avere delete all oppure delete own e il
        // nominativo deve essere il tuo
        // ritorniamo il negato per fare quando e disabilitato!
        return !(nominativo.getId() != 0 && ((httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_DELETE_SUPER))
                || (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_DELETE_ALL)
                && httpSessionUtil.getAziendaId() == nominativo.getOperatoreNominativo().getAzienda().getId())
                || (httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_DELETE_OWN)
                && httpSessionUtil.getOperatorId() == nominativo.getOperatoreNominativo().getId())));
    }

    private void setNominativoSelectedOnSearch() {
        final ClienteViewModel nominativoSelected = (ClienteViewModel) passaggioParametriUtils.getParametri()
                .get(Parametro.NOMINATIVO_SELECTED_ON_SEARCH);
        if (nominativoSelected != null) {
            getPreventivoList().clear();
            getTrattenuteNominativoList().clear();
            setNominativo(nominativoService.findClienteByIdCliente(nominativoSelected.getId()));
            final ResidenzaViewModel residenzaViewModelFromDb = nominativoService
                    .findResidenzaByIdCliente(nominativoSelected.getId());
            if (residenzaViewModelFromDb != null) {
                setResidenzaViewModel(residenzaViewModelFromDb);
            }
            setPreventivoList(nominativoService.findPraticheByIdCliente(nominativoSelected.getId()));
            setTrattenuteNominativoList(nominativoService.findTrattenuteByIdCliente(nominativoSelected.getId()));
            viewAmministrazioneAssegnata(nominativoSelected.getId());
            archiviazioneRendered = ArchiviazioneRendered.builder().documentiClienteRendered(true)
                    .documentiPraticaRendered(true).build();
            saveClienteNominativoIdIntoPassaggioParametri(nominativoSelected.getId());
        }
    }

    private void setNominativoFromCheckNominativo() {
        nominativo.setCognome(
                (String) passaggioParametriUtils.getParametri().get(Parametro.COGNOME_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setNome(
                (String) passaggioParametriUtils.getParametri().get(Parametro.NOME_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setDataNascita(
                (Date) passaggioParametriUtils.getParametri().get(Parametro.DATA_NASCITA_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setSesso(
                (Cliente.Sesso) passaggioParametriUtils.getParametri().get(Parametro.SESSO_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setLuogoNascita(
                (String) passaggioParametriUtils.getParametri().get(Parametro.LUOGO_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setTelefono(
                (String) passaggioParametriUtils.getParametri().get(Parametro.CELLULARE_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setTelefonoFisso(
                (String) passaggioParametriUtils.getParametri().get(Parametro.FISSO_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setEmail(
                (String) passaggioParametriUtils.getParametri().get(Parametro.EMAIL_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setCf((String) passaggioParametriUtils.getParametri().get(Parametro.CF_CHECK_NOMINATIVO_PARAMETER));
        nominativo.setOperatoreNominativo(operatorService.getOne(httpSessionUtil.getOperatorId()));
        nominativo.setCittadinanza("Italiana");
        calcReverseCf(nominativo);
        nominativo.setEta(clienteUtil.calcEta(nominativo.getDataNascita()));
        clearPassaggioParametriCheckNominativo();
    }

    public void calcReverseCf(final ClienteViewModel nominativo) {
        if (nominativo.getCf() != null && !nominativo.getCf().isEmpty() && nominativo.getCf().length() == 16) {
            nominativo.setDataNascita(cfGenerator.elaboraDataNascitaDaCf(nominativo.getCf()));
            nominativo.setLuogoNascita(cfGenerator.elaboraComuneDaCodiceComune(nominativo.getCf()));
            nominativo.setSesso(cfGenerator.elaboraSessoDaCf(nominativo.getCf()));
        }
    }

    private void clearPassaggioParametriCheckNominativo() {
        passaggioParametriUtils.getParametri().remove(Parametro.COGNOME_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.NOME_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.DATA_NASCITA_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.SESSO_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.LUOGO_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.CELLULARE_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.FISSO_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.EMAIL_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.CF_CHECK_NOMINATIVO_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.NUOVO_NOMINATIVO_PARAMETER);
    }

    public void printPdfNominativo() {
        // TODO controllare lato server stampa pdf abilitata
        passaggioParametriUtils.getParametri().put(Parametro.NOMINATIVO_PDF_PARAMETER, nominativo);
        passaggioParametriUtils.getParametri().put(Parametro.IMPEGNI_NOMINATIVO_PDF_PARAMETER,
                trattenuteNominativoList);
        passaggioParametriUtils.getParametri().put(Parametro.PREVENTIVI_NOMINATIVO_PDF_PARAMETER, preventivoList);
    }

    public String getPdfPage() {
        return Constants.getPdfNominativoPath(true);
    }

    public boolean isDisabledButtonPdf() {
        return !(nominativo.getNome() != null && nominativo.getCognome() != null && !nominativo.getNome().isEmpty()
                && !nominativo.getCognome().isEmpty() && !isPrintPdfDisabled());
    }

    // TODO superclasse con metodo in comune
    public boolean isPrintPdfDisabled() {
        return !httpSessionUtil.isPrintPdfEnabled();
    }

    public boolean isDisabledButtonMail() {
        return StringUtils.isEmpty(nominativo.getEmail());
    }

    public boolean isDisabledButtonSms() {
        return StringUtils.isEmpty(nominativo.getTelefono()) ||
               StringUtils.isEmpty(settingService.getByAziendaId(httpSessionUtil.getAziendaId()).getAlias());
    }

    public boolean isDisabledButtonContestaNominativo() {
        if (nominativo.getFornitoreLead() == null)
            return true;
        boolean isDevisprox = nominativo.getProvenienza().equalsIgnoreCase("Lead") &&
                nominativo.getFornitoreLead().equalsIgnoreCase("DevisProx");
        //isAlreadyDisputed return true only if statoNominativo is not StatoNominativo
        boolean isAlreadyDisputed = (nominativo.getStatoNominativo().equalsIgnoreCase(StatoNominativo.CONTESTATO.getValue()) ||
                nominativo.getStatoNominativo().equalsIgnoreCase(StatoNominativo.CONTESTAZIONE_ACCETTATA.getValue()) ||
                nominativo.getStatoNominativo().equalsIgnoreCase(StatoNominativo.CONTESTAZIONE_RIFIUTATA.getValue()) );
        log.info("isDevisprox="+isDevisprox+" getProvenienza returned "+nominativo.getProvenienza());
        log.info("isAlreadyDisputed=" + isAlreadyDisputed + " getStatoNominativo returned "+ nominativo.getStatoNominativo());
        return !(!isAlreadyDisputed && isDevisprox);
    }

    public boolean isRenderedDataCaricamento() {
        return nominativo.getId() != 0;
    }

    public boolean isRenderedClock() {
        return nominativo.getId() == 0;
    }

    public List<AmministrazioneViewModel> completeAmministrazione(final String query) {
        return amministrazioneService.completeAmministrazione(query);
    }

    public boolean isRenderedEsitoSmsNotificaLead() {
        return nominativo.getId() != 0 && notificaLeadSmsViewModel != null;
    }

    public String getEsitoSmsNotificaLead() {
        if (notificaLeadSmsViewModel != null) {
            return notificaLeadSmsViewModel.getEsito().getValue();
        } else {
            return null;
        }
    }

    public LocalDateTime getDataEsitoSmsNotificaLead() {
        if (notificaLeadSmsViewModel != null) {
            return notificaLeadSmsViewModel.getDataEsito();
        } else {
            return null;
        }
    }

    public void resetDataRecall() {
        nominativo.setDataRecallNominativo(null);
    }

    public boolean isRenderedLogPanel() {
        return nominativo.getId() != 0;
    }

    public boolean isAppuntamentoDisabled() {
        return nominativo.getId() == 0;
    }

    public void setParameterAppuntamento() {
        passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_VIEWMODEL_APPUNTAMENTO_PARAMETER, nominativo);
        passaggioParametriUtils.getParametri().put(Parametro.PRATICA_VIEWMODEL_APPUNTAMENTO_PARAMETER, null);
        RequestContext.getCurrentInstance().execute("PF('newAppuntamentoFromNominativoCliente').show();");
    }

    public boolean canWriteNominativo() {
        return httpSessionUtil.hasAuthority(RoleName.NOMINATIVI_WRITE_SUPER)
                || (httpSessionUtil.hasAuthority(RoleName.NOMINATIVI_WRITE_ALL)
                && nominativo.getOperatoreNominativo().getAzienda().getId() == httpSessionUtil.getAziendaId())
                || (httpSessionUtil.hasAnyAuthority(RoleName.NOMINATIVI_WRITE_OWN)
                && nominativo.getOperatoreNominativo().getId() == httpSessionUtil.getOperatorId());
    }

    public boolean isDeleteNominativoButtonRendered() {
        log.debug("Nominativo id: "+nominativo.getId());
        return httpSessionUtil.hasAuthority(RoleName.NOMINATIVI_DELETE_SUPER)
                || (httpSessionUtil.hasAuthority(RoleName.NOMINATIVI_DELETE_ALL)
                && nominativo.getOperatoreNominativo().getAzienda().getId() == httpSessionUtil.getAziendaId())
                || (httpSessionUtil.hasAnyAuthority(RoleName.NOMINATIVI_DELETE_OWN)
                && nominativo.getOperatoreNominativo().getId() == httpSessionUtil.getOperatorId());
    }

    public void resetSendDialog() {
        mailClient.reset();
        smsText = "";
        contestaNominativoCommento = "";
        motivoContestazione = 0;
    }

    public void resetContaNominativoDialog() {

    }

    /**
     * If we have a leadId, it means the lead is kind of certified because we got it from a third party service, so the users must not be able to change his "source" and "sourceDescription" values
     */
    public boolean isDisabledProvenienza() {
        return !StringUtils.isEmpty(nominativo.getLeadId());
    }

    public boolean isDisabledFornitoreLead() {
        return !Provenienza.LEAD.getValue().equalsIgnoreCase(nominativo.getProvenienza());
    }

    public boolean isRequiredFornitoreLead() {
        return (Provenienza.LEAD.getValue().equalsIgnoreCase(nominativo.getProvenienza())
                && StringUtils.isEmpty(nominativo.getFornitoreLead()));
    }

    public void addPreventivoFromSimulatorResultRowChoosen() {
        final PraticaViewModel preventivoFromSimulator = (PraticaViewModel) simulatorUtilsView.getObjectFromSimulatorResultRowChoosen(true);
        if (preventivoFromSimulator != null) {
            preventivoList.add(preventivoFromSimulator);
        }
    }

    public boolean isDisabledButtonSimulator() {
        return preventivoList != null && preventivoList.size() == 3;
    }

    /**
     * This is used for FileManagerView to upload files
     * @param nominativoId
     */
    private void saveClienteNominativoIdIntoPassaggioParametri(final long nominativoId) {
        passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_ID, nominativoId);
    }

}
