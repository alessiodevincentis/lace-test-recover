package com.woonders.lacemscommon.db.repository;


import com.woonders.lacemscommon.db.entity.TimeParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeParametersRepository extends JpaRepository<TimeParameters, Long>, QueryDslPredicateExecutor<TimeParameters> {
	TimeParameters findByCodeIdentify(String codeidentify);
}
