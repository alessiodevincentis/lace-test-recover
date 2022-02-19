package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.woonders.lacemscommon.db.entity.Role.RoleName.*;

/**
 * Created by emanuele on 09/01/17.
 */
@Component
public class OperatorViewModelCreator extends AbstractBaseViewModelCreator<Operator, OperatorViewModel> {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleViewModelCreator roleViewModelCreator;
    @Autowired
    private AziendaViewModelCreator aziendaViewModelCreator;

    @Override
    public Operator createModel(final OperatorViewModel operatorViewModel) {
        if (operatorViewModel != null) {
            final Operator operator = Operator.builder().email(operatorViewModel.getEmail())
                    .firstName(operatorViewModel.getFirstName()).id(operatorViewModel.getId())
                    .lastName(operatorViewModel.getLastName()).password(operatorViewModel.getPassword())
                    .phoneNumber(operatorViewModel.getPhoneNumber()).smsBalance(operatorViewModel.getSmsBalance())
                    .username(operatorViewModel.getUsername())
                    .receiveLeadEnabled(operatorViewModel.isReceiveLeadEnabled())
                    .leadRicevuti(operatorViewModel.getLeadRicevuti())
                    .azienda(aziendaViewModelCreator.createModel(operatorViewModel.getAzienda()))
                    .permissionProfile(operatorViewModel.getPermissionProfile()).build();

            final Set<Role> roleSet = new HashSet<>();
            operator.setRole(roleSet);

            final Map<OperatorViewModel.MenuSection, Map<OperatorViewModel.PermissionType, Role.RoleName>> menuSectionPermissionMap = operatorViewModel
                    .getSectionPermissionMap();

            // TODO nel caso di permessi con NOT, bisogna eliminare dal set quel
            // tipo di permesso
            for (final OperatorViewModel.MenuSection menuSection : menuSectionPermissionMap.keySet()) {
                final Map<OperatorViewModel.PermissionType, Role.RoleName> permissionTypePermissionMap = menuSectionPermissionMap
                        .get(menuSection);
                for (final OperatorViewModel.PermissionType permissionType : permissionTypePermissionMap.keySet()) {
                    // si lo so che ogni volta lo legge dal database ma non si
                    // puo inizializzare in nessun modo, ma tanto quante volte
                    // cambieranno sti permessi? probabilmente mai...
                    final Role role = roleRepository.findByRoleName(permissionTypePermissionMap.get(permissionType));
                    if (role != null) {
                        roleSet.add(role);
                    }
                }
            }

            return operator;
        }
        return null;
    }

    @Override
    public OperatorViewModel createViewModel(final Operator operator) {
        if (operator != null) {
            final OperatorViewModel operatorViewModel = OperatorViewModel.builder().email(operator.getEmail())
                    .firstName(operator.getFirstName()).id(operator.getId()).lastName(operator.getLastName())
                    .password(operator.getPassword()).phoneNumber(operator.getPhoneNumber())
                    .smsBalance(operator.getSmsBalance()).username(operator.getUsername())
                    .receiveLeadEnabled(operator.isReceiveLeadEnabled()).leadRicevuti(operator.getLeadRicevuti())
                    .azienda(aziendaViewModelCreator.createViewModel(operator.getAzienda()))
                    .permissionProfile(operator.getPermissionProfile()).build();

            // imposto anche la lista dei ruoli, potrebbe servire invece che
            // lasciarla null, tantovale metterla
            // anche perche la mappa serve solo per la gestione permessi, non
            // per altro
            operatorViewModel.setRoleViewModelList(roleViewModelCreator.fromSet(operator.getRole()));

            final Map<OperatorViewModel.MenuSection, Map<OperatorViewModel.PermissionType, Role.RoleName>> sectionPermissionMap = new HashMap<>();
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
            final Map<OperatorViewModel.PermissionType, Role.RoleName> leadsDataRetentionPermissionTypePermissionMap = new HashMap<>();
            final Map<OperatorViewModel.PermissionType, Role.RoleName> privacyPermissionTypePermissionMap = new HashMap<>();


            for (final Role role : operator.getRole()) {
                switch (role.getRoleName()) {
                	
                    // DATI AZIENDA
                    case DATI_AZIENDA_WRITE_NOT:
                        datiAziendaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                DATI_AZIENDA_WRITE_NOT);
                        break;
                    case DATI_AZIENDA_WRITE:
                        datiAziendaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                DATI_AZIENDA_WRITE);
                        break;
                    case DATI_AZIENDA_WRITE_SUPER:
                        datiAziendaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                DATI_AZIENDA_WRITE_SUPER);
                        break;
                    // PREFERENZE
                    case PREFERENZE_WRITE_NOT:
                        preferenzePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                PREFERENZE_WRITE_NOT);
                        break;
                    case PREFERENZE_WRITE:
                        preferenzePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, PREFERENZE_WRITE);
                        break;
                    case PREFERENZE_WRITE_SUPER:
                        preferenzePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                PREFERENZE_WRITE_SUPER);
                        break;
                    // CLIENTI
                    case CLIENTI_READ_NOT:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, CLIENTI_READ_NOT);
                        break;
                    case CLIENTI_READ_OWN:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, CLIENTI_READ_OWN);
                        break;
                    case CLIENTI_READ_ALL:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, CLIENTI_READ_ALL);
                        break;
                    case CLIENTI_READ_SUPER:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, CLIENTI_READ_SUPER);
                        break;
                    case CLIENTI_WRITE_NOT:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, CLIENTI_WRITE_NOT);
                        break;
                    case CLIENTI_WRITE_OWN:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, CLIENTI_WRITE_OWN);
                        break;
                    case CLIENTI_WRITE_ALL:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, CLIENTI_WRITE_ALL);
                        break;
                    case CLIENTI_WRITE_SUPER:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, CLIENTI_WRITE_SUPER);
                        break;
                    case CLIENTI_DELETE_NOT:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE, CLIENTI_DELETE_NOT);
                        break;
                    case CLIENTI_DELETE_OWN:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE, CLIENTI_DELETE_OWN);
                        break;
                    case CLIENTI_DELETE_ALL:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE, CLIENTI_DELETE_ALL);
                        break;
                    case CLIENTI_DELETE_SUPER:
                        clientiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                                CLIENTI_DELETE_SUPER);
                        break;
                    // NOMINATIVI
                    case NOMINATIVI_READ_NOT:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                NOMINATIVI_READ_NOT);
                        break;
                    case NOMINATIVI_READ_OWN:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                NOMINATIVI_READ_OWN);
                        break;
                    case NOMINATIVI_READ_ALL:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                NOMINATIVI_READ_ALL);
                        break;
                    case NOMINATIVI_READ_SUPER:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                NOMINATIVI_READ_SUPER);
                        break;
                    case NOMINATIVI_WRITE_NOT:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                NOMINATIVI_WRITE_NOT);
                        break;
                    case NOMINATIVI_WRITE_OWN:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                NOMINATIVI_WRITE_OWN);
                        break;
                    case NOMINATIVI_WRITE_ALL:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                NOMINATIVI_WRITE_ALL);
                        break;
                    case NOMINATIVI_WRITE_SUPER:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                NOMINATIVI_WRITE_SUPER);
                        break;
                    case NOMINATIVI_DELETE_NOT:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                                NOMINATIVI_DELETE_NOT);
                        break;
                    case NOMINATIVI_DELETE_OWN:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                                NOMINATIVI_DELETE_OWN);
                        break;
                    case NOMINATIVI_DELETE_ALL:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                                NOMINATIVI_DELETE_ALL);
                        break;
                    case NOMINATIVI_DELETE_SUPER:
                        nominativiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                                NOMINATIVI_DELETE_SUPER);
                        break;
                    // CAMPAGNE MARKETING
                    case CAMPAGNE_MARKETING_WRITE_NOT:
                        campagneMarketingPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                CAMPAGNE_MARKETING_WRITE_NOT);
                        break;
                    case CAMPAGNE_MARKETING_WRITE:
                        campagneMarketingPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                CAMPAGNE_MARKETING_WRITE);
                        break;
                    // FATTURAZIONE
                    case FATTURAZIONE_READ_OWN:
                        fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                FATTURAZIONE_READ_OWN);
                        break;
                    case FATTURAZIONE_READ_ALL:
                        fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                FATTURAZIONE_READ_ALL);
                        break;
                    case FATTURAZIONE_READ_SUPER:
                        fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                FATTURAZIONE_READ_SUPER);
                        break;
                    case FATTURAZIONE_WRITE_NOT:
                        fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                FATTURAZIONE_WRITE_NOT);
                        break;
                    case FATTURAZIONE_WRITE:
                        fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                FATTURAZIONE_WRITE);
                        break;
                    case FATTURAZIONE_WRITE_SUPER:
                        fatturazionePermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                FATTURAZIONE_WRITE_SUPER);
                        break;
                    // AMMINISTRAZIONI
                    case AMMINISTRAZIONI_WRITE_NOT:
                        amministrazioniPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                AMMINISTRAZIONI_WRITE_NOT);
                        break;
                    case AMMINISTRAZIONI_WRITE:
                        amministrazioniPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                AMMINISTRAZIONI_WRITE);
                        break;
                    // ANTI RICICLAGGIO
                    case ANTI_RICICLAGGIO_READ_NOT:
                        antiriciclaggioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                ANTI_RICICLAGGIO_READ_NOT);
                        break;
                    case ANTI_RICICLAGGIO_READ:
                        antiriciclaggioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                ANTI_RICICLAGGIO_READ);
                        break;
                    // GESTIONE PERMESSI
                    case GESTIONE_PERMESSI_WRITE_NOT:
                        gestionePermessiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                GESTIONE_PERMESSI_WRITE_NOT);
                        break;
                    case GESTIONE_PERMESSI_WRITE:
                        gestionePermessiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                GESTIONE_PERMESSI_WRITE);
                        break;
                    case GESTIONE_PERMESSI_WRITE_SUPER:
                        gestionePermessiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                GESTIONE_PERMESSI_WRITE_SUPER);
                        break;
                    // RICARICA
                    case RICARICA_WRITE_NOT:
                        ricaricaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RICARICA_WRITE_NOT);
                        break;
                    case RICARICA_WRITE:
                        ricaricaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, RICARICA_WRITE);
                        break;
                    case RICARICA_WRITE_SUPER:
                        ricaricaPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                RICARICA_WRITE_SUPER);
                        break;
                    // GRAFICI
                    case GRAFICI_READ_OWN:
                        graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, GRAFICI_READ_OWN);
                        break;
                    case GRAFICI_READ_ALL:
                        graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, GRAFICI_READ_ALL);
                        break;
                    case GRAFICI_READ_SUPER:
                        graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, GRAFICI_READ_SUPER);
                        break;
                    case GRAFICI_READ_NOT:
                        graficiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, GRAFICI_READ_NOT);
                        break;
                    // CALENDARIO
                    case CALENDARIO_READ_OWN:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                CALENDARIO_READ_OWN);
                        break;
                    case CALENDARIO_READ_ALL:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                CALENDARIO_READ_ALL);
                        break;
                    case CALENDARIO_READ_SUPER:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ,
                                CALENDARIO_READ_SUPER);
                        break;
                    case CALENDARIO_WRITE_OWN:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                CALENDARIO_WRITE_OWN);
                        break;
                    case CALENDARIO_WRITE_ALL:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                CALENDARIO_WRITE_ALL);
                        break;
                    case CALENDARIO_WRITE_SUPER:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
                                CALENDARIO_WRITE_SUPER);
                        break;
                    case CALENDARIO_DELETE_OWN:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                                CALENDARIO_DELETE_OWN);
                        break;
                    case CALENDARIO_DELETE_ALL:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                                CALENDARIO_DELETE_ALL);
                        break;
                    case CALENDARIO_DELETE_SUPER:
                        calendarioPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
                                CALENDARIO_DELETE_SUPER);
                        break;
                    // SIMULATORI
                    case SIMULATORI_READ_NOT:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, SIMULATORI_READ_NOT);
                        break;
                    case SIMULATORI_READ_OWN:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, SIMULATORI_READ_OWN);
                        break;
                    case SIMULATORI_READ_ALL:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, SIMULATORI_READ_ALL);
                        break;
                    case SIMULATORI_READ_SUPER:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, SIMULATORI_READ_SUPER);
                        break;
                    case SIMULATORI_WRITE_NOT:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, SIMULATORI_WRITE_NOT);
                        break;
                    case SIMULATORI_WRITE_OWN:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, SIMULATORI_WRITE_OWN);
                        break;
                    case SIMULATORI_WRITE_ALL:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, SIMULATORI_WRITE_ALL);
                        break;
                    case SIMULATORI_WRITE_SUPER:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, SIMULATORI_WRITE_SUPER);
                        break;
                    case SIMULATORI_DELETE_NOT:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE, SIMULATORI_DELETE_NOT);
                        break;
                    case SIMULATORI_DELETE_OWN:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE, SIMULATORI_DELETE_OWN);
                        break;
                    case SIMULATORI_DELETE_ALL:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE, SIMULATORI_DELETE_ALL);
                        break;
                    case SIMULATORI_DELETE_SUPER:
                        simulatoriPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE, SIMULATORI_DELETE_SUPER);
                        break;
                    // Notice Board
                    case NOTICE_BOARD_READ_SUPER:
                        noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, NOTICE_BOARD_READ_SUPER);
                        break;
                    case NOTICE_BOARD_READ:
                        noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.READ, NOTICE_BOARD_READ);
                        break;
                    case NOTICE_BOARD_WRITE_SUPER:
                        noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, NOTICE_BOARD_WRITE_SUPER);
                        break;
                    case NOTICE_BOARD_WRITE:
                        noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, NOTICE_BOARD_WRITE);
                        break;
                    case NOTICE_BOARD_WRITE_NOT:
                        noticeBoardPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE, NOTICE_BOARD_WRITE_NOT);
                        break;
                    
                    //**** Mod Cristian 10-11-2021
                    // UTENTI
	                case UTENTI_WRITE_NOT:
	                	utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                			UTENTI_WRITE_NOT);
	                    break;
	                case UTENTI_WRITE:
	                	utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                    		UTENTI_WRITE);
	                    break;
	                case UTENTI_WRITE_SUPER:
	                	utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                			UTENTI_WRITE_SUPER);
	                    break;
	                case UTENTI_DELETE_NOT:
	                	utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
	                			UTENTI_DELETE_NOT);
	                    break;
	                case UTENTI_DELETE:
	                	utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
	                    		UTENTI_DELETE);
	                    break;
	                case UTENTI_DELETE_SUPER:
	                	utentiPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.DELETE,
	                			UTENTI_DELETE_SUPER);
	                    break;
	                //LEADS DATARETENTION
	                case LEADS_DATARETENTION_WRITE_NOT:
	                	leadsDataRetentionPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                			LEADS_DATARETENTION_WRITE_NOT);
	                    break;
	                case LEADS_DATARETENTION_WRITE:
	                	leadsDataRetentionPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                			LEADS_DATARETENTION_WRITE);
	                    break;
	                case LEADS_DATARETENTION_WRITE_SUPER:
	                	leadsDataRetentionPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                			LEADS_DATARETENTION_WRITE_SUPER);
	                    break;
	                //PRIVACY
	                case PRIVACY_WRITE_NOT:
	                	privacyPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                			PRIVACY_WRITE_NOT);
	                    break;
	                case PRIVACY_WRITE:
	                	privacyPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                			PRIVACY_WRITE);
	                    break;
	                case PRIVACY_WRITE_SUPER:
	                	privacyPermissionTypePermissionMap.put(OperatorViewModel.PermissionType.WRITE,
	                			PRIVACY_WRITE_SUPER);
	                    break; 
                }
            }
            sectionPermissionMap.put(OperatorViewModel.MenuSection.DATI_AZIENDA,
                    datiAziendaPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.PREFERENZE, preferenzePermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.CAMPAGNE_MARKETING,
                    campagneMarketingPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.FATTURAZIONE,
                    fatturazionePermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.AMMINISTRAZIONI,
                    amministrazioniPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.ANTI_RICICLAGGIO,
                    antiriciclaggioPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.GESTIONE_PERMESSI,
                    gestionePermessiPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.RICARICA, ricaricaPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.NOMINATIVI, nominativiPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.CLIENTI, clientiPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.GRAFICI, graficiPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.CALENDARIO, calendarioPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.SIMULATORI, simulatoriPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.NOTICE_BOARD, noticeBoardPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.UTENTI, utentiPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.LEADS_DATARETENTION, leadsDataRetentionPermissionTypePermissionMap);
            sectionPermissionMap.put(OperatorViewModel.MenuSection.PRIVACY, privacyPermissionTypePermissionMap);
            operatorViewModel.setSectionPermissionMap(sectionPermissionMap);
            return operatorViewModel;
        }
        return null;
    }

}
