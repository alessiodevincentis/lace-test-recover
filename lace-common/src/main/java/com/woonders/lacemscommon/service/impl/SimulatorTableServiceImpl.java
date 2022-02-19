package com.woonders.lacemscommon.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.model.ViewModelPageImpl;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableDetailsViewModel;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.OperatorViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.SimulatorTableDetailsViewModelCreator;
import com.woonders.lacemscommon.app.viewmodel.creator.SimulatorTableViewModelCreator;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.*;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.repository.*;
import com.woonders.lacemscommon.exception.EmptyJobTypeListException;
import com.woonders.lacemscommon.exception.EmptyTanException;
import com.woonders.lacemscommon.exception.SimulatorTableInUseException;
import com.woonders.lacemscommon.exception.UnableToCloneSimulatorTableException;
import com.woonders.lacemscommon.service.SimulatorTableService;
import com.woonders.lacemscommon.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by ema98 on 8/10/2017.
 */
@Service
@Transactional(readOnly = true)
public class SimulatorTableServiceImpl implements SimulatorTableService {

    public static final String NAME = "simulatorTableServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private SimulatorTableRepository simulatorTableRepository;
    @Autowired
    private SimulatorTableDetailsRepository simulatorTableDetailsRepository;

    @Autowired
    private SimulatorTableViewModelCreator simulatorTableViewModelCreator;
    @Autowired
    private SimulatorTableDetailsViewModelCreator simulatorTableDetailsViewModelCreator;
    @Autowired
    private QueryDSLHelper queryDSLHelper;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private NumberUtil numberUtil;
    @Autowired
    private PraticaRepository praticaRepository;
    @Autowired
    private PreventivoRepository preventivoRepository;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;

    @PreAuthorize("hasAnyAuthority('SIMULATORI_READ_OWN', 'SIMULATORI_READ_ALL', 'SIMULATORI_READ_SUPER')")
    @Override
    public ViewModelPage<SimulatorTableViewModel> findAll(final Long aziendaId, final long operatorId, final Role.RoleName simulatorReadRoleName, final int firstElementIndex, final int pageSize, final String sortField, final QueryDSLHelper.SortOrder sortOrder, final Map<QueryDSLHelper.TableField, Object> filters) {
        final BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(queryDSLHelper.createFilterPredicate(filters));

        switch (simulatorReadRoleName) {
            case SIMULATORI_READ_OWN:
                booleanBuilder.and(QSimulatorTable.simulatorTable.grantedOperatorSet.any().id.eq(operatorId).or(QSimulatorTable.simulatorTable.creatorOperator.id.eq(operatorId)));
                break;
            case SIMULATORI_READ_ALL:
                booleanBuilder.and(QSimulatorTable.simulatorTable.azienda.id.eq(aziendaId));
                break;
            case SIMULATORI_READ_SUPER:
                if (aziendaId != null) {
                    booleanBuilder.and(QSimulatorTable.simulatorTable.azienda.id.eq(aziendaId));
                }
                break;
        }

        final Page<SimulatorTable> simulatorTablePage = simulatorTableRepository.findAll(booleanBuilder, new PageRequest(firstElementIndex / pageSize, pageSize));
        return new ViewModelPageImpl<>(simulatorTableViewModelCreator.createViewModelList(simulatorTablePage), simulatorTablePage.getTotalPages(), simulatorTablePage.getTotalElements());
    }

    @Override
    public List<SimulatorTableViewModel> findAssigned(final long operatorId, final Pratica.TipoPratica simulatorTableType, final List<Impiego> jobTypeList) {
        BooleanExpression booleanExpression = QSimulatorTable.simulatorTable.grantedOperatorSet.any().id.eq(operatorId)
                .and(QSimulatorTable.simulatorTable.enabled.isTrue()).and(QSimulatorTable.simulatorTable.simulatorTableType.eq(simulatorTableType));

        if (jobTypeList != null) {
            booleanExpression = booleanExpression.and(QSimulatorTable.simulatorTable.jobTypeSet.any().in(jobTypeList));
        }

        return simulatorTableViewModelCreator.createViewModelList(simulatorTableRepository.findAll(booleanExpression));
    }

    @Override
    public List<SimulatorTableDetailsViewModel> getDetails(final long simulatorTableId) {
        return simulatorTableDetailsViewModelCreator.createViewModelList(simulatorTableDetailsRepository.findAll(QSimulatorTableDetails.simulatorTableDetails.simulatorTable.id.eq(simulatorTableId)));
    }

    @Override
    public SimulatorTableViewModel findOne(final long simulatorTableId) {
        return simulatorTableViewModelCreator.createViewModel(simulatorTableRepository.findOne(simulatorTableId));
    }

    @Transactional(rollbackFor = {EmptyTanException.class, EmptyJobTypeListException.class})
    @Override
    public SimulatorTableViewModel save(final long creatorOperatorId, final long creatorOperatorAziendaId, final SimulatorTableViewModel simulatorTableViewModel, final List<SimulatorTableDetailsViewModel> simulatorTableDetailsViewModelList) throws EmptyTanException, EmptyJobTypeListException {

        if (simulatorTableDetailsViewModelList.stream().filter(simulatorTableDetailsViewModel -> numberUtil.isNullOrZero(simulatorTableDetailsViewModel.getTan())).count() > 0) {
            throw new EmptyTanException();
        }

        if (simulatorTableViewModel.getJobTypeList() == null || simulatorTableViewModel.getJobTypeList().isEmpty()) {
            throw new EmptyJobTypeListException();
        }

        if (isTableUsedSomewhere(simulatorTableViewModel.getId())) {

            SimulatorTable simulatorTable = simulatorTableRepository.getOne(simulatorTableViewModel.getId());
            simulatorTable.setGrantedOperatorSet(operatorViewModelCreator.fromList(simulatorTableViewModel.getGrantedOperatorViewModelList()));

            List<SimulatorTableDetails> simulatorTableDetailsList = simulatorTable.getSimulatorTableDetailsList();

            SimulatorTableViewModel simulatorTableViewModelToReturn = simulatorTableViewModelCreator.createViewModel(simulatorTable);
            simulatorTableViewModelToReturn.setSimulatorTableDetailsViewModelList(simulatorTableDetailsViewModelCreator.createViewModelList(simulatorTableDetailsList));

            return simulatorTableViewModelToReturn;
            
        } else {
            final Operator creatorOperator = operatorRepository.findOne(creatorOperatorId);

            final SimulatorTable simulatorTableToSave = simulatorTableViewModelCreator.createModel(simulatorTableViewModel);
            simulatorTableToSave.setCreatorOperator(creatorOperator);

            //if not multiagency, fetch the agency of the operator and set it
            if (simulatorTableToSave.getAzienda() == null && aziendaRepository.count() == 1) {
                simulatorTableToSave.setAzienda(aziendaRepository.getOne(creatorOperatorAziendaId));
            }

            final SimulatorTable savedSimulatorTable = simulatorTableRepository.save(simulatorTableToSave);
            final SimulatorTableViewModel savedSimulatorTableViewModel = simulatorTableViewModelCreator.createViewModel(savedSimulatorTable);

            final List<SimulatorTableDetails> simulatorTableDetailsList = simulatorTableDetailsViewModelCreator.createModelList(simulatorTableDetailsViewModelList);
            for (final SimulatorTableDetails simulatorTableDetails : simulatorTableDetailsList) {
                simulatorTableDetails.setSimulatorTable(savedSimulatorTable);
            }
            final List<SimulatorTableDetails> savedSimulatorTableDetailsList = simulatorTableDetailsRepository.save(simulatorTableDetailsList);


            savedSimulatorTableViewModel.setSimulatorTableDetailsViewModelList(simulatorTableDetailsViewModelCreator.createViewModelList(savedSimulatorTableDetailsList));
            return savedSimulatorTableViewModel;
        }
    }

    @Transactional(rollbackFor = SimulatorTableInUseException.class)
    @Override
    public void delete(final long simulatorTableId) throws SimulatorTableInUseException {
        if (isTableUsedSomewhere(simulatorTableId)) {
            throw new SimulatorTableInUseException();
        }
        simulatorTableRepository.delete(simulatorTableId);
    }

    @Transactional
    @Override
    public boolean enable(final long simulatorTableId, final boolean enabled) {
        //TODO se non lo trova andrebbe lanciata eccezione
        final SimulatorTable simulatorTable = simulatorTableRepository.findOne(simulatorTableId);
        if (simulatorTable != null) {
            simulatorTable.setEnabled(enabled);
            return simulatorTable.isEnabled();
        }
        return false;
    }

    @Transactional
    @Override
    public long clone(final long simulatorTableId) {
        final SimulatorTable simulatorTable = simulatorTableRepository.findOne(simulatorTableId);
        if (simulatorTable != null) {
            //zozzata per non clonare i campi XD crea un oggetto uguale ma detached, cosi lo salvo nuovo senza modificare quello che sto clonando
            final SimulatorTable simulatorTableClone = simulatorTableViewModelCreator.createModel(simulatorTableViewModelCreator.createViewModel(simulatorTable));
            simulatorTableClone.setId(0);
            simulatorTableClone.setName(simulatorTableClone.getName() + " - Copia");
            final SimulatorTable simulatorTableCloneAfterSave = simulatorTableRepository.save(simulatorTableClone);

            final QSimulatorTableDetails qSimulatorTableDetails = QSimulatorTableDetails.simulatorTableDetails;
            final BooleanExpression simulatorTableIdBooleanExpression = qSimulatorTableDetails.simulatorTable.id.eq(simulatorTableId);
            //zozzata per non clonare i campi XD crea un oggetto uguale ma detached, cosi lo salvo nuovo senza modificare quello che sto clonando
            final List<SimulatorTableDetails> simulatorTableDetailsList = simulatorTableDetailsViewModelCreator.fromViewModelList(simulatorTableDetailsViewModelCreator.createViewModelList(simulatorTableDetailsRepository.findAll(simulatorTableIdBooleanExpression)));

            simulatorTableDetailsList.forEach(simulatorTableDetails -> {
                simulatorTableDetails.setId(0);
                simulatorTableDetails.setSimulatorTable(simulatorTableCloneAfterSave);
            });

            simulatorTableDetailsRepository.save(simulatorTableDetailsList);

            return simulatorTableCloneAfterSave.getId();

        }
        throw new UnableToCloneSimulatorTableException();
    }

    @Override
    public boolean isTableUsedSomewhere(final long simulatorTableId) {
        return (praticaRepository.count(QPratica.pratica.simulatorTable.id.eq(simulatorTableId))
                + preventivoRepository.count(QPreventivo.preventivo.simulatorTable.id.eq(simulatorTableId))) > 0;
    }
}
