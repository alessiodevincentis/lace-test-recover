package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.woonders.lacemscommon.db.entity.RicaricaComunicazione;

/**
 * Created by Emanuele on 22/01/2017.
 */
public interface RicaricaComunicazioneRepository
		extends JpaRepository<RicaricaComunicazione, Long>, QueryDslPredicateExecutor<RicaricaComunicazione> {
}
