package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by ema98 on 8/10/2017.
 */
@Entity
@Table(name = "simulator_table_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulatorTableDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;
    /**
     * Durata finanziamento
     */
    @Column(nullable = false)
    private int length;
    /**
     * Prelievo ogni x mensilita?!?!
     */
    @Column(nullable = false)
    private int mensilita;
    @Column(nullable = false)
    private BigDecimal tan;
    /**
     * Taeg max inseribile nella pratica
     */
    @Column
    private BigDecimal maxTaeg;
    /**
     * Provvigioni max inseribili nella pratica
     */
    @Column
    private BigDecimal maxAgentFees;
    /**
     * Spese istruttoria
     */
    @Column
    private BigDecimal inquiryCost;
    /**
     * Spese istruttoria nel taeg
     */
    @Column
    private BigDecimal inquiryCostTaeg;
    /**
     * MAX Spese istruttoria
     */
    @Column
    private BigDecimal maxInquiryCost;
    /**
     * Commissioni gestione
     */
    @Column
    private BigDecimal managementFees;
    /**
     * Commissioni gestione nel taeg
     */
    @Column
    private BigDecimal managementFeesTaeg;
    /**
     * MAX Commissioni gestione
     */
    @Column
    private BigDecimal maxManagementFees;
    /**
     * Costo imposta di bollo
     */
    @Column
    private BigDecimal stampDutyCost;
    /**
     * Costo imposta di bollo nel taeg
     */
    @Column
    private BigDecimal stampDutyCostTaeg;
    /**
     * MAX Costo imposta di bollo
     */
    @Column
    private BigDecimal maxStampDutyCost;
    /**
     * Altri costi
     */
    @Column
    private BigDecimal otherCosts;
    /**
     * Altri costi nel taeg
     */
    @Column
    private BigDecimal otherCostsTaeg;
    /**
     * MAX Altri costi
     */
    @Column
    private BigDecimal maxOtherCosts;
    /**
     * Costo polizza
     */
    @Column
    private BigDecimal insuranceCost;
    /**
     * Costo polizza nel taeg
     */
    @Column
    private BigDecimal insuranceCostTaeg;
    /**
     * Costo polizza Max.
     */
    @Column
    private BigDecimal maxInsuranceCost;
    @OneToOne
    private SimulatorTable simulatorTable;

}
