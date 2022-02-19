package com.woonders.lacemsjsf.view.simulator;

import com.woonders.lacemscommon.app.viewmodel.OperatorViewModel;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableDetailsViewModel;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Role;
import com.woonders.lacemscommon.db.entityenum.FeeType;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.Mensilita;
import com.woonders.lacemscommon.exception.EmptyJobTypeListException;
import com.woonders.lacemscommon.exception.EmptyTanException;
import com.woonders.lacemscommon.exception.SimulatorTableInUseException;
import com.woonders.lacemscommon.service.OperatorService;
import com.woonders.lacemscommon.service.SimulatorTableService;
import com.woonders.lacemscommon.service.impl.OperatorServiceImpl;
import com.woonders.lacemscommon.service.impl.SimulatorTableServiceImpl;
import com.woonders.lacemscommon.util.NumberUtil;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ManagedBean(name = "simulatorView")
@ViewScoped
@Getter
@Setter
@Slf4j
public class SimulatorView implements Serializable {

    private static final String EURO = "€";
    private static final String PERCENTAGE = "%";
    private static final String TABLE_ACTIVE = " - Tabella Attiva";
    private static final String TABLE_NOT_ACTIVE = " - Tabella Disattivata";
    private SimulatorTableViewModel simulatorTableViewModel;
    private List<SimulatorTableDetailsViewModel> simulatorTableDetailsViewModelList;
    private SimulatorTableDetailsViewModel simulatorTableDetailsViewModelNewRow;
    private SimulatorTableDetailsViewModel simulatorTableDetailsViewModelAllLenght;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    private List<OperatorViewModel> allOperatorViewModelList;
    @ManagedProperty(OperatorServiceImpl.JSF_NAME)
    private OperatorService operatorService;
    @ManagedProperty(SimulatorTableServiceImpl.JSF_NAME)
    private SimulatorTableService simulatorTableService;
    @ManagedProperty(NumberUtil.JSF_NAME)
    private NumberUtil numberUtil;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;


    public SimulatorView() {
        simulatorTableViewModel = new SimulatorTableViewModel();
        simulatorTableDetailsViewModelNewRow = new SimulatorTableDetailsViewModel();
        simulatorTableDetailsViewModelAllLenght = new SimulatorTableDetailsViewModel();
        simulatorTableDetailsViewModelList = new ArrayList<>();
        for (int i = 120; i > 12; i = i - 12) {
            final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel = new SimulatorTableDetailsViewModel();
            simulatorTableDetailsViewModel.setLength(i);
            simulatorTableDetailsViewModel.setMensilita(Mensilita.MENSILITA_12.getValue());
            simulatorTableDetailsViewModelList.add(simulatorTableDetailsViewModel);
        }
    }

    @PostConstruct
    public void init() {
        loadOperatorsFromDb();
        if (passaggioParametriUtils.getParametri().containsKey(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_ID_PARAMETER)) {
            final long simulatorTableId = (long) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_ID_PARAMETER);
            simulatorTableViewModel = simulatorTableService.findOne(simulatorTableId);
            simulatorTableDetailsViewModelList = simulatorTableService.getDetails(simulatorTableId);
            //TODO VA SISTEMATO, ma non solo qui, ovunque. Se refreshi la pagina non hai piu il valore!
            passaggioParametriUtils.getParametri().remove(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_ID_PARAMETER);
        }
    }

    public void loadOperatorsFromDb() {
        log.info("in loadOperatorsFromDb");
        allOperatorViewModelList = operatorService.findByRoleNameInAndAzienda_Id(Arrays.asList(Role.RoleName.getSimulatoriReadRoles()),
                getChosenAziendaId());
        //automatically assign the simulator table to the operator who creates it, if he is of that agency
        if (simulatorTableViewModel.getAziendaViewModel() == null || simulatorTableViewModel.getAziendaViewModel().getId() == httpSessionUtil.getAziendaId()) {
            simulatorTableViewModel.setGrantedOperatorViewModelList(Arrays.asList(operatorService.getOne(httpSessionUtil.getOperatorId())));
        } else {
            simulatorTableViewModel.setGrantedOperatorViewModelList(null);
        }
    }

    private Long getChosenAziendaId() {
        log.info("in getChosenAziendaId");
        Long aziendaId = null;
        if (simulatorTableViewModel != null && simulatorTableViewModel.getAziendaViewModel() != null) {
            aziendaId = simulatorTableViewModel.getAziendaViewModel().getId();
        }
        return aziendaId;
    }

    public void selectAllOperators() {
        simulatorTableViewModel.setGrantedOperatorViewModelList(allOperatorViewModelList);
    }

    public void selectAllJobType() {
        simulatorTableViewModel.setJobTypeList(Arrays.asList(Impiego.values()));
    }

    public void deselectAllJobType() {
        simulatorTableViewModel.setJobTypeList(null);
    }

    public void addRowTableAction() {
        final int newLenght = simulatorTableDetailsViewModelNewRow.getLength();
        final int newMensilita = simulatorTableDetailsViewModelNewRow.getMensilita();
        if (newMensilita == 0) {
            FacesUtil.addMessage(propertiesUtil.getMsgSimulatorMensilitaZero(), FacesMessage.SEVERITY_WARN);
            return;
        }
        for (final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel : simulatorTableDetailsViewModelList) {
            if (newLenght == simulatorTableDetailsViewModel.getLength()) {
                FacesUtil.addMessage(propertiesUtil.getMsgSimulatorNewRowExistLenght(), FacesMessage.SEVERITY_WARN);
                return;
            }
        }
        simulatorTableDetailsViewModelList.add(simulatorTableDetailsViewModelNewRow);
        simulatorTableDetailsViewModelNewRow = new SimulatorTableDetailsViewModel();
    }

    public void removeRowTableAction(final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel) {
        simulatorTableDetailsViewModelList.remove(simulatorTableDetailsViewModel);
    }

    public void addValueOnAllLenghtOfTable() {
        for (final SimulatorTableDetailsViewModel simulatorTableDetailsViewModel : simulatorTableDetailsViewModelList) {

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getTan())) {
                simulatorTableDetailsViewModel.setTan(simulatorTableDetailsViewModelAllLenght.getTan());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getManagementFees())) {
                simulatorTableDetailsViewModel.setManagementFees(simulatorTableDetailsViewModelAllLenght.getManagementFees());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getManagementFeesTaeg())) {
                simulatorTableDetailsViewModel.setManagementFeesTaeg(simulatorTableDetailsViewModelAllLenght.getManagementFeesTaeg());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getMaxManagementFees())) {
                simulatorTableDetailsViewModel.setMaxManagementFees(simulatorTableDetailsViewModelAllLenght.getMaxManagementFees());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getMaxAgentFees())) {
                simulatorTableDetailsViewModel.setMaxAgentFees(simulatorTableDetailsViewModelAllLenght.getMaxAgentFees());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getInquiryCost())) {
                simulatorTableDetailsViewModel.setInquiryCost(simulatorTableDetailsViewModelAllLenght.getInquiryCost());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getInquiryCostTaeg())) {
                simulatorTableDetailsViewModel.setInquiryCostTaeg(simulatorTableDetailsViewModelAllLenght.getInquiryCostTaeg());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getMaxInquiryCost())) {
                simulatorTableDetailsViewModel.setMaxInquiryCost(simulatorTableDetailsViewModelAllLenght.getMaxInquiryCost());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getInsuranceCost())) {
                simulatorTableDetailsViewModel.setInsuranceCost(simulatorTableDetailsViewModelAllLenght.getInsuranceCost());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getInsuranceCostTaeg())) {
                simulatorTableDetailsViewModel.setInsuranceCostTaeg(simulatorTableDetailsViewModelAllLenght.getInsuranceCostTaeg());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getMaxInsuranceCost())) {
                simulatorTableDetailsViewModel.setMaxInsuranceCost(simulatorTableDetailsViewModelAllLenght.getMaxInsuranceCost());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getOtherCosts())) {
                simulatorTableDetailsViewModel.setOtherCosts(simulatorTableDetailsViewModelAllLenght.getOtherCosts());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getOtherCostsTaeg())) {
                simulatorTableDetailsViewModel.setOtherCostsTaeg(simulatorTableDetailsViewModelAllLenght.getOtherCostsTaeg());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getMaxOtherCosts())) {
                simulatorTableDetailsViewModel.setMaxOtherCosts(simulatorTableDetailsViewModelAllLenght.getMaxOtherCosts());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getMaxTaeg())) {
                simulatorTableDetailsViewModel.setMaxTaeg(simulatorTableDetailsViewModelAllLenght.getMaxTaeg());
            }

            if (simulatorTableDetailsViewModelAllLenght.getMensilita() != 0) {
                simulatorTableDetailsViewModel.setMensilita(simulatorTableDetailsViewModelAllLenght.getMensilita());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getStampDutyCost())) {
                simulatorTableDetailsViewModel.setStampDutyCost(simulatorTableDetailsViewModelAllLenght.getStampDutyCost());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getStampDutyCostTaeg())) {
                simulatorTableDetailsViewModel.setStampDutyCostTaeg(simulatorTableDetailsViewModelAllLenght.getStampDutyCostTaeg());
            }

            if (!numberUtil.isNullOrZero(simulatorTableDetailsViewModelAllLenght.getMaxStampDutyCost())) {
                simulatorTableDetailsViewModel.setMaxStampDutyCost(simulatorTableDetailsViewModelAllLenght.getMaxStampDutyCost());
            }
        }
        simulatorTableDetailsViewModelAllLenght = new SimulatorTableDetailsViewModel();
    }

    public void saveTable() {
        final SimulatorTableViewModel simulatorTableViewModelSaved;
        try {
            simulatorTableViewModelSaved = simulatorTableService.save(httpSessionUtil.getOperatorId(), httpSessionUtil.getAziendaId(), simulatorTableViewModel,
                    simulatorTableDetailsViewModelList);
            updateAllModels(simulatorTableViewModelSaved, simulatorTableViewModelSaved.getSimulatorTableDetailsViewModelList());
            if (simulatorTableService.isTableUsedSomewhere(simulatorTableViewModelSaved.getId())) {
                FacesUtil.addMessage(propertiesUtil.getMsgSimulatorTableSavedOnlyOperators());
            } else {
                FacesUtil.addMessage(propertiesUtil.getMsgSimulatorSavedTable());
            }
        } catch (final EmptyTanException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgSimulatorErrorTan(), FacesMessage.SEVERITY_ERROR);
        } catch (final EmptyJobTypeListException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgSimulatorErrorEmptyJobTypeList(), FacesMessage.SEVERITY_ERROR);
        }
    }

    private void updateAllModels(final SimulatorTableViewModel simulatorTableViewModel, final List<SimulatorTableDetailsViewModel>
            simulatorTableDetailsViewModelList) {
        setSimulatorTableViewModel(simulatorTableViewModel);
        setSimulatorTableDetailsViewModelList(simulatorTableDetailsViewModelList);
    }

    private void updateEnabledDisabledTable(final boolean isEnabled) {
        simulatorTableViewModel.setSimulatorTableStatus(SimulatorTableViewModel.SimulatorTableStatus.fromValue(isEnabled));
    }

    public String setSymbolInputNumber(final FeeType feeType) {
        if (feeType != null) {
            switch (feeType) {
                case FIXED:
                    return EURO;
                case PERCENTAGE:
                    return PERCENTAGE;
            }
        }
        return "";
    }

    public boolean isDisabledIfNoFeeType(final FeeType feeType) {
        return feeType == null;
    }

    public boolean isRenderedIdZero() {
        return simulatorTableViewModel.getId() != 0;
    }

    public boolean isDisabledButtonIfActive() {
        return simulatorTableViewModel.getSimulatorTableStatus() != null && simulatorTableViewModel.getSimulatorTableStatus().isEnabled();
    }

    public void activateDisableTable(final boolean enabled) {
        final boolean isEnabled = simulatorTableService.enable(simulatorTableViewModel.getId(), enabled);
        updateEnabledDisabledTable(isEnabled);
        if (enabled) {
            FacesUtil.addMessage(propertiesUtil.getMsgSimulatorActivatedSuccess());
        } else {
            FacesUtil.addMessage(propertiesUtil.getMsgSimulatorDisabledSuccess());
        }
    }

    public void cloneTable() throws IOException {
        simulatorTableService.clone(simulatorTableViewModel.getId());
        FacesUtil.addMessage(propertiesUtil.getMsgSimulatorCloneSuccess());
        FacesUtil.redirect(Constants.getSimulatorTableListTablePath(true));
    }

    public void deleteTable() throws IOException {
        try {
            simulatorTableService.delete(simulatorTableViewModel.getId());
            FacesUtil.addMessage(propertiesUtil.getMsgSimulatorDeleteSuccess());
            FacesUtil.redirect(Constants.getSimulatorTableListTablePath(true));
        } catch (final SimulatorTableInUseException e) {
            FacesUtil.addMessage(propertiesUtil.getMsgSimulatorErrorTableInUse(), FacesMessage.SEVERITY_ERROR);
        }
    }


    public String getStatusTableText() {
        if (simulatorTableViewModel.getId() != 0) {
            return simulatorTableViewModel.getSimulatorTableStatus().getLabel();
        }
        return null;
    }

    public String getInfoCreatorTableText() {
        if (simulatorTableViewModel.getId() != 0) {
            return simulatorTableViewModel.getCreatorOperatorViewModel().getUsername();
        }
        return null;
    }

    public boolean isDisabledColumn(final FeeType feeType) {
        return feeType == null;
    }

    public boolean isDisabledButtonDeleteRow() {
        log.info("in isDisabledButtonDeleteRow");
        return simulatorTableDetailsViewModelList.size() == 1 || isTableUsedSomewhere();
    }

    public boolean isRenderedButtonDeleteTable() {
        log.info("in isRenderedButtonDeleteTable");
        return (isRenderedIdZero() && (httpSessionUtil.hasAuthority(Role.RoleName.SIMULATORI_DELETE_SUPER) ||
                (httpSessionUtil.hasAuthority(Role.RoleName.SIMULATORI_DELETE_ALL) && httpSessionUtil.getAziendaId() == simulatorTableViewModel.getCreatorOperatorViewModel().getAzienda().getId()) ||
                (httpSessionUtil.hasAuthority(Role.RoleName.SIMULATORI_DELETE_OWN) && httpSessionUtil.getOperatorId() == simulatorTableViewModel.getCreatorOperatorViewModel().getId())));
    }

    /**
     * Serve per vedere se aggiungere la colonna cessioni nella tabella, per ora non usato
     */
    public boolean isPrestitoTable() {
        return Pratica.TipoPratica.PRESTITO.equals(simulatorTableViewModel.getSimulatorTableType());
    }

    public boolean isTableUsedSomewhere() {
        return simulatorTableService.isTableUsedSomewhere(simulatorTableViewModel.getId());
    }
}