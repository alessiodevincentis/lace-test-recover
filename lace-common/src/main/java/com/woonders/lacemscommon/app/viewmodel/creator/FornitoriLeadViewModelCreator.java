package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.FornitoriLeadViewModel;
import com.woonders.lacemscommon.db.entity.FornitoriLead;

@Component
public class FornitoriLeadViewModelCreator extends AbstractBaseViewModelCreator<FornitoriLead, FornitoriLeadViewModel> {

	@Override
	public FornitoriLead createModel(FornitoriLeadViewModel viewModel) {
		if (viewModel != null) {
            final FornitoriLead fornitoreLeads = new FornitoriLead();
            fornitoreLeads.setFornitoreLeadName(viewModel.getFornitoreLeadName());
            fornitoreLeads.setProvenienza(viewModel.getProvenienza());
            fornitoreLeads.setProvenienzaDesc(viewModel.getProvenienzaDesc());
            fornitoreLeads.setDataRetention(viewModel.getDataRetention());
            
            return fornitoreLeads;
        }
        return null;
	}

	@Override
	public FornitoriLeadViewModel createViewModel(FornitoriLead model) {
		if (model != null) {
            final FornitoriLeadViewModel fornitoreLeadsViewModel = new FornitoriLeadViewModel();
            fornitoreLeadsViewModel.setFornitoreLeadName(model.getFornitoreLeadName());
            fornitoreLeadsViewModel.setProvenienza(model.getProvenienza());
            fornitoreLeadsViewModel.setProvenienzaDesc(model.getProvenienzaDesc());
            fornitoreLeadsViewModel.setDataRetention(model.getDataRetention());
            
            return fornitoreLeadsViewModel;
        }
        return null;
	}

}
