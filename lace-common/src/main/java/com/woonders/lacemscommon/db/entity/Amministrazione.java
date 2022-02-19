package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "amministrazione")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Amministrazione {

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "amministrazione")
	private Set<Cliente> cliente = new HashSet<>();
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "amministrazione")
	private Set<ValutazioneAmministrazione> valutazioneAmministrazione = new HashSet<>();
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "codiceamm", unique = true, nullable = false)
	private long codiceAmm;
	@Column(name = "ragionesociale", length = 85)
	private String ragioneSociale;
	@Column(name = "piva", length = 45)
	private String piva;
	@Column(name = "codicefiscale", length = 45)
	private String codiceFiscale;
	@Column(name = "cqs")
	private boolean cqs;
	@Column(name = "dlg")
	private boolean dlg;
	@Column(name = "indirizzosedelegale")
	private String indirizzoSedeLegale;
	@Column(name = "luogosedelegale")
	private String luogoSedeLegale;
	@Column(name = "provSedeLegale")
	private String provSedeLegale;
	@Column(name = "capsedelegale", length = 45)
	private String capSedeLegale;
	@Column(name = "civicosedeLegale", length = 45)
	private String civicoSedeLegale;
	@Column(name = "nazionesedelegale")
	private String nazioneSedeLegale;
	@Column(name = "indirizzosedenotifica")
	private String indirizzoSedeNotifica;
	@Column(name = "luogosedenotifica")
	private String luogoSedeNotifica;
	@Column(name = "provsedenotifica")
	private String provSedeNotifica;
	@Column(name = "capsedenotifica", length = 45)
	private String capSedeNotifica;
	@Column(name = "civicosedenotifica", length = 45)
	private String civicoSedeNotifica;
	@Column(name = "nazionesedenotifica")
	private String nazioneSedeNotifica;
	@Column(name = "notificapec")
	private boolean notificaPec;
	@Column(name = "indirizzopec", length = 85)
	private String indirizzoPec;
	@Column(name = "referente", length = 45)
	private String referente;
	@Column(name = "ruoloazienda", length = 45)
	private String ruolo;
	@Column(name = "telefono", length = 45)
	private String telefono;
	@Column(name = "fax", length = 45)
	private String fax;
	@Column(name = "email", length = 45)
	private String email;
	@Column(name = "note", length = 160)
	private String note;

}
