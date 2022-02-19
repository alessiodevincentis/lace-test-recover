package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.laceenum.PermissionProfile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by emanuele on 09/01/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class OperatorViewModel extends AbstractBaseViewModel {

    private long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int smsBalance;
    private boolean receiveLeadEnabled;
    private long leadRicevuti;
    private AziendaViewModel azienda;
    private List<RoleViewModel> roleViewModelList;
    private Map<MenuSection, Map<PermissionType, Role.RoleName>> sectionPermissionMap;
    private PermissionProfile permissionProfile;

    @Override
    protected long getIdToCompare() {
        return id;
    }

    public enum MenuSection {
        DATI_AZIENDA("Dati azienda"), PREFERENZE("Preferenze"), CLIENTI("Clienti"), NOMINATIVI(
                "Nominativi"), CAMPAGNE_MARKETING("Campagne Marketing"), FATTURAZIONE("Fatturazione"), AMMINISTRAZIONI(
                "Amministrazioni"), ANTI_RICICLAGGIO("Anti Riciclaggio"), GESTIONE_PERMESSI(
                "Gestione Permessi"), RICARICA(
                "Ricarica"), GRAFICI("Grafici"), CALENDARIO("Calendario"), SIMULATORI("Simulatori"),
        		NOTICE_BOARD("Bacheca Avvisi"), UTENTI("Gestione Utenti"), 
        		LEADS_DATARETENTION("Gestione Policy Leads Data Retention"), PRIVACY("Gestione Privacy");

        private final String value;

        MenuSection(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Permission {
        READ_NOT("Non può leggere"), READ_OWN("Può leggere solo i propri"), READ_ALL("Può leggere tutto"), WRITE_NOT(
                "Non può scrivere"), WRITE_OWN("Può scrivere e modificare solo i propri"), WRITE_ALL(
                "Può scrivere e modificare tutto"), DELETE_NOT("Non può eliminare"), DELETE_OWN(
                "Può eliminare solo i propri"), DELETE_ALL("Può eliminare tutto");
        private final String value;

        Permission(final String value) {
            this.value = value;
        }

        public static List<Permission> getReadPermission() {
            final List<Permission> readPermission = new ArrayList<>();
            readPermission.add(READ_NOT);
            readPermission.add(READ_OWN);
            readPermission.add(READ_ALL);
            return readPermission;
        }

        public static List<Permission> getWritePermission() {
            final List<Permission> writePermission = new ArrayList<>();
            writePermission.add(WRITE_NOT);
            writePermission.add(WRITE_OWN);
            writePermission.add(WRITE_ALL);
            return writePermission;
        }

        public static List<Permission> getDeletePermission() {
            final List<Permission> deletePermission = new ArrayList<>();
            deletePermission.add(DELETE_NOT);
            deletePermission.add(DELETE_OWN);
            deletePermission.add(DELETE_ALL);
            return deletePermission;
        }

        public String getValue() {
            return value;
        }

    }

    public enum PermissionType {
        READ("Permessi di lettura"), WRITE("Permessi di scrittura"), DELETE("Permessi di cancellazione");

        private final String value;

        private PermissionType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
