package com.woonders.lacemsjsf.util;

import lombok.Getter;
import lombok.Setter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;

@ManagedBean(name = PassaggioParametriUtils.NAME)
@SessionScoped
@Getter
@Setter
public class PassaggioParametriUtils implements Serializable {

    public static final String NAME = "passaggioParametriUtils";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private final HashMap<Parametro, Object> parametri = new HashMap<>();

    public enum Parametro {
        AMMINISTRAZIONE_PDF_SCHEDA_ATC_PARAMETER, AMMINISTRAZIONE_VIEWMODEL_PARAMETER, RAGIONE_SOCIALE_PARAMETER,
        CLIENTE_DA_CHECK_NOMINATIVO_PARAMETER, NOMINATIVO_SELECTED_ON_SEARCH, CLIENTE_SELECTED_ON_SEARCH,
        LIST_NOMINATIVI_CLIENTI_PARAMETER, TIPORICERCA_ANTIRICICLAGGIO_PARAMETER, CF_ANTIRICICLAGGIO_PARAMETER,
        DATACARICAMENTO_START_ANTIRICICLAGGIO_PARAMETER, DATACARICAMENTO_END_ANTIRICICLAGGIO_PARAMETER,
        LIST_IMPEGNI_NOMINATIVI_PARAMETER, LIST_NOMINATIVI_DA_RICHIAMARE_PARAMETER, COGNOME_CHECK_NOMINATIVO_PARAMETER,
        NOME_CHECK_NOMINATIVO_PARAMETER, DATA_NASCITA_CHECK_NOMINATIVO_PARAMETER, SESSO_CHECK_NOMINATIVO_PARAMETER,
        LUOGO_CHECK_NOMINATIVO_PARAMETER, CELLULARE_CHECK_NOMINATIVO_PARAMETER, FISSO_CHECK_NOMINATIVO_PARAMETER,
        CF_CHECK_NOMINATIVO_PARAMETER, NOMINATIVO_PDF_PARAMETER, IMPEGNI_NOMINATIVO_PDF_PARAMETER,
        PREVENTIVI_NOMINATIVO_PDF_PARAMETER, NOMINATIVI_CLIENTI_LIST_CHECK_NOMINATIVO_PARAMETER, AMMINISTRAZIONE_LIST,
        ARCHIVIAZIONE_RENDERED, CLIENTE_ID, TENANT_ID, CLIENTE_PRATICA_ID, FILE_CATEGORY, FILE_NAME, PRATICA_ID,
        DELETE_PRATICA_LIST_PARAMETER, LIST_CLIENTE_RECALL_PARAMETER, LIST_PRATICA_RINNOVABILE_PARAMETER,
        LIST_COESISTENZA_RINNOVABILE_PARAMETER, NOTIFICATION_CATEGORY_PARAMETER,
        SELECTED_FINANZIARIA, ESTINZIONE_LIST_PDF_PARAMETER, AMMINISTRAZIONE_PDF_PARAMETER, PDF_CATEGORY_PARAMETER,
        PRATICA_PDF_PARAMETER,
        RESIDENZA_PDF_PARAMETER, CONTO_PDF_PARAMETER, DOCUMENTO_PDF_PARAMETER, CLIENTE_PDF_PARAMETER,
        CLIENTE_NUOVA_PRATICA_PARAMETER, CENSITO_NUOVA_PRATICA_PARAMETER, TIPO_PRATICA_NUOVA_PRATICA_PARAMETER,
        STATO_PRATICA_PARAMETER, TIPO_PRATICA_PANNELLO_STATO_PRATICA_PARAMETER, AZIENDA_PDF_PARAMETER,
        RICARICA_PDF_PARAMETER, STATO_NOMINATIVO_PARAMETER, NUOVO_NOMINATIVO_PARAMETER,
        ID_NOMINATIVO_ON_FORM_NOMINATIVO_PARAMETER, PDF_LINK, EMAIL_CHECK_NOMINATIVO_PARAMETER,
        PRATICHE_DA_PERFEZIONARE_DATAINIZIO_PARAMETER, PRATICHE_DA_PERFEZIONARE_DATAFINE_PARAMETER,
        PRATICHE_DA_PERFEZIONARE_LIQUIDATA_PARAMETER, PRATICHE_DA_PERFEZIONARE_LIQUIDAZIONE_PARAMETER,
        PRATICHE_DA_PERFEZIONARE_QUIETANZATA_PARAMETER, PRATICHE_PERFEZIONATE_DATAINIZIO_PARAMETER,
        PRATICHE_PERFEZIONATE_DATAFINE_PARAMETER, PRATICHE_DA_PERFEZIONARE_PERFEZIONAMENTO_PARAMETER,
        PRATICHE_PERFEZIONATE_OPERATORE_PARAMETER, CAMPAGNA_ID, CLIENTE_VIEWMODEL_APPUNTAMENTO_PARAMETER,
        PDF_AZIENDA_OPERATORE_ASSEGNATO_PARAMETER, PRATICA_VIEWMODEL_APPUNTAMENTO_PARAMETER,
        SIMULATOR_TABLE_ID_PARAMETER, SIMULATOR_TABLE_SELECTION, SIMULATOR_RESULT_ROW_CHOOSEN, SIMULATOR_PAYMENT_CHOOSEN,
        SIMULATOR_AMOUNT_CHOOSEN, SIMULATOR_AGENT_FEES_CHOOSEN, SIMULATOR_TYPE_CHOOSEN, SIMULATOR_TABLE_VIEW_MODEL,
        SIMULATOR_SOURCE, STATO_CONTEGGIO_ESTINZIONE_PARAMETER, TIPO_ESTINZIONE_PANNELLO_STATO_CONTEGGIO_ESTINZIONE_PARAMETER,
        NOTICE_SELECTED_ID, NOTICE_ID_TO_DISPLAY_PDF, ADVANCED_SEARCH_CLIENTI_PARAMETER, ADVANCED_SEARCH_NOMINATIVI_PARAMETER,
        PROVENIENZA, PROVENIENZA_DESC, OPERATORS
    }

}
