package com.woonders.lacemscommon.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.Residenza;

/**
 * Created by Emanuele on 13/11/2016.
 */
@Repository
public interface ResidenzaRepository extends JpaRepository<Residenza, Long> {

	Residenza findDistinctByCliente_Id(long clienteId);
	

    @Query("select distinct residenza.provResidenza from Residenza residenza where residenza.provResidenza is not null and residenza.provResidenza <> '' order by residenza.provResidenza")
    List<String> getDistinctProvice();
}
