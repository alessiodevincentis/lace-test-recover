package com.woonders.lacemscommon.app.viewmodel.creator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.woonders.lacemscommon.app.viewmodel.SimulatorTableViewModel;
import com.woonders.lacemscommon.db.entity.SimulatorTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ema98 on 8/10/2017.
 */
@Component
public class SimulatorTableViewModelCreator extends AbstractBaseViewModelCreator<SimulatorTable, SimulatorTableViewModel> {

    @Autowired
    private OperatorViewModelCreator operatorViewModelCreator;
    @Autowired
    private AziendaViewModelCreator aziendaViewModelCreator;

    @Override
    public SimulatorTable createModel(final SimulatorTableViewModel viewModel) {
        if (viewModel != null) {
            final SimulatorTable simulatorTable = SimulatorTable.builder().id(viewModel.getId())
                    .name(viewModel.getName())
                    .insuranceCostFeeType(viewModel.getInsuranceCostFeeType())
                    .over(viewModel.getOver())
                    .simulatorRoundingMode(viewModel.getSimulatorRoundingMode())
                    .inquiryCostFeeType(viewModel.getInquiryCostFeeType())
                    .managementFeesFeeType(viewModel.getManagementFeesFeeType())
                    .stampDutyFeeType(viewModel.getStampDutyFeeType())
                    .otherCostsFeeType(viewModel.getOtherCostsFeeType())
                    .creatorOperator(operatorViewModelCreator.createModel(viewModel.getCreatorOperatorViewModel()))
                    .azienda(aziendaViewModelCreator.createModel(viewModel.getAziendaViewModel()))
                    .simulatorTableType(viewModel.getSimulatorTableType())
                    .jobTypeSet(Sets.newHashSet(viewModel.getJobTypeList()))
                    .grantedOperatorSet(operatorViewModelCreator.fromList(viewModel.getGrantedOperatorViewModelList()))
                    .build();
            if (viewModel.getSimulatorTableStatus() != null) {
                simulatorTable.setEnabled(viewModel.getSimulatorTableStatus().isEnabled());
            }
            return simulatorTable;
        }
        return null;
    }

    @Override
    public SimulatorTableViewModel createViewModel(final SimulatorTable model) {
        if (model != null) {
            return SimulatorTableViewModel.builder().id(model.getId())
                    .name(model.getName())
                    .insuranceCostFeeType(model.getInsuranceCostFeeType())
                    .over(model.getOver())
                    .simulatorRoundingMode(model.getSimulatorRoundingMode())
                    .inquiryCostFeeType(model.getInquiryCostFeeType())
                    .managementFeesFeeType(model.getManagementFeesFeeType())
                    .stampDutyFeeType(model.getStampDutyFeeType())
                    .otherCostsFeeType(model.getOtherCostsFeeType())
                    .simulatorTableStatus(SimulatorTableViewModel.SimulatorTableStatus.fromValue(model.isEnabled()))
                    .creatorOperatorViewModel(operatorViewModelCreator.createViewModel(model.getCreatorOperator()))
                    .aziendaViewModel(aziendaViewModelCreator.createViewModel(model.getAzienda()))
                    .simulatorTableType(model.getSimulatorTableType())
                    .jobTypeList(Lists.newArrayList(model.getJobTypeSet()))
                    .grantedOperatorViewModelList(operatorViewModelCreator.fromSet(model.getGrantedOperatorSet()))
                    .build();
        }
        return null;
    }
}
