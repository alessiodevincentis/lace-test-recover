package com.woonders.lacemscommon.service.impl;


import java.util.List;

import com.woonders.lacemscommon.db.tenantrepository.SmsTenant;
import com.woonders.lacemscommon.db.tenantrepository.SmsTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.woonders.lacemscommon.config.tenant.TenantDbConfig.TENANT_DB_TRANSACTION_MANAGER;

/**
 * Created by Giulio on 31/07/2019.
 */
@Service
// fondamentale, deve usare il transaction manager corretto altrimenti esplode tutto!
@Transactional(readOnly = true, transactionManager = TENANT_DB_TRANSACTION_MANAGER)
public class SmsTenantServiceImpl {

    public static final String NAME = "smsTenantService";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private SmsTenantRepository smsTenantRepository;


    public List<SmsTenant> getSmsTenantList() {
        return smsTenantRepository.findAll();
    }
}
