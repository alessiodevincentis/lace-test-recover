package com.woonders.lacemscommon.db.repository;


import com.woonders.lacemscommon.db.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long>, QueryDslPredicateExecutor<ResetPassword> {
	ResetPassword findByOperatorId(long operatorId);
}
