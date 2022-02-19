package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woonders.lacemscommon.db.entity.FornitoriLead;


/**
 * Created by Cristian on 03/11/21.
 */
public interface FornitoriLeadRepository extends JpaRepository<FornitoriLead, Long> {
	FornitoriLead findByFornitoreLeadName(String fornitoreLeadName);
}
