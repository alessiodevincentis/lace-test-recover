package com.woonders.lacemscommon.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.woonders.lacemscommon.db.entity.CalendarioAppuntamento;

public interface CalendarioAppuntamentoRepository
		extends JpaRepository<CalendarioAppuntamento, Long>, QueryDslPredicateExecutor<CalendarioAppuntamento> {

	List<CalendarioAppuntamento> findByNominativo_IdAndPratica_CodicePraticaIsNull(long nominativoId);
}
