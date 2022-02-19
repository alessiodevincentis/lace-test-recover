package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "cf", unique = true, length = 16)
    private String cf;
    @Column(name = "cognome")
    private String cognome;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Temporal(TemporalType.DATE)
    @Column(name = "datanascita")
    private Date dataNascita;
    @Column(name = "luogonascita")
    private String luogoNascita;
    @Column(name = "provincianascita")
    private String provNascita;
    @Column(name = "capnascita", length = 30)
    private String capNascita;
    @Column(name = "nazionenascita")
    private String nazioneNascita;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "email")
    private String email;
    @Column(name = "statocivile", length = 45)
    private String statoCivile;
    @Column(name = "beni", length = 45)
    private String beni;
    @Column(name = "impiego", length = 45)
    private String impiego;
    @Column(name = "qualifica", length = 45)
    private String qualifica;
    @Column(name = "occupazione", length = 150)
    private String occupazione;
    @Temporal(TemporalType.DATE)
    @Column(name = "datainizio")
    private Date dataInizio;
    @Column(name = "ente")
    private String ente;
    @Column(name = "cat")
    private String cat;
    @Column(name = "note")
    private String note;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datains")
    private Date dataIns = new Date();
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Conto conto;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Documento documento;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Residenza residenza;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Set<Trattenute> trattenuta = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Set<Pratica> pratica = new HashSet<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "sesso")
    private Sesso sesso;
    @Column(name = "tipo", length = 45)
    private String tipo;
    @Temporal(TemporalType.DATE)
    @Column(name = "datacliente")
    private Date dataCliente;
    @Column(name = "provenienza")
    private String provenienza;
    @Column(name = "provenienzadesc")
    private String provenienzaDesc;
    @Column(name = "fornitore_lead")
    private String fornitoreLead;
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operatoreNominativo;
    @Column(name = "statonominativo", length = 45)
    private String statoNominativo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datarecallnominativo")
    private Date dataRecallNominativo;
    @Column(name = "telefonofisso")
    private String telefonoFisso;
    @Column(name = "tipocontrattolavoro", length = 45)
    private String tipoContrattoLavoro;
    @Column(name = "telefonolavoro")
    private String telefonoLavoro;
    @Column(name = "nrcomponentifamiliari")
    private Integer nrComponentiFamiliari = 0;
    @Column(name = "nettomensilenominativo", precision = 10)
    private BigDecimal nettoMensileNominativo = BigDecimal.ZERO;
    @Column(name = "importorichiesto", precision = 10)
    private BigDecimal importoRichiesto = BigDecimal.ZERO;
    @Column(name = "impiego2", length = 45)
    private String impiego2;
    @Column(name = "qualifica2", length = 45)
    private String qualifica2;
    @Column(name = "occupazione2", length = 150)
    private String occupazione2;
    @Temporal(TemporalType.DATE)
    @Column(name = "datainizio2")
    private Date dataInizio2;
    @Column(name = "ente2")
    private String ente2;
    @Column(name = "cat2")
    private String cat2;
    @Column(name = "tipocontrattolavoro2", length = 45)
    private String tipoContrattoLavoro2;
    @Column(name = "telefonolavoro2")
    private String telefonoLavoro2;
    @Column(name = "seconda_occupazione_predefinita")
    private boolean secondaOccupazionePredefinita;
    @Column(name = "seconda_occupazione_rendered")
    private boolean secondaOccupazioneRendered;
    @Column(name = "durata_richiesta_mesi")
    private Integer durataRichiestaMesi;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cliente_amministrazione", joinColumns = {
            @JoinColumn(name = "cliente_id")}, inverseJoinColumns = {
            @JoinColumn(name = "amministrazione_codiceamm")})
    private Set<Amministrazione> amministrazione = new HashSet<>();
    @Column(name = "leadId")
    private String leadId;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Set<Preventivo> preventivo = new HashSet<>();
    @Column(name = "cittadinanza")
    private String cittadinanza;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Set<ClientePraticaFile> clienteFileSet;
    

    public enum Sesso {
        MASCHIO("M", "Maschio"), FEMMINA("F", "Femmina");

        private final String value;
        private final String formValue;

        Sesso(final String value, final String formValue) {
            this.value = value;
            this.formValue = formValue;
        }

        public static Sesso fromValue(final String value) {
            if (value == null) {
                return null;
            }
            for (final Sesso sesso : Sesso.values()) {
                if (sesso.toString().equalsIgnoreCase(value)) {
                    return sesso;
                }
            }
            throw new IllegalArgumentException("No constant with value " + value);
        }

        public String getValue() {
            return value;
        }

        public String getFormValue() {
            return formValue;
        }

    }
}
