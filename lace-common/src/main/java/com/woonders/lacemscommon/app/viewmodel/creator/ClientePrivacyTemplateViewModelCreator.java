package com.woonders.lacemscommon.app.viewmodel.creator;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.ClientePrivacyTemplateViewModel;
import com.woonders.lacemscommon.db.entity.ClientePrivacyTemplate;

@Component
public class ClientePrivacyTemplateViewModelCreator extends AbstractBaseViewModelCreator<ClientePrivacyTemplate, ClientePrivacyTemplateViewModel> {

	@Override
	public ClientePrivacyTemplate createModel(ClientePrivacyTemplateViewModel viewModel) {
		if (viewModel != null) {
            final ClientePrivacyTemplate clientePrivacyTemplate = new ClientePrivacyTemplate();
            clientePrivacyTemplate.setClienteId(viewModel.getClienteId());
            clientePrivacyTemplate.setPrivacyTemplatesId(viewModel.getPrivacyTemplatesId());
            clientePrivacyTemplate.setDateCreate(viewModel.getDateCreate());
            
            return clientePrivacyTemplate;
        }
        return null;
	}

	@Override
	public ClientePrivacyTemplateViewModel createViewModel(ClientePrivacyTemplate model) {
		if (model != null) {
            final ClientePrivacyTemplateViewModel clientePrivacyTemplateViewModel = new ClientePrivacyTemplateViewModel();
            clientePrivacyTemplateViewModel.setClienteId(model.getClienteId());
            clientePrivacyTemplateViewModel.setPrivacyTemplatesId(model.getPrivacyTemplatesId());
            clientePrivacyTemplateViewModel.setDateCreate(model.getDateCreate());
            
            return clientePrivacyTemplateViewModel;
        }
        return null;
	}

}
