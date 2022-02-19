package com.woonders.lacemscommon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.app.viewmodel.TimeParametersViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.TimeParametersViewModelCreator;
import com.woonders.lacemscommon.db.repository.TimeParametersRepository;
import com.woonders.lacemscommon.exception.ItemNotFoundException;
import com.woonders.lacemscommon.service.TimeParametersService;

@Service
@Transactional(readOnly = true)
public class TimeParametersServiceImpl implements TimeParametersService {

	public static final String NAME = "timeParametersServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private TimeParametersRepository timeParametersRepository;
    @Autowired
	private TimeParametersViewModelCreator timeParametersViewModelCreator;
    
	@Override
	public List<TimeParametersViewModel> findAll() {
		// TODO Auto-generated method stub
		return timeParametersViewModelCreator.createViewModelList(timeParametersRepository.findAll());
	}

	@Override
    @Transactional
	public void save(TimeParametersViewModel timeParametersViewModel) {
		// TODO Auto-generated method stub
		timeParametersRepository.save(timeParametersViewModelCreator.createModel(timeParametersViewModel));
	}

	@Override
	public TimeParametersViewModel findOne(Long id) throws ItemNotFoundException {
		try {
            return timeParametersViewModelCreator.createViewModel(timeParametersRepository.findOne(id));
        } catch (final DataAccessException e) {
            throw new ItemNotFoundException(e.getRootCause().getMessage());
        }
	}

	@Override
	public TimeParametersViewModel findByCodeIdentify(String codeIdentify) throws ItemNotFoundException {
		try {
            return timeParametersViewModelCreator.createViewModel(timeParametersRepository.findByCodeIdentify(codeIdentify));
        } catch (final DataAccessException e) {
            throw new ItemNotFoundException(e.getRootCause().getMessage());
        }
	}

}
