package com.woonders.lacemsjsf.view.util;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel.MenuSection;
import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel.PermissionType;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Cliente.Sesso;
import com.woonders.lacemscommon.db.entity.Documento;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Pratica.TipoPratica;
import com.woonders.lacemscommon.db.entity.Preventivo;
import com.woonders.lacemscommon.db.entity.Trattenute;
import com.woonders.lacemscommon.db.entityenum.*;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.Articolo;
import com.woonders.lacemscommon.laceenum.*;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ManagedBean(name = ConstantResolverView.NAME)
@Setter
@ApplicationScoped
public class ConstantResolverView implements Serializable {

    public static final String NAME = "constantResolverView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    public static final long PDF_SIZE_LIMIT = 7340032; // bytes = 7mb
    private static final Integer minAnniAssunzioneCampagnaRange = 5;
    private static final Integer maxAnniAssunzioneCampagnaRange = 30;
    private static final int MAX_CAT_PENSIONE_LENGTH = 30;
    private final Integer minEtaCampagnaRange = 18;
    private final Integer maxEtaCampagnaRange = 99;
    // so che sono costanti, ma i getter con i nomi in maiuscolo potrebbero fare
    // danni
    private final Integer amministrazioneListMaxSize = 2;
    private final Integer valutazioneListMaxSize = 6;
    private final Integer calendarMinHour = 8;
    private final Integer minLengthTelephone = 5;
    private final Integer maxLengthTelephone = 16;
    private final Integer minLengthLastname = 2;
    private final String chooseLabelButtonFile = "Scegli";
    private final String importLabelButtonFile = "Importa";
    private final String cancelLabelButtonFile = "Annulla";
    private final String uploadLabelButtonFile = "Carica";
    private final String allowTypesFile = "/(\\.|\\/)(pdf)$/";
    private final String allowTypesImportFileCampagne = "/(\\.|\\/)(xls|xlsx)$/";
    private final String cessioneStipendio = TipoPratica.CESSIONE_S.getValue();
    private final String cessionePensione = TipoPratica.CESSIONE_P.getValue();
    private final String delega = TipoPratica.DELEGA.getValue();
    private final String prestito = TipoPratica.PRESTITO.getValue();
    private final Integer preventivoListMaxSize = 3;
    private final Integer impegniEstinguibiliMaxSize = 2;
    private final long excelCampagneSizeLimit = 5242880; // bytes = 5mb
    private final Integer amministrazioneListNominativoMaxSize = 1;
    private final Integer minValueOfSpinnerGiorniNotifica = 1;
    private final String STRIPE_PUBLISHABLE_KEY = System.getProperty("STRIPE_PUBLISHABLE_KEY");
    private final int maxLengthCommentoLog = 160;
    private final boolean CUSTOMERLY_ENABLED = Boolean.valueOf(System.getProperty("CUSTOMERLY_ENABLED", "false"));
    private final Integer maxFileImportCampagne = 1;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    private List<String> istitutoList;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;

    @PostConstruct
    public void init() {
        final List<String> istitutoList = Arrays.asList("Italcredi", "BPP", "Vivi Banca", "Santander", "Towers CQ", "Intesa", "Findomestic",
                "Fincontinuo", "Mediocredito Europeo", "Pitagora", "BPM", "Accedo S.p.A.", "IBL Banca", "PrestItalia", "Unicredit", "BNL",
                "AGOS", "Unifin", "Sigla Credit", "Banca di Sassari", "CariChieti", "Deutsche", "Credem");
        Collections.sort(istitutoList);
        this.istitutoList = Collections.unmodifiableList(istitutoList);
    }

    public Assicurazioni[] getCompagnie() {
        return Assicurazioni.values();
    }

    public ValutazioneCompagnia[] getValutazioni() {
        return ValutazioneCompagnia.values();
    }

    public Preventivo.TipologiaPreventivo[] getTipologiaPreventivo() {
        return Preventivo.TipologiaPreventivo.values();
    }

    public Integer getAmministrazioneListMaxSize() {
        return amministrazioneListMaxSize;
    }

    public Integer getValutazioneListMaxSize() {
        return valutazioneListMaxSize;
    }

    public Integer getCalendarMinHour() {
        return calendarMinHour;
    }

    public DurataPratica[] getDurataPratica() {
        return DurataPratica.values();
    }

    public Provenienza[] getProvenienza() {
        return Provenienza.values();
    }

    public Integer getMinLengthTelephone() {
        return minLengthTelephone;
    }

    public Integer getMaxLengthTelephone() {
        return maxLengthTelephone;
    }

    public Integer getMinLengthLastname() {
        return minLengthLastname;
    }

    public Tipo[] getTipo() {
        return Tipo.values();
    }

    public StatoPratica[] getAllStatoPratica() {
        return StatoPratica.values();
    }

    public Object getStatoPraticaByTipo(final String tipoPratica) {
        if (TipoPratica.PRESTITO.getValue().equalsIgnoreCase(tipoPratica)) {
            return StatoPratica.getStatiPraticaPrestito();
        } else {
            return StatoPratica.getStatiPraticaCessioneDelega();
        }
    }

    public Object getStatoPraticaFiltroCampagne(final String tipoPratica) {
        if (StringUtils.isEmpty(tipoPratica)) {
            return getAllStatoPratica();
        }
        return getStatoPraticaByTipo(tipoPratica);
    }

    public StatoNominativo[] getStatoNominativo() {
        return StatoNominativo.values();
    }

    public Impiego[] getImpiego() {
        return Impiego.values();
    }

    public Pratica.TipoPratica[] getTipoPratica() {
        return Pratica.TipoPratica.values();
    }

    public String getChooseLabelButtonFile() {
        return chooseLabelButtonFile;
    }

    public String getCancelLabelButtonFile() {
        return cancelLabelButtonFile;
    }

    public String getUploadLabelButtonFile() {
        return uploadLabelButtonFile;
    }

    public String getAllowTypesFile() {
        return allowTypesFile;
    }

    public List<String> getImpieghiListString() {
        return Impiego.getImpiegoListString();
    }

    public String getCessioneStipendio() {
        return cessioneStipendio;
    }

    public String getCessionePensione() {
        return cessionePensione;
    }

    public String getDelega() {
        return delega;
    }

    public String getPrestito() {
        return prestito;
    }

    public Integer getPreventivoListMaxSize() {
        return preventivoListMaxSize;
    }

    public Integer getImpegniEstinguibiliMaxSize() {
        return impegniEstinguibiliMaxSize;
    }

    public long getPdfSizeLimit() {
        return PDF_SIZE_LIMIT;
    }

    public Integer getAmministrazioneListNominativoMaxSize() {
        return amministrazioneListNominativoMaxSize;
    }

    public Integer getMinValueOfSpinnerGiorniNotifica() {
        return minValueOfSpinnerGiorniNotifica;
    }

    public EsitoSmsNotificaLead[] getEsitoSmsNotificaLead() {
        return EsitoSmsNotificaLead.values();
    }

    public Articolo.TipoArticolo tipoArticoloFromString(final String tipoArticolo) {
        return Articolo.TipoArticolo.valueOf(tipoArticolo);
    }

    public StatoNominativo getStatoNominativoFromString(final String statoNominativo) {
        return StatoNominativo.valueOf(statoNominativo);
    }

    public List<BigDecimal> getSpeseList() {
        final List<BigDecimal> bdList = new ArrayList<>();
        final BigDecimal min = new BigDecimal(116.00);
        for (BigDecimal value = new BigDecimal(616.00); value.compareTo(min) >= 0; value = value
                .subtract(new BigDecimal(50))) {
            bdList.add(value);
        }
        return bdList;
    }

    public String getStripePublishableKey() {
        return STRIPE_PUBLISHABLE_KEY;
    }

    public int getMaxLengthCommentoLog() {
        return maxLengthCommentoLog;
    }

    public boolean isCustomerlyEnabled() {
        return CUSTOMERLY_ENABLED;
    }

    public List<Integer> getListAnniAssunzioneNuovaCampagna() {
        return IntStream.iterate(minAnniAssunzioneCampagnaRange, i -> i + minAnniAssunzioneCampagnaRange)
                .limit(maxAnniAssunzioneCampagnaRange / minAnniAssunzioneCampagnaRange).boxed()
                .collect(Collectors.toList());
    }

    public RicaricaType[] getRicaricaType() {
        return RicaricaType.values();
    }

    public Sesso[] getSesso() {
        return Sesso.values();
    }

    public Integer getMinEtaCampagnaRange() {
        return minEtaCampagnaRange;
    }

    public Integer getMaxEtaCampagnaRange() {
        return maxEtaCampagnaRange;
    }

    public String getImportLabelButtonFile() {
        return importLabelButtonFile;
    }

    public String getAllowTypesImportFileCampagne() {
        return allowTypesImportFileCampagne;
    }

    public long getExcelCampagneSizeLimit() {
        return excelCampagneSizeLimit;
    }

    public Integer getMaxFileImportCampagne() {
        return maxFileImportCampagne;
    }

    public MenuSection[] getMenuSection() {
        return MenuSection.values();
    }

    public PermissionType[] getPermissionType() {
        return PermissionType.values();
    }

    public PermissionProfile[] getPermissionProfile() {
        if (aziendaService.count() > 1) {
            return PermissionProfile.values();
        } else {
            return PermissionProfile.getNoMultiAgencyPermissionProfileArray();
        }
    }

    public FiltroRinnoviPraticaCoesistenza[] getFiltroRinnoviPraticaCoesistenza() {
        return FiltroRinnoviPraticaCoesistenza.values();
    }

    public String getTipoPraticaCessioneValue() {
        return TipoPratica.CESSIONE_S.getValue();
    }

    public String getTipoPraticaPrestitoValue() {
        return TipoPratica.PRESTITO.getValue();
    }

    public List<String> getIstitutoList() {
        return istitutoList;
    }

    public int getMaxCatPensioneLength() {
        return MAX_CAT_PENSIONE_LENGTH;
    }

    public SimulatorTableViewModel.SimulatorTableStatus[] getSimulatorTableStatus() {
        return SimulatorTableViewModel.SimulatorTableStatus.values();
    }

    public SimulatorRoundingMode[] getSimulatorRoundingMode() {
        return SimulatorRoundingMode.values();
    }

    public FeeType[] getFeeType() {
        return FeeType.values();
    }

    public Mensilita[] getMensilita() {
        return Mensilita.values();
    }

    public String getSimulatorDisclaimerMessage() {
        return propertiesUtil.getMsgSimulatorDisclaimer();
    }

    public StatoConteggioEstinzione[] getStatoConteggioEstinzione() {
        return StatoConteggioEstinzione.values();
    }

    public Trattenute.TipoImpegno[] getTipoImpegno() {
        return Trattenute.TipoImpegno.values();
    }

    public Documento.TipoDocumento[] getTipoDocumento() {
        return Documento.TipoDocumento.values();
    }

    public AnalyticsDateEnum[] getAnalyticsDates() {
        return AnalyticsDateEnum.values();
    }

    public FornitoreLead[] getFornitoreLead() {
        return FornitoreLead.values();
    }

    public GraficoEnum[] getGraficoEnum() {
        return GraficoEnum.values();
    }

    public TipoRinnovo[] getTipoRinnovo() {
        return TipoRinnovo.values();
    }

}

