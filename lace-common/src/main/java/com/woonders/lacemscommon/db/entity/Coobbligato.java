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
@Table(name = "coobbligato")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coobbligato {

	@Column(name = "nrcomponentifamiliari")
	private Integer nrComponentiFamiliari;
	@Column(name = "nettostipendio", precision = 10)
	private BigDecimal nettoStipendio;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	@Column(name = "codicefiscale", length = 16)
	private String codiceFiscale;
	@Column(name = "cognome")
	private String cognome;
	@Column(name = "nome")
	private String nome;
	@Enumerated(EnumType.STRING)
	@Column(name = "sesso", length = 10)
	private Cliente.Sesso sesso;
	@Temporal(TemporalType.DATE)
	@Column(name = "datanascita")
	private Date dataNascita;
	@Column(name = "luogonascita")
	private String luogoNascita;
	@Column(name = "provincianascita")
	private String provinciaNascita;
	@Column(name = "nazionenascita")
	private String nazioneNascita;
	@Column(name = "cittadinanza")
	private String cittadinanza;
	@Column(name = "indirizzo")
	private String indirizzo;
	@Column(name = "civicoabitazione")
	private String civicoAbitazione;
	@Column(name = "luogo")
	private String luogo;
	@Column(name = "cap", length = 45)
	private String cap;
	@Column(name = "provincia", length = 45)
	private String provincia;
	@Column(name = "nazioneabitazione")
	private String nazioneAbitazione;
	@Column(name = "telefono", length = 45)
	private String telefono;
	@Column(name = "email", length = 45)
	private String email;
	@Column(name = "impiego", length = 45)
	private String impiego;
	@Column(name = "occupazione", length = 45)
	private String occupazione;
	@Temporal(TemporalType.DATE)
	@Column(name = "dataassunzione")
	private Date dataAssunzione;
	@Column(name = "tipocontrattolavoro", length = 45)
	private String tipoContrattoLavoro;
	@Column(name = "telefonolavoro", length = 45)
	private String telefonoLavoro;
	@Column(name = "ente")
	private String ente;
	@Column(name = "cat", length = 45)
	private String cat;
	@Column(name = "tipodocumento", length = 45)
	private String tipoDocumento;
	@Column(name = "numerodocumento", length = 45)
	private String numeroDocumento;
	@Temporal(TemporalType.DATE)
	@Column(name = "datarilasciodocumento")
	private Date dataRilascioDocumento;
	@Column(name = "luogorilasciodocumento")
	private String luogoRilascioDocumento;
	@Column(name = "provinciarilasciodocumento")
	private String provinciaRilascioDocumento;
	@Column(name = "nazionerilasciodocumento")
	private String nazioneRilascioDocumento;
	@Temporal(TemporalType.DATE)
	@Column(name = "scadenzadocumento")
	private Date scadenzaDocumento;
	@ManyToOne(fetch = FetchType.LAZY)
	private Pratica pratica;
}
