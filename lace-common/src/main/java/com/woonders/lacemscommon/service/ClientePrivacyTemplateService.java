package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.ClientePrivacyTemplateViewModel;
import com.woonders.lacemscommon.db.entity.ClientePrivacyTemplate;

/**
 * Created by Cristian on 16/11/21.
 * Manages the FornitoriLeads info
 * [FornitoriLeads] table
 */
public interface ClientePrivacyTemplateService {
	

	ClientePrivacyTemplateViewModel save (ClientePrivacyTemplateViewModel privacyTemplateViewModel);
	
	ClientePrivacyTemplate save (ClientePrivacyTemplate privacyTemplate);
}
