package com.woonders.lacemscommon.db.tenantrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Emanuele on 24/03/2017.
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>, QueryDslPredicateExecutor<Tenant> {


}
