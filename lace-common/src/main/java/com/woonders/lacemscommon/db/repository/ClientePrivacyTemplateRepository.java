package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.woonders.lacemscommon.db.entity.ClientePrivacyTemplate;
import com.woonders.lacemscommon.db.entity.PrivacyTemplate;


/**
 * Created by Cristian on 03/11/21.
 */
public interface ClientePrivacyTemplateRepository extends JpaRepository<ClientePrivacyTemplate, Long>, QueryDslPredicateExecutor<ClientePrivacyTemplate> {
	
}
