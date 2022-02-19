package com.woonders.lacemsjsf.help;

import lombok.Setter;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Emanuele on 21/04/2017.
 */
@ManagedBean(name = "infoHelpView")
@ApplicationScoped
@Setter
// http://stackoverflow.com/questions/21906255/multi-line-value-in-resource-properties-file
public class InfoHelpView {

    private static final String HELP_BUNDLE = "help";
    private static final String KEY_INFO_ABILITAZIONE_SMS = "info.abilitazione.sms";
    private static final String KEY_INFO_SMS_LEAD = "info.sms.lead";
    private static final String KEY_INFO_SMS_APPUNTAMENTI = "info.sms.appuntamenti";
    private static final String KEY_INFO_RICARICA_SMS_0 = "info.ricarica.sms.0";
    private static final String KEY_INFO_RICARICA_SMS_1 = "info.ricarica.sms.1";
    private static final String KEY_INFO_RICARICA_SMS_2 = "info.ricarica.sms.2";
    private static final String KEY_INFO_NUOVA_CAMPAGNA_MARKETING = "info.nuova.campagna.marketing";
    private static final String KEY_INFO_CALENDARIO_APPUNTAMENTI = "info.calendario.appuntamenti";
    private static final String KEY_INFO_CAMPAGNA_SELEZIONATA_ESITO_0 = "info.campagna.selezionata.esito.0";
    private static final String KEY_INFO_CAMPAGNA_SELEZIONATA_ESITO_1 = "info.campagna.selezionata.esito.1";
    private static final String KEY_INFO_GESTIONEPERMESSI_0 = "info.gestionepermessi.0";
    private static final String KEY_INFO_GESTIONEPERMESSI_MODIFICA_0 = "info.gestionepermessi.modifica.0";
    private static final String KEY_INFO_GESTIONEPERMESSI_MODIFICA_1 = "info.gestionepermessi.modifica.1";
    private static final String KEY_INFO_GESTIONEPERMESSI_MODIFICA_2 = "info.gestionepermessi.modifica.2";
    private static final String KEY_INFO_COESISTENZE_0 = "info.coesistenze.0";
    private static final String KEY_INFO_COESISTENZE_1 = "info.coesistenze.1";
    private static final String KEY_INFO_COESISTENZE_2 = "info.coesistenze.2";
    private static final String KEY_INFO_COESISTENZE_3 = "info.coesistenze.3";
    private static final String KEY_INFO_IMPEGNI_0 = "info.impegni.0";
    private static final String KEY_INFO_IMPEGNI_1 = "info.impegni.1";
    private static final String KEY_INFO_IMPEGNI_2 = "info.impegni.2";
    private static final String KEY_INFO_IMPEGNI_3 = "info.impegni.3";
    private static final String KEY_INFO_ESTINZIONI_0 = "info.estinzioni.0";
    private static final String KEY_INFO_ESTINZIONI_1 = "info.estinzioni.1";
    private static final String KEY_INFO_ESTINZIONI_2 = "info.estinzioni.2";
    private static final String KEY_INFO_ESTINZIONI_3 = "info.estinzioni.3";
    private static final String KEY_INFO_MULTIAGENZIA = "info.multiagenzia";
    private static final String KEY_INFO_DATI_AGGIUNTIVI_NOMINATIVO = "info.datiaggiuntivinominativo";
    private static final String KEY_INFO_GENERIC_SIMULATOR_1 = "info.simulator.generic.1";
    private static final String KEY_INFO_GENERIC_SIMULATOR_2 = "info.simulator.generic.2";
    private static final String KEY_INFO_GENERIC_SIMULATOR_3 = "info.simulator.generic.3";
    private static final String KEY_INFO_CONFIGURATION_SIMULATOR = "info.simulator.configuration";
    private static final String KEY_INFO_DETAILS_SIMULATOR_1 = "info.simulator.details.1";
    private static final String KEY_INFO_DETAILS_SIMULATOR_2 = "info.simulator.details.2";
    private static final String KEY_INFO_DETAILS_SIMULATOR_3 = "info.simulator.details.3";
    private static final String KEY_INFO_DETAILS_SIMULATOR_4 = "info.simulator.details.4";
    private static final String KEY_INFO_DETAILS_SIMULATOR_5 = "info.simulator.details.5";
    private static final String KEY_INFO_DETAILS_SIMULATOR_6 = "info.simulator.details.6";
    private static final String KEY_INFO_SIMULATOR_RESULT_1 = "info.simulator.result.1";
    private static final String KEY_INFO_SIMULATOR_RESULT_2 = "info.simulator.result.2";
    private static final String KEY_INFO_SIMULATOR_INPUT = "info.simulator.input";
    private static final String KEY_INFO_GESTIONE_CONTEGGI_ESTINZIONE = "info.gestionesconteggiestinzione";
    private static final String KEY_INFO_ANALYTICS_CLIENTI = "info.analyticsclienti";
    private static final String KEY_INFO_ALIAS_SMS = "info.aliassms";
    private ResourceBundle italianHelpResourceBundle;

    public InfoHelpView() {
        // http://stackoverflow.com/questions/9433371/how-to-get-a-jsf-resource-bundle-property-value-in-backing-bean
        // http://stackoverflow.com/questions/2668161/internationalization-in-jsf-when-to-use-message-bundle-and-resource-bundle
        // https://stackoverflow.com/questions/25053710/read-resourcebundle-as-utf-8-getstring-method-seems-to-change-encoding-to-iso
        italianHelpResourceBundle = ResourceBundle.getBundle(HELP_BUNDLE);
    }

    public List<String> getInfoAbilitazioneSmsList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_ABILITAZIONE_SMS));
    }

    public List<String> getInfoSmsLeadList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_SMS_LEAD));
    }

    public List<String> getInfoSmsAppuntamentiList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_SMS_APPUNTAMENTI));
    }

    public List<String> getInfoRicaricaSmsList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_RICARICA_SMS_0),
                italianHelpResourceBundle.getString(KEY_INFO_RICARICA_SMS_1),
                italianHelpResourceBundle.getString(KEY_INFO_RICARICA_SMS_2));
    }

    public List<String> getInfoNuovaCampagnaMarketingList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_NUOVA_CAMPAGNA_MARKETING));
    }

    public String getInfoCalendarioAppuntamenti() {
        return italianHelpResourceBundle.getString(KEY_INFO_CALENDARIO_APPUNTAMENTI);
    }

    public List<String> getInfoEsitoCampagnaSelezionataList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_CAMPAGNA_SELEZIONATA_ESITO_0),
                italianHelpResourceBundle.getString(KEY_INFO_CAMPAGNA_SELEZIONATA_ESITO_1));
    }

    public List<String> getInfoGestionePermessiList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_GESTIONEPERMESSI_0));
    }

    public List<String> getInfoGestionePermessiModificaList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_GESTIONEPERMESSI_MODIFICA_0),
                italianHelpResourceBundle.getString(KEY_INFO_GESTIONEPERMESSI_MODIFICA_1),
                italianHelpResourceBundle.getString(KEY_INFO_GESTIONEPERMESSI_MODIFICA_2));
    }

    public List<String> getInfoCoesistenzeList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_COESISTENZE_0),
                italianHelpResourceBundle.getString(KEY_INFO_COESISTENZE_1),
                italianHelpResourceBundle.getString(KEY_INFO_COESISTENZE_2),
                italianHelpResourceBundle.getString(KEY_INFO_COESISTENZE_3));
    }

    public List<String> getInfoImpegniList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_IMPEGNI_0),
                italianHelpResourceBundle.getString(KEY_INFO_IMPEGNI_1),
                italianHelpResourceBundle.getString(KEY_INFO_IMPEGNI_2),
                italianHelpResourceBundle.getString(KEY_INFO_IMPEGNI_3));
    }

    public List<String> getInfoEstinzioniList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_ESTINZIONI_0),
                italianHelpResourceBundle.getString(KEY_INFO_ESTINZIONI_1),
                italianHelpResourceBundle.getString(KEY_INFO_ESTINZIONI_2),
                italianHelpResourceBundle.getString(KEY_INFO_ESTINZIONI_3));
    }

    public List<String> getInfoMultiAgenziaList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_MULTIAGENZIA));
    }

    public List<String> getInfoDatiAggiuntiviNominativoList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_DATI_AGGIUNTIVI_NOMINATIVO));
    }

    public List<String> getInfoGenericDataSimulatorTableList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_GENERIC_SIMULATOR_1),
                italianHelpResourceBundle.getString(KEY_INFO_GENERIC_SIMULATOR_2),
                italianHelpResourceBundle.getString(KEY_INFO_GENERIC_SIMULATOR_3));
    }

    public List<String> getInfoConfigurationDataSimulatorTableList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_CONFIGURATION_SIMULATOR));
    }

    public List<String> getInfoDetailsSimulatorTableList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_DETAILS_SIMULATOR_1),
                italianHelpResourceBundle.getString(KEY_INFO_DETAILS_SIMULATOR_2),
                italianHelpResourceBundle.getString(KEY_INFO_DETAILS_SIMULATOR_3),
                italianHelpResourceBundle.getString(KEY_INFO_DETAILS_SIMULATOR_4),
                italianHelpResourceBundle.getString(KEY_INFO_DETAILS_SIMULATOR_5),
                italianHelpResourceBundle.getString(KEY_INFO_DETAILS_SIMULATOR_6));
    }

    public List<String> getInfoSimulatorResultList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_SIMULATOR_RESULT_1),
                italianHelpResourceBundle.getString(KEY_INFO_SIMULATOR_RESULT_2));
    }

    public List<String> getInfoSimulatorInputDataList() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_SIMULATOR_INPUT));
    }

    public List<String> getKeyInfoGestioneConteggiEstinzione() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_GESTIONE_CONTEGGI_ESTINZIONE));
    }

    public List<String> getKeyInfoAnalyticsClienti() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_ANALYTICS_CLIENTI));
    }

    public List<String> getInfoAliasSms() {
        return createMessageList(italianHelpResourceBundle.getString(KEY_INFO_ALIAS_SMS));
    }

    public String getInfoAliasSmsText(){
        return italianHelpResourceBundle.getString(KEY_INFO_ALIAS_SMS);
    }

    private List<String> createMessageList(final String... strings) {
        return Arrays.asList(strings);
    }

}
