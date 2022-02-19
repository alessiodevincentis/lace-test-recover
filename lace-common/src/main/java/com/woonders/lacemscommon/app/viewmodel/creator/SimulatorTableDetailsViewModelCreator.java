package com.woonders.lacemscommon.app.viewmodel.creator;

import com.woonders.lacemscommon.app.viewmodel.SimulatorTableDetailsViewModel;
import com.woonders.lacemscommon.db.entity.SimulatorTableDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ema98 on 8/10/2017.
 */
@Component
public class SimulatorTableDetailsViewModelCreator extends AbstractBaseViewModelCreator<SimulatorTableDetails, SimulatorTableDetailsViewModel> {

    @Autowired
    private SimulatorTableViewModelCreator simulatorTableViewModelCreator;

    @Override
    public SimulatorTableDetails createModel(final SimulatorTableDetailsViewModel viewModel) {
        if (viewModel != null) {
            return SimulatorTableDetails.builder().id(viewModel.getId())
                    .length(viewModel.getLength())
                    .mensilita(viewModel.getMensilita())
                    .tan(viewModel.getTan())
                    .maxTaeg(viewModel.getMaxTaeg())
                    .maxAgentFees(viewModel.getMaxAgentFees())
                    .inquiryCost(viewModel.getInquiryCost())
                    .inquiryCostTaeg(viewModel.getInquiryCostTaeg())
                    .maxInquiryCost(viewModel.getMaxInquiryCost())
                    .managementFees(viewModel.getManagementFees())
                    .managementFeesTaeg(viewModel.getManagementFeesTaeg())
                    .maxManagementFees(viewModel.getMaxManagementFees())
                    .stampDutyCost(viewModel.getStampDutyCost())
                    .stampDutyCostTaeg(viewModel.getStampDutyCostTaeg())
                    .maxStampDutyCost(viewModel.getMaxStampDutyCost())
                    .otherCosts(viewModel.getOtherCosts())
                    .otherCostsTaeg(viewModel.getOtherCostsTaeg())
                    .maxOtherCosts(viewModel.getMaxOtherCosts())
                    .insuranceCost(viewModel.getInsuranceCost())
                    .insuranceCostTaeg(viewModel.getInsuranceCostTaeg())
                    .maxInsuranceCost(viewModel.getMaxInsuranceCost())
                    .simulatorTable(simulatorTableViewModelCreator.createModel(viewModel.getSimulatorTableViewModel()))
                    .build();
        }
        return null;
    }

    @Override
    public SimulatorTableDetailsViewModel createViewModel(final SimulatorTableDetails model) {
        if (model != null) {
            return SimulatorTableDetailsViewModel.builder().id(model.getId())
                    .length(model.getLength())
                    .mensilita(model.getMensilita())
                    .tan(model.getTan())
                    .maxTaeg(model.getMaxTaeg())
                    .maxAgentFees(model.getMaxAgentFees())
                    .inquiryCost(model.getInquiryCost())
                    .inquiryCostTaeg(model.getInquiryCostTaeg())
                    .maxInquiryCost(model.getMaxInquiryCost())
                    .managementFees(model.getManagementFees())
                    .managementFeesTaeg(model.getManagementFeesTaeg())
                    .maxManagementFees(model.getMaxManagementFees())
                    .stampDutyCost(model.getStampDutyCost())
                    .stampDutyCostTaeg(model.getStampDutyCostTaeg())
                    .maxStampDutyCost(model.getMaxStampDutyCost())
                    .otherCosts(model.getOtherCosts())
                    .otherCostsTaeg(model.getOtherCostsTaeg())
                    .maxOtherCosts(model.getMaxOtherCosts())
                    .insuranceCost(model.getInsuranceCost())
                    .insuranceCostTaeg(model.getInsuranceCostTaeg())
                    .maxInsuranceCost(model.getMaxInsuranceCost())
                    .simulatorTableViewModel(simulatorTableViewModelCreator.createViewModel(model.getSimulatorTable()))
                    .build();
        }
        return null;
    }
}
