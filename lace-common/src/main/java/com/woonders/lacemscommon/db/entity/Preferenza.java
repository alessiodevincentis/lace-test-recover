package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

import lombok.*;

/**
 * Created by Emanuele on 16/01/2017.
 */
@Entity
@Table(name = "preferenza")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preferenza {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "smsSenderValue")
	private String smsSenderValue;
}
