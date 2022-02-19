package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calendario_appuntamento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioAppuntamento {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "titolo")
	private String titolo;
	@Column(name = "inizio_appuntamento")
	private LocalDateTime inizioAppuntamento;
	@Column(name = "descrizione")
	private String descrizione;
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente nominativo;
	@ManyToOne(fetch = FetchType.LAZY)
	private Pratica pratica;
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator operator;
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator operatoreAssegnato;
	@Column(name = "fine_appuntamento")
	private LocalDateTime fineAppuntamento;
	@Column(name = "invio_sms_cliente_nominativo")
	private boolean invioSmsClienteNominativo;
	@Column(name = "invio_sms_operatore_assegnato")
	private boolean invioSmsOperatoreAssegnato;
}
