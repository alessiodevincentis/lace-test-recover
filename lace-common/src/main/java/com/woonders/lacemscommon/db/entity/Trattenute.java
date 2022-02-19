package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.querydsl.core.annotations.QueryInit;

import lombok.*;

@Entity
@Table(name = "trattenute")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trattenute {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "codstip", unique = true, nullable = false)
	private long codStip;
	@Column(name = "tipotrat", length = 45)
	private String tipoTrat;
	@Column(name = "ratatrat", precision = 10)
	private BigDecimal rataTrat = BigDecimal.ZERO;
	@Column(name = "duratatrat")
	private Integer durataTrat = 0;
	@Temporal(TemporalType.DATE)
	@Column(name = "scadenzatrat")
	private Date scadenzaTrat;
	@Temporal(TemporalType.DATE)
	@Column(name = "datarinnovotrat")
	private Date dataRinnovoTrat;
	@Column(name = "montantetrat", precision = 10)
	private BigDecimal montanteTrat = BigDecimal.ZERO;
	@Temporal(TemporalType.DATE)
	@Column(name = "busta")
	private Date busta;
	@Column(name = "calcoladat", length = 20)
	private String calcolaDat;
	@Temporal(TemporalType.DATE)
	@Column(name = "noticoes")
	private Date notiCoes;
	@Column(name = "istitutotrat")
	private String istitutoTrat;
	// https://stackoverflow.com/questions/26381064/why-is-query-dsl-entity-path-limited-to-four-levels
	// https://stackoverflow.com/questions/6385036/querydsl-generated-classes-not-able-to-access-second-level-elements-for-querying
	// http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#d0e2260
	// QUERYDSL PATH INITIALIZATION
	@ManyToOne(fetch = FetchType.LAZY)
	@QueryInit("operatoreNominativo.azienda")
	private Cliente cliente;

	public enum TipoImpegno {

		CESSIONE_P("CQP"), CESSIONE_S("CQS"), PRESTITO("PP"), DELEGA("DLG"), PIGNORAMENTO("Pignoramento");

		private final String value;

		private TipoImpegno(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}