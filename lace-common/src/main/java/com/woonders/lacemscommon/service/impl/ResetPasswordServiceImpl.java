package com.woonders.lacemscommon.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.app.viewmodel.ResetPasswordViewModel;
import com.woonders.lacemscommon.app.viewmodel.creator.ResetPasswordViewModelCreator;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.ResetPassword;
import com.woonders.lacemscommon.db.repository.ResetPasswordRepository;
import com.woonders.lacemscommon.service.ResetPasswordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ResetPasswordServiceImpl implements ResetPasswordService {

	public static final String NAME = "resetPasswordServiceImpl";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;
    @Autowired
	private ResetPasswordViewModelCreator resetPasswordViewModelCreator;

    
	@Transactional
    @Override
    public void saveResetPassword(Operator operator, Date modification_data, int duration_validity, boolean first_access) {
		duration_validity = (duration_validity == 0) ? 30 : duration_validity;
		/*
		 * Recupero se esite gia il record reset_password del operator
		 */
		ResetPassword resetPassword = resetPasswordRepository.findByOperatorId(operator.getId());
		if (resetPassword == null)
			resetPasswordRepository.save(new ResetPassword(null, operator, modification_data, duration_validity, first_access));
		else {
			resetPassword.setModification_data(modification_data);
			resetPassword.setDuration_validity(duration_validity);
			resetPassword.setFirst_access(first_access);
			resetPasswordRepository.save(resetPassword);
		}
			
    }

	@Override
	public ResetPasswordViewModel getOne(long idOpearor) {
		// TODO Auto-generated method stub
		return resetPasswordViewModelCreator.createViewModel(resetPasswordRepository.findByOperatorId(idOpearor));
		
	}
}
