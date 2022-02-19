package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.Campagna;

/**
 * Created by Emanuele on 27/03/2017.
 */
@Repository
public interface CampagnaRepository extends JpaRepository<Campagna, Long>, QueryDslPredicateExecutor<Campagna> {
}
