package com.woonders.lacemsjsf.view.formcliente;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.PraticheInCorsoModel;
import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Pratica.TipoPratica;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.db.entityenum.*;
import com.woonders.lacemscommon.db.entityutil.*;
import com.woonders.lacemscommon.exception.*;
import com.woonders.lacemscommon.laceenum.StringCalc;
import com.woonders.lacemscommon.service.*;
import com.woonders.lacemscommon.service.impl.*;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl.PdfCategory;
import com.woonders.lacemscommon.sms.MessageType;
import com.woonders.lacemscommon.util.CfGenerator;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemscommon.util.LaceMailSender;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.util.*;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.file.ArchiviazioneRendered;
import com.woonders.lacemsjsf.view.log.LogDTO;
import com.woonders.lacemsjsf.view.pdf.FinanziariaEnum;
import com.woonders.lacemsjsf.view.prestiti.CessioniRendered;
import com.woonders.lacemsjsf.view.prestiti.PraticaRendered;
import com.woonders.lacemsjsf.view.prestiti.PrestitiRendered;
import com.woonders.lacemsjsf.view.simulator.SimulatorUtilsView;
import com.woonders.lacemsjsf.view.util.ConstantResolverView;
import data.DisabledRendered;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mail.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.dao.DataAccessException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.woonders.lacemsjsf.view.formcliente.RegisterCliente.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
@Slf4j
public class RegisterCliente implements Serializable {

    public static final String NAME = "registerCliente";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private final String PRATICA_RINNOVATA = "- Pratica Rinnovata";
    private final int RESIDENZA_PRECEDENTE = 4;
    private final TrattenuteViewModel trattenuteViewModel;
    private final DisabledRendered disabledRendered;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(ResidenzaUtil.JSF_NAME)
    private ResidenzaUtil residenzaUtil;
    private List<EstinzioneViewModel> estinzioneViewModelList = new ArrayList<>();
    private List<TrattenuteViewModel> trattenuteViewModelList = new ArrayList<>();
    private List<PreventivoViewModel> preventivoViewModelList = new ArrayList<>();
    private List<CoobbligatoViewModel> coobbligatoViewModelList = new ArrayList<>();
    private List<PraticheInCorsoModel> praticheInCorsoViewModelList = new ArrayList<>();
    private ClienteViewModel clienteViewModel;
    private PraticaViewModel praticaViewModel;
    @ManagedProperty(FinanziariaServiceImpl.JSF_NAME)
    private FinanziariaService finanziariaService;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(MailClient.JSF_NAME)
    private MailClient mailClient;
    @ManagedProperty(ClienteManagerServiceImpl.JSF_NAME)
    private ClienteManagerService clienteManagerService;
    @ManagedProperty(PreventivoServiceImpl.JSF_NAME)
    private PreventivoService preventivoService;
    private PreventivoViewModel preventivoViewModel;
    private CoobbligatoViewModel coobbligatoViewModel;
    @ManagedProperty(CoobbligatoServiceImpl.JSF_NAME)
    private CoobbligatoService coobbligatoService;
    private PraticheInCorsoModel praticheInCorsoViewModel;
    @ManagedProperty(PraticheInCorsoServiceImpl.JSF_NAME)
    private PraticheInCorsoService praticheInCorsoService;
    @ManagedProperty(AmministrazioneServiceImpl.JSF_NAME)
    private AmministrazioneService amministrazioneService;
    @ManagedProperty("#{passaggioParametriUtils}")
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    private DocumentoViewModel documentoViewModel;
    private ContoViewModel contoViewModel;
    private ResidenzaViewModel residenzaViewModel;
    private ByteArrayOutputStream file;
    private List<AmministrazioneViewModel> amministrazioneViewModelList;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    private AziendaViewModel aziendaViewModel;
    @ManagedProperty("#{praticaRendered}")
    private PraticaRendered praticaRendered;
    private AmministrazioneViewModel amministrazioneSelezionata;
    @ManagedProperty(ConstantResolverView.JSF_NAME)
    private ConstantResolverView constantResolverView;
    @ManagedProperty(CalculationUtils.JSF_NAME)
    private CalculationUtils calculationUtils;
    private ArchiviazioneRendered archiviazioneRendered;
    private List<TrattenuteViewModel> coesistenzeEstinguibiliViewModelList;
    private List<TrattenuteViewModel> coesistenzeSelezionateDaEstinguereViewModelList;
    private List<PraticaViewModel> praticheEstinguibiliViewModelList;
    private List<PraticaViewModel> praticheSelezionateDaEstinguereViewModelList;
    private PreventivoViewModel preventivoDaCreareViewModel;
    private List<FinanziariaViewModel> finanziariaViewModelList;
    @ManagedProperty(PreventivoUtil.JSF_NAME)
    private PreventivoUtil preventivoUtil;
    @ManagedProperty(PreferenzaStatoPraticaServiceImpl.JSF_NAME)
    private PreferenzaStatoPraticaService preferenzaStatoPraticaService;
    @ManagedProperty(LaceMailSender.JSF_NAME)
    private LaceMailSender laceMailSender;
    @ManagedProperty(ClienteUtil.JSF_NAME)
    private ClienteUtil clienteUtil;
    @ManagedProperty(EstinzioneUtil.JSF_NAME)
    private EstinzioneUtil estinzioneUtil;
    @ManagedProperty(PraticaUtil.JSF_NAME)
    private PraticaUtil praticaUtil;
    @ManagedProperty(CfGenerator.JSF_NAME)
    private CfGenerator cfGenerator;
    private String commentoLog;
    @ManagedProperty(LogServiceImpl.JSF_NAME)
    private LogService logService;
    private String smsText;
    @ManagedProperty(SmsServiceFacadeImpl.JSF_NAME)
    private SmsServiceFacade smsServiceFacade;
    @ManagedProperty(RequestTenantIdentifierResolver.JSF_NAME)
    private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
    @ManagedProperty(ExceptionResolverUtil.JSF_NAME)
    private ExceptionResolverUtil exceptionResolverUtil;
    @ManagedProperty(SimulatorUtilsView.JSF_NAME)
    private SimulatorUtilsView simulatorUtilsView;
    @ManagedProperty(SettingServiceImpl.JSF_NAME)
    private SettingService settingService;
    @ManagedProperty(ErrorConverter.JSF_NAME)
    private ErrorConverter errorConverter;
    @ManagedProperty(PrivacyTemplateServiceImpl.JSF_NAME)
    private PrivacyTemplateService privacyTemplateService;
    @ManagedProperty(ClientePrivacyTemplateServiceImpl.JSF_NAME)
    private ClientePrivacyTemplateService clientePrivacyTemplateService;
    private ClientePrivacyTemplateViewModel clientePrivacyTemplateViewModel;
    private List<PrivacyTemplateViewModel> privacyTemplateList;

    public RegisterCliente() {
        documentoViewModel = new DocumentoViewModel();
        contoViewModel = new ContoViewModel();
        residenzaViewModel = new ResidenzaViewModel();
        aziendaViewModel = new AziendaViewModel();
        trattenuteViewModel = new TrattenuteViewModel();
        disabledRendered = new DisabledRendered();
        amministrazioneViewModelList = new ArrayList<>();
    }

    public boolean isDisabledButtonContestaNominativo() {
        boolean isDevisprox = clienteViewModel.getFornitoreLead().equalsIgnoreCase("DevisProx");
        //isAlreadyDisputed return true only if statoNominativo is not StatoNominativoContestato
        boolean isAlreadyDisputed = (clienteViewModel.getStatoNominativo().equalsIgnoreCase(StatoNominativo.CONTESTATO.getValue()) ||
                clienteViewModel.getStatoNominativo().equalsIgnoreCase(StatoNominativo.CONTESTAZIONE_ACCETTATA.getValue()) ||
                clienteViewModel.getStatoNominativo().equalsIgnoreCase(StatoNominativo.CONTESTAZIONE_RIFIUTATA.getValue()) );
        log.info("isDevisprox="+isDevisprox+" getProvenienza returned "+clienteViewModel.getProvenienza());
        log.info("isAlreadyDisputed=" + isAlreadyDisputed + " getStatoNominativo returned "+ clienteViewModel.getStatoNominativo());
        return !(!isAlreadyDisputed && isDevisprox);
    }

    public boolean isDisabledButtonMail() {
        return StringUtils.isEmpty(clienteViewModel.getEmail());
    }

    public boolean isDisabledButtonSms() {
        return StringUtils.isEmpty(clienteViewModel.getTelefono());
    }

    @PostConstruct
    public void init() {
        clienteViewModel = new ClienteViewModel();
        praticaViewModel = new PraticaViewModel();
        finanziariaViewModelList = finanziariaService.findAll();
        privacyTemplateList = privacyTemplateService.findAll();
        for (FinanziariaViewModel finanziariaViewModel : finanziariaViewModelList)
        {
            log.info("---------------------------------------"+finanziariaViewModel.getId() + "   " +finanziariaViewModel.getName());
            if (finanziariaViewModel.getName().equalsIgnoreCase("italcredi"))
            {
                List<FinanziariaViewModel> clone = new ArrayList<FinanziariaViewModel>(finanziariaViewModelList);
                clone.remove(finanziariaViewModelList.indexOf(finanziariaViewModel));
                clone.add(0, finanziariaViewModel);
                finanziariaViewModelList = new ArrayList<FinanziariaViewModel>(clone);
                break;
            }
        }
        if (passaggioParametriUtils.getParametri().get(Parametro.CLIENTE_NUOVA_PRATICA_PARAMETER) != null) {
            setClienteNuovaPratica();
        } else {
            setClienteSelectedOnSearch();
        }
        passaggioParametriUtils.getParametri().put(Parametro.ID_NOMINATIVO_ON_FORM_NOMINATIVO_PARAMETER,
                LogDTO.builder().praticaId(praticaViewModel.getCodicePratica()).nominativoId(clienteViewModel.getId())
                        .tipoLog(LogService.TipoLog.PRATICA).build());
    }

    // link per il controllo documentoViewModel
    public void checkDoc() {
        FacesUtil.openNewWindow(propertiesUtil.getLinkDocumentiValue());
    }

    // link per il controllo iban
    public void checkIban() {
        FacesUtil.openNewWindow(propertiesUtil.getLinkIbanValue() + contoViewModel.getIban());
    }

    public String praticaRinnovataOnBigTopic() {
        if (praticaViewModel.isRinnovata()) {
            return PRATICA_RINNOVATA;
        } else {
            return "";
        }
    }

    public void sendEmail() throws IOException {
        try {
            laceMailSender.sendEmail(clienteViewModel.getEmail(),
                    praticaViewModel.getOperatore().getAzienda().getNomeAzienda(),
                    praticaViewModel.getOperatore().getAzienda().getEmail(), mailClient.getSubject(),
                    mailClient.getBody());
            logService.logCustomAction(praticaViewModel.getCodicePratica(), LogService.TipoLog.PRATICA,
                    mailClient.getBody(), LogService.CustomAction.EMAIL, httpSessionUtil.getOperatorId());
            FacesUtil.addMessage(propertiesUtil.getMsgSendMailOk());
        } catch (final UnableToSendEmailException e) {
            FacesUtil.addMessage(errorConverter.convertSendEmailError(e.getUnableToSendEmailError()), FacesMessage.SEVERITY_ERROR);
        }
        finally {
        	if (clientePrivacyTemplateViewModel != null)
        		clientePrivacyTemplateService.save(clientePrivacyTemplateViewModel);
        }
    }
    
    
    
    
    
    
    
    //*** Add By Cristian 16-11-2021
    // inserisco privacy nel body della mail
    public void addPrivacyToBody() {
        //final AziendaViewModel aziendaViewModel = aziendaService.getOne(httpSessionUtil.getAziendaId());
        try
        {
        	 final String oggetto = mailClient.getOggettoMailPrivacy(clienteViewModel);
        	 final Map<Long, StringBuilder> privacyTemplateMap = mailClient.textPrivacyTemplate(clienteViewModel, privacyTemplateList);
            
        	 Map.Entry<Long, StringBuilder> entry = privacyTemplateMap.entrySet().iterator().next();
             Long privacy_id  = entry.getKey();
             String privacy = entry.getValue().toString();
             
             mailClient.setBody(privacy);
             
             if (privacy_id > 0)
             {
            	//*** Registro l'invio del privacy template al determinato cliente con la data 
                 clientePrivacyTemplateViewModel = new ClientePrivacyTemplateViewModel();
                 clientePrivacyTemplateViewModel.setClienteId(clienteViewModel.getId());
                 clientePrivacyTemplateViewModel.setPrivacyTemplatesId(privacy_id);
             }
             else
             {
            	 FacesUtil.addMessage("Non è stato trovato nessun Testo della Privacy Associato!", FacesMessage.SEVERITY_ERROR);
             }
             
        }
        catch (final Exception ex)
        {
        	FacesUtil.addMessage(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
       
    }
    
    
    
    
    
    

    public void addRichiestaBustaToBody() {
        aziendaViewModel = aziendaService.getOne(httpSessionUtil.getAziendaId());
        final String oggetto = mailClient.getOggettoMailBustapaga(clienteViewModel);
        final String richiestaBustapaga = mailClient.textRichiestaBusta(clienteViewModel,
                praticaViewModel.getTipoPratica(), aziendaViewModel);
        mailClient.setSubject(oggetto);
        mailClient.setBody(richiestaBustapaga);
    }

    public void addRichiestaBustaToBodySMS() {
        final String richiestaBustapaga = mailClient.textRichiestaBustaSMS(clienteViewModel,
                praticaViewModel.getTipoPratica(), aziendaViewModel);
        smsText = richiestaBustapaga;
    }

    public void sendSms() {
        try {
            if (smsServiceFacade.checkEnoughBalance(clienteViewModel.getTelefono(), smsText, httpSessionUtil.getAziendaId())) {
                try {
                    smsServiceFacade.sendSms(clienteViewModel.getTelefono(), smsText, MessageType.SMS_SINGLE,
                            httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId(),
                            requestTenantIdentifierResolver.getTenantIdentifier(), praticaViewModel.getCodicePratica(), LogService.TipoLog.PRATICA);
                    logService.logCustomAction(praticaViewModel.getCodicePratica(), LogService.TipoLog.PRATICA, smsText,
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

    public void saveNewCliente() {
    	log.info("ABLE --------------------------saveNewCliente called");
    	log.info("ABLE --------------------------preventivoViewModelList size: " + preventivoViewModelList.size());
        final ClientePratica clientePratica = clienteManagerService.saveNewClienteWithPratica(clienteViewModel,
                contoViewModel, documentoViewModel, residenzaViewModel, trattenuteViewModelList,
                estinzioneViewModelList, praticaViewModel, coobbligatoViewModelList, amministrazioneViewModelList,
                preventivoViewModelList, commentoLog, httpSessionUtil.getOperatorId());

        if (clienteViewModel.getId() == 0 && praticaViewModel.getCodicePratica() == 0) {
            FacesUtil.addMessage(
                    "Il Cliente " + this.clienteViewModel.getCognome() + " e' stato salvato con successo");
        } else if (clienteViewModel.getId() != 0 && praticaViewModel.getCodicePratica() == 0) {
            FacesUtil.addMessage("Nuova Pratica aggiunta con successo");
        } else {
            FacesUtil.addMessage("Salvataggio eseguito con successo");
        }

        updateAllViewModels(clientePratica);
        saveClientePraticaIdIntoPassaggioParametri(clientePratica);
        passaggioParametriUtils.getParametri().put(Parametro.ID_NOMINATIVO_ON_FORM_NOMINATIVO_PARAMETER,
                LogDTO.builder().praticaId(praticaViewModel.getCodicePratica())
                        .nominativoId(clienteViewModel.getId()).tipoLog(LogService.TipoLog.PRATICA).build());
        setCommentoLog(null);

        archiviazioneRendered = ArchiviazioneRendered.builder().documentiClienteRendered(true)
                .documentiPraticaRendered(true).build();

        log.info("ABLE --------------------------saveNewCliente exiting");

    }

    private void updateAllViewModels(final ClientePratica clientePratica) {
        setClienteViewModel(clientePratica.getClienteViewModel());
        setContoViewModel(clientePratica.getClienteViewModel().getContoViewModel());
        setDocumentoViewModel(clientePratica.getClienteViewModel().getDocumentoViewModel());
        setResidenzaViewModel(clientePratica.getClienteViewModel().getResidenzaViewModel());
        setTrattenuteViewModelList(clientePratica.getClienteViewModel().getTrattenutaViewModelList());
        setEstinzioneViewModelList(clientePratica.getPraticaViewModel().getEstinzioneViewModelList());
        setPraticaViewModel(clientePratica.getPraticaViewModel());
        setCoobbligatoViewModelList(clientePratica.getPraticaViewModel().getCoobbligatoViewModelList());
        setAmministrazioneViewModelList(clientePratica.getClienteViewModel().getAmministrazioneViewModelList());
        setPreventivoViewModelList(clientePratica.getClienteViewModel().getPreventivoViewModelList());
    }

    public void renderedPrestito(final String tipoPratica) {
        if (tipoPratica.equalsIgnoreCase(Pratica.TipoPratica.PRESTITO.getValue())) {
            setPraticaRendered(new PrestitiRendered());
            praticaViewModel.setTipoPratica(Pratica.TipoPratica.PRESTITO.getValue());
        } else {
            setPraticaRendered(new CessioniRendered());
        }
    }

    public void addCoobbligato() {
        final CoobbligatoViewModel coobbligato = new CoobbligatoViewModel();
        coobbligatoViewModelList.add(coobbligato);
    }

    public void removeCoobbligato(final CoobbligatoViewModel coobbligato) {
        try {
            if (coobbligato.getId() != 0) {
                coobbligatoService.delete(coobbligato);
            }
            coobbligatoViewModelList.remove(coobbligato);

        } catch (final DataAccessException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void viewCoobbligato(final long codicePratica) {
        coobbligatoViewModelList.clear();
        coobbligatoViewModelList.addAll(coobbligatoService.findDistinctByPratica_CodicePratica(codicePratica));
    }

    public boolean isAddCoobbligatoDisabled() {
        boolean addCoobbligato = false;
        if (!(coobbligatoViewModelList.size() < 1)) {
            addCoobbligato = true;
        }
        return addCoobbligato;
    }

    public void viewPraticheInCorso(final long idCliente, final long idPratica) {
        praticheInCorsoViewModelList.clear();
        praticheInCorsoViewModelList.addAll(praticheInCorsoService
                .getAltrePraticheInCorsoByClienteSenzaPraticaAttualmenteVisibile(idCliente, idPratica));
    }

    public void setClienteNuovaPratica() {
        final ClientePratica clientePratica = (ClientePratica) passaggioParametriUtils.getParametri()
                .get(Parametro.CLIENTE_NUOVA_PRATICA_PARAMETER);

        if (clientePratica != null) {
            final boolean censito = (boolean) passaggioParametriUtils.getParametri()
                    .get(PassaggioParametriUtils.Parametro.CENSITO_NUOVA_PRATICA_PARAMETER);

            if (censito) {
                setClienteViewModel(
                        clienteManagerService.findClienteByIdCliente(clientePratica.getClienteViewModel().getId()));
                setContoViewModel(
                        clienteManagerService.findContoByIdCliente(clientePratica.getClienteViewModel().getId()));
                setDocumentoViewModel(
                        clienteManagerService.findDocumentoByIdCliente(clientePratica.getClienteViewModel().getId()));
                setResidenzaViewModel(
                        clienteManagerService.findResidenzaByIdCliente(clientePratica.getClienteViewModel().getId()));
                trattenuteViewModelList.clear();
                trattenuteViewModelList.addAll(
                        clienteManagerService.findTrattenuteByIdCliente(clientePratica.getClienteViewModel().getId()));
                preventivoViewModelList.clear();
                preventivoViewModelList.addAll(
                        clienteManagerService.findPreventiviByIdCliente(clientePratica.getClienteViewModel().getId()));
                viewPraticheInCorso(clientePratica.getClienteViewModel().getId(),
                        clientePratica.getPraticaViewModel().getCodicePratica());
                viewAmministrazioniAssegnate(clientePratica.getClienteViewModel());
            } else {
                setClienteViewModel(clientePratica.getClienteViewModel());
                clienteViewModel.setCittadinanza("Italiana");
                calcReverseCf(clienteViewModel);
                clienteViewModel.setEta(clienteUtil.calcEta(clienteViewModel.getDataNascita()));
                contoViewModel = new ContoViewModel();
                documentoViewModel = new DocumentoViewModel();
                residenzaViewModel = new ResidenzaViewModel();
                trattenuteViewModelList.clear();
                preventivoViewModelList.clear();
            }

            estinzioneViewModelList.clear();
            coobbligatoViewModelList.clear();

            praticaViewModel.setOperatore(operatorService.getOne(httpSessionUtil.getOperatorId()));
            praticaViewModel.setStatoPratica(StatoPratica.PREISTRUTTORIA.getValue());
            praticaViewModel.setDataNotificaStatoPratica(clienteManagerService
                    .getDataNotificaByStatoPraticaAndAziendaId(StatoPratica.PREISTRUTTORIA.getValue(),
                            praticaViewModel.getOperatore().getAzienda().getId()));
            clientePratica.setPraticaViewModel(praticaViewModel);

            renderedPrestito((String) passaggioParametriUtils.getParametri()
                    .get(Parametro.TIPO_PRATICA_NUOVA_PRATICA_PARAMETER));

            disabledField();
            saveClientePraticaIdIntoPassaggioParametri(clientePratica);

            archiviazioneRendered = (ArchiviazioneRendered) passaggioParametriUtils.getParametri()
                    .get(Parametro.ARCHIVIAZIONE_RENDERED);

            passaggioParametriUtils.getParametri().remove(Parametro.CLIENTE_NUOVA_PRATICA_PARAMETER);
            passaggioParametriUtils.getParametri().remove(Parametro.TIPO_PRATICA_NUOVA_PRATICA_PARAMETER);
            passaggioParametriUtils.getParametri().remove(Parametro.CENSITO_NUOVA_PRATICA_PARAMETER);
        }
    }

    public void calcReverseCf(final ClienteViewModel clienteViewModel) {
        if (clienteViewModel.getCf() != null && !clienteViewModel.getCf().isEmpty()
                && clienteViewModel.getCf().length() == 16) {
            clienteViewModel.setDataNascita(cfGenerator.elaboraDataNascitaDaCf(clienteViewModel.getCf()));
            clienteViewModel.setLuogoNascita(cfGenerator.elaboraComuneDaCodiceComune(clienteViewModel.getCf()));
            clienteViewModel.setSesso(cfGenerator.elaboraSessoDaCf(clienteViewModel.getCf()));
        }
    }

    private void saveClientePraticaIdIntoPassaggioParametri(final ClientePratica clientePratica) {
        passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_ID, clientePratica.getClienteViewModel().getId());
        passaggioParametriUtils.getParametri().put(Parametro.PRATICA_ID,
                clientePratica.getPraticaViewModel().getCodicePratica());
    }

    public void printPdf(final String category) {
        passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_PDF_PARAMETER, clienteViewModel);
        passaggioParametriUtils.getParametri().put(Parametro.RESIDENZA_PDF_PARAMETER, residenzaViewModel);
        passaggioParametriUtils.getParametri().put(Parametro.CONTO_PDF_PARAMETER, contoViewModel);
        passaggioParametriUtils.getParametri().put(Parametro.DOCUMENTO_PDF_PARAMETER, documentoViewModel);
        passaggioParametriUtils.getParametri().put(Parametro.PRATICA_PDF_PARAMETER, praticaViewModel);
        passaggioParametriUtils.getParametri().put(Parametro.ESTINZIONE_LIST_PDF_PARAMETER, estinzioneViewModelList);
        passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_PDF_PARAMETER, amministrazioneViewModelList);
        passaggioParametriUtils.getParametri().put(Parametro.PDF_CATEGORY_PARAMETER, PdfCategory.fromValue(category));
        final long aziendaId;
        // aziendaId deve essere l'id dell azienda dell operatore a cui la
        // pratica e assegnata
        // se non disponibile, cioe prima del salvataggio, uso come fallback l
        // id dell azienda dell operatore corrente
        if (praticaViewModel.getOperatore() != null) {
            aziendaId = praticaViewModel.getOperatore().getAzienda().getId();
        } else {
            aziendaId = httpSessionUtil.getAziendaId();
        }
        passaggioParametriUtils.getParametri().put(Parametro.PDF_AZIENDA_OPERATORE_ASSEGNATO_PARAMETER, aziendaId);
        FacesUtil.openNewWindow(Constants.getPdfModPath(false));
    }

    public void disabledField() {

        final Role.RoleName clienteWriteRole = httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.CLIENTI, OperatorViewModel.PermissionType.WRITE);
        boolean disableCinquantaAndOtherFields = true;
        switch (clienteWriteRole) {
            case CLIENTI_WRITE_NOT:
            case CLIENTI_WRITE_OWN:
                disableCinquantaAndOtherFields = true;
                break;
            case CLIENTI_WRITE_ALL:
                if (praticaViewModel == null || praticaViewModel.getCodicePratica() == 0 || praticaViewModel.getOperatore().getAzienda().getId() == httpSessionUtil.getAziendaId()) {
                    disableCinquantaAndOtherFields = false;
                }
                break;
            case CLIENTI_WRITE_SUPER:
                disableCinquantaAndOtherFields = false;
                break;
        }

        if (disableCinquantaAndOtherFields) {
            disabledRendered.setDisabledCinquanta(true);
            disabledRendered.setDisabledOpt(true);
            disabledRendered.setDisabledPerf(true);
            disabledRendered.setDisabledProv(true);
            disabledRendered.setDisabledWeb(true);
        } else {
            disabledRendered.setDisabledCinquanta(false);
            disabledRendered.setDisabledOpt(false);
            disabledRendered.setDisabledPerf(false);
            disabledRendered.setDisabledProv(false);
            disabledRendered.setDisabledWeb(false);

        }
    }

    public void provConfirm() {
        try {
            laceMailSender.sendEmail(operatorService.mail(praticaViewModel.getOperatore().getUsername()),
                    praticaViewModel.getOperatore().getAzienda().getNomeAzienda(),
                    praticaViewModel.getOperatore().getAzienda().getEmail(),
                    "Conferma Provvigione di " + clienteViewModel.getCognome() + " " + clienteViewModel.getNome(),
                    "Pratica Deliberata Vi confermiamo i dati dell'operazione:" + "\n" + "Rata: "
                            + praticaViewModel.getRata() + "\n" + "Durata: " + praticaViewModel.getDurata() + "\n"
                            + "Importo Erogato: " + praticaViewModel.getImportoErogato() + "\n" + "Provvigione in %: "
                            + praticaViewModel.getPercProv() + "%" + "\n" + "Provvigione in Euro: "
                            + praticaViewModel.getProvvigione() + "\n" + "Provvigione Tot.: "
                            + praticaViewModel.getProvTotale() + "\n"
                            + "Vi Preghiamo di segnalarci eventuali errori prima della firma dei contratti." + "\n"
                            + "Grazie e Buon Lavoro");
            praticaViewModel.setConfermaProv(true);
            saveNewCliente();
            FacesUtil.addMessage(propertiesUtil.getMsgSendMailOk());
        } catch (final UnableToSendEmailException e) {
            FacesUtil.addMessage(errorConverter.convertSendEmailError(e.getUnableToSendEmailError()), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addTrattenuta() {
        final TrattenuteViewModel trattenuteViewModel = new TrattenuteViewModel();
        trattenuteViewModelList.add(trattenuteViewModel);
    }

    public void removeTrattenuta(final TrattenuteViewModel trattenuteViewModel) {
        try {
            if (trattenuteViewModel.getCodStip() != 0) {
                clienteManagerService.deleteTrattenuta(trattenuteViewModel);
            }
            trattenuteViewModelList.remove(trattenuteViewModel);
            //ABLE - UPDATE Cliente
            clienteViewModel.setTrattenutaViewModelList(trattenuteViewModelList);
        } catch (final DataAccessException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addEstinzione() {
        final EstinzioneViewModel estinzioneViewModel = new EstinzioneViewModel();
        estinzioneViewModelList.add(estinzioneViewModel);
    }

    public void addSecondaOccupazione() {
        clienteViewModel.setSecondaOccupazioneRendered(true);
    }

    public void removeSecondaOccupazione() {
        try {
            if (clienteViewModel.getId() != 0) {
                clienteManagerService.deleteSecondaOccupazione(clienteViewModel.getId());
            }
            clienteViewModel.setSecondaOccupazioneRendered(false);
        } catch (final DataAccessException e) {
            FacesUtil.addMessage("Errore! Occupazione non eliminata", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void viewAmministrazioniAssegnate(final ClienteViewModel clienteViewModel) {
        amministrazioneViewModelList = new ArrayList<>();
        amministrazioneViewModelList.addAll(amministrazioneService.findDistinctByCliente_Id(clienteViewModel.getId()));
    }

    // metodo per visualizzare l'amministrazione assegnata se nell'autocomplete
    // c'è solo un'amministrazione altrimenti apre la dialog con un datatable
    public void viewAmministrazioneSelezionata() throws ItemNotFoundException {
        if (amministrazioneViewModelList != null && (!amministrazioneViewModelList.isEmpty()))
            if (amministrazioneViewModelList.size() < constantResolverView.getAmministrazioneListMaxSize()) {
                getAmministrazioneAssegnata(amministrazioneViewModelList.get(0).getRagioneSociale());
                RequestContext.getCurrentInstance().execute("PF('dlgnuovaamministrazione').show();");
            } else {
                RequestContext.getCurrentInstance().execute("PF('dlgselezionaamministrazione').show();");
            }
    }

    public void getAmministrazioneAssegnata(final String ragioneSociale) throws ItemNotFoundException {
        passaggioParametriUtils.getParametri().put(Parametro.AMMINISTRAZIONE_VIEWMODEL_PARAMETER,
                amministrazioneService.findByRagioneSociale(ragioneSociale));
    }

    public void clearPassaggioParametriAmministrazione() {
        passaggioParametriUtils.getParametri().remove(Parametro.AMMINISTRAZIONE_VIEWMODEL_PARAMETER);
        passaggioParametriUtils.getParametri().remove(Parametro.RAGIONE_SOCIALE_PARAMETER);
    }

    public void setPropertiesUtil(final PropertiesUtil propertiesUtil) {
        this.propertiesUtil = propertiesUtil;
    }

    public boolean isDisabledButtonPdf() {
        return !(clienteViewModel.getCf() != null && clienteViewModel.getCognome() != null
                && clienteViewModel.getNome() != null && !isPrintPdfDisabled());
    }

    public boolean isDisabledButtonAllegati() {
        if (!isPrintPdfDisabled()) {
            if (!clienteViewModel.isSecondaOccupazionePredefinita()) {
                return clienteViewModel.getImpiego() == null || clienteViewModel.getImpiego().equalsIgnoreCase("")
                        || Impiego.CARABINIERE.getValue().equalsIgnoreCase(clienteViewModel.getImpiego())
                        //solo per italcredi disabilitare il tasto allegati se pensionato
                        || (praticaViewModel.getFinanziariaViewModel() != null
                        && FinanziariaEnum.ITALCREDI.getFieldName().equalsIgnoreCase(praticaViewModel.getFinanziariaViewModel().getName())
                        && Impiego.PENSIONATO.getValue().equalsIgnoreCase(clienteViewModel.getImpiego()))
                        || Impiego.AUTONOMO.getValue().equalsIgnoreCase(clienteViewModel.getImpiego());
            } else {
                return clienteViewModel.getImpiego2() == null || clienteViewModel.getImpiego2().equalsIgnoreCase("")
                        || Impiego.CARABINIERE.getValue().equalsIgnoreCase(clienteViewModel.getImpiego2())
                        //solo per italcredi disabilitare il tasto allegati se pensionato
                        || (praticaViewModel.getFinanziariaViewModel() != null
                        && FinanziariaEnum.ITALCREDI.getFieldName().equalsIgnoreCase(praticaViewModel.getFinanziariaViewModel().getName())
                        && Impiego.PENSIONATO.getValue().equalsIgnoreCase(clienteViewModel.getImpiego()))
                        || Impiego.AUTONOMO.getValue().equalsIgnoreCase(clienteViewModel.getImpiego2());
            }
        }
        return true;
    }

    // TODO superclasse con metodo in comune
    public boolean isPrintPdfDisabled() {
        return !httpSessionUtil.isPrintPdfEnabled();
    }

    private void setClienteSelectedOnSearch() {
        final ClientePratica clienteSelected = (ClientePratica) passaggioParametriUtils.getParametri()
                .get(Parametro.CLIENTE_SELECTED_ON_SEARCH);

        if (clienteSelected != null) {
            setClienteViewModel(
                    clienteManagerService.findClienteByIdCliente(clienteSelected.getClienteViewModel().getId()));
            setContoViewModel(
                    clienteManagerService.findContoByIdCliente(clienteSelected.getClienteViewModel().getId()));
            setDocumentoViewModel(
                    clienteManagerService.findDocumentoByIdCliente(clienteSelected.getClienteViewModel().getId()));
            setResidenzaViewModel(
                    clienteManagerService.findResidenzaByIdCliente(clienteSelected.getClienteViewModel().getId()));
            getTrattenuteViewModelList().clear();
            getTrattenuteViewModelList().addAll(
                    clienteManagerService.findTrattenuteByIdCliente(clienteSelected.getClienteViewModel().getId()));
            setPraticaViewModel(clienteManagerService
                    .findPraticaByCodicePratica(clienteSelected.getPraticaViewModel().getCodicePratica()));
            getEstinzioneViewModelList().clear();
            getEstinzioneViewModelList().addAll(clienteManagerService
                    .findEstinzioniByIdPratica(clienteSelected.getPraticaViewModel().getCodicePratica()));

            preventivoViewModelList.clear();
            preventivoViewModelList.addAll(
                    clienteManagerService.findPreventiviByIdCliente(clienteSelected.getClienteViewModel().getId()));
            renderedPrestito(clienteSelected.getPraticaViewModel().getTipoPratica());
            viewCoobbligato(clienteSelected.getPraticaViewModel().getCodicePratica());
            viewPraticheInCorso(clienteSelected.getClienteViewModel().getId(),
                    clienteSelected.getPraticaViewModel().getCodicePratica());
            viewAmministrazioniAssegnate(clienteSelected.getClienteViewModel());

            disabledField();
            saveClientePraticaIdIntoPassaggioParametri(clienteSelected);

            archiviazioneRendered = ArchiviazioneRendered.builder().documentiClienteRendered(true)
                    .documentiPraticaRendered(true).build();
        }
    }

    public void addPreventivo() {
        final PreventivoViewModel preventivo = new PreventivoViewModel();
        preventivoViewModelList.add(preventivo);
    }

    public void removePreventivo(final PreventivoViewModel preventivo) {
        try {
            if (preventivo.getIdPreventivo() != 0) {
                preventivoService.deletePreventivo(preventivo.getIdPreventivo());
            }
            preventivoViewModelList.remove(preventivo);
          //ABLE - UPDATE THE CLIENT
            clienteViewModel.setPreventivoViewModelList(preventivoViewModelList);
        } catch (final DataAccessException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorGeneric(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public boolean isDisabledAddPreventivo() {
        if (preventivoViewModelList.size() < constantResolverView.getPreventivoListMaxSize()) {
            return false;
        }
        return true;
    }

    public boolean isDisabledAddPreventivoDaSimulatore() {
        if (preventivoViewModelList.size() < constantResolverView.getPreventivoListMaxSize()
                && settingService.isSimulatorEnabledByAziendaId(httpSessionUtil.getAziendaId())
                && httpSessionUtil.hasAnyAuthority(Role.RoleName.getSimulatoriReadRoles())) {
            return false;
        }
        return true;
    }

    // Utilizzato per visualizzare tutte le pratiche che possono essere estinte
    // quando si crea un preventivo cliente
    public List<PraticaViewModel> retrieveAllPraticheCliente() {
        return preventivoService.getAllPraticheAttiveEstinguibili(clienteViewModel.getId());
    }

    // Utilizzato per visualizzare tutte le coesistenze/trattenute che possono
    // essere estinte quando si crea un preventivo cliente
    public List<TrattenuteViewModel> retrieveAllCoesistenzeCliente() {
        return preventivoService.getAllTrattenuteEstinguibili(clienteViewModel.getId());
    }

    private Integer checkLimitEstinzioniSelezionate() {
        Integer numeroImpegniSelezionati = 0;
        if (coesistenzeSelezionateDaEstinguereViewModelList != null
                && praticheSelezionateDaEstinguereViewModelList != null) {
            numeroImpegniSelezionati = coesistenzeSelezionateDaEstinguereViewModelList.size()
                    + praticheSelezionateDaEstinguereViewModelList.size();
        }
        return numeroImpegniSelezionati;
    }

    public void setPreventivoDaCreare(final PreventivoViewModel preventivo) {
        preventivoDaCreareViewModel = preventivo;
        RequestContext.getCurrentInstance().execute("PF('dlgcreapratica').show();");
    }

    public void creaPraticaDaPreventivo() throws IOException {
        if (checkLimitEstinzioniSelezionate() > constantResolverView.getImpegniEstinguibiliMaxSize()) {
            FacesUtil.showMessageInDialog(FacesMessage.SEVERITY_WARN, propertiesUtil.getMsgAttenzione(),
                    propertiesUtil.getMsgCreaPraticaLimitEstinzioni());
            return;
        } else {
            try {
                preventivoService.creaPratica(preventivoDaCreareViewModel, praticheSelezionateDaEstinguereViewModelList,
                        coesistenzeSelezionateDaEstinguereViewModelList, clienteViewModel.getId(), httpSessionUtil.getAziendaId(),
                        httpSessionUtil.getOperatorId());
                FacesUtil.addMessage(propertiesUtil.getMsgCreaPraticaSuccess());
                FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.getDashboardPath(false));
            } catch (final RinnovoNotSavedException e) {
                FacesUtil.addMessage(propertiesUtil.getMsgCreaPraticaError(), FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    // inserisco i preventivi del clienteViewModel nel body della mail
    public void addPreventiviToBody() {
        aziendaViewModel = aziendaService.getOne(httpSessionUtil.getAziendaId());
        final String oggetto = mailClient.getOggettoMailPreventivo(clienteViewModel);
        final String preventivi = mailClient.textPreventivi(clienteViewModel, preventivoViewModelList,
                aziendaViewModel);
        mailClient.setSubject(oggetto);
        mailClient.setBody(preventivi);
    }

    // inserisco i preventivi del clienteViewModel nel body dell'SMS
    public void addPreventiviToBodySMS() {
        final String preventivi = mailClient.textPreventiviSMS(clienteViewModel, preventivoViewModelList);
        smsText = preventivi;
    }

    public List<AmministrazioneViewModel> completeAmministrazione(final String query) {
        return amministrazioneService.completeAmministrazione(query);
    }

    public void setDataNotificaStatoPratica() {
        final Date dataNototificaStatoPratica = clienteManagerService.getDataNotificaByStatoPraticaAndAziendaId(
                praticaViewModel.getStatoPratica(), praticaViewModel.getOperatore().getAzienda().getId());
        praticaViewModel.setDataNotificaStatoPratica(dataNototificaStatoPratica);
    }

    // TODO Questo metodo è duplicato anche dentro StatoPraticheServiceImpl
    // trovare un modo per unificarli
    public void setNewDateByStatoPratica() {
        if (praticaViewModel != null && praticaViewModel.getStatoPratica() != null
                && !praticaViewModel.getStatoPratica().isEmpty()) {

            final Date dateNow = DateToCalendar.getDateWithoutTime(new Date());
            final StatoPratica statoPratica = StatoPratica.fromValue(praticaViewModel.getStatoPratica());
            switch (statoPratica) {
                case ISTRUTTORIA:
                    praticaViewModel.setDataIstruttoria(dateNow);
                    break;
                case ISTRUITA:
                    praticaViewModel.setDataIstruita(dateNow);
                    break;
                case DELIBERA:
                    praticaViewModel.setDataDelibera(dateNow);
                    break;
                case POLIZZA:
                    praticaViewModel.setDataFirmaContratti(dateNow);
                    break;
                case IN_VALUTAZIONE:
                    praticaViewModel.setDataDelibera(dateNow);
                    break;
                case LIQUIDATA:
                    if (TipoPratica.PRESTITO.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                        praticaViewModel.setDataFirmaContratti(dateNow);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isProvvigioneEuroDisabled() {
        return praticaViewModel.isConfermaProv()
                || !StringCalc.EURO.getValue().equalsIgnoreCase(praticaViewModel.getTipoProvvigione());
    }

    public boolean isProvvigionePercentualeDisabled() {
        return praticaViewModel.isConfermaProv()
                || !StringCalc.PERC.getValue().equalsIgnoreCase(praticaViewModel.getTipoProvvigione());
    }

    public boolean isDataPerfezionamentoDisabled() {
        return !praticaViewModel.isPerfezionata();
    }

    public boolean isMenoQuattroAnni() {
        return residenzaViewModel.getDataInizioResidenza() == null
                || residenzaUtil.anniResidenza(residenzaViewModel.getDataInizioResidenza()) >= RESIDENZA_PRECEDENTE;
    }

    public boolean isDomicilioAltroDisabled() {
        return residenzaViewModel.getCorrispondenza() == null || residenzaViewModel.getCorrispondenza().isEmpty()
                || Corrispondenza.RESIDENZA.getValue().equalsIgnoreCase(residenzaViewModel.getCorrispondenza());
    }

    public boolean isAppuntamentoDisabled() {
        return clienteViewModel.getId() == 0;
    }

    public void setParameterAppuntamento() {
        passaggioParametriUtils.getParametri().put(Parametro.CLIENTE_VIEWMODEL_APPUNTAMENTO_PARAMETER,
                clienteViewModel);
        passaggioParametriUtils.getParametri().put(Parametro.PRATICA_VIEWMODEL_APPUNTAMENTO_PARAMETER,
                praticaViewModel);
        RequestContext.getCurrentInstance().execute("PF('newAppuntamentoFromNominativoCliente').show();");
    }

    public boolean isRenderedLogPanel() {
        return praticaViewModel.getCodicePratica() != 0;
    }

    public boolean canWriteCliente() {
        return httpSessionUtil.hasAuthority(RoleName.CLIENTI_WRITE_SUPER)
                || (httpSessionUtil.hasAuthority(RoleName.CLIENTI_WRITE_ALL)
                && praticaViewModel.getOperatore().getAzienda().getId() == httpSessionUtil.getAziendaId())
                || (httpSessionUtil.hasAnyAuthority(RoleName.CLIENTI_WRITE_OWN)
                && praticaViewModel.getOperatore().getId() == httpSessionUtil.getOperatorId());
    }

    public void resetSendDialog() {
        mailClient.reset();
        smsText = "";
    }

    public void resetDataRecall() {
        praticaViewModel.setDataRecall(null);
    }

    public void addPreventivoFromSimulatorResultRowChoosen() {
        final PreventivoViewModel preventivoFromSimulator = (PreventivoViewModel) simulatorUtilsView.getObjectFromSimulatorResultRowChoosen(false);
        if (preventivoFromSimulator != null) {
            preventivoViewModelList.add(preventivoFromSimulator);
        }
    }

    public boolean isDisabledProvenienza() {
        return clienteViewModel.getDataCliente() != null;
    }

    public boolean isDisabledFornitoreLead() {
        return !Provenienza.LEAD.getValue().equalsIgnoreCase(clienteViewModel.getProvenienza());
    }

}