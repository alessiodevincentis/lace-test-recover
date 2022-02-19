package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "preventivo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preventivo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idpreventivo", unique = true, nullable = false)
    private long idPreventivo;
    @Column(name = "tipo_pratica", length = 45)
    private String tipoPratica;
    @Column(name = "rata", precision = 10)
    private BigDecimal rata = BigDecimal.ZERO;
    @Column(name = "durata")
    private Integer durata = new Integer(0);
    @Column(name = "importo", precision = 10)
    private BigDecimal importo = BigDecimal.ZERO;
    @Column(name = "tan", precision = 10)
    private BigDecimal tan = BigDecimal.ZERO;
    @Column(name = "taeg", precision = 10)
    private BigDecimal taeg = BigDecimal.ZERO;
    @Column(name = "teg", precision = 10)
    private BigDecimal teg = BigDecimal.ZERO;
    @Column(name = "tipoprovvigione", length = 45)
    private String tipoProvvigione;
    @Column(name = "provvigione", precision = 10)
    private BigDecimal provvigione = BigDecimal.ZERO;
    @Column(name = "percProvvigione", precision = 10)
    private BigDecimal percProvvigione = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipologia_preventivo", length = 45)
    private TipologiaPreventivo tipologiaPreventivo;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_creazione")
    private Date dataCreazione;
    @Column(name = "notifica_preventivo")
    private boolean notificaPreventivo;
    @Column(name = "spese", precision = 2)
    private BigDecimal spese = BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
    @OneToOne(fetch = FetchType.LAZY)
    private SimulatorTable simulatorTable;

    public enum TipologiaPreventivo {
        NUOVO("Nuova Pratica"), RINNOVO("Rinnovo");

        private final String value;

        private TipologiaPreventivo(String value) {
            this.value = value;
        }

        public static TipologiaPreventivo fromValue(String value) {
            if (value == null) {
                return null;
            }
            for (TipologiaPreventivo tipologiaPreventivo : TipologiaPreventivo.values()) {
                if (tipologiaPreventivo.toString().equalsIgnoreCase(value)) {
                    return tipologiaPreventivo;
                }
            }
            throw new IllegalArgumentException("No constant with value " + value);
        }

        public String getValue() {
            return value;
        }
    }

}
