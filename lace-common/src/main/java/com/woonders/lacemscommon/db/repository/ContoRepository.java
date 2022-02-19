package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woonders.lacemscommon.db.entity.Conto;

/**
 * Created by emanuele on 03/01/17.
 */
public interface ContoRepository extends JpaRepository<Conto, Long> {
	Conto findDistinctByCliente_Id(long clienteId);
}
