package com.woonders.lacemscommon.config;

/**
 * Created by emanuele on 02/01/17.
 */
public class Constants {

    public static final String REPOSITORY_PACKAGE = "com.woonders.lacemscommon.db.repository";
    public static final String APP_DOMAIN = System.getProperty("APP_DOMAIN");
    public static final String EMPTY_DEFAULT_DB_NAME = "emptydefault";
    public static final String TENANT_NAME_LOG_KEY = "tenantName";
    public static final int SLOT_MINUTES_SCHEDULE = 30;
    public static final String SELECT_ONE_MENU_AZIENDA_NAME = "topbarform:aziendaDaVisualizzareSelect";
    public static final String IL_COMPARATORE_LEAD_ACTIVATION_EMAIL = System
            .getProperty("COMPARATORE_LEAD_ACTIVATION_EMAIL", "lace.application@gmail.com");
    public static final String EXCEPTION_ERROR = "exceptionError";
    public static final String APP_WORKING_DIR = System.getProperty("APP_WORKING_DIR");
    public static final boolean HTTPS_ENABLED = Boolean.parseBoolean(System.getProperty("HTTPS_ENABLED"));
    public static final int MAX_LOGIN_ATTEMPTS = 100;
    // http://stackoverflow.com/questions/11277366/what-is-the-difference-between-redirect-and-navigation-forward-and-when-to-use-w
    private static final String REDIRECT = "?faces-redirect=true";
    private static final String APP_PATH = "/app";
    private static final String SECURE_PATH = APP_PATH + "/secure";
    private static final String SYSADMIN_PATH = SECURE_PATH + "/sysadmin";
    private static final String ADMIN_PATH = "/admin";
    private static final String AMMINISTRAZIONI_PATH = SECURE_PATH + "/amministrazioni";
    private static final String ANTIRICICLAGGIO_PATH = SECURE_PATH + "/antiriciclaggio/antiriciclaggio.xhtml";
    private static final String AZIENDA_PATH = SECURE_PATH + "/azienda";
    private static final String CHANGEPSW_PATH = SECURE_PATH + "/changepsw";
    private static final String CLIENTI_PATH = SECURE_PATH + "/clienti";
    private static final String DATI_PERSONALI_PATH = SECURE_PATH + "/datipersonali";
    private static final String FATTURAZIONE_PATH = SECURE_PATH + "/fatturazione";
    private static final String MARKETING_PATH = SECURE_PATH + "/marketing";
    private static final String NOMINATIVI_PATH = SECURE_PATH + "/nominativi";
    private static final String RICARICA_PATH = SECURE_PATH + "/ricarica";
    private static final String SEARCH_PATH = SECURE_PATH + "/search";
    private static final String SETTINGS_PATH = SECURE_PATH + "/settings";
    private static final String DELETE_KEY_PATH = "/delete";
    private static final String DATATABLE_ANTIRICICLAGGIO_PATH = SECURE_PATH
            + "/antiriciclaggio/datatableantiriciclaggio.xhtml";
    private static final String NUOVA_PRATICA_CHECK_CF_PATH = CLIENTI_PATH + "/checkcf.xhtml";
    private static final String SEARCH_CLIENTE_NOMINATIVO_PATH = SEARCH_PATH + "/search.xhtml";
    private static final String DELETE_CLIENTE_PRATICA_PATH = CLIENTI_PATH + DELETE_KEY_PATH + "/delete.xhtml";
    private static final String FATTURAZIONE_INDEX_PATH = FATTURAZIONE_PATH + "/fatturazione.xhtml";
    private static final String NUOVA_AMMINISTRAZIONE_CHECK_RAGIONE_SOCIALE_PATH = AMMINISTRAZIONI_PATH
            + "/checkRagSociale.xhtml";
    private static final String SEARCH_AMMINISTRAZIONE_PATH = AMMINISTRAZIONI_PATH + "/searchAmministrazione.xhtml";
    private static final String DASHBOARD_NOMINATIVO_PATH = NOMINATIVI_PATH + "/dashboardNominativo.xhtml";
    private static final String NUOVO_NOMINATIVO_PATH = NOMINATIVI_PATH + "/checkNominativo.xhtml";
    
    private static final String OPERATORE_PATH = SECURE_PATH + "/utenti";
    private static final String NUOVO_OPERATORE_PATH = OPERATORE_PATH + "/nuovoOperatore.xhtml";
    private static final String ELIMINA_OPERATORE_PATH = OPERATORE_PATH + "/eliminaOperatore.xhtml";
    
    private static final String SYSADMIN_DASHBOARD_PATH = SYSADMIN_PATH + "/dashboard.xhtml";
    private static final String MANAGE_USERS_PATH = SYSADMIN_PATH + "/manage_users.xhtml";
    private static final String MANAGE_ROLES_PATH = SYSADMIN_PATH + "/manage_roles.xhtml";
    private static final String EMPTY_STRING = "";
    private static final String NEW_LINE = "(\r\n|\n)";
    private static final String CHOOSE_DOMAIN_PATH = APP_PATH + "/choosedomain.xhtml";
    private static final String STATO_PRATICHE_CESSIONI_DELEGHE_PATH = CLIENTI_PATH + "/statopratichecessionideleghe.xhtml";
    private static final String STATO_PRATICHE_PRESTITI_PATH = CLIENTI_PATH + "/statopraticheprestiti.xhtml";
    private static final String DATATABLE_STATO_PRATICA_PATH = CLIENTI_PATH + "/datatablestatopratica.xhtml";
    private static final String DATATABLE_STATO_CONTEGGIO_ESTINZIONE_PATH = CLIENTI_PATH + "/datatablestatoconteggioestinzione.xhtml";
    private static final String NUOVA_CAMPAGNA_MARKETING_PATH = MARKETING_PATH + "/nuovacampagnamarketing.xhtml";
    private static final String INFO_CAMPAGNE_MARKETING_PATH = MARKETING_PATH + "/infocampagne.xhtml";
    private static final String DETTAGLIO_CAMPAGNA_MARKETING_PATH = MARKETING_PATH
            + "/dettagliocampagnamarketing.xhtml";
    private static final String SETTINGS_CAMPAGNE_MARKETING_PATH = MARKETING_PATH + "/settingscampagnemarketing.xhtml";
    private static final String ANTIRICICLAGGIO_ADMIN_PATH = SECURE_PATH
            + "/antiriciclaggio/antiriciclaggio_admin.xhtml";
    private static final String DATATABLE_ANTIRICICLAGGIO_ADMIN_PATH = SECURE_PATH
            + "/antiriciclaggio/datatableantiriciclaggio_admin.xhtml";
    private static final String DATI_PERSONALI_INDEX_PATH = DATI_PERSONALI_PATH + "/dat.xhtml";
    private static final String PREFERENZE_INDEX_PATH = SETTINGS_PATH + "/preferenze.xhtml";
    private static final String CAMBIO_PASSWORD_INDEX_PATH = CHANGEPSW_PATH + "/psw.xhtml";
    private static final String CAMBIO_PASSWORD_ANTIRICICLAGGIO_INDEX_PATH = SECURE_PATH
            + "/antiriciclaggio/changePw.xhtml";
    private static final String RICARICA_SMS_PATH = RICARICA_PATH + "/ricarica_sms.xhtml";
    private static final String PDF_VIEW_PATH = SECURE_PATH + "/pdfview.xhtml";
    private static final String STATO_NOMINATIVI_PATH = NOMINATIVI_PATH + "/statonominativi.xhtml";
    private static final String DATATABLE_STATO_NOMINATIVO_PATH = NOMINATIVI_PATH + "/datatablestatonominativo.xhtml";
    private static final String SESSION_EXPIRED_PATH = "/session-expired.xhtml";
    private static final String GESTIONE_PERMESSI_PATH = SECURE_PATH + "/gestionepermessi";
    private static final String GESTIONE_PERMESSI_INDEX_PATH = GESTIONE_PERMESSI_PATH + "/gestionepermessi.xhtml";
    private static final String BACKUP_PATH = SECURE_PATH + "/backup";
    private static final String ACCESS_LOG_PATH = SECURE_PATH + "/accesslog";
    private static final String SIMULATOR_PATH = SECURE_PATH + "/simulator";
    private static final String BACKUP_INDEX_PATH = BACKUP_PATH + "/backup.xhtml";
    private static final String ACCESS_LOG_INDEX_PATH = ACCESS_LOG_PATH + "/accesslog.xhtml";
    private static final String NEW_SIMULATOR_TABLE_PATH = SIMULATOR_PATH + "/createnewtable.xhtml";
    private static final String SIMULATOR_TABLE_LIST_PATH = SIMULATOR_PATH + "/simulatortablelist.xhtml";
    private static final String RUN_SIMULATOR_PATH = SIMULATOR_PATH + "/runsimulator.xhtml";
    private static final String INVITE_FRIENDS_PATH = SECURE_PATH + "/invitefriends";
    private static final String INVITE_FRIENDS_INDEX_PATH = INVITE_FRIENDS_PATH + "/invitefriends.xhtml";
    private static final String INFO_ACCOUNT_PATH = SECURE_PATH + "/infoaccount";
    private static final String INFO_ACCOUNT_INDEX_PATH = INFO_ACCOUNT_PATH + "/infoaccount.xhtml";
    private static final String ANALYTICS_PATH = SECURE_PATH + "/analytics";
    private static final String ANALYTICS_CLIENTI_INDEX_PATH = ANALYTICS_PATH + "/analyticsclienti.xhtml";
    private static final String ANALYTICS_NOMINATIVI_INDEX_PATH = ANALYTICS_PATH + "/analyticsnominativi.xhtml";
    private static final String NOTICE_BOARD_PATH = SECURE_PATH + "/noticeboard";
    private static final String NOTICE_BOARD_INDEX_PATH = NOTICE_BOARD_PATH + "/noticeboard.xhtml";
    private static final String NOTICE_BOARD_DATATABLE_INDEX_PATH = NOTICE_BOARD_PATH + "/noticeboarddatatable.xhtml";
    private static final String DATATABLE_ADVANCED_SEARCH_CLIENTI_INDEX_PATH = SEARCH_PATH + "/datatableadvancedsearchclienti.xhtml";
    private static final String DATATABLE_ADVANCED_SEARCH_NOMINATIVI_INDEX_PATH = SEARCH_PATH + "/datatableadvancedsearchnominativi.xhtml";

    private static final String LEADS_PATH = SECURE_PATH + "/leads";
    private static final String GESTIONE_LEADS_PATH = LEADS_PATH + "/gestioneleads.xhtml";
    
    private static final String PRICAVY_PATH = SECURE_PATH + "/privacy";
    private static final String GESTIONE_PRICAVYTEMPLATE_PATH = PRICAVY_PATH + "/nuovotemplate.xhtml";
    private static final String GESTIONE_PRICAVYTEMPLATEMOD_PATH = PRICAVY_PATH + "/gestionetemplate.xhtml";

    private static String appendRedirect(final String path, final boolean appendRedirect) {
        if (appendRedirect) {
            return path + REDIRECT;
        } else {
            return path;
        }
    }

    public static String getLoginPath(final boolean appendRedirect) {
        final String path = APP_PATH + "/login.xhtml";
        return appendRedirect(path, appendRedirect);
    }
    
    public static String getResetPasswordPath(final boolean appendRedirect) {
        final String path = APP_PATH + "/resetpassword.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDashboardPath(final boolean appendRedirect) {
        final String path = SECURE_PATH + "/dashboard.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getAziendaPath(final boolean appendRedirect) {
        final String path = AZIENDA_PATH + "/azienda.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getFormsPath(final boolean appendRedirect) {
        final String path = CLIENTI_PATH + "/forms.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatableDashboardNotificationPath(final boolean appendRedirect) {
        final String path = SECURE_PATH + "/datatableDashboardNotification.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatableDashboardNotificationPreventivoPath(final boolean appendRedirect) {
        final String path = SECURE_PATH + "/datatableDashboardNotificationPreventivo.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatableCheckNominativoPath(final boolean appendRedirect) {
        final String path = NOMINATIVI_PATH + "/datatableCheckNominativo.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getFormsNominativoPath(final boolean appendRedirect) {
        final String path = NOMINATIVI_PATH + "/formsNominativo.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatableNominativoRinnoviPath(final boolean appendRedirect) {
        final String path = NOMINATIVI_PATH + "/datatableNominativoRinnovi.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatableNominativoOmessiPath(final boolean appendRedirect) {
        final String path = NOMINATIVI_PATH + "/datatableNominativiOmessi.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDashboardNominativoPath(final boolean appendRedirect) {
        final String path = NOMINATIVI_PATH + "/dashboardNominativo.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getPdfNominativoPath(final boolean appendRedirect) {
        final String path = SECURE_PATH + "/pdfNominativo.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getPdfAmministrazionePath(final boolean appendRedirect) {
        final String path = SECURE_PATH + "/pdfAmm.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getFormsAmministrazionePath(final boolean appendRedirect) {
        final String path = AMMINISTRAZIONI_PATH + "/formsAmministrazione.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDeletePath(final boolean appendRedirect) {
        final String path = CLIENTI_PATH + DELETE_KEY_PATH + "/delete.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getFatturazionePath(final boolean appendRedirect) {
        final String path = FATTURAZIONE_PATH + "/searchFatturazione.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatablePraticheDaPerfezionarePath(final boolean appendRedirect) {
        final String path = FATTURAZIONE_PATH + "/datatablePraticheDaPerfezionare.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatablePratichePerfezionatePath(final boolean appendRedirect) {
        final String path = FATTURAZIONE_PATH + "/datatablePratichePerfezionate.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getPdfModPath(final boolean appendRedirect) {
        final String path = SECURE_PATH + "/pdfMod.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatablePath(final boolean appendRedirect) {
        final String path = SEARCH_PATH + "/datatable.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatableCheckPath(final boolean appendRedirect) {
        final String path = SEARCH_PATH + "/datatablecheck.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatableAmministrazionePath(final boolean appendRedirect) {
        final String path = AMMINISTRAZIONI_PATH + "/datatableAmministrazione.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDeleteDatatablePath(final boolean appendRedirect) {
        final String path = SECURE_PATH + ADMIN_PATH + "/datatableDeletePratica.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getAntiriciclaggioPath(final boolean appendRedirect) {
        return appendRedirect(ANTIRICICLAGGIO_PATH, appendRedirect);
    }

    public static String getDatatableAntiriciclaggioPath(final boolean appendRedirect) {
        return appendRedirect(DATATABLE_ANTIRICICLAGGIO_PATH, appendRedirect);
    }

    public static String getNuovaPraticaCheckCfPath(final boolean appendRedirect) {
        return appendRedirect(NUOVA_PRATICA_CHECK_CF_PATH, appendRedirect);
    }

    public static String getSearchClienteNominativoPath(final boolean appendRedirect) {
        return appendRedirect(SEARCH_CLIENTE_NOMINATIVO_PATH, appendRedirect);
    }

    public static String getDeleteClientePraticaPath(final boolean appendRedirect) {
        return appendRedirect(DELETE_CLIENTE_PRATICA_PATH, appendRedirect);
    }

    public static String getNuovaAmministrazioneCheckRagioneSocialePath(final boolean appendRedirect) {
        return appendRedirect(NUOVA_AMMINISTRAZIONE_CHECK_RAGIONE_SOCIALE_PATH, appendRedirect);
    }

    public static String getSearchAmministrazionePath(final boolean appendRedirect) {
        return appendRedirect(SEARCH_AMMINISTRAZIONE_PATH, appendRedirect);
    }

    public static String getNuovoNominativoPath(final boolean appendRedirect) {
        return appendRedirect(NUOVO_NOMINATIVO_PATH, appendRedirect);
    }
    
    
    public static String getNuovoOperatorePath(final boolean appendRedirect) {
        return appendRedirect(NUOVO_OPERATORE_PATH, appendRedirect);
    }
    public static String getEliminaOperatorePath(final boolean appendRedirect) {
        return appendRedirect(ELIMINA_OPERATORE_PATH, appendRedirect);
    }
    
    

    public static String getSysAdminDashboardPath(final boolean appendRedirect) {
        return appendRedirect(SYSADMIN_DASHBOARD_PATH, appendRedirect);
    }

    public static String getManageUsersPath(final boolean appendRedirect) {
        return appendRedirect(MANAGE_USERS_PATH, appendRedirect);
    }

    public static String getManageRolesPath(final boolean appendRedirect) {
        return appendRedirect(MANAGE_ROLES_PATH, appendRedirect);
    }

    public static String getChooseDomainPath(final boolean appendRedirect) {
        return appendRedirect(CHOOSE_DOMAIN_PATH, appendRedirect);
    }

    public static String getEmptyString() {
        return EMPTY_STRING;
    }

    public static String getNewLine() {
        return NEW_LINE;
    }

    public static String getStatoPraticheCessioniDeleghePath(final boolean appendRedirect) {
        return appendRedirect(STATO_PRATICHE_CESSIONI_DELEGHE_PATH, appendRedirect);
    }

    public static String getStatoPratichePrestitiPath(final boolean appendRedirect) {
        return appendRedirect(STATO_PRATICHE_PRESTITI_PATH, appendRedirect);
    }

    public static String getDatatableStatoPraticaPath(final boolean appendRedirect) {
        return appendRedirect(DATATABLE_STATO_PRATICA_PATH, appendRedirect);
    }

    public static String getDatatableStatoConteggioEstinzionePath(final boolean appendRedirect) {
        return appendRedirect(DATATABLE_STATO_CONTEGGIO_ESTINZIONE_PATH, appendRedirect);
    }

    public static String getNuovaCampagnaMarketingPath(final boolean appendRedirect) {
        return appendRedirect(NUOVA_CAMPAGNA_MARKETING_PATH, appendRedirect);
    }

    public static String getInfoCampagneMarketingPath(final boolean appendRedirect) {
        return appendRedirect(INFO_CAMPAGNE_MARKETING_PATH, appendRedirect);
    }

    public static String getDettaglioCampagnaMarketingPath(final boolean appendRedirect) {
        return appendRedirect(DETTAGLIO_CAMPAGNA_MARKETING_PATH, appendRedirect);
    }

    public static String getSettingsCampagneMarketingPath(final boolean appendRedirect) {
        return appendRedirect(SETTINGS_CAMPAGNE_MARKETING_PATH, appendRedirect);
    }

    public static String getAntiriciclaggioAdminPath(final boolean appendRedirect) {
        return appendRedirect(ANTIRICICLAGGIO_ADMIN_PATH, appendRedirect);
    }

    public static String getDatatableAntiriciclaggioAdminPath(final boolean appendRedirect) {
        return appendRedirect(DATATABLE_ANTIRICICLAGGIO_ADMIN_PATH, appendRedirect);
    }

    public static String getDatiPersonaliPath(final boolean appendRedirect) {
        return appendRedirect(DATI_PERSONALI_INDEX_PATH, appendRedirect);
    }

    public static String getPreferenzePath(final boolean appendRedirect) {
        return appendRedirect(PREFERENZE_INDEX_PATH, appendRedirect);
    }

    public static String getCambioPasswordPath(final boolean appendRedirect) {
        return appendRedirect(CAMBIO_PASSWORD_INDEX_PATH, appendRedirect);
    }

    public static String getCambioPasswordAntiriciclaggioPath(final boolean appendRedirect) {
        return appendRedirect(CAMBIO_PASSWORD_ANTIRICICLAGGIO_INDEX_PATH, appendRedirect);
    }

    public static String getRicaricaSmsPath(final boolean appendRedirect) {
        return appendRedirect(RICARICA_SMS_PATH, appendRedirect);
    }

    public static String getPdfViewPath(final boolean appendRedirect) {
        return appendRedirect(PDF_VIEW_PATH, appendRedirect);
    }

    public static String getStatoNominativiPath(final boolean appendRedirect) {
        return appendRedirect(STATO_NOMINATIVI_PATH, appendRedirect);
    }

    public static String getDatatableStatoNominativoPath(final boolean appendRedirect) {
        return appendRedirect(DATATABLE_STATO_NOMINATIVO_PATH, appendRedirect);
    }

    public static String getSessionExpiredPath(final boolean appendRedirect) {
        return appendRedirect(SESSION_EXPIRED_PATH, appendRedirect);
    }
    
    public static String getGestionePermessiPath(final boolean appendRedirect) {
        return appendRedirect(GESTIONE_PERMESSI_INDEX_PATH, appendRedirect);
    }

    public static String getBackupPath(final boolean appendRedirect) {
        return appendRedirect(BACKUP_INDEX_PATH, appendRedirect);
    }

    public static String getAccessLogPath(final boolean appendRedirect) {
        return appendRedirect(ACCESS_LOG_INDEX_PATH, appendRedirect);
    }

    public static String getCreateNewSimulatorTablePath(final boolean appendRedirect) {
        return appendRedirect(NEW_SIMULATOR_TABLE_PATH, appendRedirect);
    }

    public static String getSimulatorTableListTablePath(final boolean appendRedirect) {
        return appendRedirect(SIMULATOR_TABLE_LIST_PATH, appendRedirect);
    }

    public static String getRunSimulatorPath(final boolean appendRedirect) {
        return appendRedirect(RUN_SIMULATOR_PATH, appendRedirect);
    }

    public static String getInviteFriendsPath(final boolean appendRedirect) {
        return appendRedirect(INVITE_FRIENDS_INDEX_PATH, appendRedirect);
    }

    public static String getInfoAccountPath(final boolean appendRedirect) {
        return appendRedirect(INFO_ACCOUNT_INDEX_PATH, appendRedirect);
    }

    public static String getAnalyticsClientiPath(final boolean appendRedirect) {
        return appendRedirect(ANALYTICS_CLIENTI_INDEX_PATH, appendRedirect);
    }

    public static String getAnalyticsNominativiPath(final boolean appendRedirect) {
        return appendRedirect(ANALYTICS_NOMINATIVI_INDEX_PATH, appendRedirect);
    }

    public static String getNoticeBoardPath(final boolean appendRedirect) {
        return appendRedirect(NOTICE_BOARD_INDEX_PATH, appendRedirect);
    }

    public static String getNoticeBoardDatatablePath(final boolean appendRedirect) {
        return appendRedirect(NOTICE_BOARD_DATATABLE_INDEX_PATH, appendRedirect);
    }

    public static String getPdfNoticeBoardPath(final boolean appendRedirect) {
        final String path = NOTICE_BOARD_PATH + "/pdfView.xhtml";
        return appendRedirect(path, appendRedirect);
    }

    public static String getDatatableAdvancedSearchClientiPath(final boolean appendRedirect) {
        return appendRedirect(DATATABLE_ADVANCED_SEARCH_CLIENTI_INDEX_PATH, appendRedirect);
    }

    public static String getDatatableAdvancedSearchNominativiPath(final boolean appendRedirect) {
        return appendRedirect(DATATABLE_ADVANCED_SEARCH_NOMINATIVI_INDEX_PATH, appendRedirect);
    }
    
    public static String getGestioneLeadsPath(final boolean appendRedirect) {
        return appendRedirect(GESTIONE_LEADS_PATH, appendRedirect);
    }
    
    public static String getGestionePrivacyTemplatePath(final boolean appendRedirect) {
        return appendRedirect(GESTIONE_PRICAVYTEMPLATE_PATH, appendRedirect);
    }
    
    public static String getGestionePrivacyTemplateModPath(final boolean appendRedirect) {
        return appendRedirect(GESTIONE_PRICAVYTEMPLATEMOD_PATH, appendRedirect);
    }
}
