package com.woonders.lacemscommon.db.entity;

/**
 * Created by Emanuele on 08/02/2017.
 */

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.woonders.lacemscommon.db.entityenum.StatoNominativo;
import com.woonders.lacemscommon.db.entityenum.StatoPratica;
import com.woonders.lacemscommon.service.LogService;

import lombok.*;

@Entity
@Table(name = "nominativo_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NominativoLog {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "executionDateTime")
	private LocalDateTime executionDateTime;
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator operator;
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente nominativo;
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator operatoreAssegnato;
	@Column(name = "dataRecall")
	private LocalDateTime dataRecall;
	@Enumerated(EnumType.STRING)
	@Column(name = "statoNominativo")
	private StatoNominativo statoNominativo;
	@Column(name = "commento")
	private String commento;
	@ManyToOne(fetch = FetchType.LAZY)
	private Pratica pratica;
	@Enumerated(EnumType.STRING)
	@Column(name = "statoPratica")
	private StatoPratica statoPratica;
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoLog")
	private LogService.TipoLog tipoLog;

}
