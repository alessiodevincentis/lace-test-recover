package com.woonders.lacemscommon.db.entity;

import com.woonders.lacemscommon.db.entityenum.FeeType;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.SimulatorRoundingMode;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by ema98 on 8/10/2017.
 */
@Entity
@Table(name = "simulator_table")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulatorTable {

    @ElementCollection(targetClass = Impiego.class)
    @CollectionTable(name = "simulator_table_jobtype",
            joinColumns = @JoinColumn(name = "simulatorTableId"))
    @Enumerated(EnumType.STRING)
    @Column(name = "jobTypeId")
    protected Set<Impiego> jobTypeSet;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    /**
     * Tipo costo polizza
     */
    @Column
    @Enumerated(EnumType.STRING)
    private FeeType insuranceCostFeeType;
    @Column
    private BigDecimal over;
    /**
     * Tipo arrotondamento
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SimulatorRoundingMode simulatorRoundingMode;
    /**
     * Tipo spese istruttoria
     */
    @Column
    @Enumerated(EnumType.STRING)
    private FeeType inquiryCostFeeType;
    /**
     * Tipo commissioni gestione
     */
    @Column
    @Enumerated(EnumType.STRING)
    private FeeType managementFeesFeeType;
    /**
     * Tipo imposta di bollo
     */
    @Column
    @Enumerated(EnumType.STRING)
    private FeeType stampDutyFeeType;
    /**
     * Tipo altri costi
     */
    @Column
    @Enumerated(EnumType.STRING)
    private FeeType otherCostsFeeType;
    @Column
    private boolean enabled;
    @OneToOne(fetch = FetchType.LAZY)
    private Operator creatorOperator;
    @OneToOne(fetch = FetchType.LAZY)
    private Azienda azienda;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Pratica.TipoPratica simulatorTableType;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "simulatorTable")
    private List<SimulatorTableDetails> simulatorTableDetailsList;
    /**
     * Operatori assegnati alla tabella
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "simulator_table_operator", joinColumns = {
            @JoinColumn(name = "simulatorTableId")}, inverseJoinColumns = {
            @JoinColumn(name = "operatorId")})
    private Set<Operator> grantedOperatorSet;
}
