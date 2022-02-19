package com.woonders.lacemscommon.service;

import java.util.Date;

import com.woonders.lacemscommon.app.viewmodel.ResetPasswordViewModel;
import com.woonders.lacemscommon.db.entity.Operator;


/**
 * Manages access to Lace saving/retrieving reset password events done by operators
 * [reset_password] table
 */
public interface ResetPasswordService {
	
	ResetPasswordViewModel getOne(long idOperator);

    /**
     * Store a login event done by operatorName
     */
    void saveResetPassword(Operator operator, Date modification_data, int duration_validity, boolean first_access);

    
}
