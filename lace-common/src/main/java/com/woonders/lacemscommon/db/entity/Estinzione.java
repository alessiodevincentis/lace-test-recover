package com.woonders.lacemscommon.db.entity;

import com.woonders.lacemscommon.db.entityenum.StatoConteggioEstinzione;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "estinzione")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estinzione {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idestinzione", unique = true, nullable = false)
    private long idEstinzione;
    @Column(name = "tipoestinzione", length = 45)
    private String tipoEstinzione;
    @Column(name = "rataestinzione", precision = 10)
    private BigDecimal rataEstinzione = BigDecimal.ZERO;
    @Column(name = "durataestinzione")
    private Integer durataEstinzione = new Integer(0);
    @Temporal(TemporalType.DATE)
    @Column(name = "scadenzaestinzione")
    private Date scadenzaEstinzione;
    @Column(name = "montanteestinzione", precision = 10)
    private BigDecimal montanteEstinzione = BigDecimal.ZERO;
    @Column(name = "istitutoest")
    private String istitutoEst;
    @Temporal(TemporalType.DATE)
    @Column(name = "databustapaga")
    private Date dataBustapaga;
    @Column(name = "calcolascadenza", length = 45)
    private String calcolaScadenza;
    @ManyToOne(fetch = FetchType.LAZY)
    private Pratica pratica;
    @Temporal(TemporalType.DATE)
    @Column(name = "dataRinnovoEstinzione")
    private Date dataRinnovoEstinzione;
    @Temporal(TemporalType.DATE)
    @Column(name = "dataNotificaConteggioEstinzione")
    private Date dataNotificaConteggioEstinzione;
    @Column(name = "statoConteggioEstinzione")
    @Enumerated(EnumType.STRING)
    private StatoConteggioEstinzione statoConteggioEstinzione;
}
