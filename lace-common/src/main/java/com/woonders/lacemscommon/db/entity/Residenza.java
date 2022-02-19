package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import lombok.*;

/**
 * Created by Emanuele on 07/11/2016.
 */
@Entity
@Table(name = "residenza")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Residenza {

	@Column(name = "speseabitazione", precision = 10)
	private BigDecimal speseAbitazione;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "codiceres", unique = true, nullable = false)
	private long codiceRes;
	@Column(name = "luogoresidenza")
	private String luogoResidenza;
	@Column(name = "provresidenza")
	private String provResidenza;
	@Column(name = "capresidenza", length = 30)
	private String capResidenza;
	@Column(name = "indirizzoresidenza")
	private String indirizzoResidenza;
	@Column(name = "civicoresidenza", length = 45)
	private String civicoResidenza;
	@Column(name = "nazioneresidenza")
	private String nazioneResidenza;
	@Column(name = "indirizzodomicilio")
	private String indirizzoDomicilio;
	@Column(name = "luogodomicilio")
	private String luogoDomicilio;
	@Column(name = "provdomicilio")
	private String provDomicilio;
	@Column(name = "capdomicilio", length = 30)
	private String capDomicilio;
	@Column(name = "civicodomicilio", length = 45)
	private String civicoDomicilio;
	@Column(name = "nazionedomicilio")
	private String nazioneDomicilio;
	@Column(name = "corrispondenza", length = 45)
	private String corrispondenza;
	@Temporal(TemporalType.DATE)
	@Column(name = "datainizioresidenza")
	private Date dataInizioResidenza;
	@Column(name = "tipoabitazione", length = 45)
	private String tipoAbitazione;
	@Column(name = "mutuo")
	private boolean mutuo;
	@Column(name = "indirizzoprecedente")
	private String indirizzoPrecedente;
	@Column(name = "luogoprecedente")
	private String luogoPrecedente;
	@Column(name = "provinciaprecedente")
	private String provinciaPrecedente;
	@Column(name = "capprecedente", length = 45)
	private String capPrecedente;
	@Column(name = "civicoprecedente", length = 45)
	private String civicoPrecedente;
	@Column(name = "nazioneprecedente")
	private String nazionePrecedente;
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
}
