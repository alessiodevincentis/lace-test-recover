package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woonders.lacemscommon.db.entity.Documento;

/**
 * Created by emanuele on 03/01/17.
 */
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
	Documento findDistinctByCliente_Id(long clienteId);
}
