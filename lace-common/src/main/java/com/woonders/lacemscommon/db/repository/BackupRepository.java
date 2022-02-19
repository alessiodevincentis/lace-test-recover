package com.woonders.lacemscommon.db.repository;

import com.woonders.lacemscommon.db.entity.Backup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long> {
}
