package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.GeneralSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ema98 on 9/21/2017.
 */
@Repository
public interface GeneralSettingRepository extends JpaRepository<GeneralSetting, Long> {

}
