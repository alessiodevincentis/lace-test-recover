package com.woonders.lacemscommon.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ClientePratica;
import com.woonders.lacemscommon.app.model.EstinzionePraticaCliente;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.model.util.ClientePraticaUtil;
import com.woonders.lacemscommon.app.viewmodel.EstinzioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ClienteViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.EstinzioneViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.PraticaViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entity.Pratica.TipoPratica;
import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.db.entityutil.EstinzioneUtil;
import com.woonders.lacemscommon.db.repository.AziendaRepository;
import com.woonders.lacemscommon.db.repository.ClienteRepository;
import com.woonders.lacemscommon.db.repository.EstinzioneRepository;
import com.woonders.lacemscommon.db.repository.PraticaRepository;
import com.woonders.lacemscommon.exception.PermissionDeniedException;
import com.woonders.lacemscommon.exception.UnableToUpdateException;
import com.woonders.lacemscommon.service.StatoPraticheService;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemscommon.util.PredicateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StatoPraticheServiceImpl extends AbstractAppServiceImpl implements StatoPraticheService {

    public static final String NAME = "statoPraticheServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private PraticaViewModelCreator praticaViewModelCreator;
    @Autowired
    private ClienteViewModelCreator clienteViewModelCreator;
    @Autowired
    private PredicateHelper predicateHelper;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private ClientePraticaUtil clientePraticaUtil;
    @Autowired
    private EstinzioneRepository estinzioneRepository;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private EstinzioneViewModelCreator estinzioneViewModelCreator;
    @Autowired
    private EstinzioneUtil estinzioneUtil;
    @Autowired
    private ClienteRepository clienteRepository;

    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public long countByStatoPraticaAndTipoClienteAndUsername(final Long currentAziendaId, final long currentOperatorId,
                                                             final Role.RoleName clientiReadRoleName, final String statoPratica, final String tipoPratica) {

        try {
            return praticaRepository.count(predicateHelper.getPredicateForfindByStatoPraticaAndTipoClienteAndUsername(
                    currentAziendaId, currentOperatorId, clientiReadRoleName, statoPratica, tipoPratica));
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public ViewModelPage<ClientePratica> findByStatoPraticaAndTipoClienteAndUsername(final Long currentAziendaId,
                                                                                     final long currentOperatorId, final Role.RoleName clientiReadRoleName, final String statoPratica, final String tipoPratica,
                                                                                     final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder,
                                                                                     final Map<QueryDSLHelper.TableField, Object> filters) {

        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        try {

            BooleanExpression statoPraticheExpression = predicateHelper.getPredicateForfindByStatoPraticaAndTipoClienteAndUsername(currentAziendaId,
                    currentOperatorId, clientiReadRoleName, statoPratica, tipoPratica);

            statoPraticheExpression = statoPraticheExpression.and(queryDSLHelper.createFilterPredicate(filters));

            final Page<Pratica> page = praticaRepository.findAll(statoPraticheExpression,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));
            return new ViewModelPageImpl<>(clientePraticaUtil.generateClientePraticaList(page.getContent()), page.getTotalPages(),
                    page.getTotalElements());
        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }


    }

    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public long countEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(final Long currentAziendaId,
                                                                                   final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                                                   final StatoConteggioEstinzione statoConteggioEstinzione, final String tipoEstinzione) {

        final Date notifyDate = calcNotifyDateForFindEstinzioni(currentAziendaId);

        try {
            final BooleanExpression statoConteggioEstinzioneExpression = predicateHelper.
                    getPredicateForFindEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(currentAziendaId, currentOperatorId,
                            clientiReadRoleName, statoConteggioEstinzione, notifyDate, tipoEstinzione);
            return estinzioneRepository.count(statoConteggioEstinzioneExpression);
        } catch (final PermissionDeniedException e) {
            return 0;
        }
    }

    @PreAuthorize("hasAuthority('CLIENTI_READ_SUPER') or (hasAuthority('CLIENTI_READ_ALL') and @authorizationConditionChecker.isSameAziendaId(#currentAziendaId))"
            + " or hasAuthority('CLIENTI_READ_OWN')")
    @Override
    public ViewModelPage<EstinzionePraticaCliente> findEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(final Long currentAziendaId,
                                                                                                                     final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                                                                                                     final StatoConteggioEstinzione statoConteggioEstinzione, final String tipoEstinzione,
                                                                                                                     final int firstElementIndex, final int pageSize, final String sortField,
                                                                                                                     final QueryDSLHelper.SortOrder sortOrder,
                                                                                                                     final Map<QueryDSLHelper.TableField, Object> filters) {

        final QSort qSort = queryDSLHelper.createSortOrder(QueryDSLHelper.TableField.fromValue(sortField), sortOrder);

        final Date notifyDate = calcNotifyDateForFindEstinzioni(currentAziendaId);

        try {
            BooleanExpression statoConteggioEstinzioneExpression = predicateHelper.
                    getPredicateForFindEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(currentAziendaId, currentOperatorId,
                            clientiReadRoleName, statoConteggioEstinzione, notifyDate, tipoEstinzione);

            statoConteggioEstinzioneExpression = statoConteggioEstinzioneExpression.and(queryDSLHelper.createFilterPredicate(filters));

            final Page<Estinzione> page = estinzioneRepository.findAll(statoConteggioEstinzioneExpression,
                    new PageRequest(firstElementIndex / pageSize, pageSize, qSort));
            return new ViewModelPageImpl<>(clientePraticaUtil.generateEstinzionePraticaListFromEstinzioneList(page.getContent()),
                    page.getTotalPages(), page.getTotalElements());
        } catch (final PermissionDeniedException e) {
            return new ViewModelPageImpl<>(new LinkedList<>(), 0, 0);
        }
    }

    private Date calcNotifyDateForFindEstinzioni(final Long currentAziendaId) {
        final Integer giorniNotificaConteggioEstinzione = aziendaRepository.getOne(currentAziendaId).getGiorniNotificaConteggioEstinzione();
        return DateToCalendar.addDays(new Date(), giorniNotificaConteggioEstinzione);
    }

    // TODO Questo metodo è duplicato anche dentro RegisterCliente trovare un
    // modo per unificarli
    @Override
    @Transactional
    public PraticaViewModel updateStatoPraticaByCodicePratica(final String statoPratica, final long codicePratica)
            throws UnableToUpdateException {
        try {
            final Pratica pratica = praticaRepository.findOne(codicePratica);
            pratica.setStatoPratica(statoPratica);
            pratica.setDataNotificaStatoPratica(getDataNotificaByStatoPraticaAndAziendaId(statoPratica, pratica.getOperatore().getAzienda().getId()));
            final StatoPratica statoPraticaEnum = StatoPratica.fromValue(statoPratica);
            final Date dateNow = DateToCalendar.getDateWithoutTime(new Date());
            switch (statoPraticaEnum) {
                case ISTRUTTORIA:
                    pratica.setDataIstruttoria(dateNow);
                    break;
                case ISTRUITA:
                    pratica.setDataIstruita(dateNow);
                    break;
                case DELIBERA:
                    pratica.setDataDelibera(dateNow);
                    break;
                case POLIZZA:
                    pratica.setDataFirmaContratti(dateNow);
                    break;
                case IN_VALUTAZIONE:
                    pratica.setDataDelibera(dateNow);
                    break;
                case LIQUIDATA:
                    if (TipoPratica.PRESTITO.getValue().equalsIgnoreCase(pratica.getTipoPratica())) {
                        pratica.setDataFirmaContratti(dateNow);
                    }
                    break;
                default:
                    break;
            }
            return praticaViewModelCreator.createViewModel(praticaRepository.save(pratica));
        } catch (

                final DataAccessException e) {
            throw new UnableToUpdateException(e);
        }
    }

    @Override
    @Transactional
    public PraticaViewModel updateDataNotificaByCodicePratica(final Date dataNotifica, final long codicePratica)
            throws UnableToUpdateException {
        try {
            final Pratica pratica = praticaRepository.findOne(codicePratica);
            pratica.setDataNotificaStatoPratica(dataNotifica);
            return praticaViewModelCreator.createViewModel(praticaRepository.save(pratica));
        } catch (final DataAccessException e) {
            throw new UnableToUpdateException(e);
        }
    }

    @Override
    @Transactional
    public EstinzioneViewModel updateStatoConteggioEstinzioneByIdEstinzione(final StatoConteggioEstinzione statoConteggioEstinzione, final long idEstinzione)
            throws UnableToUpdateException {
        try {
            final Estinzione estinzione = estinzioneRepository.findOne(idEstinzione);
            estinzione.setStatoConteggioEstinzione(statoConteggioEstinzione);
            estinzione.setDataNotificaConteggioEstinzione(estinzioneUtil.getDataNotificaConteggioEstinzione(statoConteggioEstinzione));
            return estinzioneViewModelCreator.createViewModel(estinzioneRepository.save(estinzione));
        } catch (final DataAccessException e) {
            throw new UnableToUpdateException(e);
        }
    }

    @Override
    @Transactional
    public EstinzioneViewModel updateDataNotificaConteggioEstinzioneByIdEstinzione(final Date dataNotifica, final long idEstinzione)
            throws UnableToUpdateException {
        try {
            final Estinzione estinzione = estinzioneRepository.findOne(idEstinzione);
            estinzione.setDataNotificaConteggioEstinzione(dataNotifica);
            return estinzioneViewModelCreator.createViewModel(estinzioneRepository.save(estinzione));
        } catch (final DataAccessException e) {
            throw new UnableToUpdateException(e);
        }
    }

    @Override
    public boolean isContainsDatesToBeNotified(final String statoPratica, final Role.RoleName clientiReadRoleName, final long operatorId, final long currentAzienda, final String tipoPratica) {
        final long countResult;
        try {
            countResult = praticaRepository.count(predicateHelper.getPredicateForfindByStatoPraticaAndDataNotificaExpiredAndTipoClienteAndUsername(currentAzienda, operatorId, clientiReadRoleName, statoPratica, tipoPratica));
        } catch (final PermissionDeniedException e) {
            return false;
        }

        return countResult > 0;
    }

    @Override
    public boolean isContainsDatesToBeNotified(final Long currentAziendaId,
                                               final long currentOperatorId, final Role.RoleName clientiReadRoleName,
                                               final StatoConteggioEstinzione statoConteggioEstinzione, final String tipoEstinzione) {

        final BooleanExpression dataNotificaConteggioLessOrEqualTo = QEstinzione.estinzione.dataNotificaConteggioEstinzione.loe(new Date());
        final Date notifyDate = calcNotifyDateForFindEstinzioni(currentAziendaId);

        final long count;
        try {
            count = estinzioneRepository.count(predicateHelper.getPredicateForFindEstinzioniByStatoConteggioEstinzioneAndTipoClienteAndUsername(currentAziendaId,
                    currentOperatorId, clientiReadRoleName, statoConteggioEstinzione, notifyDate, tipoEstinzione).and(dataNotificaConteggioLessOrEqualTo));
            return count > 0;
        } catch (final PermissionDeniedException e) {
            return false;
        }
    }

    @Override
    public ClientePratica getClientePratica(final long id) {
        final Pratica pratica = praticaRepository.findOne(id);
        return ClientePratica.builder().praticaViewModel(praticaViewModelCreator.createViewModel(pratica))
                .clienteViewModel(clienteViewModelCreator.createViewModel(pratica.getCliente())).build();
    }

    @Override
    public EstinzioneViewModel getEstinzione(final long id) {
        final Estinzione estinzione = estinzioneRepository.findOne(id);
        return estinzioneViewModelCreator.createViewModel(estinzione);
    }

    @Override
    public EstinzionePraticaCliente getEstinzionePraticaCliente(final long id) {
        final Estinzione estinzione = estinzioneRepository.findOne(id);
        final Pratica pratica = praticaRepository.findOne(estinzione.getPratica().getCodicePratica());
        final Cliente cliente = clienteRepository.findByPratica_CodicePratica(pratica.getCodicePratica());
        return EstinzionePraticaCliente.builder().estinzioneViewModel(estinzioneViewModelCreator.createViewModel(estinzione))
                .praticaViewModel(praticaViewModelCreator.createViewModel(pratica))
                .clienteViewModel(clienteViewModelCreator.createViewModel(cliente)).build();
    }

}
