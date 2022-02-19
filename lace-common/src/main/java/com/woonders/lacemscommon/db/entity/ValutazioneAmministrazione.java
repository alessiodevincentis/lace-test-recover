package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

/**
 * Created by Emanuele on 07/11/2016.
 */
@Entity
@Table(name = "valutazione_amministrazione")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValutazioneAmministrazione {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	@Column(name = "compagnia", nullable = false)
	private String compagnia;
	@Column(name = "valutazione", nullable = false)
	private String valutazione;
	@Temporal(TemporalType.DATE)
	@Column(name = "data_valutazione")
	private Date dataValutazione;
	@Column(name = "info_aggiuntive")
	private String infoAggiuntive;
	@ManyToOne(fetch = FetchType.LAZY)
	private Amministrazione amministrazione;
}
