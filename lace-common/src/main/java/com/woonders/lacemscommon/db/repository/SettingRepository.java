package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.Setting;

/**
 * Created by Emanuele on 30/01/2017.
 */
@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

	Setting findByAzienda_Id(long aziendaId);
}
