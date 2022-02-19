package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.app.viewmodel.InfoAccountViewModel;

/**
 * Manages the agency account information
 * [info_account] table
 */
public interface InfoAccountService {

    /**
     * Returns agency account information
     */
    InfoAccountViewModel getOne();

    /**
     * Counts how many operator accounts are available
     */
    long getOperatorsNumber();
}
