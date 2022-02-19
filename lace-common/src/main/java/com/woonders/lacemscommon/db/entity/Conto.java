package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

/**
 * Created by Emanuele on 07/11/2016.
 */
@Entity
@Table(name = "conto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conto {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "codiceconto", unique = true, nullable = false)
	private long codiceConto;
	@Column(name = "banca")
	private String banca;
	@Column(name = "sportello")
	private String sportello;
	@Column(name = "iban", length = 27)
	private String iban;
	@Temporal(TemporalType.DATE)
	@Column(name = "dataconto")
	private Date dataConto;
	@Column(name = "stipendio")
	private boolean stipendio;
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
}
