package enumPdf;

public enum NameFieldsPdfCliente {

    //Anagrafica
    CF("CF"),
    COGNOME("COGNOME"),
    NOME("NOME"),
    SOTTOSCRITTO("SOTTOSCRITTO"),
    DATA_NASCITA("IL"),
    LUOGO_NASCITA("NATO A"),
    PROV_NASCITA("PROVINCIA NASCITA"),
    CITTADINANZA("CITTADINANZA"),
    STATO_CIVILE("STATO CIVILE"),
    TELEFONO("TELEFONO"),
    TELEFONO_FISSO("TELEFONO FISSO"),
    FISSO("FISSO"),
    EMAIL("EMAIL"),
    COMUNIONE("COMUNIONE"),
    SEPARAZIONE("SEPARAZIONE"),
    NOTE("NOTE"),
    NOTE_PRATICA("NOTE PRATICA"),
    MASCHIO("MASCHIO_CHECKBOX"),
    FEMMINA("FEMMINA_CHECKBOX"),

    //Residenza
    LUOGO_RESIDENZA("RESIDENTE"),
    PROVINCIA_RESIDENZA("PROVINCIA RESIDENZA"),
    CAP_RESIDENZA("CAP RESIDENZA"),
    INDIRIZZO_RESIDENZA("INDIRIZZO DI RESIDENZA"),
    INDIRIZZO_RESIDENZA_NO_CIVICO("INDIRIZZO DI RESIDENZA NO CIVICO"),
    CIVICO_RESIDENZA("CIVICO RESIDENZA"),
    CHECKBOX_RESIDENZA("C RESIDENZA"),

    //Corrispondenza
    LUOGO_CORRISPONDENZA("CORRISPONDENZA"),
    INDIRIZZO_CORRISPONDENZA("INDIRIZZO CORRISPONDENZA"),
    PROV_CORRISPONDENZA("PR CORRISPONDENZA"),
    CAP_CORRISPONDENZA("CORRISPONDENZA CAP"),

    //Azienda
    NOME_AZIENDA("DENOMINAZIONE AZIENDA"),
    COGNOME_TITOLARE_AZIENDA("COGNOME TITOLARE AZIENDA"),
    NOME_TITOLARE_AZIENDA("NOME TITOLARE AZIENDA"),
    TELEFONO_AZIENDA("TELEFONO AZIENDA"),
    FAX_AZIENDA("FAX AZIENDA"),
    EMAIL_AZIENDA("EMAIL AZIENDA"),
    INDIRIZZO_AZIENDA("INDIRIZZO AZIENDA"),
    LUOGO_AZIENDA("LUOGO AZIENDA"),
    PROVINCIA_AZIENDA("PROVINCIA AZIENDA"),
    CAP_AZIENDA("CAP AZIENDA"),
    PIVA_AZIENDA("PIVA AZIENDA"),
    PEC_AZIENDA("PEC AZIENDA"),

    //Domicilio
    CHECKBOX_DOMICILIO("C DOMICILIO"),
    INDIRIZZO_DOMICILIO("VIA DOMICILIO"),
    INDIRIZZO_DOMICILIO_NO_CIVICO("VIA DOMICILIO NO CIVICO"),
    CIVICO_DOMICILIO("CIVICO DOMICILIO"),
    LUOGO_DOMICILIO("DOMICILIATO"),
    PROVINCIA_DOMICILIO("PROVINCIA DOMICILIO"),
    CAP_DOMICILIO("CAP DOMICILIO"),

    //Altra Corrispondenza
    CHECKBOX_ALTRA_CORRISPONDENZA("C ALTRO"),
    INDIRIZZO_ALTRA_CORRISPONDENZA("VIA CORRISPONDENZA"),
    CIVICO_ALTRA_CORRISPONDENZA("CIVICO CORRISPONDENZA"),
    LUOGO_ALTRA_CORRISPONDENZA("LUOGO CORRISPONDENZA"),
    PROVINCIA_ALTRA_CORRISPONDENZA("PROVINCIA CORRISPONDENZA"),
    CAP_ALTRA_CORRISPONDENZA("CAP CORRISPONDENZA"),

    //Occupazione
    CHECKBOX_PENSIONATO("PENSIONATO"),
    ENTE_PENSIONISTICO("ENTE"),
    DATA_INIZIO_PENSIONE("ENTE PENSIONISTICO DAL"),
    CATEGORIA_PENSIONE("CATEGORIA"),
    CHECKBOX_QUOTA_CEDIBILE("QUOTA CEDIBILE"),
    CHECKBOX_DIPENDENTE("DIPENDENTE"),
    OCCUPAZIONE("AZIENDA"),
    DATA_INIZIO_OCCUPAZIONE("DATA DAL"),
    IMPIEGO("ATTIVITA"),
    CHECKBOX_PRIVATO("PRIVATO_CB"),
    CHECKBOX_STATALE("STATALE_CB"),
    CHECKBOX_PUBBLICO("PUBBLICO_CB"),

    //Pratica
    TIPO_PRATICA_LETTERE("TIPO DI OPERAZIONE"),
    CQS_LETTERE("CESSIONE DEL QUINTO STIPENDIO"),
    CHECKBOX_CQS("CQS"),
    CQP_LETTERE("CESSIONE DEL QUINTO PENSIONE"),
    CHECKBOX_CQP("CQP"),
    DLG_LETTERE("DELEGAZIONE DI PAGAMENTO"),
    CHECKBOX_DLG("DLG"),
    CHECKBOX_PP("PP"),
    PP_LETTERE("PRESTITO PERSONALE"),
    RATA("QUOTA"),
    DURATA("DURATA2"),
    DURATA_CQ("DURATA CESSIONE"),
    DURATA_DLG("DURATA DELEGA"),
    MONTANTE("MONTANTE"),
    MONTANTE_LETTERE("MONTANTE IN LETTERE"),
    SPESE("SPESE"),
    SPESE_LETTERE("SPESE IN LETTERE"),
    TAN("TAN"),
    TAEG("TAEG"),
    TEG("TEG"),
    NETTO_RICAVO("NETTO RICAVO"),
    NUMERO_CTR("CTR"),
    NUMERO_SECCI("SECCI"),
    INTERESSI("INTERESSI"),
    INTERESSI_LETTERE("INTERESSI IN LETTERE"),
    GARANZIA("ASSICURAZIONE PRESTITO"),
    IMPORTO_EROGATO("IMPORTO EROGATO"),
    IMPORTO_EROGATO_LETTERE("FINANZIATO IN LETTERE 2"),
    REDDITO_ANNUO("REDDITO ANNUO NETTO 2"),
    NETTO_MENSILE("NETTO MENSILE"),
    RATA_EST("RATA ESTINZIONE"),
    RATA_SECONDA_EST("RATA ESTINZIONE2"),
    ISTITUTO_EST("ESTINZIONE"),
    SECONDA_EST("ESTINZIONE2"),
    SCADENZA_EST("SCADENZA ESTINZIONE"),
    CONTEGGIO_EST("CONTEGGIO ESTINZIONE"),
    DATA_CARICAMENTO("DATA CARICAMENTO"),
    CAPITALE_FIN("CAPITALE FINANZIATO"),
    OPERATORE("OPERATORE"),
    NOME_COGNOME_OPERATORE("NOME_COGNOME_OPERATORE"),
    DATA_DECORRENZA("DATA_DECORRENZA"),
    PERC_PROVVIGIONI("PERC_PROVVIGIONI"),
    EURO_PROVVIGIONI("PROVV IN EURO"),
    EURO_PROVVIGIONI_LETTERE("PROVV IN LETTERE"),
    ID_PRATICA("IDPRATICA"),


    //Conto
    DATA_APERTURA_CONTO("DATA CONTO"),
    IBAN("IBAN"),
    FILIALE("BANCA"),
    SPORTELLO("SPORTELLO"),

    //Documento
    CHECKBOX_CARTA_IDENTITA("CI"),
    CHECKBOX_PASSAPORTO("PASS"),
    CHECKBOX_PATENTE("PA"),
    NUMERO_DOC("NUMERO DOCUMENTO"),
    RILASCIO_DOC("RILASCIO DOCUMENTO"),
    DATA_RILASCIO_DOC("DATA RILASCIO DOCUMENTO"),
    SCADENZA_DOC("SCADENZA DOCUMENTO"),
    TIPO_DOC("TIPO DOCUMENTO"),

    //Altro
    DATA_CORRENTE("DATA CORRENTE"),

    //Amministazione
    RAGIONE_SOCIALE("RAGIONE SOCIALE"),
    PIVA("PIVA"),
    SEDE_LEGALE("SEDE LEGALE"),
    SEDE_NOTIFICA("SEDE NOTIFICA"),
    REFERENTE("REFERENTE"),
    RUOLO("RUOLO"),
    CHECKBOX_PEC_SI("PEC SI"),
    CHECKBOX_PEC_NO("PEC NO"),
    FAX("FAX");


    private final String fieldName;

    private NameFieldsPdfCliente(final String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }


}
