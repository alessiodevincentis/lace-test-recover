package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.Residenza;

/**
 * Created by Emanuele on 13/11/2016.
 */
@Repository
public interface ResidenzaRepository extends JpaRepository<Residenza, Long> {

	Residenza findDistinctByCliente_Id(long clienteId);
}
