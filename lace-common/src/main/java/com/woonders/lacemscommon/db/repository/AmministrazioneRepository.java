package com.woonders.lacemscommon.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.woonders.lacemscommon.db.entity.Amministrazione;

/**
 * Created by emanuele on 03/01/17.
 */
public interface AmministrazioneRepository extends JpaRepository<Amministrazione, Long> {

	List<Amministrazione> findDistinctByRagioneSocialeContainingIgnoreCase(String ragioneSociale);

	Amministrazione findByRagioneSocialeIgnoreCase(String ragioneSociale);

	List<Amministrazione> findByPivaIgnoreCase(String piva);

	List<Amministrazione> findByCodiceFiscaleIgnoreCase(String codiceFiscale);

	@Query("select distinct cliente.amministrazione from Cliente cliente where cliente.id = :clienteId")
	List<Amministrazione> findDistinctByCliente_Id(@Param("clienteId") long clienteId);
}
