package com.woonders.lacemscommon.db.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Emanuele on 30/01/2017.
 */
@Entity
@Table(name = "setting")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "leadIlComparatoreEnabled", nullable = false)
    private boolean leadIlComparatoreEnabled;
    @Column(name = "simulatorEnabled", nullable = false)
    private boolean simulatorEnabled;
    @Column(name = "agenzia_sms_balance", nullable = false)
    private int agenziaSmsBalance;
    @Column(name = "send_sms_lead", nullable = false)
    private boolean sendSmsLead;
    @Column(name = "mittente_sms", nullable = false)
    private String mittenteSms;
    @Column(name = "testo_sms_lead")
    private String testoSmsLead;
    @Column(name = "testo_sms_appuntamento_cliente_nominativo")
    private String testoSmsAppuntamentoClienteNominativo;
    @Column(name = "testo_sms_appuntamento_operatore_assegnato")
    private String testoSmsAppuntamentoOperatoreAssegnato;
    @ManyToOne(fetch = FetchType.LAZY)
    private Azienda azienda;
    @Column(name = "alias", nullable = false)
    private String alias;

}
