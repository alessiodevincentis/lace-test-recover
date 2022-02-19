package com.woonders.lacemscommon.db.entity;

import javax.persistence.*;

import com.woonders.lacemscommon.db.entityenum.StatoPratica;

import lombok.*;

@Entity
@Table(name = "preferenza_stato_pratica")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenzaStatoPratica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	@Column(name = "nome_stato_pratica", nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private StatoPratica nomeStatoPratica;
	@Column(name = "giorni_notifica", nullable = false, unique = true)
	private int giorniNotifica;
	@ManyToOne(fetch = FetchType.LAZY)
	private Azienda azienda;
}
