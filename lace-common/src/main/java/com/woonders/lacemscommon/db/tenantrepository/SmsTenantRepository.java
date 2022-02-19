package com.woonders.lacemscommon.db.tenantrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Giulio on 31/07/2019.
 */
@Repository
public interface SmsTenantRepository extends JpaRepository<SmsTenant, String>, QueryDslPredicateExecutor<SmsTenant> {
    SmsTenant findByIdSms(String idSms);
    List<SmsTenant> findAll();
}
