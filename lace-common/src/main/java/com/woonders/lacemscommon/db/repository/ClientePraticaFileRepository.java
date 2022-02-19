package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.ClientePraticaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by ema98 on 9/17/2017.
 */
public interface ClientePraticaFileRepository extends JpaRepository<ClientePraticaFile, Long>, QueryDslPredicateExecutor<ClientePraticaFile> {

    List<ClientePraticaFile> findByCliente_Id(Long clienteId);

    List<ClientePraticaFile> findByPratica_CodicePratica(Long codicePratica);
}
