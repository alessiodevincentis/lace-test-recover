package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.*;

import lombok.*;

/**
 * Created by Cristian on 16/11/2021.
 */
@Entity
@Table(name = "privacy_templates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyTemplate {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	@Column(name = "provenienza")
	private String provenienza;
	@Column(name = "fornitore_lead")
	private String fornitoreLead;
	@Column(name = "privacy_text")
	private String privacyText;
	@Temporal(TemporalType.DATE)
	@Column(name = "date_create")
	private Date dateCreate;
}
