package com.woonders.lacemscommon.laceenum;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entity.Role.RoleName;

import java.util.HashMap;
import java.util.Map;

public enum PermissionProfile {
    DIPENDENTE_AVANZATO_MULTIAGENZIA("Dipendente avanzato multiagenzia"),
    DIPENDENTE_AVANZATO("Dipendente avanzato"),
    DIPENDENTE_BASE("Dipendente base"),
    COLLABORATORE("Collaboratore"),
    UTENTE_DISABILITATO("Utente disabilitato");

    private final String value;

    PermissionProfile(final String value) {
        this.value = value;
    }

    public static Map<OperatorViewModel.MenuSection, Map<OperatorViewModel.PermissionType, RoleName>> getMenuSectionPermissionMap(
            final PermissionProfile permissionProfile) {
        final Map<OperatorViewModel.MenuSection, Map<OperatorViewModel.PermissionType, RoleName>> menuSectionPermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> datiAziendaPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> preferenzePermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> campagneMarketingPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> fatturazionePermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> amministrazioniPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> antiriciclaggioPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> gestionePermessiPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> ricaricaPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> graficiPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> calendarioPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> clientiPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> nominativiPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> simulatoriPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> noticeBoardPermissionTypePermissionMap = new HashMap<>();
        
        final Map<OperatorViewModel.PermissionType, Role.RoleName> utentiPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> leadDataretentionPermissionTypePermissionMap = new HashMap<>();
        final Map<OperatorViewModel.PermissionType, Role.RoleName> privacyPermissionTypePermissionMap = new HashMap<>();
        
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.DATI_AZIENDA,
                datiAziendaPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.PREFERENZE, preferenzePermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.CAMPAGNE_MARKETING,
                campagneMarketingPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.FATTURAZIONE,
                fatturazionePermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.AMMINISTRAZIONI,
                amministrazioniPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.ANTI_RICICLAGGIO,
                antiriciclaggioPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.GESTIONE_PERMESSI,
                gestionePermessiPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.RICARICA, ricaricaPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.NOMINATIVI, nominativiPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.CLIENTI, clientiPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.GRAFICI, graficiPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.CALENDARIO, calendarioPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.NOTICE_BOARD, noticeBoardPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.SIMULATORI, simulatoriPermissionTypePermissionMap);

        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.UTENTI, utentiPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.LEADS_DATARETENTION, leadDataretentionPermissionTypePermissionMap);
        menuSectionPermissionMap.put(OperatorViewModel.MenuSection.PRIVACY, privacyPermissionTypePermissionMap);
        
        switch (permissionProfile) {
            case UTENTE_DISABILITATO:
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOTICE_BOARD_READ_NOT);
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOTICE_BOARD_WRITE_NOT);
                datiAziendaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.DATI_AZIENDA_WRITE_NOT);
                preferenzePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.PREFERENZE_WRITE_NOT);
                campagneMarketingPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CAMPAGNE_MARKETING_WRITE_NOT);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.FATTURAZIONE_READ_NOT);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.FATTURAZIONE_WRITE_NOT);
                amministrazioniPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.AMMINISTRAZIONI_WRITE_NOT);
                antiriciclaggioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.ANTI_RICICLAGGIO_READ_NOT);
                gestionePermessiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.GESTIONE_PERMESSI_WRITE_NOT);
                ricaricaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.RICARICA_WRITE_NOT);
                graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, 
                		RoleName.GRAFICI_READ_OWN);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.CALENDARIO_READ_NOT);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CALENDARIO_WRITE_NOT);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CALENDARIO_DELETE_NOT);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.CLIENTI_READ_NOT);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.CLIENTI_WRITE_NOT);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CLIENTI_DELETE_NOT);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOMINATIVI_READ_NOT);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOMINATIVI_WRITE_NOT);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.NOMINATIVI_DELETE_NOT);
                //SIMULATORI
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_READ_NOT);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_WRITE_NOT);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_DELETE_NOT);
                
                //UTENTI
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_DELETE_NOT);
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_WRITE_NOT);
                //LEAD DATARETENTION
                leadDataretentionPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.LEADS_DATARETENTION_WRITE_NOT);
                //PRIVACY
                privacyPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.PRIVACY_WRITE_NOT);
                

                break;

            case COLLABORATORE:
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOTICE_BOARD_READ);
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOTICE_BOARD_WRITE_NOT);
                datiAziendaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.DATI_AZIENDA_WRITE_NOT);
                preferenzePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.PREFERENZE_WRITE_NOT);
                campagneMarketingPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CAMPAGNE_MARKETING_WRITE_NOT);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.FATTURAZIONE_READ_OWN);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.FATTURAZIONE_WRITE_NOT);
                amministrazioniPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.AMMINISTRAZIONI_WRITE);
                antiriciclaggioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.ANTI_RICICLAGGIO_READ_NOT);
                gestionePermessiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.GESTIONE_PERMESSI_WRITE_NOT);
                ricaricaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.RICARICA_WRITE_NOT);
                graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, 
                		RoleName.GRAFICI_READ_OWN);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.CALENDARIO_READ_OWN);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CALENDARIO_WRITE_OWN);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CALENDARIO_DELETE_OWN);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.CLIENTI_READ_OWN);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.CLIENTI_WRITE_OWN);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CLIENTI_DELETE_NOT);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOMINATIVI_READ_OWN);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOMINATIVI_WRITE_OWN);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.NOMINATIVI_DELETE_OWN);
                //SIMULATORI
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_READ_NOT);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_WRITE_NOT);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_DELETE_NOT);
                
                //UTENTI
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_DELETE_NOT);
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_WRITE_NOT);
                //LEAD DATARETENTION
                leadDataretentionPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.LEADS_DATARETENTION_WRITE_NOT);
                //PRIVACY
                privacyPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.PRIVACY_WRITE_NOT);
                

                break;
            case DIPENDENTE_BASE:
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOTICE_BOARD_READ);
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOTICE_BOARD_WRITE_NOT);
                datiAziendaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.DATI_AZIENDA_WRITE_NOT);
                preferenzePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.PREFERENZE_WRITE_NOT);
                campagneMarketingPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CAMPAGNE_MARKETING_WRITE);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.FATTURAZIONE_READ_OWN);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.FATTURAZIONE_WRITE_NOT);
                amministrazioniPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.AMMINISTRAZIONI_WRITE);
                antiriciclaggioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.ANTI_RICICLAGGIO_READ_NOT);
                gestionePermessiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.GESTIONE_PERMESSI_WRITE_NOT);
                ricaricaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.RICARICA_WRITE_NOT);
                graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.GRAFICI_READ_ALL);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.CALENDARIO_READ_ALL);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CALENDARIO_WRITE_ALL);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CALENDARIO_DELETE_ALL);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.CLIENTI_READ_ALL);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.CLIENTI_WRITE_ALL);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CLIENTI_DELETE_NOT);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOMINATIVI_READ_ALL);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOMINATIVI_WRITE_ALL);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.NOMINATIVI_DELETE_OWN);
                //SIMULATORI
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_READ_OWN);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_WRITE_NOT);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_DELETE_NOT);
                
                //UTENTI
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_DELETE_NOT);
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_WRITE_NOT);
                //LEAD DATARETENTION
                leadDataretentionPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.LEADS_DATARETENTION_WRITE_NOT);
                //PRIVACY
                privacyPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.PRIVACY_WRITE_NOT);
                
                break;
            case DIPENDENTE_AVANZATO:
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOTICE_BOARD_READ);
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOTICE_BOARD_WRITE);
                datiAziendaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.DATI_AZIENDA_WRITE);
                preferenzePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.PREFERENZE_WRITE);
                campagneMarketingPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CAMPAGNE_MARKETING_WRITE);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.FATTURAZIONE_READ_ALL);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.FATTURAZIONE_WRITE);
                amministrazioniPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.AMMINISTRAZIONI_WRITE);
                antiriciclaggioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.ANTI_RICICLAGGIO_READ);
                gestionePermessiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.GESTIONE_PERMESSI_WRITE_NOT);
                ricaricaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.RICARICA_WRITE);
                graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.GRAFICI_READ_ALL);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.CALENDARIO_READ_ALL);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CALENDARIO_WRITE_ALL);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CALENDARIO_DELETE_ALL);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.CLIENTI_READ_ALL);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.CLIENTI_WRITE_ALL);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CLIENTI_DELETE_ALL);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOMINATIVI_READ_ALL);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOMINATIVI_WRITE_ALL);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.NOMINATIVI_DELETE_ALL);
                //SIMULATORI
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_READ_ALL);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_WRITE_ALL);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_DELETE_ALL);
                
                //UTENTI
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_WRITE);
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_DELETE);
                //LEAD DATARETENTION
                leadDataretentionPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.LEADS_DATARETENTION_WRITE);
                //PRIVACY
                privacyPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.PRIVACY_WRITE);
                
                break;

            case DIPENDENTE_AVANZATO_MULTIAGENZIA:
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOTICE_BOARD_READ_SUPER);
                noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOTICE_BOARD_WRITE_SUPER);
                datiAziendaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.DATI_AZIENDA_WRITE_SUPER);
                preferenzePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.PREFERENZE_WRITE_SUPER);
                campagneMarketingPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CAMPAGNE_MARKETING_WRITE);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.FATTURAZIONE_READ_SUPER);
                fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.FATTURAZIONE_WRITE_SUPER);
                amministrazioniPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.AMMINISTRAZIONI_WRITE);
                antiriciclaggioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.ANTI_RICICLAGGIO_READ);
                gestionePermessiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.GESTIONE_PERMESSI_WRITE_NOT);
                ricaricaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.RICARICA_WRITE_SUPER);
                graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.GRAFICI_READ_SUPER);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.CALENDARIO_READ_SUPER);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.CALENDARIO_WRITE_SUPER);
                calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CALENDARIO_DELETE_SUPER);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.CLIENTI_READ_SUPER);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.CLIENTI_WRITE_SUPER);
                clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.CLIENTI_DELETE_SUPER);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                        RoleName.NOMINATIVI_READ_SUPER);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                        RoleName.NOMINATIVI_WRITE_SUPER);
                nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                        RoleName.NOMINATIVI_DELETE_SUPER);
                //SIMULATORI
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_READ_SUPER);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_WRITE_SUPER);
                simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, RoleName.SIMULATORI_DELETE_SUPER);
                
                //UTENTI
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_DELETE_SUPER);
                utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.UTENTI_WRITE_SUPER);
                //LEAD DATARETENTION
                leadDataretentionPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.LEADS_DATARETENTION_WRITE_SUPER);
                //PRIVACY
                privacyPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RoleName.PRIVACY_WRITE_SUPER);
                break;
        }
        return menuSectionPermissionMap;
    }

    public static PermissionProfile[] getNoMultiAgencyPermissionProfileArray() {
        return new PermissionProfile[]{DIPENDENTE_AVANZATO, DIPENDENTE_BASE, COLLABORATORE, UTENTE_DISABILITATO};
    }

    public String getValue() {
        return value;
    }

}
