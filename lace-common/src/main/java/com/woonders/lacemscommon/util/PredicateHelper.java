package com.woonders.lacemscommon.util;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.AdvancedSearch;
import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entity.Pratica.TipoPratica;
import com.woonders.lacemscommon.db.entity.Role.RoleName;
import com.woonders.lacemscommon.db.entityenum.*;
import com.woonders.lacemscommon.exception.PermissionDeniedException;
import com.woonders.lacemscommon.laceenum.FiltroRinnoviPraticaCoesistenza;
import com.woonders.lacemscommon.service.AntiriciclaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Emanuele on 29/03/2017.
 */
@Component
public class PredicateHelper {

    @Autowired
    private DateConversionUtil dateConversionUtil;


    public BooleanExpression getPredicateForClienteByDataRecallNominativo(final Long currentAziendaId, final long currentOperatorId,
                                                                          final Role.RoleName nominativiReadRoleName, final Date date) throws PermissionDeniedException {
        final Date dataInizio = DateToCalendar.settaGiorno(date, 00, 00, 00);
        final Date dataFine = DateToCalendar.settaGiorno(date, 23, 59, 59);

        final QCliente qCliente = QCliente.cliente;
        final String tipoNominativo = Tipo.NOMINATIVO.getValue();
        final BooleanExpression dataRecallNominativoBetween = qCliente.dataRecallNominativo.between(dataInizio, dataFine);
        final BooleanExpression tipoNominativoEquals = qCliente.tipo.eq(tipoNominativo);
        BooleanExpression nominativiDaRichiamareExpression = dataRecallNominativoBetween.and(tipoNominativoEquals);

        if (nominativiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (nominativiReadRoleName) {
            case NOMINATIVI_READ_OWN:
                nominativiDaRichiamareExpression = nominativiDaRichiamareExpression
                        .and(qCliente.operatoreNominativo.id.eq(currentOperatorId));
                break;
            case NOMINATIVI_READ_ALL:
                nominativiDaRichiamareExpression = nominativiDaRichiamareExpression
                        .and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                break;
            case NOMINATIVI_READ_SUPER:
                if (currentAziendaId != null) {
                    nominativiDaRichiamareExpression = nominativiDaRichiamareExpression
                            .and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return nominativiDaRichiamareExpression;
    }

    public BooleanExpression getPredicateForFindPraticheDaPerfezionare(String provenienza, String provenienzaDesc, Date dataInizio, Date dataFine,
                                                                       final boolean liquidazione, final boolean liquidata, final boolean quietanzata,
                                                                       final boolean perfezionamentoSelected, final Role.RoleName fatturazioneWriteRoleName, final Long currentAziendaId)
            throws PermissionDeniedException {

        final String tipoCliente = Tipo.CLIENTE.getValue();
        final QCliente qCliente = QCliente.cliente;
        final QPratica qPratica = QPratica.pratica;
        
        final BooleanExpression tipoClienteEquals = qCliente.tipo.eq(tipoCliente);
        
        
        
        final BooleanExpression isPerfezionataFalse = qPratica.perfezionata.eq(false);
        final BooleanExpression statoPraticaNotEquals = qPratica.statoPratica.ne(StatoPratica.ANNULATA.getValue())
                .and(qPratica.statoPratica.ne(StatoPratica.RESPINTA.getValue())
                        .and(qPratica.statoPratica.ne(StatoPratica.NON_ISTRUITA.getValue())));

        BooleanExpression praticheDaPerfezionareExpression = tipoClienteEquals.and(isPerfezionataFalse)
                .and(statoPraticaNotEquals);

        if (dataInizio != null && dataFine != null) {
            dataInizio = DateToCalendar.settaGiorno(dataInizio, 00, 00, 00);
            dataFine = DateToCalendar.settaGiorno(dataFine, 23, 59, 59);
            final BooleanExpression dataCaricamentoBetween = qPratica.dataCaricamento.between(dataInizio, dataFine);
            praticheDaPerfezionareExpression = praticheDaPerfezionareExpression.and(dataCaricamentoBetween);
        }
        
        //*** mod by Cristian 23-11-21 ***
        if (provenienza != null && provenienza.length() > 0)
        {
        	final BooleanExpression provenienzaClienteEquals =  qCliente.provenienza.eq(provenienza);
        	praticheDaPerfezionareExpression = praticheDaPerfezionareExpression.and(provenienzaClienteEquals);
        }
        if (provenienzaDesc != null && provenienzaDesc.length() > 0)
        {
        	final BooleanExpression provenienzaDescClienteEquals =  qCliente.provenienzaDesc.eq(provenienzaDesc);
        	praticheDaPerfezionareExpression = praticheDaPerfezionareExpression.and(provenienzaDescClienteEquals);
        }
        //*************
        

        if (liquidata || liquidazione || quietanzata || perfezionamentoSelected) {
            final Collection<String> collection = new LinkedList<>();

            if (liquidata) {
                collection.add(StatoPratica.LIQUIDATA.getValue());
            }

            if (liquidazione) {
                collection.add(StatoPratica.LIQUIDAZIONE.getValue());
            }

            if (quietanzata) {
                collection.add(StatoPratica.DECORRENZA.getValue());
            }

            if (perfezionamentoSelected) {
                collection.add(StatoPratica.PERFEZIONAMENTO.getValue());
            }

            praticheDaPerfezionareExpression = praticheDaPerfezionareExpression
                    .and(qPratica.statoPratica.in(collection));
        }

        if (fatturazioneWriteRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (fatturazioneWriteRoleName) {
            case FATTURAZIONE_WRITE:
                praticheDaPerfezionareExpression = praticheDaPerfezionareExpression
                        .and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                break;
            case FATTURAZIONE_WRITE_SUPER:
                if (currentAziendaId != null) {
                    praticheDaPerfezionareExpression = praticheDaPerfezionareExpression
                            .and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return praticheDaPerfezionareExpression;
    }

    public BooleanExpression getPredicateForFindPratichePerfezionate(String provenienza, String provenienzaDesc, Date dataInizio, Date dataFine,
                                                                     final long currentOperatorId, final long currentOperatorAziendaId, final Long operatorToSearchId,
                                                                     final Role.RoleName fatturazioneReadRoleName, final Long currentAziendaId) throws PermissionDeniedException {
        final String tipoCliente = Tipo.CLIENTE.getValue();
        final QCliente qCliente = QCliente.cliente;
        final QPratica qPratica = QPratica.pratica;

        final BooleanExpression tipoClienteEquals = qCliente.tipo.eq(tipoCliente);
        final BooleanExpression isPerfezionataTrue = qPratica.perfezionata.eq(true);
        BooleanExpression pratichePerfezionateExpression = tipoClienteEquals.and(isPerfezionataTrue);

        dataInizio = DateToCalendar.settaGiorno(dataInizio, 00, 00, 00);
        dataFine = DateToCalendar.settaGiorno(dataFine, 23, 59, 59);
        final BooleanExpression dataPerfezionamentoBetween = qPratica.dataPerf.between(dataInizio, dataFine);
        
        
        //*** mod by Cristian 23-11-21 ***
        if (provenienza != null && provenienza.length() > 0)
        {
        	final BooleanExpression provenienzaClienteEquals =  qCliente.provenienza.eq(provenienza);
        	pratichePerfezionateExpression = pratichePerfezionateExpression.and(provenienzaClienteEquals);
        }

        if (provenienzaDesc != null && provenienzaDesc.length() > 0)
        {
        	final BooleanExpression provenienzaDescClienteEquals =  qCliente.provenienzaDesc.eq(provenienzaDesc);
        	pratichePerfezionateExpression = pratichePerfezionateExpression.and(provenienzaDescClienteEquals);
        }
        //*************
        

        pratichePerfezionateExpression = pratichePerfezionateExpression.and(dataPerfezionamentoBetween);

        if (fatturazioneReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (fatturazioneReadRoleName) {
            case FATTURAZIONE_READ_OWN:
                pratichePerfezionateExpression = pratichePerfezionateExpression
                        .and(qPratica.operatore.id.eq(currentOperatorId));
                break;
            case FATTURAZIONE_READ_ALL:
                if (operatorToSearchId == null) {
                    pratichePerfezionateExpression = pratichePerfezionateExpression
                            .and(qPratica.operatore.azienda.id.eq(currentOperatorAziendaId));
                } else {
                    pratichePerfezionateExpression = pratichePerfezionateExpression
                            .and(qPratica.operatore.id.eq(operatorToSearchId));
                }
                break;
            case FATTURAZIONE_READ_SUPER:
                if (operatorToSearchId == null) {
                    if (currentAziendaId != null) {
                        pratichePerfezionateExpression = pratichePerfezionateExpression
                                .and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                    }
                } else {
                    pratichePerfezionateExpression = pratichePerfezionateExpression
                            .and(qPratica.operatore.id.eq(operatorToSearchId));
                }
        }

        return pratichePerfezionateExpression;
    }

    public Predicate getPredicateForNuovaCampagnaMarketing(
            final FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel, final boolean isClientiReadSuper,
            final boolean isNominativiReadSuper, final boolean isClientiReadAll, final boolean isNominativiReadAll,
            final boolean isClientiReadOwn, final boolean isNominativiReadOwn, final long currentOperatorId,
            final long currentOperatorAziendaId, final Long currentAziendaId) {
        final Date dataNascitaInizio = dateConversionUtil.fromYearsToDate(filtroCampagnaMarketingViewModel.getEtaTo());
        final Date dataNascitaFine = dateConversionUtil.fromYearsToDate(filtroCampagnaMarketingViewModel.getEtaFrom());
        final Date dataAssunzioneInizio = dateConversionUtil
                .fromYearsToDate(filtroCampagnaMarketingViewModel.getAnniAssunzione());
        final Date dataPensionamentoInizio = dateConversionUtil
                .fromYearsToDate(filtroCampagnaMarketingViewModel.getAnniAssunzione());
        final Date dataInizioRinnovoPraticaCoesistenzaCliente = dateConversionUtil.calcDateFromLocalDate(
                filtroCampagnaMarketingViewModel.getDataInizioRinnovoPraticaCoesistenzaCliente());
        final Date dataFineRinnovoPraticaCoesistenzaCliente = dateConversionUtil
                .calcDateFromLocalDate(filtroCampagnaMarketingViewModel.getDataFineRinnovoPraticaCoesistenzaCliente());
        final Date dataInizioRinnovoImpegnoNominativo = dateConversionUtil
                .calcDateFromLocalDate(filtroCampagnaMarketingViewModel.getDataInizioRinnovoImpegnoNominativo());
        final Date dataFineRinnovoImpegnoNominativo = dateConversionUtil
                .calcDateFromLocalDate(filtroCampagnaMarketingViewModel.getDataFineRinnovoImpegnoNominativo());
        String ragioneSociale = null;
        if (filtroCampagnaMarketingViewModel.getAmministrazioneViewModel() != null) {
            ragioneSociale = filtroCampagnaMarketingViewModel.getAmministrazioneViewModel().getRagioneSociale();
        }

        return getPredicateForNuovaCampagnaMarketing(filtroCampagnaMarketingViewModel.getTipo(),
                filtroCampagnaMarketingViewModel.getSesso(), dataNascitaInizio, dataNascitaFine,
                filtroCampagnaMarketingViewModel.getProvinciaResidenza(), ragioneSociale,
                filtroCampagnaMarketingViewModel.getProvenienza(), dataAssunzioneInizio, dataPensionamentoInizio,
                dataInizioRinnovoPraticaCoesistenzaCliente, dataFineRinnovoPraticaCoesistenzaCliente,
                filtroCampagnaMarketingViewModel.getStatoPratica(), filtroCampagnaMarketingViewModel.getTipoPratica(),
                dataInizioRinnovoImpegnoNominativo, dataFineRinnovoImpegnoNominativo,
                filtroCampagnaMarketingViewModel.getStatoNominativo(), filtroCampagnaMarketingViewModel.getImpiego(),
                filtroCampagnaMarketingViewModel.getFiltroRinnoviPraticaCoesistenza(), isClientiReadSuper,
                isNominativiReadSuper, isClientiReadAll, isNominativiReadAll, isClientiReadOwn, isNominativiReadOwn,
                currentOperatorId, currentOperatorAziendaId, currentAziendaId);
    }

    private Predicate getPredicateForNuovaCampagnaMarketing(final Tipo tipo, final Cliente.Sesso sesso, final Date dateNascitaInizio,
                                                            final Date dateNascitaFine, final String provinciaResidenza, final String ragioneSociale, final String provenienza,
                                                            final Date dataInizioAssunzione, final Date dataInizioPensionamento, final Date dataInizioRinnovoPraticaCoesistenzaCliente,
                                                            final Date dataFineRinnovoPraticaCoesistenzaCliente, final String statoPratica, final String tipoPratica,
                                                            final Date dataInizioRinnovoImpegnoNominativo, final Date dataFineRinnovoImpegnoNominativo, final String statoNominativo,
                                                            final String impiego, final FiltroRinnoviPraticaCoesistenza filtroRinnoviPraticaCoesistenza, final boolean isClientiReadSuper,
                                                            final boolean isNominativiReadSuper, final boolean isClientiReadAll, final boolean isNominativiReadAll,
                                                            final boolean isClientiReadOwn, final boolean isNominativiReadOwn, final long currentOperatorId,
                                                            final long currentOperatorAziendaId, final Long currentAziendaId) {

        final QCliente qCliente = QCliente.cliente;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        final BooleanExpression telefonoNotNullNotEmpty = qCliente.telefono.isNotNull().and(qCliente.telefono.isNotEmpty());
        booleanBuilder = booleanBuilder.and(telefonoNotNullNotEmpty);

        if (tipo != null) {
            final BooleanExpression tipoEquals = qCliente.tipo.eq(tipo.getValue());
            // mi sono accorto di questo che non riassegna booleanBuilder, ho
            // messo un breakpoint e sembra che non serva riasseggnarlo, quindi
            // potremmo tranquillamente non riassegnarlo anche in tutti gli
            // altri casi
            booleanBuilder.and(tipoEquals);
            if (Tipo.CLIENTE.equals(tipo)) {
                if (dataInizioRinnovoPraticaCoesistenzaCliente != null
                        && dataFineRinnovoPraticaCoesistenzaCliente != null
                        && filtroRinnoviPraticaCoesistenza != null) {

                    final BooleanExpression dataRinnovoPraticaBetween = qCliente.pratica.any().dataRinnovo.between(
                            dataInizioRinnovoPraticaCoesistenzaCliente, dataFineRinnovoPraticaCoesistenzaCliente);
                    final BooleanExpression dataRinnovoCoesistenzaBetween = qCliente.trattenuta.any().dataRinnovoTrat.between(
                            dataInizioRinnovoPraticaCoesistenzaCliente, dataFineRinnovoPraticaCoesistenzaCliente);

                    switch (filtroRinnoviPraticaCoesistenza) {
                        case ALL_RINNOVI:
                            booleanBuilder = booleanBuilder
                                    .and(dataRinnovoPraticaBetween.or(dataRinnovoCoesistenzaBetween));
                            break;
                        case RINNOVI_PRATICA:
                            booleanBuilder = booleanBuilder.and(dataRinnovoPraticaBetween);
                            break;
                        case RINNOVI_COESISTENZA:
                            booleanBuilder = booleanBuilder.and(dataRinnovoCoesistenzaBetween);
                            break;
                    }

                }
                if (!StringUtils.isEmpty(statoPratica)) {
                    final BooleanExpression statoPraticaEquals = qCliente.pratica.any().statoPratica.eq(statoPratica);
                    booleanBuilder = booleanBuilder.and(statoPraticaEquals);
                }

                if (!StringUtils.isEmpty(tipoPratica)) {
                    final BooleanExpression tipoPraticaEquals = qCliente.pratica.any().tipoPratica.eq(tipoPratica);
                    booleanBuilder = booleanBuilder.and(tipoPraticaEquals);
                }
            } else {
                if (dataInizioRinnovoImpegnoNominativo != null && dataFineRinnovoImpegnoNominativo != null) {
                    final BooleanExpression dataRinnovoImpegniBetween = qCliente.trattenuta.any().dataRinnovoTrat
                            .between(dataInizioRinnovoImpegnoNominativo, dataFineRinnovoImpegnoNominativo);
                    booleanBuilder = booleanBuilder.and(dataRinnovoImpegniBetween);
                }
                if (!StringUtils.isEmpty(statoNominativo)) {
                    final BooleanExpression statoNominativoEquals = qCliente.statoNominativo.eq(statoNominativo);
                    booleanBuilder = booleanBuilder.and(statoNominativoEquals);
                }
            }
        }
        if (sesso != null) {
            final BooleanExpression sessoEquals = qCliente.sesso.eq(sesso);
            booleanBuilder = booleanBuilder.and(sessoEquals);
        }
        if (dateNascitaInizio != null && dateNascitaFine != null) {
            final BooleanExpression etaBetween = qCliente.dataNascita.between(dateNascitaInizio, dateNascitaFine);
            booleanBuilder = booleanBuilder.and(etaBetween);
        }
        if (!StringUtils.isEmpty(provinciaResidenza)) {
            final BooleanExpression provinciaResidenzaEquals = qCliente.residenza.provResidenza.eq(provinciaResidenza);
            booleanBuilder = booleanBuilder.and(provinciaResidenzaEquals);
        }
        if (!StringUtils.isEmpty(ragioneSociale)) {
            final BooleanExpression amministrazioneContains = qCliente.amministrazione.any().ragioneSociale
                    .eq(ragioneSociale);
            booleanBuilder = booleanBuilder.and(amministrazioneContains);
        }
        if (!StringUtils.isEmpty(provenienza)) {
            final BooleanExpression provenienzaEquals = qCliente.provenienza.eq(provenienza);
            booleanBuilder = booleanBuilder.and(provenienzaEquals);
        }
        if (dataInizioAssunzione != null) {
            final BooleanExpression anniAssunzione1After = qCliente.dataInizio.loe(dataInizioAssunzione);
            final BooleanExpression anniAssunzione2After = qCliente.dataInizio2.loe(dataInizioAssunzione);
            final BooleanExpression dataInzioAssunzioneAfter = anniAssunzione1After.or(anniAssunzione2After);
            booleanBuilder = booleanBuilder.and(dataInzioAssunzioneAfter);
        }
        if (dataInizioPensionamento != null) {
            final BooleanExpression anniPensionamentoAfter = qCliente.dataInizio.loe(dataInizioPensionamento);
            final BooleanExpression anniPensionamento2After = qCliente.dataInizio2.loe(dataInizioPensionamento);
            final BooleanExpression dataInzioPensionamentoAfter = anniPensionamentoAfter.or(anniPensionamento2After);
            booleanBuilder = booleanBuilder.and(dataInzioPensionamentoAfter);
        }
        if (!StringUtils.isEmpty(impiego)) {
            final BooleanExpression impiegoEquals = qCliente.impiego.eq(impiego).or(qCliente.impiego2.eq(impiego));
            booleanBuilder = booleanBuilder.and(impiegoEquals);
        }

        BooleanBuilder tipoAndOperatoreClientePraticaBuilder = new BooleanBuilder();

        // FIX identico a sotto per autocomplete calendario

        // prendo in considerazione quelle con Nominativo solo se tipo e
        // null, quindi sto cercando qualsiasi, o se tipo e nominativo,
        // perche sto cercando i nominativi. Stessa cosa per clienti

        // NOMINATIVI
        if (isNominativiReadOwn && (tipo == null || Tipo.NOMINATIVO.equals(tipo))) {
            tipoAndOperatoreClientePraticaBuilder = tipoAndOperatoreClientePraticaBuilder.or(qCliente.tipo
                    .eq(Tipo.NOMINATIVO.getValue()).and(qCliente.operatoreNominativo.id.eq(currentOperatorId)));
        }

        if (isNominativiReadAll && (tipo == null || Tipo.NOMINATIVO.equals(tipo))) {
            tipoAndOperatoreClientePraticaBuilder = tipoAndOperatoreClientePraticaBuilder
                    .or(qCliente.tipo.eq(Tipo.NOMINATIVO.getValue())
                            .and(qCliente.operatoreNominativo.azienda.id.eq(currentOperatorAziendaId)));
        }

        if (isNominativiReadSuper && (tipo == null || Tipo.NOMINATIVO.equals(tipo))) {
            if (currentAziendaId != null) {
                tipoAndOperatoreClientePraticaBuilder = tipoAndOperatoreClientePraticaBuilder
                        .or(qCliente.tipo.eq(Tipo.NOMINATIVO.getValue())
                                .and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId)));
            } else {
                tipoAndOperatoreClientePraticaBuilder = tipoAndOperatoreClientePraticaBuilder
                        .or(qCliente.tipo.eq(Tipo.NOMINATIVO.getValue()));
            }
        }

        // CLIENTI
        if (isClientiReadOwn && (tipo == null || Tipo.CLIENTE.equals(tipo))) {
            tipoAndOperatoreClientePraticaBuilder = tipoAndOperatoreClientePraticaBuilder.or(qCliente.tipo
                    .eq(Tipo.CLIENTE.getValue()).and(qCliente.pratica.any().operatore.id.eq(currentOperatorId)));
        }

        if (isClientiReadAll && (tipo == null || Tipo.CLIENTE.equals(tipo))) {
            tipoAndOperatoreClientePraticaBuilder = tipoAndOperatoreClientePraticaBuilder
                    .or(qCliente.tipo.eq(Tipo.CLIENTE.getValue())
                            .and(qCliente.pratica.any().operatore.azienda.id.eq(currentOperatorAziendaId)));
        }

        if (isClientiReadSuper && (tipo == null || Tipo.CLIENTE.equals(tipo))) {
            if (currentAziendaId != null) {
                tipoAndOperatoreClientePraticaBuilder = tipoAndOperatoreClientePraticaBuilder
                        .or(qCliente.tipo.eq(Tipo.CLIENTE.getValue())
                                .and(qCliente.pratica.any().operatore.azienda.id.eq(currentAziendaId)));
            } else {
                tipoAndOperatoreClientePraticaBuilder = tipoAndOperatoreClientePraticaBuilder
                        .or(qCliente.tipo.eq(Tipo.CLIENTE.getValue()));
            }
        }
        return booleanBuilder.and(tipoAndOperatoreClientePraticaBuilder);

    }

    public BooleanExpression getPredicateForDettaglioCampagna(final long idCampagna) {

        final QCampagnaSms qCampagnaSms = QCampagnaSms.campagnaSms;
        final BooleanExpression idCampagnaEquals = qCampagnaSms.campagna.id.eq(idCampagna);

        return idCampagnaEquals;
    }

    public Predicate getPredicateForEsitoCampagna(final long idCampagna, final EsitoSmsNotificaLead esito) {
        return getPredicateForEsitoCampagnaAndTipo(idCampagna, esito, null, false);
    }

    public Predicate getPredicateForEsitoCampagnaAndTipo(final long idCampagna, final EsitoSmsNotificaLead esito, final Tipo tipo) {
        return getPredicateForEsitoCampagnaAndTipo(idCampagna, esito, tipo, true);
    }

    private Predicate getPredicateForEsitoCampagnaAndTipo(final long idCampagna, final EsitoSmsNotificaLead esito, final Tipo tipo,
                                                          final boolean checkTipo) {

        final QCampagnaSms qCampagnaSms = QCampagnaSms.campagnaSms;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (idCampagna != 0) {
            final BooleanExpression idCampagnaEquals = qCampagnaSms.campagna.id.eq(idCampagna);
            booleanBuilder = booleanBuilder.and(idCampagnaEquals);
        }

        booleanBuilder.and(qCampagnaSms.esito.eq(esito));

        if (checkTipo) {
            if (Tipo.CLIENTE.equals(tipo)) {
                final BooleanExpression tipoEquals = qCampagnaSms.cliente.tipo.eq(Tipo.CLIENTE.getValue());
                booleanBuilder = booleanBuilder.and(tipoEquals);
            } else if (Tipo.NOMINATIVO.equals(tipo)) {
                final BooleanExpression tipoEquals = qCampagnaSms.cliente.tipo.eq(Tipo.NOMINATIVO.getValue());
                booleanBuilder = booleanBuilder.and(tipoEquals);
            } else {
                // sms spediti a nominativi da file, quindi non hanno un cliente
                // id
                final BooleanExpression tipoEquals = qCampagnaSms.cliente.id.isNull();
                booleanBuilder = booleanBuilder.and(tipoEquals);
            }
        }
        return booleanBuilder;
    }

    /**
     * @param start
     * @param end
     * @param operatoriSelezionati
     * @param calendarioReadRoleNome
     * @param currentUserId
     * @param id                                             id dell appuntamento che non deve essere considerato quando
     *                                                       prima di salvare un nuovo appuntamento si controlla se ce ne
     *                                                       sono altri gia presenti in quella fascia oraria
     * @param operatoreAssegnatoForCheckAppuntamentiPresenti viene usato per controllare quando si crea un nuovo
     *                                                       appuntamento se esistono gia appuntamenti per quell operatore
     * @return
     */
    public BooleanExpression getPredicateForFindAppuntamenti(final LocalDateTime start, final LocalDateTime end,
                                                             final List<String> operatoriSelezionati, final Role.RoleName calendarioReadRoleNome, final long currentUserId,
                                                             final long operatorAziendaId, final Long aziendaSelectedId, final long id,
                                                             final String operatoreAssegnatoForCheckAppuntamentiPresenti) {
        final QCalendarioAppuntamento qCalendarioAppuntamento = QCalendarioAppuntamento.calendarioAppuntamento;

        BooleanExpression appuntamentoBetween = qCalendarioAppuntamento.inizioAppuntamento.loe(end)
                .and(qCalendarioAppuntamento.fineAppuntamento.goe(start));

        if (id != 0) {
            final BooleanExpression idNotEquals = qCalendarioAppuntamento.id.ne(id);
            appuntamentoBetween = appuntamentoBetween.and(idNotEquals);
        }

        if (operatoriSelezionati != null && !operatoriSelezionati.isEmpty()) {
            appuntamentoBetween = appuntamentoBetween
                    .and(qCalendarioAppuntamento.operatoreAssegnato.username.in(operatoriSelezionati));
        }

        switch (calendarioReadRoleNome) {
            case CALENDARIO_READ_OWN:
                appuntamentoBetween = appuntamentoBetween
                        .and(qCalendarioAppuntamento.operatoreAssegnato.id.eq(currentUserId));
                break;
            case CALENDARIO_READ_ALL:
                appuntamentoBetween = appuntamentoBetween
                        .and(qCalendarioAppuntamento.operatoreAssegnato.azienda.id.eq(operatorAziendaId));
                if (!StringUtils.isEmpty(operatoreAssegnatoForCheckAppuntamentiPresenti)) {
                    appuntamentoBetween = appuntamentoBetween.and(qCalendarioAppuntamento.operatoreAssegnato.username
                            .eq(operatoreAssegnatoForCheckAppuntamentiPresenti));
                }
                break;
            case CALENDARIO_READ_SUPER:
                if (aziendaSelectedId != null) {
                    appuntamentoBetween = appuntamentoBetween
                            .and(qCalendarioAppuntamento.operatoreAssegnato.azienda.id.eq(aziendaSelectedId));
                }
                if (!StringUtils.isEmpty(operatoreAssegnatoForCheckAppuntamentiPresenti)) {
                    appuntamentoBetween = appuntamentoBetween.and(qCalendarioAppuntamento.operatoreAssegnato.username
                            .eq(operatoreAssegnatoForCheckAppuntamentiPresenti));
                }
                break;
        }

        return appuntamentoBetween;
    }

    public BooleanExpression getPredicateForFindByCognomeContainingAndUsername(final String cognome, final Role.RoleName clientiReadRoleName,
                                                                               final Role.RoleName nominativiReadRoleName, final long currentOperatorId, final long aziendaId, final Tipo tipo) {

        final QCliente qCliente = QCliente.cliente;
        final BooleanExpression cognomeContaining = qCliente.cognome.containsIgnoreCase(cognome);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (RoleName.CLIENTI_READ_SUPER.equals(clientiReadRoleName) && RoleName.NOMINATIVI_READ_SUPER.equals(nominativiReadRoleName)) {
            return cognomeContaining;
        } else {

            //non so se servono i isNotNull() ma lasciamoli
            switch (tipo) {
                case CLIENTE:
                    if (RoleName.CLIENTI_READ_OWN.equals(clientiReadRoleName)) {
                        booleanBuilder = booleanBuilder.or(qCliente.tipo.eq(Tipo.CLIENTE.getValue())
                                .and(qCliente.pratica.any().operatore.id.eq(currentOperatorId)));
                    }

                    if (RoleName.CLIENTI_READ_ALL.equals(clientiReadRoleName)) {
                        booleanBuilder = booleanBuilder.or(qCliente.tipo.eq(Tipo.CLIENTE.getValue())
                                .and(qCliente.pratica.any().operatore.isNotNull()
                                        .and(qCliente.pratica.any().operatore.azienda.isNotNull()
                                                .and(qCliente.pratica.any().operatore.azienda.id.eq(aziendaId)))));
                    }
                    break;
                case NOMINATIVO:
                    if (RoleName.NOMINATIVI_READ_OWN.equals(nominativiReadRoleName)) {
                        booleanBuilder = booleanBuilder.or(qCliente.tipo.eq(Tipo.NOMINATIVO.getValue())
                                .and(qCliente.operatoreNominativo.id.eq(currentOperatorId)));
                    }

                    if (RoleName.NOMINATIVI_READ_ALL.equals(nominativiReadRoleName)) {
                        booleanBuilder = booleanBuilder.or(qCliente.tipo.eq(Tipo.NOMINATIVO.getValue())
                                .and(qCliente.operatoreNominativo.isNotNull()
                                        .and(qCliente.operatoreNominativo.azienda.isNotNull()
                                                .and(qCliente.operatoreNominativo.azienda.id.eq(aziendaId)))));
                    }
                    break;
            }

            return cognomeContaining.and(booleanBuilder);
        }
    }

    public BooleanExpression getPredicateForMatchNominativiByCognomeAndNomeAndDataNascita(final String cognome, final String nome,
                                                                                          final Date dataNascita) {

        final QCliente qCliente = QCliente.cliente;
        final BooleanExpression cognomeEquals = qCliente.cognome.eq(cognome);
        final BooleanExpression nomeEquals = qCliente.nome.eq(nome);
        BooleanExpression matchingNominativi = cognomeEquals.and(nomeEquals);

        if (dataNascita != null) {
            DateToCalendar.getDateWithoutTime(dataNascita);
            final BooleanExpression dataNascitaEquals = qCliente.dataNascita.eq(dataNascita);
            matchingNominativi = matchingNominativi.and(dataNascitaEquals);
        }

        return matchingNominativi;

    }

    public BooleanExpression getPredicateForTipoClienteAndStatoPraticaAndBetweenDateAndOperatorsSelectedAndTipoPratica(
            final List<String> statoPraticaCollection, final Role.RoleName clientiReadRoleName, final long currentOperatorId,
            final long aziendaId, final Long currentAziendaId, final Boolean isPratichePrestito, final Date from,
            final Date to, final TipoPratica tipoPratica, final List<String> operatoriSelezionati) {

        final QPratica qPratica = QPratica.pratica;
        final String tipoCliente = Tipo.CLIENTE.getValue();

        final BooleanExpression tipoEquals = qPratica.cliente.tipo.eq(tipoCliente);


        final BooleanExpression tipoPraticaNotNull = qPratica.tipoPratica.isNotNull()
                .and(qPratica.tipoPratica.isNotEmpty());
        final BooleanExpression dataCaricamentoNotNull = qPratica.dataCaricamento.isNotNull();
        final BooleanExpression dataCaricamentoBetween = qPratica.dataCaricamento.between(from, to);
        final BooleanExpression statoPraticaIn = qPratica.statoPratica.in(statoPraticaCollection);

        BooleanExpression praticheTotal = tipoEquals.and(dataCaricamentoNotNull).and(dataCaricamentoBetween)
                .and(tipoPraticaNotNull).and(statoPraticaIn);

        if (isPratichePrestito != null) {
            if (isPratichePrestito) {
                final BooleanExpression tipoPraticaEquals = qPratica.tipoPratica.eq(TipoPratica.PRESTITO.getValue());
                praticheTotal = praticheTotal.and(tipoPraticaEquals);
            } else {
                final BooleanExpression tipoPraticaNotEquals = qPratica.tipoPratica.ne(TipoPratica.PRESTITO.getValue());
                praticheTotal = praticheTotal.and(tipoPraticaNotEquals);
            }
        } else if (tipoPratica != null) {
            praticheTotal = praticheTotal.and(qPratica.tipoPratica.equalsIgnoreCase(tipoPratica.getValue()));
        }

        if (operatoriSelezionati != null && !operatoriSelezionati.isEmpty()) {
            praticheTotal = praticheTotal
                    .and(qPratica.operatore.username.in(operatoriSelezionati));
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                final BooleanExpression operatoreEquals = qPratica.operatore.id.eq(currentOperatorId);
                praticheTotal = praticheTotal.and(operatoreEquals);
                break;
            case CLIENTI_READ_ALL:
                final BooleanExpression aziendaEquals = qPratica.operatore.azienda.id.eq(aziendaId);
                praticheTotal = praticheTotal.and(aziendaEquals);
                break;
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    praticheTotal = praticheTotal.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
        }

        return praticheTotal;
    }

    public BooleanExpression getPredicateForTipoClienteAndStatoPraticaAndBetweenDateAndOperatoreAndTipoPratica(
            final List<String> statoPraticaCollection, final long operatorId, final Date from, final Date to,
            final TipoPratica tipoPratica) {

        final QPratica qPratica = QPratica.pratica;
        final String tipoCliente = Tipo.CLIENTE.getValue();

        final BooleanExpression tipoEquals = qPratica.cliente.tipo.eq(tipoCliente);


        final BooleanExpression tipoPraticaNotNull = qPratica.tipoPratica.isNotNull()
                .and(qPratica.tipoPratica.isNotEmpty());
        final BooleanExpression dataCaricamentoNotNull = qPratica.dataCaricamento.isNotNull();
        final BooleanExpression dataCaricamentoBetween = qPratica.dataCaricamento.between(from, to);
        final BooleanExpression statoPraticaIn = qPratica.statoPratica.in(statoPraticaCollection);

        BooleanExpression praticheTotal = tipoEquals.and(dataCaricamentoNotNull).and(dataCaricamentoBetween)
                .and(tipoPraticaNotNull).and(statoPraticaIn);

        if (tipoPratica != null) {
            praticheTotal = praticheTotal.and(qPratica.tipoPratica.equalsIgnoreCase(tipoPratica.getValue()));
        }

        praticheTotal = praticheTotal
                .and(qPratica.operatore.id.eq(operatorId));

        return praticheTotal;
    }

    public BooleanExpression getPredicateForCampagnaSmsNonClienti(final long idCampagna) {
        final QCampagnaSms qCampagnaSms = QCampagnaSms.campagnaSms;
        final BooleanExpression idCampagnaBooleanExpression = qCampagnaSms.campagna.id.eq(idCampagna);
        final BooleanExpression clienteNullBooleanExpression = qCampagnaSms.cliente.isNull();
        final BooleanExpression smsRicevutoBooleanExpression = QCampagnaSms.campagnaSms.esito
                .eq(EsitoSmsNotificaLead.RICEVUTO);
        return idCampagnaBooleanExpression.and(clienteNullBooleanExpression).and(smsRicevutoBooleanExpression);
    }

    public BooleanExpression getPredicateForListEsitoCampagnaSms(final long idCampagna,
                                                                 final List<EsitoSmsNotificaLead> esitoList) {
        final QCampagnaSms qCampagnaSms = QCampagnaSms.campagnaSms;
        final BooleanExpression idCampagnaBooleanExpression = qCampagnaSms.campagna.id.eq(idCampagna);
        final BooleanExpression esitoCampagnaIn = qCampagnaSms.esito.in(esitoList);
        return idCampagnaBooleanExpression.and(esitoCampagnaIn);
    }
    
    //*** Add Cristian 18/11/2021
    public BooleanExpression getPredicateForListPrivacy(final Date date) {
			final QPrivacyTemplate qPrivacyTemplate = QPrivacyTemplate.privacyTemplate;
			final BooleanExpression fornitoreLeadBooleanExpression = qPrivacyTemplate.dateCreate.eq(date);
			return fornitoreLeadBooleanExpression;
    }
    //*****

    public BooleanExpression getPredicateForOperatorByUsernameAndPermessiWrite(final long currentOperatorId,
                                                                               final boolean isGestionePermessiWrite, final Long currentAziendaId, final boolean isGestionePermessiWriteSuper) {

        final QOperator qOperator = QOperator.operator;

        final BooleanExpression antiriciclaggioUserExclude = qOperator.role.any().roleName.ne(RoleName.ANTI_RICICLAGGIO_OUT);

        if (isGestionePermessiWrite) {
            final BooleanExpression aziendaEquals = qOperator.azienda.id.eq(currentAziendaId);
            return antiriciclaggioUserExclude.and(aziendaEquals);
        } else if (isGestionePermessiWriteSuper) {
            if (currentAziendaId != null) {
                final BooleanExpression aziendaEquals = qOperator.azienda.id.eq(currentAziendaId);
                return antiriciclaggioUserExclude.and(aziendaEquals);
            }
            return antiriciclaggioUserExclude;
        } else {
            final BooleanExpression usernameEquals = qOperator.id.eq(currentOperatorId);
            return antiriciclaggioUserExclude.and(usernameEquals);
        }

    }

    public BooleanExpression getPredicateForfindByStatoPraticaAndTipoClienteAndUsername(final Long currentAziendaId,
                                                                                        final long currentOperatorId, final Role.RoleName clientiReadRoleName, final String statoPratica, final String tipoPratica)
            throws PermissionDeniedException {

        final QPratica qPratica = QPratica.pratica;
        final String tipoCliente = Tipo.CLIENTE.getValue();

        final BooleanExpression tipoClienteEquals = qPratica.cliente.tipo.eq(tipoCliente);
        final BooleanExpression statoPraticaNotNull = qPratica.statoPratica.isNotNull();
        final BooleanExpression tipoPraticaNotNull = qPratica.tipoPratica.isNotNull();
        BooleanExpression statoPraticaEquals = qPratica.statoPratica.eq(statoPratica).and(statoPraticaNotNull).and(tipoPraticaNotNull).and(tipoClienteEquals);

        if (TipoPratica.PRESTITO.getValue().equalsIgnoreCase(tipoPratica)) {
            final BooleanExpression tipoPraticaEquals = qPratica.tipoPratica.eq(TipoPratica.PRESTITO.getValue());
            statoPraticaEquals = statoPraticaEquals.and(tipoPraticaEquals);
        } else {
            final BooleanExpression tipoPraticaNotEquals = qPratica.tipoPratica.ne(TipoPratica.PRESTITO.getValue());
            statoPraticaEquals = statoPraticaEquals.and(tipoPraticaNotEquals);
        }

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.id.eq(currentOperatorId));
                break;
            case CLIENTI_READ_ALL:
                statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                break;
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return statoPraticaEquals;

    }
    
    public BooleanExpression getPredicateForfindByStatoPraticaAndTipoClienteAndUsernameAndOperators(final Long currentAziendaId,
            final long currentOperatorId, final Role.RoleName clientiReadRoleName, 
            final String statoPratica, final String tipoPratica, final List<String> operators)
		throws PermissionDeniedException {
		
		final QPratica qPratica = QPratica.pratica;
		final String tipoCliente = Tipo.CLIENTE.getValue();
		
		final BooleanExpression tipoClienteEquals = qPratica.cliente.tipo.eq(tipoCliente);
		final BooleanExpression statoPraticaNotNull = qPratica.statoPratica.isNotNull();
		final BooleanExpression tipoPraticaNotNull = qPratica.tipoPratica.isNotNull();
		BooleanExpression statoPraticaEquals = qPratica.statoPratica.eq(statoPratica).and(statoPraticaNotNull)
				.and(tipoPraticaNotNull)
				.and(tipoClienteEquals)
				.and(qPratica.operatore.username.in(operators));
		
		if (TipoPratica.PRESTITO.getValue().equalsIgnoreCase(tipoPratica)) {
		final BooleanExpression tipoPraticaEquals = qPratica.tipoPratica.eq(TipoPratica.PRESTITO.getValue());
		statoPraticaEquals = statoPraticaEquals.and(tipoPraticaEquals);
		} else {
		final BooleanExpression tipoPraticaNotEquals = qPratica.tipoPratica.ne(TipoPratica.PRESTITO.getValue());
		statoPraticaEquals = statoPraticaEquals.and(tipoPraticaNotEquals);
		}
		
		if (clientiReadRoleName == null) {
		throw new PermissionDeniedException();
		}
		
		switch (clientiReadRoleName) {
		case CLIENTI_READ_OWN:
		statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.id.eq(currentOperatorId));
		break;
		case CLIENTI_READ_ALL:
		statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
		break;
		case CLIENTI_READ_SUPER:
		if (currentAziendaId != null) {
		statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
		}
		break;
		}
		
		return statoPraticaEquals;
		
		}

    public BooleanExpression getPredicateForfindByStatoPraticaAndDataNotificaExpiredAndTipoClienteAndUsername(final Long currentAziendaId,
                                                                                                              final long currentOperatorId, final Role.RoleName clientiReadRoleName, final String statoPratica, final String tipoPratica)
            throws PermissionDeniedException {

        final QPratica qPratica = QPratica.pratica;
        final String tipoCliente = Tipo.CLIENTE.getValue();
        final Date today = new Date();
        final BooleanExpression tipoClienteEquals = qPratica.cliente.tipo.eq(tipoCliente);
        final BooleanExpression statoPraticaNotNull = qPratica.statoPratica.isNotNull();
        final BooleanExpression tipoPraticaNotNull = qPratica.tipoPratica.isNotNull();
        final BooleanExpression dataNotificaIsLessOrEqualThan = qPratica.dataNotificaStatoPratica.loe(today);
        BooleanExpression statoPraticaEquals = qPratica.statoPratica.eq(statoPratica).and(statoPraticaNotNull)
                .and(tipoPraticaNotNull).and(tipoClienteEquals).and(dataNotificaIsLessOrEqualThan);

        if (TipoPratica.PRESTITO.getValue().equalsIgnoreCase(tipoPratica)) {
            final BooleanExpression tipoPraticaEquals = qPratica.tipoPratica.eq(TipoPratica.PRESTITO.getValue());
            statoPraticaEquals = statoPraticaEquals.and(tipoPraticaEquals);
        } else {
            final BooleanExpression tipoPraticaNotEquals = qPratica.tipoPratica.ne(TipoPratica.PRESTITO.getValue());
            statoPraticaEquals = statoPraticaEquals.and(tipoPraticaNotEquals);
        }

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.id.eq(currentOperatorId));
                break;
            case CLIENTI_READ_ALL:
                statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                break;
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    statoPraticaEquals = statoPraticaEquals.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return statoPraticaEquals;

    }

    private Collection<String> getCollectionStatoPraticaNotEqualsForConteggiEstinzione() {
        final Collection<String> collection = new LinkedList<>();
        collection.add(StatoPratica.LIQUIDATA.getValue());
        collection.add(StatoPratica.DECORRENZA.getValue());
        collection.add(StatoPratica.PERFEZIONAMENTO.getValue());
        collection.add(StatoPratica.TERMINATA.getValue());
        collection.add(StatoPratica.ANNULATA.getValue());
        collection.add(StatoPratica.RESPINTA.getValue());
        collection.add(StatoPratica.ESTINTA.getValue());
        collection.add(StatoPratica.NON_ISTRUITA.getValue());
        return collection;
    }

    public BooleanExpression getPredicateForFindEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(final Long currentAziendaId,
                                                                                                              final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                                                                              final StatoConteggioEstinzione statoConteggioEstinzione,
                                                                                                              final Date notifyDate, final String tipoEstinzione)
            throws PermissionDeniedException {

        final QEstinzione qEstinzione = QEstinzione.estinzione;
        final String tipoCliente = Tipo.CLIENTE.getValue();

        final Collection<String> collection = getCollectionStatoPraticaNotEqualsForConteggiEstinzione();

        final BooleanExpression tipoClienteEquals = qEstinzione.pratica.cliente.tipo.eq(tipoCliente);
        final BooleanExpression statoPraticaNotEquals = qEstinzione.pratica.statoPratica.notIn(collection);
        final BooleanExpression statoConteggioEstinzioneEquals = qEstinzione.statoConteggioEstinzione.eq(statoConteggioEstinzione);
        final BooleanExpression tipoEstinzioneIsNotNull = qEstinzione.tipoEstinzione.isNotNull();
        final BooleanExpression tipoEstinzioneNotEqualsPignoramento = qEstinzione.tipoEstinzione.notEqualsIgnoreCase(Trattenute.TipoImpegno.PIGNORAMENTO.getValue());

        BooleanExpression statoConteggioEstinzioneExpression = tipoClienteEquals.and(statoPraticaNotEquals)
                .and(statoConteggioEstinzioneEquals).and(tipoEstinzioneIsNotNull).and(tipoEstinzioneNotEqualsPignoramento);

        if (!Trattenute.TipoImpegno.PRESTITO.getValue().equalsIgnoreCase(tipoEstinzione)) {
            final BooleanExpression dataRinnovoEstinzioneNotNull = qEstinzione.dataRinnovoEstinzione.isNotNull();
            final BooleanExpression dataRinnovoEstinzioneIsLessOrEqualThan = qEstinzione.dataRinnovoEstinzione.loe(notifyDate);
            statoConteggioEstinzioneExpression = statoConteggioEstinzioneExpression.and(dataRinnovoEstinzioneNotNull).and(dataRinnovoEstinzioneIsLessOrEqualThan);
        } else {
            final BooleanExpression dataRinnovoEstinzioneIsNull = qEstinzione.dataRinnovoEstinzione.isNull();
            statoConteggioEstinzioneExpression = statoConteggioEstinzioneExpression.and(dataRinnovoEstinzioneIsNull);
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                return statoConteggioEstinzioneExpression.and(QPratica.pratica.operatore.id.eq(currentOperatorId));
            case CLIENTI_READ_ALL:
                return statoConteggioEstinzioneExpression.and(QPratica.pratica.operatore.azienda.id.eq(currentAziendaId));
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    return statoConteggioEstinzioneExpression.and(QPratica.pratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }
        return statoConteggioEstinzioneExpression;
    }
    
    public BooleanExpression getPredicateForFindEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsernameAndOperators(final Long currentAziendaId,
            final long currentOperatorId, final Role.RoleName clientiReadRoleName,
            final StatoConteggioEstinzione statoConteggioEstinzione,
            final Date notifyDate, final String tipoEstinzione, final List<String> operators)
		throws PermissionDeniedException {
		
		final QEstinzione qEstinzione = QEstinzione.estinzione;
		final String tipoCliente = Tipo.CLIENTE.getValue();
		
		final Collection<String> collection = getCollectionStatoPraticaNotEqualsForConteggiEstinzione();
		
		final BooleanExpression tipoClienteEquals = qEstinzione.pratica.cliente.tipo.eq(tipoCliente);
		final BooleanExpression statoPraticaNotEquals = qEstinzione.pratica.statoPratica.notIn(collection);
		final BooleanExpression statoConteggioEstinzioneEquals = qEstinzione.statoConteggioEstinzione.eq(statoConteggioEstinzione);
		final BooleanExpression tipoEstinzioneIsNotNull = qEstinzione.tipoEstinzione.isNotNull();
		final BooleanExpression tipoEstinzioneNotEqualsPignoramento = qEstinzione.tipoEstinzione.notEqualsIgnoreCase(Trattenute.TipoImpegno.PIGNORAMENTO.getValue());
		
		BooleanExpression statoConteggioEstinzioneExpression = tipoClienteEquals.and(statoPraticaNotEquals)
				.and(statoConteggioEstinzioneEquals)
				.and(tipoEstinzioneIsNotNull)
				.and(tipoEstinzioneNotEqualsPignoramento);
		if (operators != null && operators.size() > 0)
			statoConteggioEstinzioneExpression.and(QPratica.pratica.operatore.username.in(operators));
			
		
		if (!Trattenute.TipoImpegno.PRESTITO.getValue().equalsIgnoreCase(tipoEstinzione)) {
			final BooleanExpression dataRinnovoEstinzioneNotNull = qEstinzione.dataRinnovoEstinzione.isNotNull();
			final BooleanExpression dataRinnovoEstinzioneIsLessOrEqualThan = qEstinzione.dataRinnovoEstinzione.loe(notifyDate);
			statoConteggioEstinzioneExpression = statoConteggioEstinzioneExpression.and(dataRinnovoEstinzioneNotNull).and(dataRinnovoEstinzioneIsLessOrEqualThan);
		} else {
			final BooleanExpression dataRinnovoEstinzioneIsNull = qEstinzione.dataRinnovoEstinzione.isNull();
			statoConteggioEstinzioneExpression = statoConteggioEstinzioneExpression.and(dataRinnovoEstinzioneIsNull);
		}
		
		switch (clientiReadRoleName) {
			case CLIENTI_READ_OWN:
				return statoConteggioEstinzioneExpression.and(QPratica.pratica.operatore.id.eq(currentOperatorId));
			case CLIENTI_READ_ALL:
				return statoConteggioEstinzioneExpression.and(QPratica.pratica.operatore.azienda.id.eq(currentAziendaId));
			case CLIENTI_READ_SUPER:
				if (currentAziendaId != null) {
					return statoConteggioEstinzioneExpression.and(QPratica.pratica.operatore.azienda.id.eq(currentAziendaId));
				}
				break;
		}
		return statoConteggioEstinzioneExpression;
	}

    public BooleanExpression getPredicateForAntiriciclaggio(final String codiceFiscale, final Date dataInizio, final Date dataFine,
                                                            final AntiriciclaggioService.TipoRicercaAntiriciclaggio tipoRicercaAntiriciclaggio,
                                                            final Role.RoleName clientiReadRoleName, final long currentOperatorId, final Long currentAziendaId)
            throws PermissionDeniedException {

        final QPratica qPratica = QPratica.pratica;
        final String tipoCliente = Tipo.CLIENTE.getValue();
        BooleanExpression antiriciclaggioFindExpression = qPratica.statoPratica
                .ne(StatoPratica.NON_ISTRUITA.getValue()).and(qPratica.cliente.tipo.eq(tipoCliente));
        switch (tipoRicercaAntiriciclaggio) {
            case CF:
                antiriciclaggioFindExpression = antiriciclaggioFindExpression.and(qPratica.cliente.cf.eq(codiceFiscale));
                break;
            case DATA:
                antiriciclaggioFindExpression = antiriciclaggioFindExpression
                        .and(qPratica.dataCaricamento.between(dataInizio, dataFine));
                break;
        }

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                return antiriciclaggioFindExpression.and(qPratica.operatore.id.eq(currentOperatorId));
            case CLIENTI_READ_ALL:
                return antiriciclaggioFindExpression.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
            // caso ANTI_RICICLAGGIO_OUT come CLIENTI_READ_SUPER
            case ANTI_RICICLAGGIO_OUT:
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    return antiriciclaggioFindExpression.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return antiriciclaggioFindExpression;
    }

    public BooleanExpression getPredicateForImpegniRinnovabili(final Long currentAziendaId, final long currentOperatorId,
                                                               final Role.RoleName nominativiReadRoleName, final Date dataRinnovoTrattenuta) throws PermissionDeniedException {
        final String tipoCliente = Tipo.NOMINATIVO.getValue();
        final QTrattenute qTrattenute = QTrattenute.trattenute;
        final BooleanExpression dataRinnovoBeforeOrEqual = qTrattenute.dataRinnovoTrat.before(dataRinnovoTrattenuta)
                .or(qTrattenute.dataRinnovoTrat.eq(dataRinnovoTrattenuta));
        final BooleanExpression clienteTipoEqualsNominativo = qTrattenute.cliente.tipo.eq(tipoCliente);
        final BooleanExpression impegnoNotiCoesNullOrDataRinnovoNotEqualDataNotifica = qTrattenute.notiCoes.isNull()
                .or(qTrattenute.dataRinnovoTrat.ne(qTrattenute.notiCoes));

        BooleanExpression impegniRinnovabiliExpression = dataRinnovoBeforeOrEqual.and(clienteTipoEqualsNominativo)
                .and(impegnoNotiCoesNullOrDataRinnovoNotEqualDataNotifica);

        if (nominativiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (nominativiReadRoleName) {
            case NOMINATIVI_READ_OWN:
                impegniRinnovabiliExpression = impegniRinnovabiliExpression
                        .and(qTrattenute.cliente.operatoreNominativo.id.eq(currentOperatorId));
                break;
            case NOMINATIVI_READ_ALL:
                impegniRinnovabiliExpression = impegniRinnovabiliExpression
                        .and(qTrattenute.cliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                break;
            case NOMINATIVI_READ_SUPER:
                if (currentAziendaId != null) {
                    impegniRinnovabiliExpression = impegniRinnovabiliExpression
                            .and(qTrattenute.cliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return impegniRinnovabiliExpression;
    }

    public BooleanExpression getPredicateForCountNewLead(final Long currentAziendaId, final long currentOperatorId,
                                                         final Role.RoleName nominativiReadRoleName) throws PermissionDeniedException {

        final QCliente qCliente = QCliente.cliente;

        final Collection<String> provenienzaCollection = new LinkedList<>();
        provenienzaCollection.add(Provenienza.LEAD.getValue());
        provenienzaCollection.add(Provenienza.WEB.getValue());
        provenienzaCollection.add(Provenienza.CAMPAGNA_DA_FILE.getValue());
        provenienzaCollection.add(Provenienza.FACEBOOK.getValue());

        BooleanExpression leadExpression = qCliente.statoNominativo.eq(StatoNominativo.DA_LAVORARE.getValue())
                .and(qCliente.tipo.eq(Tipo.NOMINATIVO.getValue())).and(qCliente.provenienza.in(provenienzaCollection));

        if (nominativiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (nominativiReadRoleName) {
            case NOMINATIVI_READ_OWN:
                leadExpression = leadExpression.and(qCliente.operatoreNominativo.id.eq(currentOperatorId));
                break;
            case NOMINATIVI_READ_ALL:
                leadExpression = leadExpression.and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                break;
            case NOMINATIVI_READ_SUPER:
                if (currentAziendaId != null) {
                    leadExpression = leadExpression.and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return leadExpression;
    }

    public BooleanExpression getPredicateForFindOperatorByRoleNameInAndAzienda_Id(
            final Collection<RoleName> roleNameCollection, final Long aziendaId) {
        final QOperator qOperator = QOperator.operator;

        BooleanExpression booleanExpression = qOperator.role.any().roleName.in(roleNameCollection);

        if (aziendaId != null) {
            booleanExpression = booleanExpression.and(qOperator.azienda.id.eq(aziendaId));
        }

        return booleanExpression;
    }

    public BooleanExpression getPredicateForPraticheRinnovabili(final Long currentAziendaId, final long operatorAziendaId,
                                                                final long currentOperatorId, final Role.RoleName clientiReadRoleName, final Date endDate) throws PermissionDeniedException {
        final QPratica qPratica = QPratica.pratica;
        BooleanExpression praticheRinnovabiliExpression = qPratica.dataRinnovo.loe(endDate)
                .and(qPratica.statoPratica.ne(StatoPratica.ESTINTA.getValue()))
                .and((qPratica.notiRinnovo.isNull().or(qPratica.notiRinnovo.ne(qPratica.dataRinnovo))));

        // intellij dice che e duplicato, sicuro!

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                praticheRinnovabiliExpression = praticheRinnovabiliExpression
                        .and(qPratica.operatore.id.eq(currentOperatorId));
                break;
            case CLIENTI_READ_ALL:
                praticheRinnovabiliExpression = praticheRinnovabiliExpression
                        .and(qPratica.operatore.azienda.id.eq(operatorAziendaId));
                break;
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    praticheRinnovabiliExpression = praticheRinnovabiliExpression
                            .and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }
        return praticheRinnovabiliExpression;
    }

    public BooleanExpression getPredicateForTrattenuteByDataRinnovoTrattenutaBeforeThanDays(final Long currentAziendaId,
                                                                                            final long operatorAziendaId, final long currentOperatorId, final Role.RoleName clientiReadRoleName, final Date endDate)
            throws PermissionDeniedException {
        final QTrattenute qTrattenute = QTrattenute.trattenute;
        final QCliente qCliente = QCliente.cliente;
        final BooleanExpression dataRinnovoBeforeOrEqual = qTrattenute.dataRinnovoTrat.before(endDate)
                .or(qTrattenute.dataRinnovoTrat.eq(endDate));
        final BooleanExpression clienteTipoeqCliente = qCliente.tipo.eq(Tipo.CLIENTE.getValue());
        final BooleanExpression trattenutaNotiCoesNullOrDataRinnovoNotEqualDataNotifica = qTrattenute.notiCoes.isNull()
                .or(qTrattenute.dataRinnovoTrat.ne(qTrattenute.notiCoes));
        BooleanExpression trattenuteInScadenzaExpression = dataRinnovoBeforeOrEqual.and(clienteTipoeqCliente)
                .and(trattenutaNotiCoesNullOrDataRinnovoNotEqualDataNotifica);

        // TODO a me non piace tanto sta cosa, ma che fa???

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                trattenuteInScadenzaExpression = trattenuteInScadenzaExpression
                        .and(qTrattenute.cliente.pratica.any().operatore.id.eq(currentOperatorId));
                break;
            case CLIENTI_READ_ALL:
                trattenuteInScadenzaExpression = trattenuteInScadenzaExpression
                        .and(qTrattenute.cliente.pratica.any().operatore.azienda.id.eq(operatorAziendaId));
                break;
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    trattenuteInScadenzaExpression = trattenuteInScadenzaExpression
                            .and(qTrattenute.cliente.pratica.any().operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return trattenuteInScadenzaExpression;
    }

    public BooleanExpression getPredicateForClientiDaRichiamare(final Long currentAziendaId, final long operatorAziendaId,
                                                                final long currentOperatorId, final Role.RoleName clientiReadRoleName, final Date endDate) throws PermissionDeniedException {

        final QPratica qPratica = QPratica.pratica;

        BooleanExpression clientiDaRichiamareExpression = qPratica.dataRecall.loe(endDate)
                .and((qPratica.notiRecall.isNull().or(qPratica.dataRecall.ne(qPratica.notiRecall))));

        // intellij dice che e duplicato, sicuro!

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                clientiDaRichiamareExpression = clientiDaRichiamareExpression
                        .and(qPratica.operatore.id.eq(currentOperatorId));
                break;
            case CLIENTI_READ_ALL:
                clientiDaRichiamareExpression = clientiDaRichiamareExpression
                        .and(qPratica.operatore.azienda.id.eq(operatorAziendaId));
                break;
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    clientiDaRichiamareExpression = clientiDaRichiamareExpression
                            .and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return clientiDaRichiamareExpression;

    }

    public BooleanExpression getPredicateForPreventiviClientiInCorso(final Long currentAziendaId, final long operatorAziendaId,
                                                                     final long currentOperatorId, final Role.RoleName clientiReadRoleName) throws PermissionDeniedException {

        final QPreventivo qPreventivo = QPreventivo.preventivo;

        BooleanExpression preventiviClientiInCorsoBooleanExpression = qPreventivo.notificaPreventivo.isFalse();

        // intellij dice che e duplicato, sicuro!

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                preventiviClientiInCorsoBooleanExpression = preventiviClientiInCorsoBooleanExpression
                        .and(qPreventivo.cliente.pratica.any().operatore.id.eq(currentOperatorId));
                break;
            case CLIENTI_READ_ALL:
                preventiviClientiInCorsoBooleanExpression = preventiviClientiInCorsoBooleanExpression
                        .and(qPreventivo.cliente.pratica.any().operatore.azienda.id.eq(operatorAziendaId));
                break;
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    preventiviClientiInCorsoBooleanExpression = preventiviClientiInCorsoBooleanExpression
                            .and(qPreventivo.cliente.pratica.any().operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return preventiviClientiInCorsoBooleanExpression;
    }

    // TODO magari si puo fare un mix con quello per la fatturazione???
    public BooleanExpression getPredicateForFindPratichePerfezionateWidget(Date dataInizio, Date dataFine,
                                                                           final String tipoPratica, final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId,
                                                                           final Role.RoleName fatturazioneReadRoleName) throws PermissionDeniedException {

        final QPratica qPratica = QPratica.pratica;

        BooleanExpression pratichePerfezionateExpression = qPratica.perfezionata.isTrue()
                .and(qPratica.tipoPratica.eq(tipoPratica));

        dataInizio = DateToCalendar.settaGiorno(dataInizio, 00, 00, 00);
        dataFine = DateToCalendar.settaGiorno(dataFine, 23, 59, 59);
        pratichePerfezionateExpression = pratichePerfezionateExpression
                .and(qPratica.dataPerf.between(dataInizio, dataFine));

        if (fatturazioneReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (fatturazioneReadRoleName) {
            case FATTURAZIONE_READ_OWN:
                pratichePerfezionateExpression = pratichePerfezionateExpression
                        .and(qPratica.operatore.id.eq(currentOperatorId));
                break;
            case FATTURAZIONE_READ_ALL:
                pratichePerfezionateExpression = pratichePerfezionateExpression
                        .and(qPratica.operatore.azienda.id.eq(operatorAziendaId));
                break;
            case FATTURAZIONE_READ_SUPER:
                if (currentAziendaId != null) {
                    pratichePerfezionateExpression = pratichePerfezionateExpression
                            .and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return pratichePerfezionateExpression;
    }

    public Predicate getPredicateForFindPraticheCaricateWidget(final String tipoPratica, final Date startDate, final Date endDate,
                                                               final Long currentAziendaId, final long operatorAziendaId, final long currentOperatorId, final Role.RoleName clientiReadRoleName)
            throws PermissionDeniedException {

        final QPratica qPratica = QPratica.pratica;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!StringUtils.isEmpty(tipoPratica)) {
            booleanBuilder = booleanBuilder.and(qPratica.tipoPratica.eq(tipoPratica));
        }

        booleanBuilder = booleanBuilder.and(qPratica.dataCaricamento.between(startDate, endDate)
                .and(qPratica.cliente.tipo.eq(Tipo.CLIENTE.getValue())));

        // intellij dice che e duplicato, sicuro!

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                booleanBuilder = booleanBuilder.and(qPratica.operatore.id.eq(currentOperatorId));
                break;
            case CLIENTI_READ_ALL:
                booleanBuilder = booleanBuilder.and(qPratica.operatore.azienda.id.eq(operatorAziendaId));
                break;
            case CLIENTI_READ_SUPER:
                if (currentAziendaId != null) {
                    booleanBuilder = booleanBuilder.and(qPratica.operatore.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return booleanBuilder;
    }

    public BooleanExpression getPredicateForDataRecallNominativoExpired(final Long currentAziendaId, final long currentOperatorId,
                                                                        final Role.RoleName nominativiReadRoleName) throws PermissionDeniedException {
        final Date today = DateToCalendar.settaGiorno(new Date(), 00, 00, 00);

        final QCliente qCliente = QCliente.cliente;
        final String tipoNominativo = Tipo.NOMINATIVO.getValue();
        final BooleanExpression dataRecallNominativoNotNull = qCliente.dataRecallNominativo.isNotNull();
        final BooleanExpression dataRecallNominativoLess = qCliente.dataRecallNominativo.lt(today);
        final BooleanExpression tipoNominativoEquals = qCliente.tipo.eq(tipoNominativo);
        BooleanExpression nominativiDataRecallExpired = dataRecallNominativoNotNull.and(tipoNominativoEquals).and(dataRecallNominativoLess);

        if (nominativiReadRoleName == null) {
            throw new PermissionDeniedException();
        }

        switch (nominativiReadRoleName) {
            case NOMINATIVI_READ_OWN:
                nominativiDataRecallExpired = nominativiDataRecallExpired
                        .and(qCliente.operatoreNominativo.id.eq(currentOperatorId));
                break;
            case NOMINATIVI_READ_ALL:
                nominativiDataRecallExpired = nominativiDataRecallExpired
                        .and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                break;
            case NOMINATIVI_READ_SUPER:
                if (currentAziendaId != null) {
                    nominativiDataRecallExpired = nominativiDataRecallExpired
                            .and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                }
                break;
        }

        return nominativiDataRecallExpired;
    }


    public BooleanExpression getPredicateForTipoAndDataClienteNotNullAndProvenienzaAndBetweenDataInsAndStatoNominativoNotNullAndOperatorIn(final Long currentAziendaId,
                                                                                                                                           final long operatorId,
                                                                                                                                           final Role.RoleName nominativiReadRoleName,
                                                                                                                                           final Provenienza provenienza,
                                                                                                                                           final Date from, final Date to,
                                                                                                                                           final long aziendaId,
                                                                                                                                           final List<String> operatoriSelezionati,
                                                                                                                                           final boolean isEnabledToDoSwitch,
                                                                                                                                           final boolean isOnlyClienti,
                                                                                                                                           final String fornitoreLead) {

        final QCliente qCliente = QCliente.cliente;
        final String tipoClienteValue = Tipo.CLIENTE.getValue();
        final String tipoNominativoValue = Tipo.NOMINATIVO.getValue();
        final BooleanExpression dataClienteNotNull = qCliente.dataCliente.isNotNull();

        final BooleanExpression dataInserimentoBetween = qCliente.dataIns.isNotNull()
                .and(qCliente.dataIns.between(from, to));

        final BooleanExpression statoNominativoNotNullOrEmpty = qCliente.statoNominativo.isNotNull()
                .and(qCliente.statoNominativo.isNotEmpty());

        BooleanExpression nominativiTotal = dataInserimentoBetween.and(statoNominativoNotNullOrEmpty)
                .and(qCliente.provenienza.isNotNull()).and(qCliente.provenienza.isNotEmpty())
                .and(qCliente.operatoreNominativo.isNotNull());

        if (isOnlyClienti) {
            final BooleanExpression tipoClienteAndDataClienteNotNull = qCliente.tipo.eq(tipoClienteValue).and(dataClienteNotNull);
            nominativiTotal = nominativiTotal.and(tipoClienteAndDataClienteNotNull);
        } else {
            final BooleanExpression tipoNominativoEqualsOrTipoClienteAndDataClienteNotNull = qCliente.tipo.eq(tipoNominativoValue)
                    .or(qCliente.tipo.eq(tipoClienteValue).and(dataClienteNotNull));
            nominativiTotal = nominativiTotal.and(tipoNominativoEqualsOrTipoClienteAndDataClienteNotNull);
        }

        if (provenienza != null) {
            final BooleanExpression provenienzaEquals = qCliente.provenienza.equalsIgnoreCase(provenienza.getValue());
            nominativiTotal = nominativiTotal.and(provenienzaEquals);
        }

        if (!StringUtils.isEmpty(fornitoreLead)) {
            final BooleanExpression fornitoreLeadEquals = qCliente.fornitoreLead.equalsIgnoreCase(fornitoreLead);
            nominativiTotal = nominativiTotal.and(fornitoreLeadEquals);
        }

        if (operatoriSelezionati != null && !operatoriSelezionati.isEmpty()) {
            nominativiTotal = nominativiTotal
                    .and(qCliente.operatoreNominativo.username.in(operatoriSelezionati));
        }

        if (isEnabledToDoSwitch) {
            switch (nominativiReadRoleName) {
                case NOMINATIVI_READ_OWN:
                    nominativiTotal = nominativiTotal
                            .and(qCliente.operatoreNominativo.id.eq(operatorId));
                    break;
                case NOMINATIVI_READ_ALL:
                    nominativiTotal = nominativiTotal
                            .and(qCliente.operatoreNominativo.azienda.id.eq(aziendaId));
                    break;
                case NOMINATIVI_READ_SUPER:
                    if (currentAziendaId != null) {
                        nominativiTotal = nominativiTotal
                                .and(qCliente.operatoreNominativo.azienda.id.eq(currentAziendaId));
                    }
                    break;
            }
        } else {
            nominativiTotal = nominativiTotal.and(qCliente.operatoreNominativo.id.eq(operatorId));
        }

        return nominativiTotal;
    }


    public BooleanExpression getPredicateForNoticeBoardByAziendaId(final Long currentAziendaId, final long aziendaId,
                                                                   final boolean haveReadSuperAuthority) {
        final QNoticeBoard qNoticeBoard = QNoticeBoard.noticeBoard;

        BooleanExpression allNoticeBoard = qNoticeBoard.title.isNotNull().and(qNoticeBoard.title.isNotEmpty());

        if (haveReadSuperAuthority) {
            if (currentAziendaId != null) {
                allNoticeBoard = allNoticeBoard.and(qNoticeBoard.aziendaAssigned.id.eq(currentAziendaId));
            }
        } else {
            allNoticeBoard = allNoticeBoard.and(qNoticeBoard.aziendaAssigned.id.eq(aziendaId));

        }
        return allNoticeBoard;
    }


    public BooleanExpression getPredicateForAdvancedSearchCliente(final long operatorId,
                                                                  final Role.RoleName clientiReadRoleName,
                                                                  final long aziendaId,
                                                                  final AdvancedSearch advancedSearch)
            throws PermissionDeniedException {

        if (clientiReadRoleName == null) {
            throw new PermissionDeniedException();
        }


        final QPratica qPratica = QPratica.pratica;
        BooleanExpression advancedSearchClientiPredicate = qPratica.cliente.tipo.eq(Tipo.CLIENTE.getValue());

        if (advancedSearch.getAziendaId() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.operatore.azienda.id.eq(advancedSearch.getAziendaId()));
        }

        if (advancedSearch.getOperatorList() != null && !advancedSearch.getOperatorList().isEmpty()) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.operatore.username.in(advancedSearch.getOperatorList()));
        }

        if (advancedSearch.getSesso() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.sesso.isNotNull())
                    .and(qPratica.cliente.sesso.eq(advancedSearch.getSesso()));
        }

        if (advancedSearch.getDataNascitaInizio() != null && advancedSearch.getDataNascitaFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.dataNascita.isNotNull())
                    .and(qPratica.cliente.dataNascita.between(advancedSearch.getDataNascitaInizio(),
                            advancedSearch.getDataNascitaFine()));
        }

        if (!StringUtils.isEmpty(advancedSearch.getProvinciaResidenza())) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.residenza.provResidenza.isNotNull())
                    .and(qPratica.cliente.residenza.provResidenza.isNotEmpty())
                    .and(qPratica.cliente.residenza.provResidenza.equalsIgnoreCase(advancedSearch.getProvinciaResidenza()));
        }

        if (advancedSearch.getStatoPraticaList() != null && !advancedSearch.getStatoPraticaList().isEmpty()) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.statoPratica.isNotNull())
                    .and(qPratica.statoPratica.isNotEmpty())
                    .and(qPratica.statoPratica.in(advancedSearch.getStatoPraticaList()));
        }

        if (advancedSearch.getImpieghiList() != null && !advancedSearch.getImpieghiList().isEmpty()
                && advancedSearch.getDataAssunzione() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.impiego.isNotNull())
                    .and(qPratica.cliente.impiego.isNotEmpty())
                    .and(qPratica.cliente.impiego.in(advancedSearch.getImpieghiList()))
                    .and(qPratica.cliente.dataInizio.loe(advancedSearch.getDataAssunzione()));
        } else if (advancedSearch.getImpieghiList() != null && !advancedSearch.getImpieghiList().isEmpty()) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.impiego.isNotNull())
                    .and(qPratica.cliente.impiego.isNotEmpty())
                    .and(qPratica.cliente.impiego.in(advancedSearch.getImpieghiList()));
        }

        if (advancedSearch.getProvenienzaList() != null && !advancedSearch.getProvenienzaList().isEmpty()) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.provenienza.isNotNull())
                    .and(qPratica.cliente.provenienza.isNotEmpty())
                    .and(qPratica.cliente.provenienza.in(advancedSearch.getProvenienzaList()));
        }
        
        //*** Mod Cristian 11-11-2021
        if (advancedSearch.getProvenienzaDescList() != null && !advancedSearch.getProvenienzaDescList().isEmpty()) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.provenienzaDesc.isNotNull())
                    .and(qPratica.cliente.provenienzaDesc.isNotEmpty())
                    .and(qPratica.cliente.provenienzaDesc.in(advancedSearch.getProvenienzaDescList()));
        }
        //*******

        if (advancedSearch.getTipoRinnovo() != null
                && advancedSearch.getDataRinnovoInizio() != null
                && advancedSearch.getDataRinnovoFine() != null) {

            switch (advancedSearch.getTipoRinnovo()) {
                case PRATICA:
                    advancedSearchClientiPredicate = advancedSearchClientiPredicate
                            .and(qPratica.dataRinnovo.isNotNull())
                            .and(qPratica.dataRinnovo.between(advancedSearch.getDataRinnovoInizio(),
                                    advancedSearch.getDataRinnovoFine()));
                    break;
                case COESISTENZA:
                    advancedSearchClientiPredicate = advancedSearchClientiPredicate
                            .and(qPratica.cliente.trattenuta.any().isNotNull())
                            .and(qPratica.cliente.trattenuta.any().dataRinnovoTrat
                                    .between(advancedSearch.getDataRinnovoInizio(),
                                            advancedSearch.getDataRinnovoFine()));
                    break;
            }
        }

        if (advancedSearch.getDataRecallInizio() != null
                && advancedSearch.getDataRecallFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.dataRecall.isNotNull())
                    .and(qPratica.dataRecall.between(advancedSearch.getDataRecallInizio(),
                            advancedSearch.getDataRecallFine()));
        }

        if (advancedSearch.getDataInizioInserimento() != null
                && advancedSearch.getDataFineInserimento() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.dataIns.isNotNull())
                    .and(qPratica.cliente.dataIns.between(advancedSearch.getDataInizioInserimento(),
                            advancedSearch.getDataFineInserimento()));
        }

        if (advancedSearch.getDataInizioCaricamento() != null
                && advancedSearch.getDataFineCaricamento() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.dataCaricamento.isNotNull())
                    .and(qPratica.dataCaricamento.between(advancedSearch.getDataInizioCaricamento(),
                            advancedSearch.getDataFineCaricamento()));
        }

        if (advancedSearch.getDataIstruttoriaInizio() != null
                && advancedSearch.getDataIstruttoriaFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.dataIstruttoria.isNotNull())
                    .and(qPratica.dataIstruttoria.between(advancedSearch.getDataIstruttoriaInizio(),
                            advancedSearch.getDataIstruttoriaFine()));
        }

        if (advancedSearch.getDataIstruitaInizio() != null
                && advancedSearch.getDataIstruitaFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.dataIstruita.isNotNull())
                    .and(qPratica.dataIstruita.between(advancedSearch.getDataIstruitaInizio(),
                            advancedSearch.getDataIstruitaFine()));
        }

        if (advancedSearch.getDataDeliberaInizio() != null
                && advancedSearch.getDataDeliberaFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.dataDelibera.isNotNull())
                    .and(qPratica.dataDelibera.between(advancedSearch.getDataDeliberaInizio(),
                            advancedSearch.getDataDeliberaFine()));
        }

        if (advancedSearch.getDataFirmaContrattiInizio() != null
                && advancedSearch.getDataFirmaContrattiFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.dataFirmaContratti.isNotNull())
                    .and(qPratica.dataFirmaContratti.between(advancedSearch.getDataFirmaContrattiInizio(),
                            advancedSearch.getDataFirmaContrattiFine()));
        }

        if (advancedSearch.getDataDecorrenzaInizio() != null
                && advancedSearch.getDataDecorrenzaFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.decorrenza.isNotNull())
                    .and(qPratica.decorrenza.between(advancedSearch.getDataDecorrenzaInizio(),
                            advancedSearch.getDataDecorrenzaFine()));
        }

        if (advancedSearch.getDataLiquidazioneInizio() != null
                && advancedSearch.getDataLiquidazioneFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.dataLiquidazione.isNotNull())
                    .and(qPratica.dataLiquidazione.between(advancedSearch.getDataLiquidazioneInizio(),
                            advancedSearch.getDataLiquidazioneFine()));
        }

        if (advancedSearch.getDataPerfezionamentoInizio() != null
                && advancedSearch.getDataPerfezionamentoFine() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.dataPerf.isNotNull())
                    .and(qPratica.dataPerf.between(advancedSearch.getDataPerfezionamentoInizio(),
                            advancedSearch.getDataPerfezionamentoFine()));
        }

        if (advancedSearch.getAmministrazione() != null) {
            advancedSearchClientiPredicate = advancedSearchClientiPredicate
                    .and(qPratica.cliente.amministrazione.any().eq(advancedSearch.getAmministrazione()));
        }

        //Porcodio però
        switch (clientiReadRoleName) {
            case CLIENTI_READ_OWN:
                advancedSearchClientiPredicate = advancedSearchClientiPredicate
                        .and(qPratica.operatore.id.eq(operatorId));
                break;
            case CLIENTI_READ_ALL:
                advancedSearchClientiPredicate = advancedSearchClientiPredicate
                        .and(qPratica.operatore.azienda.id.eq(aziendaId));
                break;
            case CLIENTI_READ_SUPER:
                break;
        }

        return advancedSearchClientiPredicate;
    }


    public BooleanExpression getPredicateForAdvancedSearchNominativo(final long operatorId,
                                                                     final Role.RoleName nominativiReadRoleName,
                                                                     final long aziendaId,
                                                                     final AdvancedSearch advancedSearch)
            throws PermissionDeniedException {

        if (nominativiReadRoleName == null) {
            throw new PermissionDeniedException();
        }


        final QCliente qCliente = QCliente.cliente;
        BooleanExpression advancedSearchNominativiPredicate = qCliente.tipo.eq(Tipo.NOMINATIVO.getValue());

        if (advancedSearch.getAziendaId() != null) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.operatoreNominativo.azienda.id.eq(advancedSearch.getAziendaId()));
        }

        if (advancedSearch.getOperatorList() != null && !advancedSearch.getOperatorList().isEmpty()) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.operatoreNominativo.username.in(advancedSearch.getOperatorList()));
        }

        if (advancedSearch.getSesso() != null) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.sesso.isNotNull())
                    .and(qCliente.sesso.eq(advancedSearch.getSesso()));
        }

        if (advancedSearch.getDataNascitaInizio() != null && advancedSearch.getDataNascitaFine() != null) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.dataNascita.isNotNull())
                    .and(qCliente.dataNascita.between(advancedSearch.getDataNascitaInizio(),
                            advancedSearch.getDataNascitaFine()));
        }

        if (!StringUtils.isEmpty(advancedSearch.getProvinciaResidenza())) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.residenza.provResidenza.isNotNull())
                    .and(qCliente.residenza.provResidenza.isNotEmpty())
                    .and(qCliente.residenza.provResidenza.equalsIgnoreCase(advancedSearch.getProvinciaResidenza()));
        }

        if (advancedSearch.getStatoNominativoList() != null && !advancedSearch.getStatoNominativoList().isEmpty()) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.statoNominativo.isNotNull())
                    .and(qCliente.statoNominativo.isNotEmpty())
                    .and(qCliente.statoNominativo.in(advancedSearch.getStatoNominativoList()));
        }

        if (advancedSearch.getProvenienzaList() != null && !advancedSearch.getProvenienzaList().isEmpty()) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.provenienza.isNotNull())
                    .and(qCliente.provenienza.isNotEmpty())
                    .and(qCliente.provenienza.in(advancedSearch.getProvenienzaList()));
        }
        
      //*** Mod Cristian 11-11-2021
        if (advancedSearch.getProvenienzaDescList() != null && !advancedSearch.getProvenienzaDescList().isEmpty()) {
        	advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.provenienzaDesc.isNotNull())
                    .and(qCliente.provenienzaDesc.isNotEmpty())
                    .and(qCliente.provenienzaDesc.in(advancedSearch.getProvenienzaDescList()));
        }
        //*******

        if (advancedSearch.getDataRecallInizio() != null
                && advancedSearch.getDataRecallFine() != null) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.dataRecallNominativo.isNotNull())
                    .and(qCliente.dataRecallNominativo.between(advancedSearch.getDataRecallInizio(),
                            advancedSearch.getDataRecallFine()));
        }

        if (advancedSearch.getImpieghiList() != null && !advancedSearch.getImpieghiList().isEmpty()
                && advancedSearch.getDataAssunzione() != null) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.impiego.isNotNull())
                    .and(qCliente.impiego.isNotEmpty())
                    .and(qCliente.impiego.in(advancedSearch.getImpieghiList()))
                    .and(qCliente.dataInizio.loe(advancedSearch.getDataAssunzione()));
        } else if (advancedSearch.getImpieghiList() != null && !advancedSearch.getImpieghiList().isEmpty()) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.impiego.isNotNull())
                    .and(qCliente.impiego.isNotEmpty())
                    .and(qCliente.impiego.in(advancedSearch.getImpieghiList()));
        }

        if (advancedSearch.getDataInizioInserimento() != null
                && advancedSearch.getDataFineInserimento() != null) {
            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.dataIns.isNotNull())
                    .and(qCliente.dataIns.between(advancedSearch.getDataInizioInserimento(),
                            advancedSearch.getDataFineInserimento()));
        }

        if (advancedSearch.getDataRinnovoInizio() != null && advancedSearch.getDataRinnovoFine() != null) {

            advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                    .and(qCliente.trattenuta.any().dataRinnovoTrat
                            .between(advancedSearch.getDataRinnovoInizio(),
                                    advancedSearch.getDataRinnovoFine()));
        }

        //SEmpre porcodi
        switch (nominativiReadRoleName) {
            case NOMINATIVI_READ_OWN:
                advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                        .and(qCliente.operatoreNominativo.id.eq(operatorId));
                break;
            case NOMINATIVI_READ_ALL:
                advancedSearchNominativiPredicate = advancedSearchNominativiPredicate
                        .and(qCliente.operatoreNominativo.azienda.id.eq(aziendaId));
                break;
            case NOMINATIVI_READ_SUPER:
                break;
        }

        return advancedSearchNominativiPredicate;
    }
}
