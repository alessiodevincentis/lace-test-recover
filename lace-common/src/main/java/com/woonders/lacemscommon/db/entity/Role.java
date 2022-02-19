package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Emanuele on 14/11/2016.
 */
@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rolename", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public enum RoleName {
        // Servono solo per importare i nuovi permessi da cancellare in seguito
        ADMIN(""), OPERATOR(""), ANTI_RICICLAGGIO(""), SYSADMIN(""),
        // DATI AZIENDA
        DATI_AZIENDA_WRITE_NOT("Non può modificare i dati azienda"),
        DATI_AZIENDA_WRITE("Può modificare i dati azienda della propria agenzia"),
        DATI_AZIENDA_WRITE_SUPER("Può modificare i dati azienda di ogni agenzia"),
        // PREFERENZE
        PREFERENZE_WRITE_NOT("Non può modificare le preferenze"),
        PREFERENZE_WRITE("Può modificare le preferenze della propria agenzia"),
        PREFERENZE_WRITE_SUPER("Può modificare le preferenze di ogni agenzia"),
        // CLIENTI
        CLIENTI_READ_NOT("Non può visualizzare i clienti"),
        CLIENTI_READ_OWN("Può visualizzare solo i propri clienti"),
        CLIENTI_READ_ALL("Può visualizzare tutti i clienti della propria agenzia"),
        CLIENTI_READ_SUPER("Può visualizzare tutti i clienti di ogni agenzia"),
        CLIENTI_WRITE_NOT("Non può aggiungere/modificare i clienti"),
        CLIENTI_WRITE_OWN("Può aggiungere/modificare solo i propri clienti"),
        CLIENTI_WRITE_ALL("Può aggiungere/modificare tutti i clienti della propria agenzia"),
        CLIENTI_WRITE_SUPER("Può aggiungere/modificare tutti i clienti di ogni agenzia"),
        CLIENTI_DELETE_NOT("Non può eliminare i clienti"),
        CLIENTI_DELETE_OWN("Può eliminare i propri clienti"),
        CLIENTI_DELETE_ALL("Può eliminare tutti i clienti della propria agenzia"),
        CLIENTI_DELETE_SUPER("Può eliminare tutti i clienti di ogni agenzia"),
        // NOMINATIVI
        NOMINATIVI_READ_NOT("Non può visualizzare i nominativi"),
        NOMINATIVI_READ_OWN("Può visualizzare solo i propri nominativi"),
        NOMINATIVI_READ_ALL("Può visualizzare tutti i nominativi della propria agenzia"),
        NOMINATIVI_READ_SUPER("Può visualizzare tutti i nominativi di ogni agenzia"),
        NOMINATIVI_WRITE_NOT("Non può aggiungere/modificare i nominativi"),
        NOMINATIVI_WRITE_OWN("Può aggiungere/modificare solo i propri nominativi"),
        NOMINATIVI_WRITE_ALL("Può aggiungere/modificare tutti i nominativi della propria agenzia"),
        NOMINATIVI_WRITE_SUPER("Può aggiungere/modificare tutti i nominativi di ogni agenzia"),
        NOMINATIVI_DELETE_NOT("Non può cancellare i nominativi"),
        NOMINATIVI_DELETE_OWN("Può cancellare solo i propri nominativi"),
        NOMINATIVI_DELETE_ALL("Può cancellare tutti i nominativi della propria agenzia"),
        NOMINATIVI_DELETE_SUPER("Può cancellare tutti i nominativi di ogni agenzia"),
        // CAMPAGNE MARKETING
        CAMPAGNE_MARKETING_WRITE_NOT("Non può effettuare le campagne marketing"),
        CAMPAGNE_MARKETING_WRITE("Può effettuare le campagne marketing"),
        // FATTURAZIONE
        FATTURAZIONE_READ_NOT("Non può visualizzare le pratiche"),
        FATTURAZIONE_READ_OWN("Può visualizzare solo le proprie pratiche da perfezionare"),
        FATTURAZIONE_READ_ALL("Può visualizzare tutte le pratiche da perfezionare della propria agenzia"),
        FATTURAZIONE_READ_SUPER("Può visualizzare tutte le pratiche da perfezionare di ogni agenzia"),
        FATTURAZIONE_WRITE_NOT("Non può perfezionare le pratiche"),
        FATTURAZIONE_WRITE("Può perfezionare le pratiche della propria agenzia"),
        FATTURAZIONE_WRITE_SUPER("Può perfezionare le pratiche di ogni agenzia"),
        // AMMINISTRAZIONI
        AMMINISTRAZIONI_WRITE_NOT("Non può aggiungere/modificare amministrazioni"),
        AMMINISTRAZIONI_WRITE("Può aggiungere/modificare le amministrazioni"),
        // ANTIRICICLAGGIO
        ANTI_RICICLAGGIO_READ_NOT("Non può visualizzare la sezione antiriciclaggio"),
        ANTI_RICICLAGGIO_READ("Può visualizzare la sezione antiriciclaggio"),
        // GESTIONE PERMESSI
        GESTIONE_PERMESSI_WRITE_NOT("Non può modificare i permessi"),
        GESTIONE_PERMESSI_WRITE("Può modificare i permessi della propria agenzia"),
        GESTIONE_PERMESSI_WRITE_SUPER("Può modificare i permessi di ogni agenzia"),
        // RICARICA
        RICARICA_WRITE_NOT("Non può effettuare ricariche"),
        RICARICA_WRITE("Può effettuare ricariche agli operatori della propria agenzia"),
        RICARICA_WRITE_SUPER("Può effettuare ricariche agli operatori di ogni agenzia"),
        // UTENZA PER L'ANTIRICICLAGGIO
        ANTI_RICICLAGGIO_OUT("Utenza per l'antiriciclaggio"),
        // GRAFICI
        GRAFICI_READ_OWN("Può leggere i grafici con solo i propri dati"),
        GRAFICI_READ_ALL("Può leggere i grafici con i dati di tutta l'agenzia"),
        GRAFICI_READ_SUPER("Può leggere i grafici di ogni agenzia"),
        GRAFICI_READ_NOT("Non può leggere i grafici"),
        // CALENDARIO
        CALENDARIO_READ_OWN("Può visualizzare solo i propri appuntamenti"),
        CALENDARIO_READ_NOT("Non può visualizzare gli appuntamenti"),
        CALENDARIO_READ_ALL("Può visualizzare tutti gli appuntamenti della propria agenzia"),
        CALENDARIO_READ_SUPER("Può visualizzare tutti gli appuntamenti di ogni agenzia"),
        CALENDARIO_WRITE_NOT("Non può aggiungere/modificare gli appuntamenti"),
        CALENDARIO_WRITE_OWN("Può aggiungere/modificare solo i propri appuntamenti"),
        CALENDARIO_WRITE_ALL("Può aggiungere/modificare tutti gli appuntamenti della propria agenzia"),
        CALENDARIO_WRITE_SUPER("Può aggiungere/modificare tutti gli appuntamenti di ogni agenzia"),
        CALENDARIO_DELETE_NOT("Non può eliminare gli appuntamenti"),
        CALENDARIO_DELETE_OWN("Può eliminare solo i propri appuntamenti"),
        CALENDARIO_DELETE_ALL("Può eliminare tutti gli appuntamenti della propria agenzia"),
        CALENDARIO_DELETE_SUPER("Può eliminare tutti gli appuntamenti di ogni agenzia"),
        // BACKUP
        BACKUP_DOWNLOAD("BACKUP_DOWNLOAD"),
        //SIMULATORI
        SIMULATORI_READ_NOT("Non può usare i simulatori"),
        SIMULATORI_READ_OWN("Può usare i simulatori con le proprie tabelle (e vede solo le proprie tabelle)"),
        SIMULATORI_READ_ALL("Può usare i simulatori con le proprie tabelle (e vede le tabelle della propria agenzia)"),
        SIMULATORI_READ_SUPER("Può usare i simulatori con le proprie tabelle (e vede le tabelle di ogni agenzia)"),
        SIMULATORI_WRITE_NOT("Non può aggiungere nuove tabelle nè modificare quelle esistenti"),
        SIMULATORI_WRITE_OWN("Può aggiungere nuove tabelle e modificare solo quelle che ha creato"),
        SIMULATORI_WRITE_ALL("Può aggiungere nuove tabelle e modificare quelle della propria agenzia"),
        SIMULATORI_WRITE_SUPER("Può aggiungere nuove tabelle e modificare le tabelle di ogni agenzia"),
        SIMULATORI_DELETE_NOT("Non può eliminare alcuna tabella"),
        SIMULATORI_DELETE_OWN("Può eliminare le tabelle da lui create"),
        SIMULATORI_DELETE_ALL("Può eliminare ogni tabella della propria agenzia"),
        SIMULATORI_DELETE_SUPER("Può eliminare ogni tabella di ogni agenzia"),

        //NOTICE BOARD
        NOTICE_BOARD_READ("Può leggere gli avvisi della propria agenzia"),
        NOTICE_BOARD_READ_NOT("Non può leggere gli avvisi della propria agenzia"),
        NOTICE_BOARD_READ_SUPER("Può leggere gli avvisi di ogni agenzia"),
        NOTICE_BOARD_WRITE_NOT("Non può aggiungere avvisi"),
        NOTICE_BOARD_WRITE("Può aggiungere avvisi alla propria agenzia"),
        NOTICE_BOARD_WRITE_SUPER("Può aggiungere avvisi a tutte le agenzie"),

        //UTENTI
    	UTENTI_DELETE_NOT("Non può eliminare gli utenti"),
    	UTENTI_DELETE("Può eliminare gli utenti della propria azienda"),
    	UTENTI_DELETE_SUPER("Può eliminare gli utenti a tutte le azienda"),
    	UTENTI_WRITE_NOT("Non può aggiungere gli utenti"),
    	UTENTI_WRITE("Può aggiungere gli utenti della propria azienda"),
    	UTENTI_WRITE_SUPER("Può aggiungere gli utenti a tutte le azienda"),
    	
    	//LEADS DATARETENTION
    	LEADS_DATARETENTION_WRITE_NOT("Non può modificare le policy dei leads per la data retention"),
    	LEADS_DATARETENTION_WRITE("Può modificare le policy dei leads per la data retention della propria agenzia"),
    	LEADS_DATARETENTION_WRITE_SUPER("Può modificare le policy dei leads per la data retention di ogni agenzia"),
    	
    	//PRIVACY
    	PRIVACY_WRITE_NOT("Non può modificare il testo della Privacy"),
    	PRIVACY_WRITE("Può modificare il testo della Privacy della propria agenzia"),
    	PRIVACY_WRITE_SUPER("Può modificare il testo della Privacy di ogni agenzia"),
    	
        //ACCESS LOG
        ACCESS_LOG_READ("ACCESS_LOG_READ"),
        ACCESS_LOG_READ_SUPER("ACCESS_LOG_READ_SUPER");
    	
    	

        private final String value;

        RoleName(final String value) {
            this.value = value;
        }
        
        
        public static RoleName[] getClientiReadRoles() {
            return new RoleName[]{CLIENTI_READ_OWN, CLIENTI_READ_ALL, CLIENTI_READ_SUPER};
        }

        public static String[] getClientiReadRolesString() {
            return getRolesString(getClientiReadRoles());
        }

        public static RoleName[] getNominativiReadRoles() {
            return new RoleName[]{NOMINATIVI_READ_OWN, NOMINATIVI_READ_ALL, NOMINATIVI_READ_SUPER};
        }

        public static String[] getNominativiReadRolesString() {
            return getRolesString(getNominativiReadRoles());
        }

        public static RoleName[] getCampagneMarketingWriteRoles() {
            return new RoleName[]{CAMPAGNE_MARKETING_WRITE};
        }

        public static String[] getCampagneMarketingWriteRolesString() {
            return getRolesString(getCampagneMarketingWriteRoles());
        }

        public static RoleName[] getAntiriciclaggioReadRoles() {
            return new RoleName[]{ANTI_RICICLAGGIO_OUT, ANTI_RICICLAGGIO_READ};
        }

        public static String[] getAntiriciclaggioReadRolesString() {
            return getRolesString(getAntiriciclaggioReadRoles());
        }

        public static RoleName[] getClientiDeleteRoles() {
            return new RoleName[]{CLIENTI_DELETE_OWN, CLIENTI_DELETE_ALL, CLIENTI_DELETE_SUPER};
        }

        public static String[] getClientiDeleteRolesString() {
            return getRolesString(getClientiDeleteRoles());
        }

        public static RoleName[] getClientiWriteRoles() {
            return new RoleName[]{CLIENTI_WRITE_OWN, CLIENTI_WRITE_ALL, CLIENTI_WRITE_SUPER};
        }

        public static String[] getClientiWriteRolesString() {
            return getRolesString(getClientiWriteRoles());
        }

        public static RoleName[] getNominativiWriteRoles() {
            return new RoleName[]{NOMINATIVI_WRITE_OWN, NOMINATIVI_WRITE_ALL, NOMINATIVI_WRITE_SUPER};
        }

        public static String[] getNominativiWriteRolesString() {
            return getRolesString(getNominativiWriteRoles());
        }

        public static RoleName[] getRicaricaWriteRoles() {
            return new RoleName[]{RICARICA_WRITE, RICARICA_WRITE_SUPER};
        }

        public static String[] getRicaricaWriteRolesString() {
            return getRolesString(getRicaricaWriteRoles());
        }
        

        public static RoleName[] getGestionePermessiWriteRoles() {
            return new RoleName[]{GESTIONE_PERMESSI_WRITE, GESTIONE_PERMESSI_WRITE_SUPER};
        }

        public static String[] getGestionePermessiWriteRolesString() {
            return getRolesString(getGestionePermessiWriteRoles());
        }

        public static RoleName[] getDatiAziendaWriteRoles() {
            return new RoleName[]{DATI_AZIENDA_WRITE, DATI_AZIENDA_WRITE_SUPER};
        }

        public static String[] getDatiAziendaWriteRolesString() {
            return getRolesString(getDatiAziendaWriteRoles());
        }

        private static String[] getRolesString(final RoleName[] roleNames) {
            return Arrays.stream(roleNames).map(Enum::toString).toArray(size -> new String[roleNames.length]);
        }

        public static RoleName[] getSimulatoriReadRoles() {
            return new RoleName[]{SIMULATORI_READ_OWN, SIMULATORI_READ_ALL, SIMULATORI_READ_SUPER};
        }

        public static String[] getSimulatoriReadRolesString() {
            return getRolesString(getSimulatoriReadRoles());
        }

        public static RoleName[] getSimulatoriWriteRoles() {
            return new RoleName[]{SIMULATORI_WRITE_OWN, SIMULATORI_WRITE_ALL, SIMULATORI_WRITE_SUPER};
        }

        public static String[] getSimulatoriWriteRolesString() {
            return getRolesString(getSimulatoriWriteRoles());
        }

        public static RoleName[] getSimulatoriDeleteRoles() {
            return new RoleName[]{SIMULATORI_DELETE_OWN, SIMULATORI_DELETE_ALL, SIMULATORI_DELETE_SUPER};
        }

        public static String[] getSimulatoriDeleteRolesString() {
            return getRolesString(getSimulatoriDeleteRoles());
        }

        public static RoleName[] getNoticeBoardWriteRoles() {
            return new RoleName[]{NOTICE_BOARD_WRITE, NOTICE_BOARD_WRITE_SUPER};
        }

        public static String[] getNoticeBoardWriteRolesString() {
            return getRolesString(getNoticeBoardWriteRoles());
        }

        public static RoleName[] getNoticeBoardReadRoles() {
            return new RoleName[]{NOTICE_BOARD_READ_SUPER};
        }

        public static String[] getNoticeBoardReadRolesString() {
            return getRolesString(getNoticeBoardReadRoles());
        }

        public static RoleName[] getAccessLogReadRoles() {
            return new RoleName[]{ACCESS_LOG_READ, ACCESS_LOG_READ_SUPER};
        }

        public static String[] getAccessLogReadRolesString() {
            return getRolesString(getAccessLogReadRoles());
        }
        
        

        public static RoleName[] getUtentiDeleteRoles() {
            return new RoleName[]{UTENTI_DELETE, UTENTI_DELETE_SUPER};
        }
        
        public static String[] getUtentiDeleteRolesString() {
            return getRolesString(getUtentiDeleteRoles());
        }
        
        public static RoleName[] getUtentiWriteRoles() {
            return new RoleName[]{UTENTI_WRITE, UTENTI_WRITE_SUPER};
        }
        
        public static String[] getUtentiWriteRolesString() {
            return getRolesString(getUtentiWriteRoles());
        }
        
        public static RoleName[] getLeadsDataRetentionWriteRoles() {
            return new RoleName[]{LEADS_DATARETENTION_WRITE, LEADS_DATARETENTION_WRITE_SUPER};
        }
        
        public static String[] getLeadsDataRetentionWriteRolesString() {
            return getRolesString(getLeadsDataRetentionWriteRoles());
        }
        
        public static RoleName[] getPrivacyWriteRoles() {
            return new RoleName[]{PRIVACY_WRITE, PRIVACY_WRITE_SUPER};
        }
        
        public static String[] getPrivacyWriteRolesString() {
            return getRolesString(getPrivacyWriteRoles());
        }

        public String getValue() {
            return value;
        }
    }

}
