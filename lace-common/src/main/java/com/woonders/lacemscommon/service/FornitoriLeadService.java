package com.woonders.lacemscommon.service;

import java.util.List;

import com.woonders.lacemscommon.app.viewmodel.FornitoriLeadViewModel;
import com.woonders.lacemscommon.db.entity.FornitoriLead;
import com.woonders.lacemscommon.exception.UnableToFindException;

/**
 * Created by Cristian on 03/11/21.
 * Manages the FornitoriLeads info
 * [FornitoriLeads] table
 */
public interface FornitoriLeadService {

	FornitoriLeadViewModel save (FornitoriLeadViewModel fornitoriLeadsViewModel);
	
	FornitoriLead save (FornitoriLead fornitoriLeads);
	
	/**
	 * Returns a list of agency (if the agency has multiple sub FornitoriLeadsViewModel, there will be more, otherwise only one item)
	 */
	List<FornitoriLeadViewModel> findAll();
	
	List<FornitoriLead> findEntityAll();
	/**
     * return operator by id
     */
	FornitoriLeadViewModel getOne(long id);
	
	FornitoriLead findOne(long id);
	
	/**
     * return operator by fornitoreLeadName
     */
	FornitoriLead findOneByFornitoreLeadName(String fornitoreLeadName);
}
