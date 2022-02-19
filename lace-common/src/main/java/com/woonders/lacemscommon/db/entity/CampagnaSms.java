package com.woonders.lacemscommon.db.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;

import lombok.*;

/**
 * Created by Emanuele on 27/03/2017.
 */
@Entity
@Table(name = "campagna_sms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampagnaSms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@OneToOne
	private Cliente cliente;
	@Column(name = "nomeDestinatario")
	private String nomeDestinatario;
	@Column(name = "cognomeDestinatario")
	private String cognomeDestinatario;
	@Column(name = "numeroDestinatario")
	private String numeroDestinatario;
	@Column(name = "messageId")
	private String messageId;
	@Column(name = "esito")
	@Enumerated(EnumType.STRING)
	private EsitoSmsNotificaLead esito;
	@Column(name = "dataEsito")
	private LocalDateTime dataEsito;
	@OneToOne
	private Campagna campagna;
}
