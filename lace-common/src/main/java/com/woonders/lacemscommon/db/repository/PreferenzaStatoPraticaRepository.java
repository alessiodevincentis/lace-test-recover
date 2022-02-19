package com.woonders.lacemscommon.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woonders.lacemscommon.db.entity.PreferenzaStatoPratica;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;

public interface PreferenzaStatoPraticaRepository extends JpaRepository<PreferenzaStatoPratica, Long> {

	PreferenzaStatoPratica findByNomeStatoPraticaAndAzienda_Id(StatoPratica stato, long aziendaId);

	List<PreferenzaStatoPratica> findByAzienda_Id(long aziendaId);
}
