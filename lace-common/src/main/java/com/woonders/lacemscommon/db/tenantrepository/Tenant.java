package com.woonders.lacemscommon.db.tenantrepository;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

import lombok.*;

/**
 * Created by Emanuele on 24/03/2017.
 */
@Entity
@Table(name = "tenant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	@Column(name = "enabled", nullable = false)
	private boolean enabled;
}
