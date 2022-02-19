package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Set;

import javax.persistence.*;

import lombok.*;

/**
 * Created by emanuele on 26/11/16.
 */
@Entity
@Table(name = "finanziaria")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Finanziaria {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "finanziaria")
	private Set<Pratica> praticaSet;
}
