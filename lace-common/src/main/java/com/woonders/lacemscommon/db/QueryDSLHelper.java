package com.woonders.lacemscommon.db;

import com.querydsl.core.BooleanBuilder;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Emanuele on 31/01/2017.
 */
@Component
public class QueryDSLHelper {

    public BooleanBuilder createFilterPredicate(final Map<TableField, Object> filters) {
        final BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (filters != null) {
            if (filters.containsKey(TableField.OPERATORE_NOMINATIVO)) {
                final List<String> operatoreList = Arrays.asList((String[]) filters.get(TableField.OPERATORE_NOMINATIVO));
                // Questo serve perche qualsiasi filtro si seleziona, essendo
                // questo
                // un filtro con lista e checkbox, viene sempre inviato, anche
                // se
                // non si e selezionato niente
                if (!operatoreList.isEmpty()) {
                    booleanBuilder.and(QCliente.cliente.operatoreNominativo.username.in(operatoreList));
                }
            } else if (filters.containsKey(TableField.OPERATORE_NOMINATIVO_CLIENTE_VIEW_MODEL)) {
                final List<String> operatoreList = Arrays.asList((String[]) filters.get(TableField.OPERATORE_NOMINATIVO_CLIENTE_VIEW_MODEL));
                // Questo serve perche qualsiasi filtro si seleziona, essendo
                // questo
                // un filtro con lista e checkbox, viene sempre inviato, anche
                // se
                // non si e selezionato niente
                if (!operatoreList.isEmpty()) {
                    booleanBuilder.and(QCliente.cliente.operatoreNominativo.username.in(operatoreList));
                }
            }
            if (filters.containsKey(TableField.OPERATORE)) {
                final List<String> operatoreList = Arrays.asList((String[]) filters.get(TableField.OPERATORE));
                // Questo serve perche qualsiasi filtro si seleziona, essendo
                // questo
                // un filtro con lista e checkbox, viene sempre inviato, anche
                // se
                // non si e selezionato niente
                if (!operatoreList.isEmpty()) {
                    booleanBuilder.and(QPratica.pratica.operatore.username.in(operatoreList));
                }
            } else if (filters.containsKey(TableField.OPERATORE_PRATICA_VIEW_MODEL)) {
                final List<String> operatoreList = Arrays.asList((String[]) filters.get(TableField.OPERATORE_PRATICA_VIEW_MODEL));
                if (!operatoreList.isEmpty()) {
                    booleanBuilder.and(QPratica.pratica.operatore.username.in(operatoreList));
                }
            }
            if (filters.containsKey(TableField.PROVENIENZA)) {
                final String provenienza = (String) filters.get(TableField.PROVENIENZA);
                // questo in teoria non serve, ma l'ho messo lo stesso, per
                // ora...
                if (provenienza != null && !provenienza.isEmpty()) {
                    booleanBuilder.and(QCliente.cliente.provenienza.eq(provenienza));
                }
            }
            if (filters.containsKey(TableField.SIMULATOR_TABLE_STATUS)) {
                final SimulatorTableViewModel.SimulatorTableStatus simulatorTableStatus =
                        SimulatorTableViewModel.SimulatorTableStatus.valueOf(filters.get(TableField.SIMULATOR_TABLE_STATUS).toString());
                if (simulatorTableStatus.isEnabled()) {
                    booleanBuilder.and(QSimulatorTable.simulatorTable.enabled.isTrue());
                } else {
                    booleanBuilder.and(QSimulatorTable.simulatorTable.enabled.isFalse());
                }
            }
            if (filters.containsKey(TableField.SIMULATOR_TABLE_TYPE)) {
                final Pratica.TipoPratica simulatorTableType = Pratica.TipoPratica.valueOf(filters.get(TableField.SIMULATOR_TABLE_TYPE).toString());
                booleanBuilder.and(QSimulatorTable.simulatorTable.simulatorTableType.eq(simulatorTableType));
            }
            if (filters.containsKey(TableField.SIMULATOR_JOB_TYPE_LIST)) {
                final Object[] impiegoObjectArray = (Object[]) filters.get(TableField.SIMULATOR_JOB_TYPE_LIST);
                if (impiegoObjectArray != null && impiegoObjectArray.length > 0) {
                    final Impiego[] impiegoArray = Arrays.stream((Object[]) filters.get(TableField.SIMULATOR_JOB_TYPE_LIST)).toArray(Impiego[]::new);
                    booleanBuilder.and(QSimulatorTable.simulatorTable.jobTypeSet.any().in(impiegoArray));
                }
            }
            //provincia clienti/nominativi
            if (filters.containsKey(TableField.PROVINCIA_RESIDENZA)) {
                final String provincia = (String) filters.get(TableField.PROVINCIA_RESIDENZA);
                if (!StringUtils.isEmpty(provincia)) {
                    booleanBuilder.and(QCliente.cliente.residenza.provResidenza.equalsIgnoreCase(provincia));
                }
            } else if (filters.containsKey(TableField.PROVINCIA_RESIDENZA_RESIDENZA_VIEW_MODEL)) {
                final String provincia = (String) filters.get(TableField.PROVINCIA_RESIDENZA_RESIDENZA_VIEW_MODEL);
                if (!StringUtils.isEmpty(provincia)) {
                    booleanBuilder.and(QCliente.cliente.residenza.provResidenza.equalsIgnoreCase(provincia));
                }
            }

            if (filters.containsKey(TableField.FORNITORE_LEAD)) {
                final String fornitoreLead = (String) filters.get(TableField.FORNITORE_LEAD);
                if (!StringUtils.isEmpty(fornitoreLead)) {
                    booleanBuilder.and(QCliente.cliente.fornitoreLead.containsIgnoreCase(fornitoreLead));
                }
            }
        }
        return booleanBuilder;
    }

    public QSort createSortOrder(final TableField tableField, final SortOrder sortOrder) {
        if (tableField != null) {
            switch (tableField) {
                case OPERATORE_PRATICA_VIEW_MODEL:
                case OPERATORE:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.operatore.username.asc());
                    }
                    return new QSort(QPratica.pratica.operatore.username.desc());
                case COGNOME_CLIENTE_VIEW_MODEL:
                case COGNOME:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QCliente.cliente.cognome.asc());
                    }
                    return new QSort(QCliente.cliente.cognome.desc());
                case DATA_DI_NASCITA_CLIENTE_VIEW_MODEL:
                case DATA_DI_NASCITA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QCliente.cliente.dataNascita.asc());
                    }
                    return new QSort(QCliente.cliente.dataNascita.desc());
                case TIPO_PRATICA_PRATICA_VIEW_MODEL:
                case TIPO_PRATICA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.tipoPratica.asc());
                    }
                    return new QSort(QPratica.pratica.tipoPratica.desc());
                case RATA_PRATICA_VIEW_MODEL:
                case RATA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.rata.asc());
                    }
                    return new QSort(QPratica.pratica.rata.desc());
                case DURATA_PRATICA_VIEW_MODEL:
                case DURATA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.durata.asc());
                    }
                    return new QSort(QPratica.pratica.durata.desc());
                case IMPORTO_EROGATO_PRATICA_VIEW_MODEL:
                case IMPORTO_EROGATO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.importoErogato.asc());
                    }
                    return new QSort(QPratica.pratica.importoErogato.desc());
                case DATA_CARICAMENTO_PRATICA_VIEW_MODEL:
                case DATA_CARICAMENTO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataCaricamento.asc());
                    }
                    return new QSort(QPratica.pratica.dataCaricamento.desc());
                case IMPIEGO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QCliente.cliente.impiego.asc());
                    }
                    return new QSort(QCliente.cliente.impiego.desc());
                case PROVENIENZA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QCliente.cliente.provenienza.asc());
                    }
                    return new QSort(QCliente.cliente.provenienza.desc());
                case DESCRIZIONE_PROVENIENZA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QCliente.cliente.provenienzaDesc.asc());
                    }
                    return new QSort(QCliente.cliente.provenienzaDesc.desc());
                case DATA_INSERIMENTO_CLIENTE_VIEW_MODEL:
                case DATA_INSERIMENTO_CLIENTE:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QCliente.cliente.dataIns.asc());
                    }
                    return new QSort(QCliente.cliente.dataIns.desc());
                case TIPO_TRATTENUTA_VIEW_MODEL:
                case TIPO_TRATTENUTA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QTrattenute.trattenute.tipoTrat.asc());
                    }
                    return new QSort(QTrattenute.trattenute.tipoTrat.desc());
                case RATA_TRATTENUTA_VIEW_MODEL:
                case RATA_TRATTENUTA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QTrattenute.trattenute.rataTrat.asc());
                    }
                    return new QSort(QTrattenute.trattenute.rataTrat.desc());
                case DURATA_TRATTENUTA_VIEW_MODEL:
                case DURATA_TRATTENUTA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QTrattenute.trattenute.durataTrat.asc());
                    }
                    return new QSort(QTrattenute.trattenute.durataTrat.desc());
                case SCADENZA_TRATTENUTA_VIEW_MODEL:
                case SCADENZA_TRATTENUTA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QTrattenute.trattenute.scadenzaTrat.asc());
                    }
                    return new QSort(QTrattenute.trattenute.scadenzaTrat.desc());
                case DATA_RINNOVO_TRATTENUTA_VIEW_MODEL:
                case DATA_RINNOVO_TRATTENUTA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QTrattenute.trattenute.dataRinnovoTrat.asc());
                    }
                    return new QSort(QTrattenute.trattenute.dataRinnovoTrat.desc());
                case DATA_RECALL_NOMINATIVO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QCliente.cliente.dataRecallNominativo.asc());
                    }
                    return new QSort(QCliente.cliente.dataRecallNominativo.desc());
                case STATO_NOMINATIVO_CLIENTE_VIEW_MODEL:
                case STATO_NOMINATIVO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QCliente.cliente.statoNominativo.asc());
                    }
                    return new QSort(QCliente.cliente.statoNominativo.desc());
                case LOG_EXECUTION_DATE_TIME:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QNominativoLog.nominativoLog.executionDateTime.asc());
                    }
                    return new QSort(QNominativoLog.nominativoLog.executionDateTime.desc());
                case RICARICA_DATE_TIME:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QRicaricaComunicazione.ricaricaComunicazione.dateTime.asc());
                    }
                    return new QSort(QRicaricaComunicazione.ricaricaComunicazione.dateTime.desc());
                case RINNOVO_ESTINZIONE:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QEstinzione.estinzione.dataRinnovoEstinzione.asc());
                    }
                    return new QSort(QEstinzione.estinzione.dataRinnovoEstinzione.desc());
                case TIPO_ESTINZIONE:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QEstinzione.estinzione.tipoEstinzione.asc());
                    }
                    return new QSort(QEstinzione.estinzione.tipoEstinzione.desc());
                case RATA_ESTINZIONE:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QEstinzione.estinzione.rataEstinzione.asc());
                    }
                    return new QSort(QEstinzione.estinzione.rataEstinzione.desc());
                case DURATA_ESTINZIONE:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QEstinzione.estinzione.durataEstinzione.asc());
                    }
                    return new QSort(QEstinzione.estinzione.durataEstinzione.desc());
                case ISTITUTO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QEstinzione.estinzione.istitutoEst.asc());
                    }
                    return new QSort(QEstinzione.estinzione.istitutoEst.desc());
                case NOTIFICA_ESTINZIONE:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QEstinzione.estinzione.dataNotificaConteggioEstinzione.asc());
                    }
                    return new QSort(QEstinzione.estinzione.dataNotificaConteggioEstinzione.desc());
                case NOTIFICA_STATO_PRATICA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataNotificaStatoPratica.asc());
                    }
                    return new QSort(QPratica.pratica.dataNotificaStatoPratica.desc());
                case DATA_ISTRUTTORIA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataIstruttoria.asc());
                    }
                    return new QSort(QPratica.pratica.dataIstruttoria.desc());
                case DATA_DELIBERA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataDelibera.asc());
                    }
                    return new QSort(QPratica.pratica.dataDelibera.desc());
                case DATA_FIRMA_CONTRATTI:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataFirmaContratti.asc());
                    }
                    return new QSort(QPratica.pratica.dataFirmaContratti.desc());
                case DATA_ISTRUITA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataIstruita.asc());
                    }
                    return new QSort(QPratica.pratica.dataIstruita.desc());
                case DATA_LIQUIDAZIONE:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataLiquidazione.asc());
                    }
                    return new QSort(QPratica.pratica.dataLiquidazione.desc());
                case DATE_TIME_CREATION_NOTICE_BOARD:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QNoticeBoard.noticeBoard.dateTimeCreation.asc());
                    }
                    return new QSort(QNoticeBoard.noticeBoard.dateTimeCreation.desc());
                case DATA_PERFEZIONAMENTO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataPerf.asc());
                    }
                    return new QSort(QPratica.pratica.dataPerf.desc());
                case DATA_DECORRENZA:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.decorrenza.asc());
                    }
                    return new QSort(QPratica.pratica.decorrenza.desc());
                case TIPO_OPERAZIONE_PREVENTIVO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPreventivo.preventivo.tipoPratica.asc());
                    }
                    return new QSort(QPreventivo.preventivo.tipoPratica.desc());
                case DATA_CREAZIONE_PREVENTIVO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPreventivo.preventivo.dataCreazione.asc());
                    }
                    return new QSort(QPreventivo.preventivo.dataCreazione.desc());
                case DURATA_PREVENTIVO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPreventivo.preventivo.durata.asc());
                    }
                    return new QSort(QPreventivo.preventivo.durata.desc());
                case RATA_PREVENTIVO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPreventivo.preventivo.rata.asc());
                    }
                    return new QSort(QPreventivo.preventivo.rata.desc());
                case IMPORTO_EROGATO_PREVENTIVO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPreventivo.preventivo.importo.asc());
                    }
                    return new QSort(QPreventivo.preventivo.importo.desc());
                case TIPOLOGIA_PREVENTIVO:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPreventivo.preventivo.tipologiaPreventivo.asc());
                    }
                    return new QSort(QPreventivo.preventivo.tipologiaPreventivo.desc());
                case DATA_RINNOVO_PRATICA_VIEW_MODEL:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataRinnovo.asc());
                    }
                    return new QSort(QPratica.pratica.dataRinnovo.desc());
                case DATA_RECALL_PRATICA_VIEW_MODEL:
                    if (SortOrder.ASCENDING.equals(sortOrder)) {
                        return new QSort(QPratica.pratica.dataRecall.asc());
                    }
                    return new QSort(QPratica.pratica.dataRecall.desc());
            }
        }

        return null;
    }

    public enum SortOrder {
        ASCENDING, DESCENDING, UNSORTED
    }

    @Getter
    public enum TableField {

        OPERATORE("operatore.username"), OPERATORE_PRATICA_VIEW_MODEL("praticaViewModel.operatore.username"),
        COGNOME_CLIENTE_VIEW_MODEL("clienteViewModel.cognome"), COGNOME("cognome"), DATA_DI_NASCITA("dataNascita"),
        DATA_DI_NASCITA_CLIENTE_VIEW_MODEL("clienteViewModel.dataNascita"),
        TIPO_PRATICA_PRATICA_VIEW_MODEL("praticaViewModel.tipoPratica"),
        TIPO_PRATICA("tipoPratica"), RATA("rata"), RATA_PRATICA_VIEW_MODEL("praticaViewModel.rata"), DURATA("durata"),
        IMPORTO_EROGATO("importoErogato"), DURATA_PRATICA_VIEW_MODEL("praticaViewModel.durata"),
        IMPORTO_EROGATO_PRATICA_VIEW_MODEL("praticaViewModel.importoErogato"),
        DATA_CARICAMENTO("dataCaricamento"), DATA_CARICAMENTO_PRATICA_VIEW_MODEL("praticaViewModel.dataCaricamento"),
        DATA_RINNOVO_PRATICA_VIEW_MODEL("praticaViewModel.dataRinnovo"),
        IMPIEGO("impiego"), PROVENIENZA("provenienza"), FORNITORE_LEAD("fornitoreLead"),
        DESCRIZIONE_PROVENIENZA("provenienzaDesc"),
        DATA_INSERIMENTO_CLIENTE("dataIns"), DATA_INSERIMENTO_CLIENTE_VIEW_MODEL("clienteViewModel.dataIns"),
        OPERATORE_NOMINATIVO("operatoreNominativo.username"),
        OPERATORE_NOMINATIVO_CLIENTE_VIEW_MODEL("clienteViewModel.operatoreNominativo.username"),
        TIPO_TRATTENUTA("tipoTrat"), TIPO_TRATTENUTA_VIEW_MODEL("trattenuteViewModel.tipoTrat"),
        RATA_TRATTENUTA_VIEW_MODEL("trattenuteViewModel.rataTrat"),
        DURATA_TRATTENUTA_VIEW_MODEL("trattenuteViewModel.durataTrat"),
        RATA_TRATTENUTA("rataTrat"), DURATA_TRATTENUTA("durataTrat"),
        SCADENZA_TRATTENUTA_VIEW_MODEL("trattenuteViewModel.scadenzaTrat"),
        DATA_RINNOVO_TRATTENUTA_VIEW_MODEL("trattenuteViewModel.dataRinnovoTrat"),
        SCADENZA_TRATTENUTA("scadenzaTrat"), DATA_RINNOVO_TRATTENUTA("dataRinnovoTrat"),
        STATO_NOMINATIVO("statoNominativo"), STATO_NOMINATIVO_CLIENTE_VIEW_MODEL("clienteViewModel.statoNominativo"),
        DATA_RECALL_NOMINATIVO("dataRecallNominativo"),
        LOG_EXECUTION_DATE_TIME("executionDateTime"), RICARICA_DATE_TIME("dateTime"),
        SIMULATOR_TABLE_STATUS("simulatorTableStatus"),
        SIMULATOR_TABLE_TYPE("simulatorTableType"),
        SIMULATOR_JOB_TYPE_LIST("jobTypeList"),
        PROVINCIA_RESIDENZA("provResidenza"),
        PROVINCIA_RESIDENZA_RESIDENZA_VIEW_MODEL("clienteViewModel.residenzaViewModel.provResidenza"),
        RINNOVO_ESTINZIONE("estinzioneViewModel.dataRinnovoEstinzione"),
        TIPO_ESTINZIONE("estinzioneViewModel.tipoEstinzione"),
        DURATA_ESTINZIONE("estinzioneViewModel.durataEstinzione"),
        RATA_ESTINZIONE("estinzioneViewModel.rataEstinzione"),
        ISTITUTO("estinzioneViewModel.istitutoEst"),
        NOTIFICA_ESTINZIONE("estinzioneViewModel.dataNotificaConteggioEstinzione"),
        NOTIFICA_STATO_PRATICA("praticaViewModel.dataNotificaStatoPratica"),
        DATA_ISTRUTTORIA("praticaViewModel.dataIstruttoria"),
        DATA_DELIBERA("praticaViewModel.dataDelibera"),
        DATA_FIRMA_CONTRATTI("praticaViewModel.dataFirmaContratti"),
        DATA_ISTRUITA("praticaViewModel.dataIstruita"),
        DATE_TIME_CREATION_NOTICE_BOARD("dateTimeCreation"),
        DATA_LIQUIDAZIONE("praticaViewModel.dataLiquidazione"),
        DATA_PERFEZIONAMENTO("praticaViewModel.dataPerf"),
        DATA_DECORRENZA("praticaViewModel.decorrenza"),
        TIPOLOGIA_PREVENTIVO("preventivoViewModel.tipologiaPreventivo"),
        RATA_PREVENTIVO("preventivoViewModel.rata"),
        DURATA_PREVENTIVO("preventivoViewModel.durata"),
        IMPORTO_EROGATO_PREVENTIVO("preventivoViewModel.importo"),
        TIPO_OPERAZIONE_PREVENTIVO("preventivoViewModel.tipoPratica"),
        DATA_CREAZIONE_PREVENTIVO("preventivoViewModel.dataCreazione"),
        DATA_RECALL_PRATICA_VIEW_MODEL("praticaViewModel.dataRecall");


        private final String value;

        TableField(final String value) {
            this.value = value;
        }

        public static TableField fromValue(final String value) {
            if (!StringUtils.isEmpty(value)) {
                for (final TableField tableField : values()) {
                    if (value.equals(tableField.getValue())) {
                        return tableField;
                    }
                }
            }
            return null;
        }

    }
}
