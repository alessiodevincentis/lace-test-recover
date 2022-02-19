package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Cristian on 03/11/2021.
 */
@Entity
@Table(name = "fornitori_leads")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornitoriLead {
	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "fornitore_lead", nullable = false)
    private String fornitoreLeadName;
    @Column(name = "provenienza")
    private String provenienza;
    @Column(name = "provenienzadesc")
    private String provenienzaDesc;
    @Column(name = "data_retention", nullable = false)
    private int dataRetention;
//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "fornitori_leads")
//	private Cliente cliente;
}
