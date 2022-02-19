package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.SimulatorTableDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by ema98 on 8/10/2017.
 */
@Repository
public interface SimulatorTableDetailsRepository extends JpaRepository<SimulatorTableDetails, Long>, QueryDslPredicateExecutor<SimulatorTableDetails> {
}
