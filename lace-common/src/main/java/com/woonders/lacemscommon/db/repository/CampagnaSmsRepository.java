package com.woonders.lacemscommon.db.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.CampagnaSms;
import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;

/**
 * Created by Emanuele on 27/03/2017.
 */
@Repository
public interface CampagnaSmsRepository extends CrudRepository<CampagnaSms, Long>, QueryDslPredicateExecutor<CampagnaSms> {

	List<CampagnaSms> findByCampagnaIdAndEsitoIn(long campagnaId, Collection<EsitoSmsNotificaLead> esitoSmsNotificaLeadCollection);

}
