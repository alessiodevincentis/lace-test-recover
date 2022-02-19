package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.Pratica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by emanuele on 03/01/17.
 */
public interface PraticaRepository extends JpaRepository<Pratica, Long>, QueryDslPredicateExecutor<Pratica> {

    List<Pratica> findDistinctByCliente_IdAndStatoPraticaNotInAndRinnovata(long clienteId,
                                                                           Collection<String> statoPraticaStringCollection, boolean rinnovata);

    @Query("select distinct pratica from Pratica pratica where cliente.id = :clienteId and rinnovata = false and (decorrenza is null or scadenzaPratica >= :scadenzaPratica) and statoPratica not in :statoPraticaCollectionString")
    List<Pratica> findDistinctByCliente_IdAndRinnovataFalseAndOrDecorrenzaNullOrScadenzaAndNotInStatoPratica(
            @Param("clienteId") long clienteId, @Param("scadenzaPratica") Date scadenzaPratica,
            @Param("statoPraticaCollectionString") Collection<String> statoPraticaStringCollection);

    List<Pratica> findDistinctByCliente_Id(long idCliente);

    List<Pratica> findDistinctByCliente_IdAndOperatore_Id(long idCliente, long currentUserId);

    List<Pratica> findByNrctrEquals(String nrContratto);
}
