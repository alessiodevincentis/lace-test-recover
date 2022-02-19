package com.woonders.lacemsjsf.config;

import com.woonders.lacemscommon.app.campagneexcel.ApachePOIExcelRead;
import com.woonders.lacemsjsf.view.noticeboard.NoticeBoardView;
import com.woonders.lacemsjsf.view.util.ConstantResolverView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

@Component
public class PropertiesUtil {

    public static final String NAME = "propertiesUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static final String MSG_CF_ERROR_GIAUSATO = "msg.cf.error.giausato";
    private static final String MSG_SMS_ERROR_NOT_ENOUGH_BALANCE = "msg.sms.error.notenoughbalance";
    private static final String MSG_SMS_ERROR_PRICE_CALCULATION = "msg.sms.error.pricecalculation";
    private static final String MSG_SMS_INVIO_CAMPAGNA_SUCCESS = "msg.sms.inviocampagnasuccess";
    private static final String MSG_SCHEDULE_EVENTMOVED_SUCCESS = "msg.schedule.eventmoved.success";
    private static final String MSG_SMS_TEXT_REQUIRED = "msg.sms.textrequired";
    private static final String MSG_IMPORTAZIONE_START = "msg.importazione.start";
    private static final String MSG_IMPORTAZIONE_EXCEL_ROWS_EXCEEDED = "msg.importazione.excel.rows.exceeded";
    private static final String MSG_SMS_UPLOAD_FILELIMIT = "msg.sms.upload.filelimit";
    private static final String MSG_ERROR_MAIL_RECIPIENT_MISSING = "msg.error.mail.recipient.missing";
    private static final String MSG_ERROR_MAIL_AGENCY_NAME_MISSING = "msg.error.mail.agencyname.missing";
    private static final String MSG_ERROR_MAIL_REPLY_TO_MISSING = "msg.error.mail.replyto.missing";
    private static final String MSG_ERROR_MAIL_SUBJECT_MISSING = "msg.error.mail.subject.missing";
    private static final String MSG_ERROR_MAIL_BODY_MISSING = "msg.error.mail.body.missing";
    private static final String MSG_SMS_SENT_SUCCESS = "msg.sms.sent.success";
    private static final String MSG_ERROR_SEND_SMS_GENERIC = "msg.error.send.sms.generic";
    private static final String MSG_ERROR_SMS_MISSING_FIELDS = "msg.error.sms.missing.fields";
    private static final String MSG_ERROR_COLUMN_TRUNCATED = "msg.error.columntruncated";
    private static final String MESSAGES_MAIL_PROPERTIES = "mail/messagemail.properties";
    private static final String MSG_SIMULATOR_ERROR_TAN = "msg.simulator.error.tan";
    private static final String MSG_SIMULATOR_ERROR_TABLE_IN_USE = "msg.simulator.error.tableinuse";
    private static final String MSG_SIMULATOR_ERROR_RUN_MISSINGFIELDS = "msg.simulator.error.run.missingfields";
    private static final String MSG_SIMULATOR_ERROR_EMPTY_JOB_TYPE_LIST = "msg.simulator.error.emptyjobtypelist";
    private static final String MSG_SIMULATOR_DISCLAIMER = "msg.simulator.disclaimer";
    private static final String MSG_SIMULATOR_SAVED_TABLE = "msg.simulator.saved.table";
    private static final String STORAGE_SPACE_CHART_TITLE = "storagespace.chart.title";
    private static final String STORAGE_SPACE_NEED_MORE_SPACE = "storagespace.needmorespace";
    private static final String MSG_ERROR_TABLE_NAME_ALREADY_USED = "msg.error.tablenamealreadyused";
    private static final String MSG_SIMULATOR_TABLE_SAVED_ONLY_OPERATORS = "msg.simulatortable.savedonlyoperators";
    private static final String MSG_BACKUP_SUBJECT = "msg.backup.subject";
    private static final String MSG_BACKUP_BODY = "msg.backup.body";
    private static final String MSG_BACKUP_REPLYTO = "backup.reply.to";
    private static final String MSG_INVITE_FRIENDS_SUBJECT = "msg.invitefriends.subject";
    private static final String MSG_INVITE_FRIENDS_BODY = "msg.invitefriends.body";
    private static final String MSG_CONFIRM_UPLOAD_FILE = "msg.confirm.upload.file";
    private static final String MSG_DELETE_NOTICE_SUCCESS = "msg.delete.notice.success";
    private static final String MSG_INVALID_FILE_PDF = "msg.warning.invalidFileMessage.pdf";
    private static final String MSG_UPLOAD_ONE_FILE_LIMIT = "msg.file.upload.filelimit.noticeboard";
    private static final String MSG_ADVANCED_SEARCH_EMPTY_FILTER = "msg.advanced.search.empty.filter";
    private static final String MSG_ADVANCED_SEARCH_DATA_WRONG = "msg.advanced.search.data.wrong";

    private final String DATIFORNITI_MSG_NAME = "datiforniti";
    private final String INFORMATIVAPREVENTIVO_MSG_NAME = "informativapreventivo";
    private final String PREVENTIVO_MSG_NAME = "preventivo";
    private final String PREVENTIVO_MSG_NAME_SMS = "preventivosms";
    private final String INFODOC_MSG_NAME = "infodocumenti";
    private final String DATICLIENTE_MSG_NAME = "daticliente";
    private final String INFOULTERIORI1_MSG_NAME = "infoulteriori1";
    private final String INFOBENESTARE_MSG_NAME = "inforicevutabenestare";
    private final String COPIABUSTAPAGA_MSG_NAME = "richiestacopiabustapaga";
    private final String OGGETTOPREVENTIVO_MSG_NAME = "oggettorichiestapreventivo";
    private final String OGGETTOBUSTAPAGA_MSG_NAME = "oggettorichiestabustapaga";
    private final String LINK_PROPERTIES = "link/linkapp.properties";
    private final String CONTROLLOIBAN_LINK = "controlloiban";
    private final String CONTROLLODOCUMENTO_LINK = "controllodocumento";
    private final String SECURITY_PROPERTIES = "security/security.properties";
    private final String MAX_LOGIN_ATTEMPTS = "maxloginattempts";
    private final String MESSAGES_PROPERTIES = "messages.properties";
    private final String MSG_BLOCKED_IP = "msg.blockedip";
    private final String MSG_INVALID_LOGIN = "msg.wronglogin";
    private final String MSG_DATASOURCE_SAVED = "msg.datasource.saved";
    private final String MSG_DATASOURCE_DELETED = "msg.datasource.deleted";
    private final String MSG_LOGINOPERATOR_SAVED = "msg.loginoperator.saved";
    private final String MSG_LOGINOPERATOR_DELETED = "msg.loginoperator.deleted";
    private final String PASSWORD_MESSAGES_PROPERTIES = "password_messages.properties";
    private final String MSG_ATTENZIONE = "msg.attenzione";
    private final String MSG_ERROR_MAX_AMMINISTRAZIONI = "msg.error.maxamministrazioni";
    private final String MSG_ERROR_STESSA_AMMINISTRAZIONE = "msg.error.stessaamministrazione";
    private final String MSG_NOMINATIVO_DELETED = "msg.nominativo.deleted";
    private final String MSG_ERROR_PDF_NON_GENERATO = "msg.error.pdfnongenerato";
    private final String MSG_WARNING_DATI_NOMINATIVO_MANCANTI = "msg.warning.datanascitaorecapitimancanti";
    private final String MSG_WARNING_ACCESSO_NON_CONSENTITO = "msg.warning.accessononconsentito";
    private final String MSG_WARNING_ACCESSO_NON_CONSENTITO_SIMULATOR = "msg.warning.simulator.accessononconsentito";
    private final String MSG_WARNING_INVALID_FILE = "msg.warning.invalidFileMessage";
    private final String MSG_PRATICHE_PERFEZIONATE = "msg.pratiche.perfezionate";
    private final String MSG_ERROR_PERFEZIONATE = "msg.error.perfezionate";
    private final String MSG_SUBJECT_PROFORMA = "msg.subject.proforma";
    private final String MSG_BODY_PROFORMA = "msg.body.proforma";
    private final String MSG_ERROR_EDIT_DECORRENZA = "msg.error.editdecorrenza";
    private final String MSG_ERROR_INVALID_FILE_SIZE = "msg.warning.invalidSizeMessage";
    private final String MSG_DELETE_SUCCESS = "msg.delete.success";
    private final String MSG_DELETE_CF_NOTFOUND = "msg.delete.cfnotfound";
    private final String MSG_DELETE_ERROR = "msg.error.delete";
    private final String MSG_DELETE_PRATICA_SUCCESS = "msg.deletepratica.success";
    private final String MSG_FILE_UPLOAD_OK = "msg.file.upload.ok";
    private final String MSG_FILE_UPLOAD_ERROR = "msg.file.upload.error";
    private final String MSG_FILE_UPLOAD_NOSPACE = "msg.file.upload.nospace";
    private final String MSG_FILE_UPLOAD_FILETOOBIG = "msg.file.upload.filetoobig";
    private final String MSG_FILE_UPLOAD_ANTIRICICLAGGIO_FILELIMIT = "msg.file.upload.antiriciclaggio.filelimit";
    private final String MSG_FILE_VIEW_NOFILEFOUND = "msg.file.view.nofilefound";
    private final String MSG_FILE_VIEW_DELETED = "msg.file.view.deleted";
    private final String MSG_FILE_VIEW_DELETEERROR = "msg.file.view.deleteerror";
    private final String COMPARATORE_LINK = "linkcomparatore";
    private final String MSG_SUCCESS = "msg.success";
    private final String MSG_AZIENDA_ERROR = "msg.azienda.error";
    private final String MSG_AGENZIA_NOTFOUND = "msg.agenzia.notfound";
    private final String MSG_FUNCTIONALITY_NOT_ENABLED_SUMMARY = "msg.functionality.notenabled.summary";
    private final String MSG_FUNCTIONALITY_NOT_ENABLED_MESSAGE = "msg.functionality.notenabled.message";
    private final String MSG_CREAPRATICA_SUCCESS = "msg.creapratica.success";
    private final String MSG_CREAPRATICA_ERROR = "msg.creapratica.error";
    private final String MSG_CREAPRATICA_LIMIT_ESTINZIONI = "msg.creapratica.limitestinzioni";
    private final String MSG_ERRORE_IMPREVISTO = "msg.erroreimprevisto";
    private final String MSG_ERROR_STATO_CONTEGGIO_ESTINZIONE = "msg.error.editstatoconteggioestinzione";
    private final String MSG_ERROR_EDIT_STATO_PRATICA = "msg.error.editstatopratica";
    private final String MSG_SUCCESS_EDIT_STATO_CONTEGGIO_ESTINZIONE = "msg.success.editstatoconteggioestinzione";
    private final String MSG_SUCCESS_EDIT_STATO_PRATICA = "msg.success.editstatopratica";
    private final String MSG_PRATICA_CANNOT_DELETE = "msg.deletepratica.praticacannotdelete";
    private final String MSG_INFO = "msg.info";
    private final String MSG_ERROR = "msg.error";
    private final String MSG_SUCCESS_EDIT_DATA_NOTIFICA = "msg.success.editdatanotifica";
    private final String MSG_SUCCESS_EDIT_DATA_NOTIFICA_CONTEGGIO_ESTINZIONE = "msg.success.editdatanotificaconteggioestinzione";
    private final String MSG_ERROR_DECIMAL_VALUE = "msg.error.decimalvalue";
    private final String MSG_ENABLE_IL_COMPARATORE_LEAD_RECEPTION = "msg.success.enableilcomparatoreleadreception";
    private final String MSG_ERROR_ENABLE_IL_COMPARATORE_LEAD_RECEPTION = "msg.error.enableilcomparatoreleadreception";
    private final String MSG_ERROR_SEND_MAIL = "msg.error.sendmail";
    private final String MSG_SEND_MAIL_OK = "msg.sendmail.ok";
    private final String MSG_ERROR_CAMPI_REGISTRAZIONE_COMPARATORE_OBBLIGATORI = "msg.error.campiregistrazionecomparatoreobbligatori";
    private final String MSG_ERROR_GENERIC = "msg.error.generic";
    private final String MSG_SETTINGS_SAVE_SUCCESS = "msg.settings.save.success";
    private final String MSG_SMS_VALIDATION_ERROR = "msg.sms.validation.error";
    private final String MSG_SMS_MIN_LENGTH_ERROR = "msg.sms.error.minlength";
    private final String MSG_SMS_MAX_LENGTH_ERROR = "msg.sms.error.maxlength";
    private final String MSG_SMS_SENDER_MAX_LENGTH_ERROR = "msg.sms.sender.error.maxlength";
    private final String MSG_SMS_SENDER_INVALID_CHARACTER = "msg.sms.sender.invalid.character";
    private final String MSG_SMS_TOPUP_INVALID_CHOICE = "msg.sms.topup.invalid.choice";
    private final String MSG_SMS_TOPUP_PAYMENT_SUCCESS = "msg.sms.topup.payment.success";
    private final String MSG_SMS_TOPUP_PAYMENT_FAILED = "msg.sms.topup.payment.failed";
    private final String MSG_SMS_DATI_AZIENDA_MANCANTI = "msg.sms.datiaziendamancanti";
    private final String MSG_NUMBER_ONLY = "msg.invalid.numberonly";
    private final String MSG_WARNING_DATI_CALCOLO_CF_MANCANTI = "msg.info.daticalcolocfmancanti";
    private final String MSG_CF_COMUNE_ERRATO = "msg.cf.comune.errato";
    private final String MSG_DAPERFEZIONARE_DATI_MANCANTI = "msg.daperfezionare.datimancanti";
    private final String MSG_ERROR_IMPORT_EXCEL = "msg.error.importexcelcampagne";
    private final String MSG_ERROR_ROW_EXCEL = "msg.error.rowexcelcampagne";
    private final String MSG_INVALID_SIZE_FILE = "msg.warning.invalidsizefile";
    private final String MSG_ADD_APPUNTAMENTO_SUCCESS = "msg.addappuntamento.success";
    private final String MSG_APPUNTAMENTO_DATIMANCANTI = "msg.appuntamento.datimancanti";
    private final String MSG_APPUNTAMENTO_DATIMANCANTI2 = "msg.appuntamento.datimancanti2";
    private final String MSG_DELETE_APPUNTAMENTO_SUCCESS = "msg.deleteappuntamento.success";
    private final String MSG_VALIDATE_APPUNTAMENTO_ERROR = "msg.appuntamento.validate.error";
    private final String MSG_NOTIFICA_SMS_OPERATORE_APPUNTAMENTO_ERROR = "msg.appuntamento.notificasmsoperatore.error";
    private final String MSG_NOTIFICA_SMS_CLIENTE_NOMINATIVO_APPUNTAMENTO_ERROR = "msg.appuntamento.notificasmsclientenominativo.error";
    private final String MSG_NOTIFICA_SMS_OPERATORE_APPUNTAMENTO_SUCCESS = "msg.appuntamento.notificasmsoperatore.success";
    private final String MSG_NOTIFICA_SMS_CLIENTE_NOMINATIVO_APPUNTAMENTO_SUCCESS = "msg.appuntamento.notificasmsclientenominativo.success";
    private final String MSG_SMS_FILLABLE_ERROR_SPECIAL_CHARACTER = "msg.sms.fillable.error.specialcharacter";
    private final String MSG_NOTIFICA_SMS_OPERATORE_APPUNTAMENTO_SEND_ERROR = "msg.appuntamento.notificasmsoperatore.senderror";
    private final String MSG_NOTIFICA_SMS_CLIENTE_NOMINATIVO_APPUNTAMENTO_SEND_ERROR = "msg.appuntamento.notificasmsclientenominativo.senderror";
    private final String MSG_SEND_CAMPAGNA_SMS_NON_SPEDITI_SUCCESS = "msg.sms.inviosmsnonspeditisuccess";
    private final String MSG_READ_APPUNTAMENTO_UNAUTHORIZED = "msg.readappuntamento.unauthorized";
    private final String MSG_FILE_EXCEL_TO_IMPORT_INVALID = "msg.importazione.excel.filenotvalid.exceeded";
    private final String MSG_AT_LEAST_ONE_OPERATOR_RECEIVE_LEAD_SELECTED = "msg.at.least.one.operator.receive.lead.selected";
    private final String INFOBENESTARE_MSG_NAME_SMS = "inforicevutabenestaresms";
    private final String COPIABUSTAPAGA_MSG_NAME_SMS = "richiestacopiabustapagasms";
    private final String MSG_BACKUP_SUCCESS = "msg.backup.success";
    private final String MSG_BACKUP_DOWNLOAD_FAILED = "msg.backup.download.failed";
    private final String MSG_BACKUP_NOT_POSSIBLE = "msg.backup.notpossible";
    private final String MSG_BACKUP_PASSWORD_WRONG = "msg.backup.password.wrong";
    private final String MSG_SIMULATOR_ACTIVATED_SUCCESS = "msg.simulator.activated";
    private final String MSG_SIMULATOR_DISABLED_SUCCESS = "msg.simulator.disabled";
    private final String MSG_SIMULATOR_CLONE_SUCCESS = "msg.simulator.clone";
    private final String MSG_SIMULATOR_DELETE_SUCCESS = "msg.simulator.delete";
    private final String MSG_SIMULATOR_NEWROW_EXISTLENGHT = "msg.simulator.newrow.existlenght";
    private final String MSG_SIMULATOR_MENSILITA_ZERO = "msg.simulator.mensilita.zero";
    private final Properties messagesProperties;
    private final Properties messagesMailProperties;
    private final Properties linkProperties;

    public PropertiesUtil() {
        logger.info("PROPERTIES CREATED");
        messagesProperties = loadProperties(MESSAGES_PROPERTIES);
        messagesMailProperties = loadProperties(MESSAGES_MAIL_PROPERTIES);
        linkProperties = loadProperties(LINK_PROPERTIES);
    }

    public String getMsgErrorMailRecipientMissing() {
        return messagesProperties.getProperty(MSG_ERROR_MAIL_RECIPIENT_MISSING);
    }

    public String getMsgErrorMailAgencyNameMissing() {
        return messagesProperties.getProperty(MSG_ERROR_MAIL_AGENCY_NAME_MISSING);
    }

    public String getMsgErrorMailReplyToMissing() {
        return messagesProperties.getProperty(MSG_ERROR_MAIL_REPLY_TO_MISSING);
    }

    public String getMsgErrorMailSubjectMissing() {
        return messagesProperties.getProperty(MSG_ERROR_MAIL_SUBJECT_MISSING);
    }

    public String getMsgErrorMailBodyMissing() {
        return messagesProperties.getProperty(MSG_ERROR_MAIL_BODY_MISSING);
    }

    private Properties loadProperties(final String filename) {
        final Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(filename));
        } catch (final IOException e) {
            logger.error("Unable to load properties", e);
        }
        return properties;
    }

    public String getDatiFornitiMsgValue() {
        return messagesMailProperties.getProperty(DATIFORNITI_MSG_NAME);
    }

    public String getInformativaPreventivoMsgValue() {
        return messagesMailProperties.getProperty((INFORMATIVAPREVENTIVO_MSG_NAME));
    }

    public String getPreventivoMsgValue() {
        return messagesMailProperties.getProperty(PREVENTIVO_MSG_NAME);
    }

    public String getPreventivoMsgSMSValue() {
        return messagesMailProperties.getProperty(PREVENTIVO_MSG_NAME_SMS);
    }

    public String getInfoDocMsgValue() {
        return messagesMailProperties.getProperty(INFODOC_MSG_NAME);
    }

    public String getDatiClienteMsgValue() {
        return messagesMailProperties.getProperty(DATICLIENTE_MSG_NAME);
    }

    public String getInfoUlteriori1MsgValue() {
        return messagesMailProperties.getProperty(INFOULTERIORI1_MSG_NAME);
    }

    public String getInfoBenestareMsgValue() {
        return messagesMailProperties.getProperty(INFOBENESTARE_MSG_NAME);
    }

    public String getInfoBenestareSMSMsgValue() {
        return messagesMailProperties.getProperty(INFOBENESTARE_MSG_NAME_SMS);
    }

    public String getBustapagaMsgValue() {
        return messagesMailProperties.getProperty(COPIABUSTAPAGA_MSG_NAME);
    }

    public String getBustapagaSMSMsgValue() {
        return messagesMailProperties.getProperty(COPIABUSTAPAGA_MSG_NAME_SMS);
    }

    public String getOggettoMailPreventivoValue() {
        return messagesMailProperties.getProperty(OGGETTOPREVENTIVO_MSG_NAME);
    }

    public String getOggettoMailBustapagaValue() {
        return messagesMailProperties.getProperty(OGGETTOBUSTAPAGA_MSG_NAME);
    }

    public String getLinkDocumentiValue() {
        return linkProperties.getProperty(CONTROLLODOCUMENTO_LINK);
    }

    public String getLinkIbanValue() {
        return linkProperties.getProperty(CONTROLLOIBAN_LINK);
    }

    public String getLinkComparatoreValue() {
        return linkProperties.getProperty(COMPARATORE_LINK);
    }

    public String getMsgBlockedIp() {
        return messagesProperties.getProperty(MSG_BLOCKED_IP);
    }

    public String getMsgInvalidLogin() {
        return messagesProperties.getProperty(MSG_INVALID_LOGIN);
    }

    public String getMsgDataSourceSaved() {
        return messagesProperties.getProperty(MSG_DATASOURCE_SAVED);
    }

    public String getMsgDataSourceDeleted() {
        return messagesProperties.getProperty(MSG_DATASOURCE_DELETED);
    }

    public String getMsgLoginOperatorDeleted() {
        return messagesProperties.getProperty(MSG_LOGINOPERATOR_DELETED);
    }

    public String getMsgLoginOperatorSaved() {
        return messagesProperties.getProperty(MSG_LOGINOPERATOR_SAVED);
    }

    public Properties getPasswordMessagesProperties() {
        return loadProperties(PASSWORD_MESSAGES_PROPERTIES);
    }

    public String getMsgAttenzione() {
        return messagesProperties.getProperty(MSG_ATTENZIONE);
    }

    public String getMsgErrorStessaAmministrazione() {
        return messagesProperties.getProperty(MSG_ERROR_STESSA_AMMINISTRAZIONE);
    }

    public String getMsgErrorMaxAmministrazioni() {
        return messagesProperties.getProperty(MSG_ERROR_MAX_AMMINISTRAZIONI);
    }

    public String getMsgNominativoDeleted() {
        return messagesProperties.getProperty(MSG_NOMINATIVO_DELETED);
    }

    public String getMsgErrorPdfNonGenerato() {
        return messagesProperties.getProperty(MSG_ERROR_PDF_NON_GENERATO);
    }

    public String getMsgDatiNominativoMancanti() {
        return messagesProperties.getProperty(MSG_WARNING_DATI_NOMINATIVO_MANCANTI);
    }

    public String getMsgAccessoNonConsentito() {
        return messagesProperties.getProperty(MSG_WARNING_ACCESSO_NON_CONSENTITO);
    }

    public String getMsgAccessoNonConsentitoSimulator() {
        return messagesProperties.getProperty(MSG_WARNING_ACCESSO_NON_CONSENTITO_SIMULATOR);
    }

    public String getMsgInvalidFile() {
        return messagesProperties.getProperty(MSG_WARNING_INVALID_FILE);
    }

    public String getMsgPratichePerfezionate() {
        return messagesProperties.getProperty(MSG_PRATICHE_PERFEZIONATE);
    }

    public String getMsgErrorPerfezionate() {
        return messagesProperties.getProperty(MSG_ERROR_PERFEZIONATE);
    }

    public String getMsgSubjectProforma() {
        return messagesMailProperties.getProperty(MSG_SUBJECT_PROFORMA);
    }

    public String getMsgBodyProforma() {
        return messagesMailProperties.getProperty(MSG_BODY_PROFORMA);
    }

    public String getMsgErrorEditDecorrenza() {
        return messagesProperties.getProperty(MSG_ERROR_EDIT_DECORRENZA);
    }

    //usato in fileContent.xhtml ma non lo trova, come gli altri
    public String getMsgInvalidFileSize() {
        return MessageFormat.format(messagesProperties.getProperty(MSG_ERROR_INVALID_FILE_SIZE), ConstantResolverView.PDF_SIZE_LIMIT / 1024 / 1024);
    }

    public String getMsgInvalidFileSizeNoticeBoard() {
        return MessageFormat.format(messagesProperties.getProperty(MSG_ERROR_INVALID_FILE_SIZE), NoticeBoardView.PDF_SIZE_LIMIT / 1024 / 1024);
    }

    public String getMsgDeleteSuccess() {
        return messagesProperties.getProperty(MSG_DELETE_SUCCESS);
    }

    public String getMsgDeleteCfNotFound() {
        return messagesProperties.getProperty(MSG_DELETE_CF_NOTFOUND);
    }

    public String getMsgDeleteError() {
        return messagesProperties.getProperty(MSG_DELETE_ERROR);
    }

    public String getMsgDeletePraticaSuccess() {
        return messagesProperties.getProperty(MSG_DELETE_PRATICA_SUCCESS);
    }

    public String getMsgFileUploadOk() {
        return messagesProperties.getProperty(MSG_FILE_UPLOAD_OK);
    }

    public String getMsgFileUploadError() {
        return messagesProperties.getProperty(MSG_FILE_UPLOAD_ERROR);
    }

    public String getMsgFileUploadNoSpace() {
        return messagesProperties.getProperty(MSG_FILE_UPLOAD_NOSPACE);
    }

    public String getMsgFileUploadTooBig() {
        return messagesProperties.getProperty(MSG_FILE_UPLOAD_FILETOOBIG);
    }

    public String getMsgFileUploadAntiriciclaggioFileLimit() {
        return messagesProperties.getProperty(MSG_FILE_UPLOAD_ANTIRICICLAGGIO_FILELIMIT);
    }

    public String getMsgFileViewNoFileFound() {
        return messagesProperties.getProperty(MSG_FILE_VIEW_NOFILEFOUND);
    }

    public String getMsgFileViewDeleted() {
        return messagesProperties.getProperty(MSG_FILE_VIEW_DELETED);
    }

    public String getMsgFileViewDeleteError() {
        return messagesProperties.getProperty(MSG_FILE_VIEW_DELETEERROR);
    }

    public String getMsgSuccess() {
        return messagesProperties.getProperty(MSG_SUCCESS);
    }

    public String getMsgAziendaError() {
        return messagesProperties.getProperty(MSG_AZIENDA_ERROR);
    }

    public String getMsgAgenziaNotFound() {
        return messagesProperties.getProperty(MSG_AGENZIA_NOTFOUND);
    }

    public String getMsgFunctionalityNotEnabledSummary() {
        return messagesProperties.getProperty(MSG_FUNCTIONALITY_NOT_ENABLED_SUMMARY);
    }

    public String getMsgFunctionalityNotEnabledMessage() {
        return messagesProperties.getProperty(MSG_FUNCTIONALITY_NOT_ENABLED_MESSAGE);
    }

    public String getMsgFunctionalityNotEnabledFull() {
        return getMsgFunctionalityNotEnabledSummary() + " " + getMsgFunctionalityNotEnabledMessage();
    }

    public String getMsgCreaPraticaSuccess() {
        return messagesProperties.getProperty(MSG_CREAPRATICA_SUCCESS);
    }

    public String getMsgCreaPraticaError() {
        return messagesProperties.getProperty(MSG_CREAPRATICA_ERROR);
    }

    public String getMsgCreaPraticaLimitEstinzioni() {
        return messagesProperties.getProperty(MSG_CREAPRATICA_LIMIT_ESTINZIONI);
    }

    public String getMsgErroreImprevisto() {
        return messagesProperties.getProperty(MSG_ERRORE_IMPREVISTO);
    }

    public String getMsgErrorEditStatoPratica() {
        return messagesProperties.getProperty(MSG_ERROR_EDIT_STATO_PRATICA);
    }

    public String getMsgSuccessEditStatoPratica() {
        return messagesProperties.getProperty(MSG_SUCCESS_EDIT_STATO_PRATICA);
    }

    public String getMsgSuccessEditStatoConteggioEstinzione() {
        return messagesProperties.getProperty(MSG_SUCCESS_EDIT_STATO_CONTEGGIO_ESTINZIONE);
    }

    public String getMsgErrorEditStatoConteggioEstinzione() {
        return messagesProperties.getProperty(MSG_ERROR_STATO_CONTEGGIO_ESTINZIONE);
    }

    public String getMsgPraticaCannotDelete() {
        return messagesProperties.getProperty(MSG_PRATICA_CANNOT_DELETE);
    }

    public String getMsgInfo() {
        return messagesProperties.getProperty(MSG_INFO);
    }

    public String getMsgError() {
        return messagesProperties.getProperty(MSG_ERROR);
    }

    public String getMsgSuccessEditDataNotifica() {
        return messagesProperties.getProperty(MSG_SUCCESS_EDIT_DATA_NOTIFICA);
    }

    public String getMsgSuccessEditDataNotificaConteggioEstinzione() {
        return messagesProperties.getProperty(MSG_SUCCESS_EDIT_DATA_NOTIFICA_CONTEGGIO_ESTINZIONE);
    }

    public String getMsgErrorDecimalValue() {
        return messagesProperties.getProperty(MSG_ERROR_DECIMAL_VALUE);
    }

    public String getMsgEnableIlComparatoreLeadReception() {
        return messagesProperties.getProperty(MSG_ENABLE_IL_COMPARATORE_LEAD_RECEPTION);
    }

    public String getMsgErrorEnableIlComparatoreLeadReception() {
        return messagesProperties.getProperty(MSG_ERROR_ENABLE_IL_COMPARATORE_LEAD_RECEPTION);
    }

    public String getMsgErrorSendMail() {
        return messagesProperties.getProperty(MSG_ERROR_SEND_MAIL);
    }

    public String getMsgSendMailOk() {
        return messagesProperties.getProperty(MSG_SEND_MAIL_OK);
    }

    public String getMsgErrorCampiRegistrazioneComparatoreObbligatori() {
        return messagesProperties.getProperty(MSG_ERROR_CAMPI_REGISTRAZIONE_COMPARATORE_OBBLIGATORI);
    }

    public String getMsgErrorGeneric() {
        return messagesProperties.getProperty(MSG_ERROR_GENERIC);
    }

    public String getMsgSettingsSaveSuccess() {
        return messagesProperties.getProperty(MSG_SETTINGS_SAVE_SUCCESS);
    }

    public String getMsgSmsValidationError() {
        return messagesProperties.getProperty(MSG_SMS_VALIDATION_ERROR);
    }

    public String getMsgSmsMinLengthError() {
        return messagesProperties.getProperty(MSG_SMS_MIN_LENGTH_ERROR);
    }

    public String getMsgSmsMaxLengthError() {
        return messagesProperties.getProperty(MSG_SMS_MAX_LENGTH_ERROR);
    }

    public String getMsgSmsSenderMaxLengthError() {
        return messagesProperties.getProperty(MSG_SMS_SENDER_MAX_LENGTH_ERROR);
    }

    public String getMsgSmsSenderInvalidCharacter() {
        return messagesProperties.getProperty(MSG_SMS_SENDER_INVALID_CHARACTER);
    }

    public String getMsgSmsTopupInvalidChoice() {
        return messagesProperties.getProperty(MSG_SMS_TOPUP_INVALID_CHOICE);
    }

    public String getMsgSmsTopupPaymentSuccess() {
        return messagesProperties.getProperty(MSG_SMS_TOPUP_PAYMENT_SUCCESS);
    }

    public String getMsgSmsTopupPaymentFailed() {
        return messagesProperties.getProperty(MSG_SMS_TOPUP_PAYMENT_FAILED);
    }

    public String getMsgDatiAziendaMancanti() {
        return messagesProperties.getProperty(MSG_SMS_DATI_AZIENDA_MANCANTI);
    }

    public String getMsgNumberOnly() {
        return messagesProperties.getProperty(MSG_NUMBER_ONLY);
    }

    public String getMsgDatiCalcoloCfMancanti() {
        return messagesProperties.getProperty(MSG_WARNING_DATI_CALCOLO_CF_MANCANTI);
    }

    public String getMsgCfComuneErrato() {
        return messagesProperties.getProperty(MSG_CF_COMUNE_ERRATO);
    }

    public String getMsgDaPerfezionareDatiMancanti() {
        return messagesProperties.getProperty(MSG_DAPERFEZIONARE_DATI_MANCANTI);
    }

    public String getMsgCfErrorGiaUsato() {
        return messagesProperties.getProperty(MSG_CF_ERROR_GIAUSATO);
    }

    public String getMsgSmsErrorNotEnoughBalance() {
        return messagesProperties.getProperty(MSG_SMS_ERROR_NOT_ENOUGH_BALANCE);
    }

    public String getMsgSmsErrorPriceCalculation() {
        return messagesProperties.getProperty(MSG_SMS_ERROR_PRICE_CALCULATION);
    }

    public String getMsgSmsInvioCampagnaSuccess() {
        return messagesProperties.getProperty(MSG_SMS_INVIO_CAMPAGNA_SUCCESS);
    }

    public String getMsgErrorImportExcel() {
        return messagesProperties.getProperty(MSG_ERROR_IMPORT_EXCEL);
    }

    public String getMsgErrorRowExcel(final String numberRowError) {
        return MessageFormat.format(messagesProperties.getProperty(MSG_ERROR_ROW_EXCEL), numberRowError);
    }

    public String getMsgInvalidSizeFile() {
        return messagesProperties.getProperty(MSG_INVALID_SIZE_FILE);
    }

    public String getMsgAddAppuntamentoSuccess() {
        return messagesProperties.getProperty(MSG_ADD_APPUNTAMENTO_SUCCESS);
    }

    public String getMsgAppuntamentoDatiMancanti() {
        return messagesProperties.getProperty(MSG_APPUNTAMENTO_DATIMANCANTI);
    }

    public String getMsgAppuntamentoDatiMancanti2() {
        return messagesProperties.getProperty(MSG_APPUNTAMENTO_DATIMANCANTI2);
    }

    public String getMsgDeleteAppuntamentoSuccess() {
        return messagesProperties.getProperty(MSG_DELETE_APPUNTAMENTO_SUCCESS);
    }

    public String getMsgScheduleEventMovedSuccess() {
        return messagesProperties.getProperty(MSG_SCHEDULE_EVENTMOVED_SUCCESS);
    }

    public String getMsgAppuntamentoValidateError() {
        return messagesProperties.getProperty(MSG_VALIDATE_APPUNTAMENTO_ERROR);
    }

    public String getMsgAppuntamentoClienteNominativoNotificaSmsError() {
        return messagesProperties.getProperty(MSG_NOTIFICA_SMS_CLIENTE_NOMINATIVO_APPUNTAMENTO_ERROR);
    }

    public String getMsgAppuntamentoOperatoreNotificaSmsError() {
        return messagesProperties.getProperty(MSG_NOTIFICA_SMS_OPERATORE_APPUNTAMENTO_ERROR);
    }

    public String getMsgAppuntamentoClienteNominativoNotificaSmsSendError() {
        return messagesProperties.getProperty(MSG_NOTIFICA_SMS_CLIENTE_NOMINATIVO_APPUNTAMENTO_SEND_ERROR);
    }

    public String getMsgAppuntamentoOperatoreNotificaSmsSendError() {
        return messagesProperties.getProperty(MSG_NOTIFICA_SMS_OPERATORE_APPUNTAMENTO_SEND_ERROR);
    }

    public String getMsgAppuntamentoOperatoreNotificaSmsSuccess() {
        return messagesProperties.getProperty(MSG_NOTIFICA_SMS_OPERATORE_APPUNTAMENTO_SUCCESS);
    }

    public String getMsgAppuntamentoClienteNominativoNotificaSmsSuccess() {
        return messagesProperties.getProperty(MSG_NOTIFICA_SMS_CLIENTE_NOMINATIVO_APPUNTAMENTO_SUCCESS);
    }

    public String getMsgSmsFillableErrorSpecialCharacter() {
        return messagesProperties.getProperty(MSG_SMS_FILLABLE_ERROR_SPECIAL_CHARACTER);
    }

    public String getMsgSmsTextRequired() {
        return messagesProperties.getProperty(MSG_SMS_TEXT_REQUIRED);
    }

    public String getMsgImportazioneStart() {
        return messagesProperties.getProperty(MSG_IMPORTAZIONE_START);
    }

    public String getMsgImportExcelRowsExceeded() {
        return MessageFormat.format(messagesProperties.getProperty(MSG_IMPORTAZIONE_EXCEL_ROWS_EXCEEDED),
                ApachePOIExcelRead.MAX_EXCEL_ROWS_IMPORT);
    }

    public String getMsgSmsUploadFilelimit() {
        return messagesProperties.getProperty(MSG_SMS_UPLOAD_FILELIMIT);
    }

    public String getMsgSendCampagnaSmsNonSpediti() {
        return messagesProperties.getProperty(MSG_SEND_CAMPAGNA_SMS_NON_SPEDITI_SUCCESS);
    }

    public String getMsgReadAppuntamentoUnauthorized() {
        return messagesProperties.getProperty(MSG_READ_APPUNTAMENTO_UNAUTHORIZED);
    }

    public String getMsgFileExcelToImportNotValid() {
        return messagesProperties.getProperty(MSG_FILE_EXCEL_TO_IMPORT_INVALID);
    }

    public String getMsgAtLeastOneOperatorReceiveLeadSelected() {
        return messagesProperties.getProperty(MSG_AT_LEAST_ONE_OPERATOR_RECEIVE_LEAD_SELECTED);
    }

    public String getMsgSmsSentSuccess() {
        return messagesProperties.getProperty(MSG_SMS_SENT_SUCCESS);
    }

    public String getMsgErrorSendSmsGeneric() {
        return messagesProperties.getProperty(MSG_ERROR_SEND_SMS_GENERIC);
    }

    public String getMsgErrorSmsMissingFields() {
        return messagesProperties.getProperty(MSG_ERROR_SMS_MISSING_FIELDS);
    }

    public String getMsgBackupSuccess() {
        return messagesProperties.getProperty(MSG_BACKUP_SUCCESS);
    }

    public String getMsgBackupDownloadFailed() {
        return messagesProperties.getProperty(MSG_BACKUP_DOWNLOAD_FAILED);
    }

    public String getMsgBackupNotPossible() {
        return messagesProperties.getProperty(MSG_BACKUP_NOT_POSSIBLE);
    }

    public String getMsgBackupPasswordWrong() {
        return messagesProperties.getProperty(MSG_BACKUP_PASSWORD_WRONG);
    }

    public String getMsgErrorColumnTruncated() {
        return messagesProperties.getProperty(MSG_ERROR_COLUMN_TRUNCATED);
    }

    public String getMsgSimulatorActivatedSuccess() {
        return messagesProperties.getProperty(MSG_SIMULATOR_ACTIVATED_SUCCESS);
    }

    public String getMsgSimulatorDisabledSuccess() {
        return messagesProperties.getProperty(MSG_SIMULATOR_DISABLED_SUCCESS);
    }

    public String getMsgSimulatorCloneSuccess() {
        return messagesProperties.getProperty(MSG_SIMULATOR_CLONE_SUCCESS);
    }

    public String getMsgSimulatorDeleteSuccess() {
        return messagesProperties.getProperty(MSG_SIMULATOR_DELETE_SUCCESS);
    }

    public String getMsgSimulatorNewRowExistLenght() {
        return messagesProperties.getProperty(MSG_SIMULATOR_NEWROW_EXISTLENGHT);
    }

    public String getMsgSimulatorMensilitaZero() {
        return messagesProperties.getProperty(MSG_SIMULATOR_MENSILITA_ZERO);
    }

    public String getMsgSimulatorErrorTan() {
        return messagesProperties.getProperty(MSG_SIMULATOR_ERROR_TAN);
    }

    public String getMsgSimulatorErrorTableInUse() {
        return messagesProperties.getProperty(MSG_SIMULATOR_ERROR_TABLE_IN_USE);
    }

    public String getMsgSimulatorErrorRunMissingFields() {
        return messagesProperties.getProperty(MSG_SIMULATOR_ERROR_RUN_MISSINGFIELDS);
    }

    public String getMsgSimulatorErrorEmptyJobTypeList() {
        return messagesProperties.getProperty(MSG_SIMULATOR_ERROR_EMPTY_JOB_TYPE_LIST);
    }

    public String getMsgSimulatorDisclaimer() {
        return messagesProperties.getProperty(MSG_SIMULATOR_DISCLAIMER);
    }

    public String getMsgSimulatorSavedTable() {
        return messagesProperties.getProperty(MSG_SIMULATOR_SAVED_TABLE);
    }

    public String getStorageSpaceChartTitle() {
        return messagesProperties.getProperty(STORAGE_SPACE_CHART_TITLE);
    }

    public String getStorageSpaceNeedMoreSpace() {
        return messagesProperties.getProperty(STORAGE_SPACE_NEED_MORE_SPACE);
    }

    public String getMsgTableNameAlreadyUsed() {
        return messagesProperties.getProperty(MSG_ERROR_TABLE_NAME_ALREADY_USED);
    }

    public String getMsgSimulatorTableSavedOnlyOperators() {
        return messagesProperties.getProperty(MSG_SIMULATOR_TABLE_SAVED_ONLY_OPERATORS);
    }

    public String getMsgBackupSubject() {
        return messagesMailProperties.getProperty(MSG_BACKUP_SUBJECT);
    }

    public String getMsgBackupBody() {
        return messagesMailProperties.getProperty(MSG_BACKUP_BODY);
    }

    public String getMsgBackupReplyto() {
        return messagesMailProperties.getProperty(MSG_BACKUP_REPLYTO);
    }

    public String getMsgInviteFriendsSubject() {
        return messagesMailProperties.getProperty(MSG_INVITE_FRIENDS_SUBJECT);
    }

    public String getMsgInviteFriendsBody() {
        return messagesMailProperties.getProperty(MSG_INVITE_FRIENDS_BODY);
    }

    public String getMsgConfirmUploadFile() {
        return messagesProperties.getProperty(MSG_CONFIRM_UPLOAD_FILE);
    }

    public String getMsgDeleteNoticeSuccess() {
        return messagesProperties.getProperty(MSG_DELETE_NOTICE_SUCCESS);
    }

    public String getMsgInvalidFilePdf() {
        return messagesProperties.getProperty(MSG_INVALID_FILE_PDF);
    }

    public String getMsgUploadOneFileLimit() {
        return messagesProperties.getProperty(MSG_UPLOAD_ONE_FILE_LIMIT);
    }

    public String getMsgAdvancedSearchEmptyFilter() {
        return messagesProperties.getProperty(MSG_ADVANCED_SEARCH_EMPTY_FILTER);
    }

    public String getMsgAdvancedSearchDataWrong() {
        return messagesProperties.getProperty(MSG_ADVANCED_SEARCH_DATA_WRONG);
    }
}
