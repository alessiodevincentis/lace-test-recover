package com.woonders.lacemscommon.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.woonders.lacemscommon.db.entity.Trattenute;

/**
 * Created by emanuele on 03/01/17.
 */
public interface TrattenuteRepository extends JpaRepository<Trattenute, Long>, QueryDslPredicateExecutor<Trattenute> {

	List<Trattenute> findDistinctByCliente_Id(long clienteId);

}
