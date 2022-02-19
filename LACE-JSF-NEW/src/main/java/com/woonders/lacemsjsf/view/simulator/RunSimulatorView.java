package com.woonders.lacemsjsf.view.simulator;

import com.woonders.lacemscommon.app.model.SimulatorResult;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.laceenum.SimulatorSource;
import com.woonders.lacemscommon.service.SimulatorTableService;
import com.woonders.lacemscommon.service.impl.SimulatorTableServiceImpl;
import com.woonders.lacemscommon.util.NumberUtil;
import com.woonders.lacemscommon.util.SimulatorUtil;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.FacesUtil;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "runSimulatorView")
@ViewScoped
@Getter
@Setter
public class RunSimulatorView implements Serializable {

    //private RunSimulatorMode chooseRunSimulatorMode;
    private Pratica.TipoPratica simulatorTableType;
    private BigDecimal amountChoosen;
    private BigDecimal paymentChoosen;
    private List<SimulatorResult> simulatorResultList;
    @ManagedProperty(SimulatorTableServiceImpl.JSF_NAME)
    private SimulatorTableService simulatorTableService;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;
    private SimulatorTableViewModel simulatorTableViewModelChoosen;
    private BigDecimal agentFees;
    @ManagedProperty(SimulatorUtil.JSF_NAME)
    private SimulatorUtil simulatorUtil;
    @ManagedProperty(NumberUtil.JSF_NAME)
    private NumberUtil numberUtil;
    private List<SimulatorTableViewModel> simulatorTableViewModelList;
    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;
    private List<Impiego> selectedJobTypeList;
    private BigDecimal uprightRemaining;
    private SimulatorResult rowTableResultChoosen;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;

    public RunSimulatorView() {
        simulatorResultList = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        simulatorTableType = Pratica.TipoPratica.CESSIONE_S;
        refreshTables();
    }

    public void refreshTables() {
        simulatorTableViewModelList = simulatorTableService.findAssigned(httpSessionUtil.getOperatorId(), simulatorTableType, selectedJobTypeList);
    }

    public boolean isAmountModeChoosen() {
        return Pratica.TipoPratica.PRESTITO.equals(simulatorTableType);
    }

    public boolean isPaymentModeChoosen() {
        return Pratica.TipoPratica.CESSIONE_S.equals(simulatorTableType) || Pratica.TipoPratica.CESSIONE_P.equals(simulatorTableType)
                || Pratica.TipoPratica.DELEGA.equals(simulatorTableType);
    }

    public void runSimulatorByAmount() {
        if (isAmountModeChoosen() && !numberUtil.isNullOrZero(amountChoosen)) {
            setSimulatorResultList(simulatorUtil.calcByAmount(amountChoosen, simulatorTableViewModelChoosen.getId(), agentFees));
        } else if (isPaymentModeChoosen() && !numberUtil.isNullOrZero(paymentChoosen)) {
            setSimulatorResultList(simulatorUtil.calcByPayment(paymentChoosen, agentFees, simulatorTableViewModelChoosen.getId(), uprightRemaining));
        } else {
            FacesUtil.addMessage(propertiesUtil.getMsgSimulatorErrorRunMissingFields(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public boolean isDisabledButtonRunSimulator() {
        return simulatorTableType == null && simulatorTableViewModelChoosen == null;
    }

    public void selectAllJobType() {
        //https://stackoverflow.com/questions/17359226/com-sun-faces-renderkit-html-basic-menurenderer-createcollection-unable-to-crea
        selectedJobTypeList = new ArrayList<>();
        Collections.addAll(selectedJobTypeList, Impiego.values());
    }

    public boolean isNoTablesAvailable() {
        return simulatorTableViewModelList == null || simulatorTableViewModelList.isEmpty();
    }


    public String isExcedeedTaegOrExcedeedAgentfees(final SimulatorResult simulatorResult) {
        if (simulatorResult.isExceededAgentFees() && simulatorResult.isExceededTaeg()) {
            return "coloredRed";
        } else if (simulatorResult.isExceededTaeg()) {
            return "colored";
        } else if (simulatorResult.isExceededAgentFees()) {
            return "coloredOrange";
        }
        return null;
    }

    public boolean isDisabledTableSelection() {
        return passaggioParametriUtils.getParametri().containsKey(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_SELECTION)
                && ((boolean) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_SELECTION));
    }

    public void setParameterSimulatorTableSelectionDisabledAndSource(final boolean value, final SimulatorSource simulatorSource) {
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_SELECTION, value);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_SOURCE, simulatorSource);
    }

    private void setParameterSimulatorResultRowChoosen() {
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_RESULT_ROW_CHOOSEN, rowTableResultChoosen);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_PAYMENT_CHOOSEN, paymentChoosen);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_AMOUNT_CHOOSEN, amountChoosen);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_AGENT_FEES_CHOOSEN, agentFees);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_TYPE_CHOOSEN, simulatorTableType);
        passaggioParametriUtils.getParametri().put(PassaggioParametriUtils.Parametro.SIMULATOR_TABLE_VIEW_MODEL, simulatorTableViewModelChoosen);
        final SimulatorSource simulatorSource = (SimulatorSource) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.SIMULATOR_SOURCE);
        if (simulatorSource != null) {
            switch (simulatorSource) {
                case NOMINATIVO:
                    FacesUtil.closeDialog(FacesUtil.DialogType.SIMULATOR_DIALOG_NOMINATIVO);
                    break;
                case PREVENTIVO_CLIENTE:
                    FacesUtil.closeDialog(FacesUtil.DialogType.SIMULATOR_DIALOG_PREVENTIVO_CLIENTE);
                    break;
            }
        }
    }

    //used when close dialog on page forms and formsNominativo
    public void resetResultTable() {
        simulatorResultList = new ArrayList<>();
        rowTableResultChoosen = new SimulatorResult();
        amountChoosen = null;
        paymentChoosen = null;
        uprightRemaining = null;
        agentFees = null;
    }


    public void onRowSelect() {
        setParameterSimulatorResultRowChoosen();
    }
}
