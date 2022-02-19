package com.woonders.lacemscommon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.app.viewmodel.ClientePrivacyTemplateViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ClientePrivacyTemplateViewModelCreator;
import com.woonders.lacemscommon.db.entity.ClientePrivacyTemplate;
import com.woonders.lacemscommon.db.repository.ClientePrivacyTemplateRepository;
import com.woonders.lacemscommon.service.ClientePrivacyTemplateService;

/**
 * Created by Cristian on 16/11/21.
 */
@Service
@Transactional(readOnly = true)
public class ClientePrivacyTemplateServiceImpl implements ClientePrivacyTemplateService {

	public static final String NAME = "clientePrivacyTemplateServiceImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Autowired
	private ClientePrivacyTemplateRepository clientePrivacyTemplateRepository;
	@Autowired
	private ClientePrivacyTemplateViewModelCreator clientePrivacyTemplateViewModelCreator;

    
	@Override
	@Transactional
	public ClientePrivacyTemplateViewModel save(ClientePrivacyTemplateViewModel privacyTemplateViewModel) {
		return clientePrivacyTemplateViewModelCreator
				.createViewModel(clientePrivacyTemplateRepository.save(clientePrivacyTemplateViewModelCreator.createModel(privacyTemplateViewModel)));

	}
	@Override
	@Transactional
	public ClientePrivacyTemplate save(ClientePrivacyTemplate clientePrivacyTemplate) {
		return clientePrivacyTemplateRepository.save(clientePrivacyTemplate);
	}
	

}
