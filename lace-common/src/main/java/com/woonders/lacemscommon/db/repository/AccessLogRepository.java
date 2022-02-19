package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long>, QueryDslPredicateExecutor<AccessLog> {
}