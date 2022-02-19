package com.woonders.lacemscommon.service.impl;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.OperatorViewModelCreator;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.exception.IllegalPermissionStateException;
import com.woonders.lacemscommon.laceenum.PermissionProfile;
import com.woonders.lacemscommon.service.GestionePermessiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Emanuele on 10/05/2017.
 */
@Service
@Transactional(readOnly = true)
public class GestionePermessiServiceImpl implements GestionePermessiService {

    public static final String NAME = "gestionePermessiServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    // TODO da usare il service quando lo sposteremo su common, sta su jsf
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;

    @Transactional
    @Override
    public void saveNewPermission(final OperatorViewModel operatorViewModel, final PermissionProfile permissionProfile) throws IllegalPermissionStateException {

        operatorViewModel.getSectionPermissionMap().forEach((key, value) -> {
            for (final OperatorViewModel.PermissionType permissionType : OperatorViewModel.PermissionType.values()) {
                if (value.get(permissionType) == null) {
                    value.remove(permissionType);
                }
            }
        });

        if (permissionProfile != null) {
            //Setto il permission profile scelto
            operatorViewModel.setPermissionProfile(permissionProfile);
        }

        // CONTROLLO PERMESSI VALIDI
        // CLIENTI
        final Map<OperatorViewModel.PermissionType, Role.RoleName> clientiPermissionMap = operatorViewModel
                .getSectionPermissionMap().get(OperatorViewModel.MenuSection.CLIENTI);
        final Role.RoleName clientiDeleteRoleName = clientiPermissionMap.get(OperatorViewModel.PermissionType.DELETE);
        final Role.RoleName clientiWriteRoleName = clientiPermissionMap.get(OperatorViewModel.PermissionType.WRITE);
        final Role.RoleName clientiReadRoleName = clientiPermissionMap.get(OperatorViewModel.PermissionType.READ);

        // delete
        if (Role.RoleName.CLIENTI_DELETE_OWN.equals(clientiDeleteRoleName)
                && (Role.RoleName.CLIENTI_WRITE_NOT.equals(clientiWriteRoleName)
                || Role.RoleName.CLIENTI_READ_NOT.equals(clientiReadRoleName))) {
            throw new IllegalPermissionStateException(
                    MessageFormat.format("Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                            Role.RoleName.CLIENTI_DELETE_OWN.getValue(), Role.RoleName.CLIENTI_WRITE_NOT.getValue(),
                            Role.RoleName.CLIENTI_READ_NOT.getValue()));
        }
        if (Role.RoleName.CLIENTI_DELETE_ALL.equals(clientiDeleteRoleName)
                && (Role.RoleName.CLIENTI_WRITE_OWN.equals(clientiWriteRoleName)
                || Role.RoleName.CLIENTI_WRITE_NOT.equals(clientiWriteRoleName)
                || Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_NOT.equals(clientiReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\", \"{4}\"",
                    Role.RoleName.CLIENTI_DELETE_ALL.getValue(), Role.RoleName.CLIENTI_WRITE_OWN.getValue(),
                    Role.RoleName.CLIENTI_WRITE_NOT.getValue(), Role.RoleName.CLIENTI_READ_OWN.getValue(),
                    Role.RoleName.CLIENTI_READ_NOT.getValue()));
        }
        if (Role.RoleName.CLIENTI_DELETE_SUPER.equals(clientiDeleteRoleName)
                && (Role.RoleName.CLIENTI_WRITE_ALL.equals(clientiWriteRoleName)
                || Role.RoleName.CLIENTI_WRITE_OWN.equals(clientiWriteRoleName)
                || Role.RoleName.CLIENTI_WRITE_NOT.equals(clientiWriteRoleName)
                || Role.RoleName.CLIENTI_READ_ALL.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_NOT.equals(clientiReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\", \"{4}\", \"{5}\", \"{6}\"",
                    Role.RoleName.CLIENTI_DELETE_SUPER.getValue(), Role.RoleName.CLIENTI_WRITE_ALL.getValue(),
                    Role.RoleName.CLIENTI_WRITE_OWN.getValue(), Role.RoleName.CLIENTI_WRITE_NOT.getValue(),
                    Role.RoleName.CLIENTI_READ_ALL.getValue(), Role.RoleName.CLIENTI_READ_OWN.getValue(),
                    Role.RoleName.CLIENTI_READ_NOT.getValue()));
        }

        // write
        if (Role.RoleName.CLIENTI_WRITE_OWN.equals(clientiWriteRoleName)
                && Role.RoleName.CLIENTI_READ_NOT.equals(clientiReadRoleName)) {
            throw new IllegalPermissionStateException(
                    MessageFormat.format("Non puoi scegliere \"{0}\" se hai scelto: \"{1}\"",
                            Role.RoleName.CLIENTI_WRITE_OWN.getValue(), Role.RoleName.CLIENTI_READ_NOT.getValue()));
        }
        if (Role.RoleName.CLIENTI_WRITE_ALL.equals(clientiWriteRoleName)
                && (Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_NOT.equals(clientiReadRoleName))) {
            throw new IllegalPermissionStateException(
                    MessageFormat.format("Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                            Role.RoleName.CLIENTI_WRITE_ALL.getValue(), Role.RoleName.CLIENTI_READ_OWN.getValue(),
                            Role.RoleName.CLIENTI_READ_NOT.getValue()));
        }
        if (Role.RoleName.CLIENTI_WRITE_SUPER.equals(clientiWriteRoleName)
                && (Role.RoleName.CLIENTI_READ_ALL.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName)
                || Role.RoleName.CLIENTI_READ_NOT.equals(clientiReadRoleName))) {
            throw new IllegalPermissionStateException(
                    MessageFormat.format("Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\"",
                            Role.RoleName.CLIENTI_WRITE_SUPER.getValue(), Role.RoleName.CLIENTI_READ_ALL.getValue(),
                            Role.RoleName.CLIENTI_READ_OWN.getValue(), Role.RoleName.CLIENTI_READ_NOT.getValue()));
        }

        // NOMINATIVI
        final Map<OperatorViewModel.PermissionType, Role.RoleName> nominativiPermissionMap = operatorViewModel
                .getSectionPermissionMap().get(OperatorViewModel.MenuSection.NOMINATIVI);
        final Role.RoleName nominativiDeleteRoleName = nominativiPermissionMap.get(OperatorViewModel.PermissionType.DELETE);
        final Role.RoleName nominativiWriteRoleName = nominativiPermissionMap.get(OperatorViewModel.PermissionType.WRITE);
        final Role.RoleName nominativiReadRoleName = nominativiPermissionMap.get(OperatorViewModel.PermissionType.READ);

        // delete
        if (Role.RoleName.NOMINATIVI_DELETE_OWN.equals(nominativiDeleteRoleName)
                && (Role.RoleName.NOMINATIVI_WRITE_NOT.equals(nominativiWriteRoleName)
                || Role.RoleName.NOMINATIVI_READ_NOT.equals(nominativiReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                    Role.RoleName.NOMINATIVI_DELETE_OWN.getValue(), Role.RoleName.NOMINATIVI_WRITE_NOT.getValue(),
                    Role.RoleName.NOMINATIVI_READ_NOT.getValue()));
        }
        if (Role.RoleName.NOMINATIVI_DELETE_ALL.equals(nominativiDeleteRoleName)
                && (Role.RoleName.NOMINATIVI_WRITE_OWN.equals(nominativiWriteRoleName)
                || Role.RoleName.NOMINATIVI_WRITE_NOT.equals(nominativiWriteRoleName)
                || Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_NOT.equals(nominativiReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\", \"{4}\"",
                    Role.RoleName.NOMINATIVI_DELETE_ALL.getValue(), Role.RoleName.NOMINATIVI_WRITE_OWN.getValue(),
                    Role.RoleName.NOMINATIVI_WRITE_NOT.getValue(), Role.RoleName.NOMINATIVI_READ_OWN.getValue(),
                    Role.RoleName.NOMINATIVI_READ_NOT.getValue()));
        }
        if (Role.RoleName.NOMINATIVI_DELETE_SUPER.equals(nominativiDeleteRoleName)
                && (Role.RoleName.NOMINATIVI_WRITE_ALL.equals(nominativiWriteRoleName)
                || Role.RoleName.NOMINATIVI_WRITE_OWN.equals(nominativiWriteRoleName)
                || Role.RoleName.NOMINATIVI_WRITE_NOT.equals(nominativiWriteRoleName)
                || Role.RoleName.NOMINATIVI_READ_ALL.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_NOT.equals(nominativiReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\", \"{4}\", \"{5}\", \"{6}\"",
                    Role.RoleName.NOMINATIVI_DELETE_SUPER.getValue(), Role.RoleName.NOMINATIVI_WRITE_ALL.getValue(),
                    Role.RoleName.NOMINATIVI_WRITE_OWN.getValue(), Role.RoleName.NOMINATIVI_WRITE_NOT.getValue(),
                    Role.RoleName.NOMINATIVI_READ_ALL.getValue(), Role.RoleName.NOMINATIVI_READ_OWN.getValue(),
                    Role.RoleName.NOMINATIVI_READ_NOT.getValue()));
        }

        // write
        if (Role.RoleName.NOMINATIVI_WRITE_OWN.equals(nominativiWriteRoleName)
                && Role.RoleName.NOMINATIVI_READ_NOT.equals(nominativiReadRoleName)) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto: \"{1}\"", Role.RoleName.NOMINATIVI_WRITE_OWN.getValue(),
                    Role.RoleName.NOMINATIVI_READ_NOT.getValue()));
        }
        if (Role.RoleName.NOMINATIVI_WRITE_ALL.equals(nominativiWriteRoleName)
                && (Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_NOT.equals(nominativiReadRoleName))) {
            throw new IllegalPermissionStateException(
                    MessageFormat.format("Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                            Role.RoleName.NOMINATIVI_WRITE_ALL.getValue(), Role.RoleName.NOMINATIVI_READ_OWN.getValue(),
                            Role.RoleName.NOMINATIVI_READ_NOT.getValue()));
        }
        if (Role.RoleName.NOMINATIVI_WRITE_SUPER.equals(nominativiWriteRoleName)
                && (Role.RoleName.NOMINATIVI_READ_ALL.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName)
                || Role.RoleName.NOMINATIVI_READ_NOT.equals(nominativiReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\"",
                    Role.RoleName.NOMINATIVI_WRITE_ALL.getValue(), Role.RoleName.NOMINATIVI_READ_ALL.getValue(),
                    Role.RoleName.NOMINATIVI_READ_OWN.getValue(), Role.RoleName.NOMINATIVI_READ_NOT.getValue()));
        }

        // FATTURAZIONE
        final Map<OperatorViewModel.PermissionType, Role.RoleName> fatturazionePermissionMap = operatorViewModel
                .getSectionPermissionMap().get(OperatorViewModel.MenuSection.FATTURAZIONE);

        final Role.RoleName fatturazioneWriteRoleName = fatturazionePermissionMap.get(OperatorViewModel.PermissionType.WRITE);
        final Role.RoleName fatturazioneReadRoleName = fatturazionePermissionMap.get(OperatorViewModel.PermissionType.READ);

        if (Role.RoleName.FATTURAZIONE_WRITE.equals(fatturazioneWriteRoleName)
                && Role.RoleName.FATTURAZIONE_READ_OWN.equals(fatturazioneReadRoleName)) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto: \"{1}\"", Role.RoleName.FATTURAZIONE_WRITE.getValue(),
                    Role.RoleName.FATTURAZIONE_READ_OWN.getValue()));
        }

        if (Role.RoleName.FATTURAZIONE_WRITE_SUPER.equals(fatturazioneWriteRoleName)
                && (Role.RoleName.FATTURAZIONE_READ_ALL.equals(fatturazioneReadRoleName)
                || Role.RoleName.FATTURAZIONE_READ_OWN.equals(fatturazioneReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                    Role.RoleName.FATTURAZIONE_WRITE_SUPER.getValue(), Role.RoleName.FATTURAZIONE_READ_ALL.getValue(),
                    Role.RoleName.FATTURAZIONE_READ_OWN.getValue()));
        }

        // ANTIRICICLAGGIO
        final Map<OperatorViewModel.PermissionType, Role.RoleName> antiriciclaggioPermissionMap = operatorViewModel
                .getSectionPermissionMap().get(OperatorViewModel.MenuSection.ANTI_RICICLAGGIO);

        final Role.RoleName antiriciclaggioReadRoleName = antiriciclaggioPermissionMap
                .get(OperatorViewModel.PermissionType.READ);

        if (Role.RoleName.ANTI_RICICLAGGIO_READ.equals(antiriciclaggioReadRoleName)
                && Role.RoleName.CLIENTI_READ_NOT.equals(clientiReadRoleName)) {
            throw new IllegalPermissionStateException(
                    MessageFormat.format("Non puoi scegliere \"{0}\" se hai scelto: \"{1}\"",
                            Role.RoleName.ANTI_RICICLAGGIO_READ.getValue(), Role.RoleName.CLIENTI_READ_NOT.getValue()));
        }

        // CALENDARIO
        final Map<OperatorViewModel.PermissionType, Role.RoleName> calendarioPermissionMap = operatorViewModel
                .getSectionPermissionMap().get(OperatorViewModel.MenuSection.CALENDARIO);
        final Role.RoleName calendarioDeleteRoleName = calendarioPermissionMap.get(OperatorViewModel.PermissionType.DELETE);
        final Role.RoleName calendarioWriteRoleName = calendarioPermissionMap.get(OperatorViewModel.PermissionType.WRITE);
        final Role.RoleName calendarioReadRoleName = calendarioPermissionMap.get(OperatorViewModel.PermissionType.READ);

        // delete
        if (Role.RoleName.CALENDARIO_DELETE_ALL.equals(calendarioDeleteRoleName)
                && (Role.RoleName.CALENDARIO_WRITE_OWN.equals(calendarioWriteRoleName)
                || Role.RoleName.CALENDARIO_READ_OWN.equals(calendarioReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                    Role.RoleName.CALENDARIO_DELETE_ALL.getValue(), Role.RoleName.CALENDARIO_WRITE_OWN.getValue(),
                    Role.RoleName.CALENDARIO_READ_OWN.getValue()));
        }

        if (Role.RoleName.CALENDARIO_DELETE_SUPER.equals(calendarioDeleteRoleName)
                && (Role.RoleName.CALENDARIO_WRITE_ALL.equals(calendarioWriteRoleName)
                || Role.RoleName.CALENDARIO_WRITE_OWN.equals(calendarioWriteRoleName)
                || Role.RoleName.CALENDARIO_READ_ALL.equals(calendarioWriteRoleName)
                || Role.RoleName.CALENDARIO_READ_OWN.equals(calendarioReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\", \"{4}\"",
                    Role.RoleName.CALENDARIO_DELETE_SUPER.getValue(), Role.RoleName.CALENDARIO_WRITE_ALL.getValue(),
                    Role.RoleName.CALENDARIO_WRITE_OWN.getValue(), Role.RoleName.CALENDARIO_READ_ALL.getValue(),
                    Role.RoleName.CALENDARIO_READ_OWN.getValue()));
        }

        // write
        if (Role.RoleName.CALENDARIO_WRITE_ALL.equals(calendarioWriteRoleName)
                && Role.RoleName.CALENDARIO_READ_OWN.equals(calendarioReadRoleName)) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto: \"{1}\"", Role.RoleName.CALENDARIO_WRITE_ALL.getValue(),
                    Role.RoleName.CALENDARIO_READ_OWN.getValue()));
        }

        if (Role.RoleName.CALENDARIO_WRITE_SUPER.equals(calendarioWriteRoleName)
                && (Role.RoleName.CALENDARIO_READ_ALL.equals(calendarioReadRoleName)
                || Role.RoleName.CALENDARIO_READ_OWN.equals(calendarioReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                    Role.RoleName.CALENDARIO_WRITE_SUPER.getValue(), Role.RoleName.CALENDARIO_READ_ALL.getValue(),
                    Role.RoleName.CALENDARIO_READ_OWN.getValue()));
        }

        // SIMULATORI
        final Map<OperatorViewModel.PermissionType, Role.RoleName> simulatoriPermissionMap = operatorViewModel
                .getSectionPermissionMap().get(OperatorViewModel.MenuSection.SIMULATORI);
        final Role.RoleName simulatoriDeleteRoleName = simulatoriPermissionMap.get(OperatorViewModel.PermissionType.DELETE);
        final Role.RoleName simulatoriWriteRoleName = simulatoriPermissionMap.get(OperatorViewModel.PermissionType.WRITE);
        final Role.RoleName simulatoriReadRoleName = simulatoriPermissionMap.get(OperatorViewModel.PermissionType.READ);

        // delete
        if (Role.RoleName.SIMULATORI_DELETE_OWN.equals(simulatoriDeleteRoleName)
                && (Role.RoleName.SIMULATORI_WRITE_NOT.equals(simulatoriWriteRoleName)
                || Role.RoleName.SIMULATORI_READ_NOT.equals(simulatoriReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                    Role.RoleName.SIMULATORI_DELETE_OWN.getValue(), Role.RoleName.SIMULATORI_WRITE_NOT.getValue(),
                    Role.RoleName.SIMULATORI_READ_NOT.getValue()));
        }
        if (Role.RoleName.SIMULATORI_DELETE_ALL.equals(simulatoriDeleteRoleName)
                && (Role.RoleName.SIMULATORI_WRITE_OWN.equals(simulatoriWriteRoleName)
                || Role.RoleName.SIMULATORI_WRITE_NOT.equals(simulatoriWriteRoleName)
                || Role.RoleName.SIMULATORI_READ_OWN.equals(simulatoriReadRoleName)
                || Role.RoleName.SIMULATORI_READ_NOT.equals(simulatoriReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\", \"{4}\"",
                    Role.RoleName.SIMULATORI_DELETE_ALL.getValue(), Role.RoleName.SIMULATORI_WRITE_OWN.getValue(),
                    Role.RoleName.SIMULATORI_WRITE_NOT.getValue(), Role.RoleName.SIMULATORI_READ_OWN.getValue(),
                    Role.RoleName.SIMULATORI_READ_NOT.getValue()));
        }
        if (Role.RoleName.SIMULATORI_DELETE_SUPER.equals(simulatoriDeleteRoleName)
                && (Role.RoleName.SIMULATORI_WRITE_ALL.equals(simulatoriWriteRoleName)
                || Role.RoleName.SIMULATORI_WRITE_OWN.equals(simulatoriWriteRoleName)
                || Role.RoleName.SIMULATORI_WRITE_NOT.equals(simulatoriWriteRoleName)
                || Role.RoleName.SIMULATORI_READ_ALL.equals(simulatoriReadRoleName)
                || Role.RoleName.SIMULATORI_READ_OWN.equals(simulatoriReadRoleName)
                || Role.RoleName.SIMULATORI_READ_NOT.equals(simulatoriReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\", \"{4}\", \"{5}\", \"{6}\"",
                    Role.RoleName.SIMULATORI_DELETE_SUPER.getValue(), Role.RoleName.SIMULATORI_WRITE_ALL.getValue(),
                    Role.RoleName.SIMULATORI_WRITE_OWN.getValue(), Role.RoleName.SIMULATORI_WRITE_NOT.getValue(),
                    Role.RoleName.SIMULATORI_READ_ALL.getValue(), Role.RoleName.SIMULATORI_READ_OWN.getValue(),
                    Role.RoleName.SIMULATORI_READ_NOT.getValue()));
        }

        // write
        if (Role.RoleName.SIMULATORI_WRITE_OWN.equals(simulatoriWriteRoleName)
                && Role.RoleName.SIMULATORI_READ_NOT.equals(simulatoriReadRoleName)) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto: \"{1}\"", Role.RoleName.SIMULATORI_WRITE_OWN.getValue(),
                    Role.RoleName.SIMULATORI_READ_NOT.getValue()));
        }
        if (Role.RoleName.SIMULATORI_WRITE_ALL.equals(simulatoriWriteRoleName)
                && (Role.RoleName.SIMULATORI_READ_OWN.equals(simulatoriReadRoleName)
                || Role.RoleName.SIMULATORI_READ_NOT.equals(simulatoriReadRoleName))) {
            throw new IllegalPermissionStateException(
                    MessageFormat.format("Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\"",
                            Role.RoleName.SIMULATORI_WRITE_ALL.getValue(), Role.RoleName.SIMULATORI_READ_OWN.getValue(),
                            Role.RoleName.SIMULATORI_READ_NOT.getValue()));
        }
        if (Role.RoleName.SIMULATORI_WRITE_SUPER.equals(simulatoriWriteRoleName)
                && (Role.RoleName.SIMULATORI_READ_ALL.equals(simulatoriReadRoleName)
                || Role.RoleName.SIMULATORI_READ_OWN.equals(simulatoriReadRoleName)
                || Role.RoleName.SIMULATORI_READ_NOT.equals(simulatoriReadRoleName))) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Non puoi scegliere \"{0}\" se hai scelto uno tra: \"{1}\", \"{2}\", \"{3}\"",
                    Role.RoleName.SIMULATORI_WRITE_ALL.getValue(), Role.RoleName.SIMULATORI_READ_ALL.getValue(),
                    Role.RoleName.SIMULATORI_READ_OWN.getValue(), Role.RoleName.SIMULATORI_READ_NOT.getValue()));
        }

        // RICEZIONE LEAD
        if (operatorViewModel.isReceiveLeadEnabled()
                && Role.RoleName.NOMINATIVI_WRITE_NOT.equals(nominativiWriteRoleName)) {
            throw new IllegalPermissionStateException(MessageFormat.format(
                    "Hai scelto \"{0}\" ma l\''operatore riceve attualmente i Lead. Per potergli togliere il permesso scelto"
                            + " devi prima disabilitargli la ricezione dei Lead tramite la sezione Preferenze.",
                    Role.RoleName.NOMINATIVI_WRITE_NOT.getValue()));
        }

        // NoticeBoard
        final Map<OperatorViewModel.PermissionType, Role.RoleName> noticeBoardPermissionMap = operatorViewModel
                .getSectionPermissionMap().get(OperatorViewModel.MenuSection.NOTICE_BOARD);

        final Role.RoleName noticeBoardReadRoleName = noticeBoardPermissionMap
                .get(OperatorViewModel.PermissionType.READ);
        final Role.RoleName noticeBoardWriteRoleName = noticeBoardPermissionMap
                .get(OperatorViewModel.PermissionType.WRITE);

        if (Role.RoleName.NOTICE_BOARD_WRITE_SUPER.equals(noticeBoardWriteRoleName)
                && Role.RoleName.NOTICE_BOARD_READ.equals(noticeBoardReadRoleName)) {
            throw new IllegalPermissionStateException(
                    MessageFormat.format("Non puoi scegliere \"{0}\" se hai scelto: \"{1}\"",
                            Role.RoleName.NOTICE_BOARD_WRITE_SUPER.getValue(), Role.RoleName.NOTICE_BOARD_READ.getValue()));
        }

        // PERMESSI
        final Map<OperatorViewModel.PermissionType, Role.RoleName> gestionePermessiPermissionMap = operatorViewModel
                .getSectionPermissionMap().get(OperatorViewModel.MenuSection.GESTIONE_PERMESSI);
        final Role.RoleName gestionePermessiWriteRoleName = gestionePermessiPermissionMap
                .get(OperatorViewModel.PermissionType.WRITE);

        // controllo che ci sia almeno un altro operatore con i permessi per
        // cambiare i permessi
        // deve essercene almeno uno con WRITE della stessa agenzia dell
        // operatore che si sta modificando
        // oppure uno con permessi WRITE_SUPER
        if (Role.RoleName.GESTIONE_PERMESSI_WRITE_NOT.equals(gestionePermessiWriteRoleName)) {
            final Collection<Role.RoleName> roleNameToAvoidCollection = new LinkedList<>();
            roleNameToAvoidCollection.add(Role.RoleName.ANTI_RICICLAGGIO_OUT);
            final List<Operator> operatorList = operatorRepository.findByRoleNotIn(roleNameToAvoidCollection);
            operatorList.removeIf(operator -> operator.getUsername().equals(operatorViewModel.getUsername()));
            boolean isOkToSave = false;
            for (final Operator operator : operatorList) {
                final List<Role.RoleName> roleNameList = operator.getRole().stream().map(Role::getRoleName)
                        .collect(Collectors.toList());
                if ((operator.getAzienda().getId() == operatorViewModel.getAzienda().getId()
                        && roleNameList.contains(Role.RoleName.GESTIONE_PERMESSI_WRITE))
                        || roleNameList.contains(Role.RoleName.GESTIONE_PERMESSI_WRITE_SUPER)) {
                    isOkToSave = true;
                    break;
                }
            }
            if (!isOkToSave) {
                throw new IllegalPermissionStateException(MessageFormat.format(
                        "Non puoi rimuovere \"{0}\" poiché non ci sono altri operatori con questo permesso."
                                + "Se vuoi rimuoverlo, devi prima assegnare questo permesso ad un altro operatore",
                        Role.RoleName.GESTIONE_PERMESSI_WRITE.getValue()));
            }
        }

        operatorRepository.save(operatorViewModelCreator.createModel(operatorViewModel));

    }
}
