package com.woonders.lacemscommon.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.woonders.lacemscommon.app.model.ViewModelPage;
import com.woonders.lacemscommon.app.viewmodel.PrivacyTemplateViewModel;
import com.woonders.lacemscommon.db.QueryDSLHelper;
import com.woonders.lacemscommon.db.entity.PrivacyTemplate;

/**
 * Created by Cristian on 16/11/21.
 * Manages the FornitoriLeads info
 * [FornitoriLeads] table
 */
public interface PrivacyTemplateService {
	
	/**
     * Returns a paged list of the leads who should be called in a given [date]
     */
    ViewModelPage<PrivacyTemplateViewModel> getPrivacyToDataTable(final Date date, int firstElementIndex, int pageSize,
                                                              String sortField, QueryDSLHelper.SortOrder sortOrder, Map<QueryDSLHelper.TableField, Object> filters);


	PrivacyTemplateViewModel save (PrivacyTemplateViewModel privacyTemplateViewModel);
	
	PrivacyTemplate save (PrivacyTemplate privacyTemplate);
	
	/**
	 * Returns a list of agency (if the agency has multiple sub FornitoriLeadsViewModel, there will be more, otherwise only one item)
	 */
	List<PrivacyTemplateViewModel> findAll();
	
	List<PrivacyTemplate> findEntityAll();
	/**
     * return operator by id
     */
	PrivacyTemplateViewModel getOne(long id);
	
	PrivacyTemplate findOne(long id);
	
	/**
     * return operator by provenienza
     */
	PrivacyTemplate findOneByProvenienza(String provenienza);
	
	/**
     * return operator by fornitoreLeadName
     */
	PrivacyTemplate findOneByFornitoreLead(String fornitoreLead);
	
	
}
