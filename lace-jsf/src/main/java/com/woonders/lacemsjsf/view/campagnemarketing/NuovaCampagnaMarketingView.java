package com.woonders.lacemsjsf.view.campagnemarketing;

import com.woonders.lacemscommon.app.campagneexcel.ApachePOIExcelRead;
import com.woonders.lacemscommon.app.model.CalcSmsInfo;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.config.TenantManager;
import com.woonders.lacemscommon.db.entity.Campagna;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.exception.*;
import com.woonders.lacemscommon.service.*;
import com.woonders.lacemscommon.service.impl.*;
import com.woonders.lacemscommon.util.RequestUtil;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.view.aziendaselection.AziendaSelectionView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@ManagedBean(name = NuovaCampagnaMarketingView.NAME)
@ViewScoped
@Getter
@Setter
public class NuovaCampagnaMarketingView implements Serializable {

    public static final String NAME = "nuovaCampagnaMarketingView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String DATATABLE_VALUE_FROM_FILE = "file";
    private static final String DATATABLE_VALUE_FROM_FILTERS = "filters";
    private static String STOP_MESSAGE = "";
    private FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel;
    @ManagedProperty(AmministrazioneServiceImpl.JSF_NAME)
    private AmministrazioneService amministrazioneService;
    @ManagedProperty(CampagnaMarketingServiceImpl.JSF_NAME)
    private CampagnaMarketingService campagnaMarketingService;
    @ManagedProperty(SettingServiceImpl.JSF_NAME)
    private SettingService settingService;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    private LazyDataModel<ClienteViewModel> clienteLazyDataModel;
    @ManagedProperty(SmsServiceImpl.JSF_NAME)
    private SmsService smsService;
    @ManagedProperty(TenantManager.JSF_NAME)
    private TenantManager tenantManager;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    private String messageText;
    private boolean filtraImportButtonEnabled;
    private String nomeCampagna;
    private String descrizioneCampagna;
    private boolean inviaCampagnaEnabled;
    @ManagedProperty(RequestUtil.JSF_NAME)
    private RequestUtil requestUtil;
    @ManagedProperty(CampagnaMarketingFacadeImpl.JSF_NAME)
    private CampagnaMarketingFacade campagnaMarketingFacade;
    private List<ClienteViewModel> clienteViewModelList;
    @ManagedProperty(ApachePOIExcelRead.JSF_NAME)
    private ApachePOIExcelRead apachePOIExcelRead;
    private boolean lazy;
    private boolean firstAvantiEnabled;
    private boolean tooltipAliasDisabled;
    private CalcSmsInfo calcSmsInfo;
    private String datatableValueFrom;
    private long currentOperatorId;
    private long currentOperatorAziendaId;
    private boolean isClientiReadSuper;
    private boolean isNominativiReadSuper;
    private boolean isClientiReadAll;
    private boolean isNominativiReadAll;
    private boolean isClientiReadOwn;
    private boolean isNominativiReadOwn;
    @ManagedProperty(AziendaSelectionView.JSF_NAME)
    private AziendaSelectionView aziendaSelectionView;

    @PostConstruct
    public void init() {
        filtroCampagnaMarketingViewModel = new FiltroCampagnaMarketingViewModel();
        filtraImportButtonEnabled = true;
        inviaCampagnaEnabled = true;
        currentOperatorId = httpSessionUtil.getOperatorId();
        currentOperatorAziendaId = httpSessionUtil.getAziendaId();
        isClientiReadSuper = httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_SUPER);
        isNominativiReadSuper = httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_SUPER);
        isClientiReadAll = httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_ALL);
        isNominativiReadAll = httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_ALL);
        isClientiReadOwn = httpSessionUtil.hasAuthority(Role.RoleName.CLIENTI_READ_OWN);
        isNominativiReadOwn = httpSessionUtil.hasAuthority(Role.RoleName.NOMINATIVI_READ_OWN);
        STOP_MESSAGE = "Per interrompere invia STOP al " + aziendaService.getOne(currentOperatorAziendaId).getCellulare();
        messageText = STOP_MESSAGE;
    }

    public List<AmministrazioneViewModel> completeAmministrazione(final String query) {
        return amministrazioneService.completeAmministrazione(query);
    }

    public boolean isDatiClienteRendered() {
        return Tipo.CLIENTE.equals(filtroCampagnaMarketingViewModel.getTipo());
    }

    public boolean isDatiNominativoRendered() {
        return Tipo.NOMINATIVO.equals(filtroCampagnaMarketingViewModel.getTipo());
    }

    public void filtraAction() {
        lazy = true;
        datatableValueFrom = DATATABLE_VALUE_FROM_FILTERS;
        clienteLazyDataModel = new LazyDataModel<ClienteViewModel>() {

            @Override
            public List<ClienteViewModel> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder,
                                               final Map<String, Object> filters) {
                final ViewModelPage<ClienteViewModel> viewModelPage = campagnaMarketingService.getNuovaCampagnaResultList(
                        first, pageSize, filtroCampagnaMarketingViewModel, isClientiReadSuper, isNominativiReadSuper, isClientiReadAll, isNominativiReadAll,
                        isClientiReadOwn, isNominativiReadOwn, httpSessionUtil.getOperatorId(), currentOperatorAziendaId, aziendaSelectionView.getCurrentAziendaId());
                setRowCount((int) viewModelPage.getTotalElements());
                if (viewModelPage != null) {
                    setFirstButtonWizard(viewModelPage.getTotalPages());
                }
                return viewModelPage.getContent();
            }

            @Override
            public ClienteViewModel getRowData(final String rowKey) {
                return campagnaMarketingService.getCliente(Long.parseLong(rowKey));
            }
        };
        RequestContext.getCurrentInstance().scrollTo("form:resultFilterTable");
    }

    public void secondAvantiAction() {
        if (!StringUtils.isEmpty(messageText) && messageText.contains(STOP_MESSAGE) &&
                !STOP_MESSAGE.trim().equalsIgnoreCase(messageText.trim())) {
            FacesUtil.execute("PF('wizardCampagne').next()");
        } else {
            if (STOP_MESSAGE.trim().equalsIgnoreCase(messageText.trim())) {
                messageText = STOP_MESSAGE;
            } else {
                messageText = messageText + STOP_MESSAGE;
            }
            FacesUtil.addMessage(propertiesUtil.getMsgSmsTextRequired(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public String getMittenteSms() {
        return settingService.getByAziendaId(currentOperatorAziendaId).getAlias();
    }

    private void calcSmsDaSpedireCount() {
        try {
            if (DATATABLE_VALUE_FROM_FILTERS.equalsIgnoreCase(datatableValueFrom)) {
                calcSmsInfo = smsService.calcSmsPricingInfo(filtroCampagnaMarketingViewModel, messageText,
                        isClientiReadSuper, isNominativiReadSuper, isClientiReadAll, isNominativiReadAll,
                        isClientiReadOwn, isNominativiReadOwn, currentOperatorId, currentOperatorAziendaId, aziendaSelectionView.getCurrentAziendaId());
            } else if (DATATABLE_VALUE_FROM_FILE.equalsIgnoreCase(datatableValueFrom)) {
                final List<String> recipientNumberList = new LinkedList<>();
                for (final ClienteViewModel clienteViewModel : clienteViewModelList) {
                    recipientNumberList.add(clienteViewModel.getTelefono());
                }
                calcSmsInfo = smsService.calcSmsPricingInfo(recipientNumberList, messageText, aziendaSelectionView.getCurrentAziendaId());
            }
        } catch (final UnableToCalculateSmsPrice unableToCalculateSmsPrice) {
            FacesUtil.addMessage(propertiesUtil.getMsgSmsErrorPriceCalculation(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void inviaCampagnaAction() {
        int totalNumberOfSms = calcSmsInfo.getSmsCount()*calcSmsInfo.getDestinatariCount();
        try {
            if (DATATABLE_VALUE_FROM_FILTERS.equalsIgnoreCase(datatableValueFrom)) {
                campagnaMarketingFacade.saveAndSendCampagna(nomeCampagna, descrizioneCampagna, messageText,
                        Campagna.CampagnaType.SMS, false, httpSessionUtil.getOperatorId(),
                        filtroCampagnaMarketingViewModel, null, totalNumberOfSms, isClientiReadSuper, isNominativiReadSuper, isClientiReadAll,
                        isNominativiReadAll, isClientiReadOwn, isNominativiReadOwn, currentOperatorAziendaId, aziendaSelectionView.getCurrentAziendaId());
            } else if (DATATABLE_VALUE_FROM_FILE.equalsIgnoreCase(datatableValueFrom)) {
                campagnaMarketingFacade.saveAndSendCampagna(nomeCampagna, descrizioneCampagna, messageText,
                        Campagna.CampagnaType.SMS, true, httpSessionUtil.getOperatorId(), null, clienteViewModelList,
                        totalNumberOfSms, isClientiReadSuper, isNominativiReadSuper, isClientiReadAll, isNominativiReadAll, isClientiReadOwn,
                        isNominativiReadOwn, currentOperatorAziendaId, aziendaSelectionView.getCurrentAziendaId());
            }
            inviaCampagnaEnabled = false;
            FacesUtil.addMessage(propertiesUtil.getMsgSmsInvioCampagnaSuccess(), FacesMessage.SEVERITY_INFO);
            FacesUtil.updateSmsInfo();
        } catch (final NotEnoughCreditException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgSmsErrorNotEnoughBalance(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public boolean isDisabledAnniAssunzionePensionamento() {
        return filtroCampagnaMarketingViewModel.getImpiego() != null
                && filtroCampagnaMarketingViewModel.getImpiego().equalsIgnoreCase(Impiego.PENSIONATO.getValue());
    }

    public String onNextFlow(final FlowEvent flowEvent) {
        filtraImportButtonEnabled = "destinatariTab".equals(flowEvent.getNewStep());
        if ("riepilogoTab".equals(flowEvent.getNewStep())) {
            calcSmsDaSpedireCount();
        }
        return flowEvent.getNewStep();
    }

    public void onFirstNextButtonClicked() {
        filtraImportButtonEnabled = false;
        RequestContext.getCurrentInstance()
                .execute("PF('wizardCampagne').next();PF('panelNuovaCampagna').unselect(0);");
        RequestContext.getCurrentInstance()
                .execute("PF('wizardCampagne').next();PF('panelNuovaCampagnaDaFile').unselect(0);");
    }

    public StreamedContent getSampleFile() {
        final InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
                .getResourceAsStream("/resources/files/contattiCampagneLace.xlsx");
        return new DefaultStreamedContent(stream, "application/xlsx", "contattiCampagneLace.xlsx");
    }

    public void handleFileUpload(final FileUploadEvent event) {
        try {
            lazy = false;
            datatableValueFrom = DATATABLE_VALUE_FROM_FILE;
            clienteViewModelList = apachePOIExcelRead.getRowExcel(event.getFile().getInputstream(),
                    event.getFile().getContentType());
            if (clienteViewModelList != null) {
                setFirstButtonWizard(clienteViewModelList.size());
            }
            RequestContext.getCurrentInstance().scrollTo("resultFilterTable");
            FacesUtil.execute("PF('fileImportWidget').uploadedFileCount = 0");
        } catch (IOException | NoSuchElementException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorImportExcel(), FacesMessage.SEVERITY_ERROR);
            // non avendo caricato, resetto il counter
            FacesUtil.execute("PF('fileImportWidget').uploadedFileCount = 0");
        } catch (final CannotImportExcelException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgErrorRowExcel(e.getMessage()), FacesMessage.SEVERITY_ERROR);
            // non avendo caricato, resetto il counter
            FacesUtil.execute("PF('fileImportWidget').uploadedFileCount = 0");
        } catch (final ExcelImportRowsExceeded excelImportRowsExceeded) {
            FacesUtil.addMessage(propertiesUtil.getMsgImportExcelRowsExceeded(), FacesMessage.SEVERITY_ERROR);
            // non avendo caricato, resetto il counter
            FacesUtil.execute("PF('fileImportWidget').uploadedFileCount = 0");
        } catch (final FileExcelToImportNotValidException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgFileExcelToImportNotValid(), FacesMessage.SEVERITY_ERROR);
            // non avendo caricato, resetto il counter
            FacesUtil.execute("PF('fileImportWidget').uploadedFileCount = 0");
        }
    }

    public Object getDatatableValue(final String fileOrFilters) {
        if (DATATABLE_VALUE_FROM_FILTERS.equalsIgnoreCase(fileOrFilters)) {
            return clienteLazyDataModel;
        } else if (DATATABLE_VALUE_FROM_FILE.equalsIgnoreCase(fileOrFilters)) {
            return clienteViewModelList;
        }
        return null;
    }

    private void setFirstButtonWizard(final int sizeDatatable) {
        firstAvantiEnabled = sizeDatatable != 0 &&
                !StringUtils.isEmpty(settingService.getByAziendaId(currentOperatorAziendaId).getAlias()) &&
                !StringUtils.isEmpty(aziendaService.getOne(currentOperatorAziendaId).getCellulare());
    }

    public boolean setTooltipStatus(){
        tooltipAliasDisabled = StringUtils.isEmpty(settingService.getByAziendaId(currentOperatorAziendaId).getAlias()) ||
                StringUtils.isEmpty(aziendaService.getOne(currentOperatorAziendaId).getCellulare());
        log.info("tooltipAliasDisabled : "+ tooltipAliasDisabled);
        return tooltipAliasDisabled;
    }

}