package com.woonders.lacemscommon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.service.DeleteOperatorService;

import com.woonders.lacemscommon.db.repository.OperatorRepository;

@Service
@Transactional(readOnly = true)
public class DeleteOperatorServiceImpl implements DeleteOperatorService {

	public static final String NAME = "deleteOperatorServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";
    
	@Autowired
    private OperatorRepository operatorRepository;
	
	@Override
    @Transactional
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		//in the end delete operator
		operatorRepository.deleteByIdIgnoreCaseAndTipoIgnoreCase(id);
	}

}
