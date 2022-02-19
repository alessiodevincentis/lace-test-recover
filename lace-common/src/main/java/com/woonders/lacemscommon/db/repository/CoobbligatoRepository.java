package com.woonders.lacemscommon.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.Coobbligato;

/**
 * Created by emanuele on 03/01/17.
 */
@Repository
public interface CoobbligatoRepository extends JpaRepository<Coobbligato, Long> {
	List<Coobbligato> findDistinctByPratica_CodicePratica(long codicePratica);
}
