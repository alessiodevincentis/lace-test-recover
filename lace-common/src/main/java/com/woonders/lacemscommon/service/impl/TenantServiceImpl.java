package com.woonders.lacemscommon.service.impl;

import com.google.common.collect.Lists;
import com.woonders.lacemscommon.db.tenantrepository.QTenant;
import com.woonders.lacemscommon.db.tenantrepository.Tenant;
import com.woonders.lacemscommon.db.tenantrepository.TenantRepository;
import com.woonders.lacemscommon.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.woonders.lacemscommon.config.tenant.TenantDbConfig.TENANT_DB_TRANSACTION_MANAGER;

/**
 * Created by Emanuele on 12/06/2017.
 */
@Service
// fondamentale, deve usare il transaction manager corretto altrimenti esplode tutto!
@Transactional(readOnly = true, transactionManager = TENANT_DB_TRANSACTION_MANAGER)
public class TenantServiceImpl implements TenantService {

    public static final String NAME = "tenantService";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public List<Tenant> getActiveTenantList() {
        QTenant qTenant = QTenant.tenant;
        return Lists.newArrayList(tenantRepository.findAll(qTenant.enabled.eq(true)));
    }
}
