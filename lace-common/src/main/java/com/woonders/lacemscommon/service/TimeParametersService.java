package com.woonders.lacemscommon.service;

import java.util.List;


import com.woonders.lacemscommon.app.viewmodel.TimeParametersViewModel;
import com.woonders.lacemscommon.exception.ItemNotFoundException;

public interface TimeParametersService {

	List<TimeParametersViewModel> findAll();

    void save(TimeParametersViewModel timeParametersViewModel);
    
    TimeParametersViewModel findOne(final Long id) throws ItemNotFoundException;
    
    TimeParametersViewModel findByCodeIdentify(final String codeIdentify) throws ItemNotFoundException;
}
