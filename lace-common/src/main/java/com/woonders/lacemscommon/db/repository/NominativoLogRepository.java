package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.NominativoLog;

/**
 * Created by Emanuele on 08/02/2017.
 */
@Repository
public interface NominativoLogRepository
		extends JpaRepository<NominativoLog, Long>, QueryDslPredicateExecutor<NominativoLog> {

	NominativoLog findTopByNominativo_IdOrderByExecutionDateTimeDesc(long nominativoId);

	NominativoLog findTopByPratica_CodicePraticaOrderByExecutionDateTimeDesc(long praticaId);
}
