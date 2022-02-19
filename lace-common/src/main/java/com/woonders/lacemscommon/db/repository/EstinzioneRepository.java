package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.Estinzione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by emanuele on 03/01/17.
 */
public interface EstinzioneRepository extends JpaRepository<Estinzione, Long>, QueryDslPredicateExecutor<Estinzione> {
    List<Estinzione> findDistinctByPratica_CodicePratica(long praticaId);
}
