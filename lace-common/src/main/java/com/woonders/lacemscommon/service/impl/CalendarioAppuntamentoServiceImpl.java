package com.woonders.lacemscommon.service.impl;

import com.google.common.collect.Lists;
import com.woonders.lacemscommon.app.viewmodel.CalendarioAppuntamentoViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.SettingViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.CalendarioAppuntamentoViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.PraticaViewModelCreator;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.db.entityutil.SettingUtil;
import com.woonders.lacemscommon.db.repository.CalendarioAppuntamentoRepository;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.exception.CannotSendSmsException;
import com.woonders.lacemscommon.exception.NotEnoughCreditException;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.log.LogAppuntamentoCommentoEntry;
import com.woonders.lacemscommon.service.*;
import com.woonders.lacemscommon.util.DateConversionUtil;
import com.woonders.lacemscommon.util.PredicateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class CalendarioAppuntamentoServiceImpl implements CalendarioAppuntamentoService {

    public static final String NAME = "calendarioAppuntamentoServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String COMMENTO_NUOVO_APPUNTAMENTO = "Aggiunto appuntamento per il giorno {0} con titolo {1}";
    private static final String COMMENTO_APPUNTAMENTO_ELIMINATO = "Eliminato appuntamento del giorno {0}";
    private static final String COMMENTO_APPUNTAMENTO_MODIFICATO = "Appuntamento modificato: vecchio titolo {0} - nuovo titolo: {1}. Vecchio orario: {2} - nuovo orario: {3}";

    @Autowired
    private CalendarioAppuntamentoRepository calendarioAppuntamentoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private CalendarioAppuntamentoViewModelCreator calendarioAppuntamentoViewModelCreator;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private DateConversionUtil dateConversionUtil;
    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private LogService logService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private SettingUtil settingUtil;
    @Autowired
    private CalendarioAppuntamentoFacade calendarioAppuntamentoFacade;

    @Override
    @Transactional
    public long saveAppuntamento(final CalendarioAppuntamentoViewModel calendarioAppuntamentoViewModel, final long operatorId) {

        final CalendarioAppuntamento calendarioAppuntamento = calendarioAppuntamentoViewModelCreator
                .createModel(calendarioAppuntamentoViewModel);

        // converto i LocalDate nel TimeZone dell'utente, per ora fisso su Rome
        calendarioAppuntamento.setInizioAppuntamento(
                dateConversionUtil.moveLocalDateTimeToRomeTimeZone(calendarioAppuntamento.getInizioAppuntamento()));
        calendarioAppuntamento.setFineAppuntamento(
                dateConversionUtil.moveLocalDateTimeToRomeTimeZone(calendarioAppuntamento.getFineAppuntamento()));

        final Operator operator = operatorRepository.findOne(operatorId);

        if (calendarioAppuntamento.getNominativo() != null) {
            final Cliente clienteOrNominativo = clienteRepository
                    .findOne(calendarioAppuntamentoViewModel.getNominativo().getId());

            if (Tipo.NOMINATIVO.getValue().equalsIgnoreCase(clienteOrNominativo.getTipo())) {

                final Operator operatorAssegnato = operatorRepository
                        .findOne(clienteOrNominativo.getOperatoreNominativo().getId());

                calendarioAppuntamento.setNominativo(clienteOrNominativo);
                calendarioAppuntamento.setOperatoreAssegnato(operatorAssegnato);
                calendarioAppuntamento.setOperator(operator);

                final CalendarioAppuntamento vecchioCalendarioAppuntamento = calendarioAppuntamentoRepository
                        .findOne(calendarioAppuntamento.getId());
                if (vecchioCalendarioAppuntamento == null) {
                    logService.log(clienteOrNominativo,
                            MessageFormat.format(COMMENTO_NUOVO_APPUNTAMENTO,
                                    dateConversionUtil.calcStringFromLocalDateTime(
                                            calendarioAppuntamento.getInizioAppuntamento()),
                                    calendarioAppuntamento.getTitolo()), operatorId);
                } else {
                    final LogAppuntamentoCommentoEntry vecchiaLogEntry = LogAppuntamentoCommentoEntry.builder()
                            .titolo(vecchioCalendarioAppuntamento.getTitolo())
                            .dataOraInizioAppuntamento(vecchioCalendarioAppuntamento.getInizioAppuntamento())
                            .dataOraFineAppuntamento(vecchioCalendarioAppuntamento.getFineAppuntamento()).build();
                    final LogAppuntamentoCommentoEntry nuovaLogEntry = LogAppuntamentoCommentoEntry.builder()
                            .titolo(calendarioAppuntamento.getTitolo())
                            .dataOraInizioAppuntamento(calendarioAppuntamento.getInizioAppuntamento())
                            .dataOraFineAppuntamento(calendarioAppuntamento.getFineAppuntamento()).build();
                    if (!vecchiaLogEntry.equals(nuovaLogEntry)) {
                        logService.log(clienteOrNominativo,
                                MessageFormat.format(COMMENTO_APPUNTAMENTO_MODIFICATO,
                                        vecchioCalendarioAppuntamento.getTitolo(), calendarioAppuntamento.getTitolo(),
                                        dateConversionUtil.calcStringFromLocalDateTime(
                                                vecchioCalendarioAppuntamento.getInizioAppuntamento()),
                                        dateConversionUtil.calcStringFromLocalDateTime(
                                                calendarioAppuntamento.getInizioAppuntamento())), operatorId);
                    }
                }
            } else {
                final Pratica pratica = praticaRepository
                        .findOne(calendarioAppuntamentoViewModel.getPratica().getCodicePratica());
                final Operator operatorAssegnato = operatorRepository.findOne(pratica.getOperatore().getId());

                calendarioAppuntamento.setNominativo(clienteOrNominativo);
                calendarioAppuntamento.setPratica(pratica);
                calendarioAppuntamento.setOperatoreAssegnato(operatorAssegnato);
                calendarioAppuntamento.setOperator(operator);
            }
        } else {
            final Operator operatorAssegnato = operatorRepository
                    .findByUsername(calendarioAppuntamento.getOperatoreAssegnato().getUsername());
            calendarioAppuntamento.setOperatoreAssegnato(operatorAssegnato);
            calendarioAppuntamento.setOperator(operator);
        }

        return calendarioAppuntamentoRepository.save(calendarioAppuntamento).getId();

    }

    @Override
    @Transactional(rollbackFor = {CannotSendSmsException.class, SmsNotSentException.class,
            NotEnoughCreditException.class})
    public void sendSmsAppuntamento(final long idAppuntamento, final SMSType smsType, final long aziendaId)
            throws CannotSendSmsException, SmsNotSentException, NotEnoughCreditException {
        final CalendarioAppuntamento calendarioAppuntamento = calendarioAppuntamentoRepository.findOne(idAppuntamento);

        final SettingViewModel settingViewModel = settingService.getByAziendaId(aziendaId);

        final String numeroMittente = settingUtil.getAliasMittente(settingViewModel,
                aziendaService.getOne(aziendaId));
        String recipient = null;
        String body = null;

        if (settingViewModel.getNotificaLeadSmsBalance() < 1) {
            throw new NotEnoughCreditException();
        }

        switch (smsType) {
            case CLIENT:
                recipient = calendarioAppuntamento.getNominativo().getTelefono();
                body = settingService.getByAziendaId(aziendaId)
                        .getTestoSmsAppuntamentoClienteNominativo();
                calendarioAppuntamento.setInvioSmsClienteNominativo(true);
                break;
            case OPERATOR:
                recipient = operatorRepository.findOne(calendarioAppuntamento.getOperatoreAssegnato().getId())
                        .getPhoneNumber();
                body = settingService.getByAziendaId(aziendaId)
                        .getTestoSmsAppuntamentoOperatoreAssegnato();
                calendarioAppuntamento.setInvioSmsOperatoreAssegnato(true);
                break;
        }

        calendarioAppuntamentoFacade.sendSmsAppuntamento(numeroMittente, recipient, body,
                calendarioAppuntamento.getInizioAppuntamento(), aziendaId);

    }

    @Override
    public List<PraticaViewModel> findPraticheByIdCliente(final long id, final long operatorId, final Role.RoleName clientiReadRoleName) {
        switch (clientiReadRoleName) {
            case CLIENTI_READ_ALL:
                return praticaViewModelCreator.createViewModelList(praticaRepository.findDistinctByCliente_Id(id));
            case CLIENTI_READ_OWN:
                return praticaViewModelCreator.createViewModelList(
                        praticaRepository.findDistinctByCliente_IdAndOperatore_Id(id, operatorId));
        }
        return null;
    }

    @Override
    public List<CalendarioAppuntamentoViewModel> getAppuntamenti(final Date start, final Date end,
                                                                 final List<String> operatoriSelezionati, final Long currentAziendaId, final long id,
                                                                 final String operatoreAssegnatoForCheckAppuntamentiPresenti, final long aziendaId, final long operatorId, final Role.RoleName calendarioReadRoleName) {

        final LocalDateTime startLocalDateTime = dateConversionUtil.calcLocalDateTimeFromDate(start);
        final LocalDateTime endLocalDateTime = dateConversionUtil.calcLocalDateTimeFromDate(end);

        return getAppuntamenti(startLocalDateTime, endLocalDateTime, operatoriSelezionati, currentAziendaId, id,
                operatoreAssegnatoForCheckAppuntamentiPresenti, aziendaId, operatorId, calendarioReadRoleName);
    }

    @Override
    public List<CalendarioAppuntamentoViewModel> getAppuntamenti(final LocalDateTime startLocalDateTime,
                                                                 final LocalDateTime endLocalDateTime, final List<String> operatoriSelezionati, final Long currentAziendaId, final long id,
                                                                 final String operatoreAssegnatoForCheckAppuntamentiPresenti, final long aziendaId, final long operatorId, final Role.RoleName calendarioReadRoleName) {


        // you should always be able to read the calendar, but at the first
        // login, if you haven-t setup the correct permission this will crash,
        // so it-s better to avoid it and let you navigate to the permission
        // management page to setup the permissions
        if (calendarioReadRoleName != null) {
            final List<CalendarioAppuntamento> appuntamentiList = Lists.newArrayList(
                    calendarioAppuntamentoRepository.findAll(predicateHelper.getPredicateForFindAppuntamenti(
                            startLocalDateTime, endLocalDateTime, operatoriSelezionati, calendarioReadRoleName,
                            operatorId, aziendaId, currentAziendaId, id,
                            operatoreAssegnatoForCheckAppuntamentiPresenti)));
            return calendarioAppuntamentoViewModelCreator.createViewModelList(appuntamentiList);
        }

        return new LinkedList<>();

    }

    @Override
    public CalendarioAppuntamentoViewModel getOne(final long id) {
        return calendarioAppuntamentoViewModelCreator.createViewModel(calendarioAppuntamentoRepository.findOne(id));
    }

    @Override
    @Transactional
    public void deleteAppuntamento(final long id, final long operatorId) {
        final CalendarioAppuntamento calendarioAppuntamento = calendarioAppuntamentoRepository.getOne(id);
        if (calendarioAppuntamento.getNominativo() != null
                && Tipo.NOMINATIVO.getValue().equalsIgnoreCase(calendarioAppuntamento.getNominativo().getTipo())) {
            logService.log(calendarioAppuntamento.getNominativo(), MessageFormat.format(COMMENTO_APPUNTAMENTO_ELIMINATO,
                    dateConversionUtil.calcStringFromLocalDateTime(calendarioAppuntamento.getInizioAppuntamento())), operatorId);
        }
        calendarioAppuntamentoRepository.delete(id);
    }

    @Transactional
    @Override
    public CalendarioAppuntamentoViewModel changeTimeOne(final long id, final Date newStartDate, final Date newEndDate) {
        final LocalDateTime newStartLocalDateTime = dateConversionUtil.calcLocalDateTimeFromDate(newStartDate);
        final LocalDateTime newEndLocalDateTime = dateConversionUtil.calcLocalDateTimeFromDate(newEndDate);
        final CalendarioAppuntamento calendarioAppuntamento = calendarioAppuntamentoRepository.findOne(id);
        calendarioAppuntamento.setInizioAppuntamento(newStartLocalDateTime);
        calendarioAppuntamento.setFineAppuntamento(newEndLocalDateTime);
        return calendarioAppuntamentoViewModelCreator
                .createViewModel(calendarioAppuntamentoRepository.save(calendarioAppuntamento));
    }

}
