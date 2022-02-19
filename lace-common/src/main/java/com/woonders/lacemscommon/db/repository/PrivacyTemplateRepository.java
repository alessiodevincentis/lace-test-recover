package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.woonders.lacemscommon.db.entity.PrivacyTemplate;


/**
 * Created by Cristian on 03/11/21.
 */
public interface PrivacyTemplateRepository extends JpaRepository<PrivacyTemplate, Long>, QueryDslPredicateExecutor<PrivacyTemplate> {
	PrivacyTemplate findByProvenienza(String provenienza);
	
	PrivacyTemplate findByFornitoreLead(String fornitoreLeadName);
}
