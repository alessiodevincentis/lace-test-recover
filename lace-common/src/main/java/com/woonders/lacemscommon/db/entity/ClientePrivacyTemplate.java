package com.woonders.lacemscommon.db.entity;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

/**
 * Created by Cristian on 16/11/2021.
 */
@Entity
@Table(name = "cliente_privacy_templates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientePrivacyTemplate {

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
	@Column(name = "cliente_id", nullable = false)
	private long clienteId;
	@Column(name = "privacy_templates_id", nullable = false)
	private long privacyTemplatesId;
	@Temporal(TemporalType.DATE)
	@Column(name = "date_create", nullable = false)
	private Date dateCreate;
}
