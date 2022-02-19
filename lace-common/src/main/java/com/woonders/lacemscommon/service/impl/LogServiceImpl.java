package com.woonders.lacemscommon.service.impl;

import com.google.common.annotations.VisibleForTesting;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.NominativoLogViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.NominativoLogViewModelCreator;
import com.woonders.lacemscommon.config.InternationalizationConfig;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.NominativoLogRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.log.LogEntryData;
import com.woonders.lacemscommon.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * Created by Emanuele on 08/02/2017.
 */
@Service
@Transactional(readOnly = true)
public class LogServiceImpl implements LogService {

    public static final String NAME = "logServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private NominativoLogRepository nominativoLogRepository;
    @Autowired
    private NominativoLogViewModelCreator nominativoLogViewModelCreator;
    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private QueryDSLHelper queryDSLHelper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PraticaRepository praticaRepository;

    @Autowired
    private InternationalizationConfig.ItalyReloadableResourceBundleMessageSource italyReloadableResourceBundleMessageSource;

    @Override
    @Transactional
    public NominativoLogViewModel log(Cliente nominativoSalvato, String commento, long operatorId) {

        NominativoLog lastNominativoLogEntry = nominativoLogRepository
                .findTopByNominativo_IdOrderByExecutionDateTimeDesc(nominativoSalvato.getId());

        if (!isLastEntrySame(lastNominativoLogEntry, nominativoSalvato.getDataRecallNominativo(), nominativoSalvato.getStatoNominativo(), nominativoSalvato.getOperatoreNominativo().getId())
                || (commento != null && !commento.trim().isEmpty())) {
            return nominativoLogViewModelCreator.createViewModel(createNominativoLog(nominativoSalvato, null, commento, TipoLog.NOMINATIVO, operatorId));
        }
        return null;

    }

    @Override
    @Transactional
    public void logCustomAction(long idClienteOrPratica, TipoLog tipoLog, String testo, CustomAction customAction, long operatorId) {
        String commento;
        switch (customAction) {
            case EMAIL:
                commento = MessageFormat.format(italyReloadableResourceBundleMessageSource.getMessage("msg.email.sentlogmessage"), testo);
                break;
            case SMS:
                commento = MessageFormat.format(italyReloadableResourceBundleMessageSource.getMessage("msg.sms.sentlogmessage"), testo);
                break;
            case RECEIVED_SMS:
                commento = testo;
                break;
            case INVIO_CONTESTAZIONE:
                commento = "Contestazione inviata";
                break;
            case RICEZ_CONTESTAZIONE_OK:
                commento = "Contestazione accettata";
                break;
            case RICEZ_CONTESTAZIONE_KO:
                commento = testo;
                break;
            default:
                commento = testo;
                break;
        }

        if (!StringUtils.isEmpty(commento)) {
            switch (tipoLog) {
                case NOMINATIVO:
                    Cliente cliente = clienteRepository.findOne(idClienteOrPratica);
                    if (cliente != null) {
                        log(cliente, commento, operatorId);
                    }
                    break;
                case PRATICA:
                    Pratica pratica = praticaRepository.findOne(idClienteOrPratica);
                    if (pratica != null) {
                        log(pratica, commento, operatorId);
                    }
                    break;
            }
        }
    }

    @Override
    public NominativoLogViewModel log(Pratica praticaSalvata, String commento, long operatorId) {
        NominativoLog lastNominativoLogEntry = nominativoLogRepository
                .findTopByPratica_CodicePraticaOrderByExecutionDateTimeDesc(praticaSalvata.getCodicePratica());
        if (!isLastEntrySame(lastNominativoLogEntry, praticaSalvata.getDataRecall(), praticaSalvata.getStatoPratica(), praticaSalvata.getOperatore().getId())
                || (commento != null && !commento.trim().isEmpty())) {
            return nominativoLogViewModelCreator.createViewModel(createNominativoLog(null, praticaSalvata, commento, TipoLog.PRATICA, operatorId));
        }
        return null;
    }

    private NominativoLog createNominativoLog(Cliente nominativoSalvato, Pratica praticaSalvata, String commento, TipoLog tipoLog, long operatorId) {
        Operator operator = operatorRepository.findOne(operatorId);
        Operator operatoreAssegnato = null;
        String stato = null;
        Date dataRecall = null;
        switch (tipoLog) {
            case PRATICA:
                dataRecall = praticaSalvata.getDataRecall();
                operatoreAssegnato = operatorRepository.findOne(praticaSalvata.getOperatore().getId());
                stato = praticaSalvata.getStatoPratica();
                break;
            case NOMINATIVO:
                dataRecall = nominativoSalvato.getDataRecallNominativo();
                operatoreAssegnato = operatorRepository.findOne(nominativoSalvato.getOperatoreNominativo().getId());
                stato = nominativoSalvato.getStatoNominativo();
                break;
        }

        LocalDateTime dataRecallDateTime = null;
        if (dataRecall != null) {
            Instant instant = dataRecall.toInstant();
            ZoneId gmtZoneId = ZoneId.of("UTC");
            dataRecallDateTime = instant.atZone(gmtZoneId).toLocalDateTime();
        }

        NominativoLog nominativoLogToSave = nominativoLogViewModelCreator.createModel(NominativoLogViewModel
                .builder().executionDateTime(LocalDateTime.now(Clock.systemUTC())).dataRecall(dataRecallDateTime)
                .stato(stato).commento(commento).tipoLog(tipoLog).build());

        nominativoLogToSave.setOperator(operator);
        nominativoLogToSave.setOperatoreAssegnato(operatoreAssegnato);

        switch (tipoLog) {
            case NOMINATIVO:
                nominativoLogToSave.setNominativo(nominativoSalvato);
                break;
            case PRATICA:
                nominativoLogToSave.setPratica(praticaSalvata);
                break;
        }

        return nominativoLogRepository.save(nominativoLogToSave);

    }

    @VisibleForTesting
    public boolean isLastEntrySame(NominativoLog lastNominativoLogEntry, Date newDataRecall, String newStato, long newOperatoreAssegnatoId) {

        if (lastNominativoLogEntry != null) {
            // ultima log entry
            long operatoreAssegnatoId = 0;
            if (lastNominativoLogEntry.getOperatoreAssegnato() != null) {
                operatoreAssegnatoId = lastNominativoLogEntry.getOperatoreAssegnato().getId();
            }

            String stato;
            if (TipoLog.NOMINATIVO.equals(lastNominativoLogEntry.getTipoLog())) {
                stato = lastNominativoLogEntry.getStatoNominativo().getValue();
            } else {
                stato = lastNominativoLogEntry.getStatoPratica().getValue();
            }

            LogEntryData lastLogEntryData = LogEntryData.builder().operatoreAssegnatoId(operatoreAssegnatoId)
                    .dataRecall(lastNominativoLogEntry.getDataRecall())
                    .stato(stato).build();

            // creo nuova possibile log entry
            LocalDateTime newDataRecallDateTime = null;

            if (newDataRecall != null) {
                newDataRecallDateTime = LocalDateTime.ofInstant(newDataRecall.toInstant(),
                        ZoneId.of("UTC"));
            }

            LogEntryData newLogEntryData = LogEntryData.builder()
                    .operatoreAssegnatoId(newOperatoreAssegnatoId).dataRecall(newDataRecallDateTime)
                    .stato(newStato).build();

            // compare!
            return lastLogEntryData.equals(newLogEntryData);
        }
        // nel caso sia null vuol dire che non e mai stato scritto il log,
        // quindi va scritto!
        return false;

    }

    @Override
    public ViewModelPage<NominativoLogViewModel> getNominativoLog(long nominativoId, int firstElementIndex,
                                                                  int pageSize, String sortField, QueryDSLHelper.SortOrder sortOrder,
                                                                  Map<QueryDSLHelper.TableField, Object> filters) {

        QNominativoLog qNominativoLog = QNominativoLog.nominativoLog;

        BooleanExpression nominativoEquals = qNominativoLog.nominativo.id.eq(nominativoId);
        BooleanExpression tipoEquals = qNominativoLog.tipoLog.eq(TipoLog.NOMINATIVO);

        return getPageLog(firstElementIndex, pageSize, sortField, sortOrder, filters, nominativoEquals.and(tipoEquals));

    }

    @Override
    public ViewModelPage<NominativoLogViewModel> getPraticaLog(long nominativoId, long praticaId, int firstElementIndex, int pageSize, String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters) {
        QNominativoLog qNominativoLog = QNominativoLog.nominativoLog;

        BooleanExpression codicePraticaEquals = qNominativoLog.pratica.codicePratica.eq(praticaId);
        BooleanExpression tipoPraticaEquals = qNominativoLog.tipoLog.eq(TipoLog.PRATICA);

        BooleanExpression praticaEquals = codicePraticaEquals.and(tipoPraticaEquals);

        BooleanExpression nominativoIdEquals = qNominativoLog.nominativo.id.eq(nominativoId);
        BooleanExpression tipoNominativoEquals = qNominativoLog.tipoLog.eq(TipoLog.NOMINATIVO);

        BooleanExpression nominativoEquals = nominativoIdEquals.and(tipoNominativoEquals);

        return getPageLog(firstElementIndex, pageSize, sortField, sortOrder, filters, praticaEquals.or(nominativoEquals));

    }

    private ViewModelPage<NominativoLogViewModel> getPageLog(int firstElementIndex, int pageSize, String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters, BooleanExpression booleanExpression) {
        //filters non usati ora nella tabella!!

        QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        Page<NominativoLog> nominativoLogPage = nominativoLogRepository.findAll(booleanExpression,
                new PageRequest(firstElementIndex / pageSize, pageSize, qSort));

        return new ViewModelPageImpl<>(
                nominativoLogViewModelCreator.createViewModelList(nominativoLogPage.getContent()),
                nominativoLogPage.getTotalPages(), nominativoLogPage.getTotalElements());
    }

}

