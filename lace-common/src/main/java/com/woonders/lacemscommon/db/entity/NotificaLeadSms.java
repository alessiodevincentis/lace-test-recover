package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifica_lead_sms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificaLeadSms {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "data_esito")
	private LocalDateTime dataEsito;
	@Column(name = "esito")
	@Enumerated(EnumType.STRING)
	private EsitoSmsNotificaLead esito;
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
	@Column(name = "sms_id")
	private String smsId;
}
