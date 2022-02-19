package com.woonders.lacemscommon.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woonders.lacemscommon.db.entity.ValutazioneAmministrazione;

/**
 * Created by emanuele on 03/01/17.
 */
public interface ValutazioneAmministrazioneRepository extends JpaRepository<ValutazioneAmministrazione, Long> {

	List<ValutazioneAmministrazione> findDistinctByAmministrazione_CodiceAmm(long codiceAmm);
}
