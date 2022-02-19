package com.woonders.lacemscommon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.app.viewmodel.FornitoriLeadViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.FornitoriLeadViewModelCreator;
import com.woonders.lacemscommon.db.entity.FornitoriLead;
import com.woonders.lacemscommon.db.repository.FornitoriLeadRepository;
import com.woonders.lacemscommon.exception.UnableToFindException;
import com.woonders.lacemscommon.service.FornitoriLeadService;

/**
 * Created by Cristian on 03/11/21.
 */
@Service
@Transactional(readOnly = true)
public class FornitoriLeadServiceImpl implements FornitoriLeadService {

	public static final String NAME = "fornitoriLeadServiceImpl";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Autowired
	private FornitoriLeadRepository fornitoriLeadsRepository;
	@Autowired
	private FornitoriLeadViewModelCreator fornitoriLeadsViewModelCreator;
	
	@Override
	@Transactional
	public FornitoriLeadViewModel save(FornitoriLeadViewModel fornitoriLeadsViewModel) {
		return fornitoriLeadsViewModelCreator
				.createViewModel(fornitoriLeadsRepository.save(fornitoriLeadsViewModelCreator.createModel(fornitoriLeadsViewModel)));
	}

	@Override
	public List<FornitoriLeadViewModel> findAll() {
		// TODO Auto-generated method stub
		return fornitoriLeadsViewModelCreator.createViewModelList(fornitoriLeadsRepository.findAll());
	}
	

	@Override
	public FornitoriLeadViewModel getOne(final long id) {
		// TODO Auto-generated method stub
		
		return fornitoriLeadsViewModelCreator.createViewModel(fornitoriLeadsRepository.findOne(id));
	}

	@Override
	public List<FornitoriLead> findEntityAll() {
		// TODO Auto-generated method stub
		return fornitoriLeadsRepository.findAll();
	}

	@Override
	@Transactional
	public FornitoriLead save(FornitoriLead fornitoriLeads) {
		return fornitoriLeadsRepository.save(fornitoriLeads);
	}

	@Override
	public FornitoriLead findOne(long id) {
		return fornitoriLeadsRepository.findOne(id);
	}

	@Override
	public FornitoriLead findOneByFornitoreLeadName(String fornitoreLeadName) {
		// TODO Auto-generated method stub
		return fornitoriLeadsRepository.findByFornitoreLeadName(fornitoreLeadName);
	}

}
