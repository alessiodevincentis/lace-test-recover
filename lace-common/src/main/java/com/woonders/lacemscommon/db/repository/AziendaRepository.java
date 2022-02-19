package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woonders.lacemscommon.db.entity.Azienda;

/**
 * Created by emanuele on 03/01/17.
 */
public interface AziendaRepository extends JpaRepository<Azienda, Long> {
}
