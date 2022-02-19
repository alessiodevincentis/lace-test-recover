package com.woonders.lacemscommon.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.woonders.lacemscommon.db.entity.Preventivo;

/**
 * Created by emanuele on 03/01/17.
 */
public interface PreventivoRepository extends JpaRepository<Preventivo, Long>, QueryDslPredicateExecutor<Preventivo> {

	List<Preventivo> findDistinctByCliente_IdAndCliente_Tipo(long clienteId, String tipoCliente);

}
