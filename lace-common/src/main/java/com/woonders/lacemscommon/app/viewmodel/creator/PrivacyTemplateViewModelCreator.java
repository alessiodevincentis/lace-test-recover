package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.PrivacyTemplateViewModel;
import com.woonders.lacemscommon.db.entity.PrivacyTemplate;

@Component
public class PrivacyTemplateViewModelCreator extends AbstractBaseViewModelCreator<PrivacyTemplate, PrivacyTemplateViewModel> {

	@Override
	public PrivacyTemplate createModel(PrivacyTemplateViewModel viewModel) {
		if (viewModel != null) {
            final PrivacyTemplate privacyTemplate = new PrivacyTemplate();
            privacyTemplate.setId(viewModel.getId());
            privacyTemplate.setProvenienza(viewModel.getProvenienza());
            privacyTemplate.setFornitoreLead(viewModel.getFornitoreLead());
            privacyTemplate.setPrivacyText(viewModel.getPrivacyText());
            privacyTemplate.setDateCreate(viewModel.getDateCreate());
            
            return privacyTemplate;
        }
        return null;
	}

	@Override
	public PrivacyTemplateViewModel createViewModel(PrivacyTemplate model) {
		if (model != null) {
            final PrivacyTemplateViewModel privacyTemplateViewModel = new PrivacyTemplateViewModel();
            privacyTemplateViewModel.setId(model.getId());
            privacyTemplateViewModel.setProvenienza(model.getProvenienza());
            privacyTemplateViewModel.setFornitoreLead(model.getFornitoreLead());
            privacyTemplateViewModel.setPrivacyText(model.getPrivacyText());
            privacyTemplateViewModel.setDateCreate(model.getDateCreate());
            
            return privacyTemplateViewModel;
        }
        return null;
	}

}
