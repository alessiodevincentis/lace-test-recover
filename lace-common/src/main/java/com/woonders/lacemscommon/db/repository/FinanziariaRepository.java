package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.Finanziaria;

/**
 * Created by emanuele on 02/01/17.
 */
@Repository
public interface FinanziariaRepository extends JpaRepository<Finanziaria, Long> {
}
