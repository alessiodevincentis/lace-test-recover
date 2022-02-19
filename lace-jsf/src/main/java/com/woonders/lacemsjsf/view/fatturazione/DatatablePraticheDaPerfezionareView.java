package com.woonders.lacemsjsf.view.fatturazione;

import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.exception.CannotUpdateDecorrenzaException;
import com.woonders.lacemscommon.exception.UnableToSendEmailException;
import com.woonders.lacemscommon.service.FatturazioneService;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.impl.FatturazioneServiceImpl;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.util.LaceMailSender;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.ErrorConverter;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.dao.DataAccessException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static com.woonders.lacemsjsf.view.fatturazione.DatatablePraticheDaPerfezionareView.NAME;

@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
@Slf4j
public class DatatablePraticheDaPerfezionareView implements Serializable {

    public static final String NAME = "datatablePraticheDaPerfezionareView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private LazyDataModel<ClientePratica> clientePraticaLazyDataModel;
    private List<ClientePratica> selectedPratiche;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(FatturazioneServiceImpl.JSF_NAME)
    private FatturazioneService fatturazioneService;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    @ManagedProperty(LaceMailSender.JSF_NAME)
    private LaceMailSender laceMailSender;
    private BigDecimal totaleMontante;
    private BigDecimal totaleProvvigione;
    private Date dataInizio;
    private Date dataFine;
    private String provenienza;
    private String provenienzaDesc;
    private boolean liquidazione;
    private boolean liquidata;
    private boolean quietanzata;
    private boolean perfezionamentoSelected;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(ErrorConverter.JSF_NAME)
    private ErrorConverter errorConverter;

    @PostConstruct
    public void init() {
        retrievePraticheDaPerfezionareFromParameter();
        calcTotaleMontanteEProvvigione();
        createLazyModel();
    }

    private void retrievePraticheDaPerfezionareFromParameter() {
        //*** mod by Cristian 23-11-21
    	provenienza = (String) passaggioParametriUtils.getParametri()
                .get(Parametro.PROVENIENZA);
    	provenienzaDesc = (String) passaggioParametriUtils.getParametri()
                .get(Parametro.PROVENIENZA_DESC);
    	//******
    	
    	dataInizio = (Date) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_DA_PERFEZIONARE_DATAINIZIO_PARAMETER);
        dataFine = (Date) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_DA_PERFEZIONARE_DATAFINE_PARAMETER);
        liquidazione = (boolean) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_DA_PERFEZIONARE_LIQUIDAZIONE_PARAMETER);
        liquidata = (boolean) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_DA_PERFEZIONARE_LIQUIDATA_PARAMETER);
        quietanzata = (boolean) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_DA_PERFEZIONARE_QUIETANZATA_PARAMETER);
        perfezionamentoSelected = (boolean) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICHE_DA_PERFEZIONARE_PERFEZIONAMENTO_PARAMETER);

    }

    private void calcTotaleMontanteEProvvigione() {
    	//*** mod by Cristian 23-11-21
    	totaleMontante = fatturazioneService.sumTotaleMontantePraticheDaPerfezionare(provenienza, provenienzaDesc, dataInizio, dataFine, liquidata, liquidazione,
                quietanzata, perfezionamentoSelected, aziendaSelectionView.getCurrentAziendaId(), 
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.WRITE));
        totaleProvvigione = fatturazioneService.sumTotaleProvvigionePraticheDaPerfezionare(provenienza, provenienzaDesc, dataInizio, dataFine, liquidata, liquidazione,
                quietanzata, perfezionamentoSelected, aziendaSelectionView.getCurrentAziendaId(), 
                httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.WRITE));
    	//********
    	
//        totaleMontante = fatturazioneService.sumTotaleMontantePraticheDaPerfezionare(dataInizio, dataFine, liquidata, liquidazione,
//                quietanzata, perfezionamentoSelected, aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.WRITE));
//        totaleProvvigione = fatturazioneService.sumTotaleProvvigionePraticheDaPerfezionare(dataInizio, dataFine, liquidata, liquidazione,
//                quietanzata, perfezionamentoSelected, aziendaSelectionView.getCurrentAziendaId(), httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.WRITE));
    }

    private void createLazyModel() {
        clientePraticaLazyDataModel = new LazyDataModel<ClientePratica>() {

            @Override
            public List<ClientePratica> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                             final Map<String, Object> filters) {

                final Map<QueryDSLHelper.TableField, Object> tableFilters = new HashMap<>();
                for (final Map.Entry<String, Object> entry : filters.entrySet()) {
                    tableFilters.put(QueryDSLHelper.TableField.fromValue(entry.getKey()), entry.getValue());
                }

                final ViewModelPage<ClientePratica> viewModelPage = fatturazioneService.searchPraticheDaPerfezionare(provenienza, provenienzaDesc,
                        dataInizio, dataFine, liquidata, liquidazione, quietanzata, perfezionamentoSelected, aziendaSelectionView.getCurrentAziendaId(), first, pageSize, sortField,
                        QueryDSLHelper.SortOrder.valueOf(sortOrder.name()), tableFilters, httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.WRITE));

                setRowCount((int) viewModelPage.getTotalElements());
                return viewModelPage.getContent();
            }

            @Override
            public ClientePratica getRowData(final String rowKey) {
                return fatturazioneService.getPratica(Long.parseLong(rowKey));
            }
        };
    }

    public String perfeziona() {
        try {
            fatturazioneService.perfezionaPratiche(selectedPratiche);
            FacesUtil.openDialog(FacesUtil.DialogType.INVIA_MAIL_FATTURAZIONE);
            return null;
        } catch (final DataAccessException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorPerfezionate(), FacesMessage.SEVERITY_ERROR);
            log.error(propertiesUtil.getMsgErrorPerfezionate(), e);
            return Constants.getFatturazionePath(true);
        }
    }

    public String perfezionaTutto() {
        fatturazioneService.perfezionaPratiche(provenienza, provenienzaDesc, dataInizio, dataFine, liquidata, liquidazione, 
        		quietanzata, perfezionamentoSelected, aziendaSelectionView.getCurrentAziendaId(), 
        		httpSessionUtil.getAuthority(OperatorViewModel.MenuSection.FATTURAZIONE, OperatorViewModel.PermissionType.WRITE));
        FacesUtil.openDialog(FacesUtil.DialogType.INVIA_MAIL_FATTURAZIONE);
        return null;
    }

    public String redirectFatturazionePage() {
        FacesUtil.addMessage(propertiesUtil.getMsgPratichePerfezionate());
        return Constants.getFatturazionePath(true);
    }

    //TODO prendere SOLO le email degli operatori le cui pratiche sono state perfezionate!!!
    private List<String> getOperatorEmailList() {
        final List<String> listMail = new ArrayList<>();
        for (final String mail : operatorService.mailList()) {
            if (mail != null && !mail.isEmpty())
                listMail.add(mail);
        }
        return listMail;
    }

    public void sendConfirm() {
        boolean errorHappened = false;
        for (final String email : getOperatorEmailList()) {
            try {
                laceMailSender.sendEmail(email, "Info LACE", operatorService.findByUsernameViewModel(httpSessionUtil.getUsername()).getEmail(), propertiesUtil.getMsgSubjectProforma(),
                        propertiesUtil.getMsgBodyProforma());
            } catch (final UnableToSendEmailException e) {
                errorHappened = true;
            }
        }
        if (errorHappened) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorSendMail(), FacesMessage.SEVERITY_ERROR);
        } else {
            FacesUtil.addMessage(propertiesUtil.getMsgSendMailOk());
        }
    }

    public void onEdit(final CellEditEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final PraticaViewModel praticaViewModelConDecorennza = context.getApplication().evaluateExpressionGet(context,
                "#{c.praticaViewModel}", PraticaViewModel.class);
        try {
            fatturazioneService.updateDecorrenzaPratica(praticaViewModelConDecorennza.getCodicePratica(),
                    (Date) event.getNewValue());
        } catch (final CannotUpdateDecorrenzaException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorEditDecorrenza() + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
        RequestContext.getCurrentInstance().execute("PF('datatabledecorrenzadlg').show()");
    }

    public boolean isPerfezionaButtonEnabled() {
        return clientePraticaLazyDataModel.getRowCount() != 0;
    }

}
