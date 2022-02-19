package com.woonders.lacemscommon.service;

import com.woonders.lacemscommon.db.tenantrepository.Tenant;

import java.util.List;

/**
 * Created by Emanuele on 12/06/2017.
 */

/**
 * Manages list of tenants
 */
public interface TenantService {
    /**
     * return list of tenants still active
     */
    List<Tenant> getActiveTenantList();
}
