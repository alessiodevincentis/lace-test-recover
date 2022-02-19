package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "pratica")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pratica {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "codicepratica", unique = true, nullable = false)
    private long codicePratica;
    @Column(name = "nrsecci", length = 15)
    private String nrsecci;
    @Column(name = "nrctr", length = 15)
    private String nrctr;
    @Column(name = "rata", precision = 10)
    private BigDecimal rata = BigDecimal.ZERO;
    @Column(name = "durata")
    private Integer durata = 0;
    @Column(name = "tipopratica", length = 45)
    private String tipoPratica;
    @Column(name = "importoerogato", precision = 10)
    private BigDecimal importoErogato = BigDecimal.ZERO;
    @Column(name = "spese", precision = 10)
    private BigDecimal spese = BigDecimal.ZERO;
    @Column(name = "tan", precision = 10)
    private BigDecimal tan = BigDecimal.ZERO;
    @Column(name = "taeg", precision = 10)
    private BigDecimal taeg = BigDecimal.ZERO;
    @Column(name = "teg", precision = 10)
    private BigDecimal teg = BigDecimal.ZERO;
    @Column(name = "garanzia", length = 25)
    private String garanzia;
    @Column(name = "quintomax", precision = 10)
    private BigDecimal quintoMax = BigDecimal.ZERO;
    @Temporal(TemporalType.DATE)
    @Column(name = "datacaricamento")
    private Date dataCaricamento = new Date();
    @Temporal(TemporalType.DATE)
    @Column(name = "datarinnovo")
    private Date dataRinnovo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operatore;
    @Column(name = "tipoprovvigione", length = 45)
    private String tipoProvvigione;
    @Column(name = "provvigione", precision = 10)
    private BigDecimal provvigione = BigDecimal.ZERO;
    @Column(name = "percprov", precision = 10)
    private BigDecimal percProv = BigDecimal.ZERO;
    @Column(name = "perfezionata")
    private boolean perfezionata;
    @Column(name = "cinquanta")
    private boolean cinquanta;
    @Column(name = "confermaprov")
    private boolean confermaProv;
    @Temporal(TemporalType.DATE)
    @Column(name = "dataperf")
    private Date dataPerf;
    @Temporal(TemporalType.DATE)
    @Column(name = "notirinnovo")
    private Date notiRinnovo;
    @Column(name = "web")
    private boolean web;
    @Temporal(TemporalType.DATE)
    @Column(name = "dataliquidazione")
    private Date dataLiquidazione;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
    @Column(name = "statopratica", length = 45)
    private String statoPratica;
    @Column(name = "notepratica")
    private String notePratica;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notirecall")
    private Date notiRecall;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datarecall")
    private Date dataRecall;
    @Temporal(TemporalType.DATE)
    @Column(name = "decorrenza")
    private Date decorrenza;
    @Column(name = "rinnovata")
    private boolean rinnovata;
    @Temporal(TemporalType.DATE)
    @Column(name = "scadenzapratica")
    private Date scadenzaPratica;
    @Column(name = "antiriciclaggio_file_caricato")
    private boolean antiriciclaggioFileCaricato;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pratica")
    private Set<Estinzione> estinzione = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pratica")
    private Set<Coobbligato> coobbligato = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Finanziaria finanziaria;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_notifica_stato_pratica")
    private Date dataNotificaStatoPratica;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_istruttoria")
    private Date dataIstruttoria;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_istruita")
    private Date dataIstruita;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_delibera")
    private Date dataDelibera;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_firma_contratti")
    private Date dataFirmaContratti;
    @Column(name = "capitale_finanziato")
    private BigDecimal capitaleFinanziato;
    @OneToOne(fetch = FetchType.LAZY)
    private SimulatorTable simulatorTable;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pratica")
    private Set<ClientePraticaFile> praticaFileSet;

    public enum TipoPratica {

        CESSIONE_P("CQP"), CESSIONE_S("CQS"), PRESTITO("PP"), DELEGA("DLG");

        private final String value;

        private TipoPratica(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
