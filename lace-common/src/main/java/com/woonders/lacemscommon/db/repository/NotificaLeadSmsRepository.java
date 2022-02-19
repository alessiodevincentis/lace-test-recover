package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.NotificaLeadSms;

@Repository
public interface NotificaLeadSmsRepository extends JpaRepository<NotificaLeadSms, Long> {

	NotificaLeadSms findByCliente_Id(long nominativoId);

	NotificaLeadSms findBySmsId(String smsId);
}
