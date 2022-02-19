package com.woonders.lacemscommon.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.woonders.lacemscommon.db.entityenum.RicaricaType;

import lombok.*;

/**
 * Created by Emanuele on 16/01/2017.
 */
@Entity
@Table(name = "ricarica_comunicazione")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RicaricaComunicazione {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "dateTime")
	private LocalDateTime dateTime;
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator payer;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "paymentMethod")
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	@Column(name = "quantity")
	private int quantity;
	@Column(name = "ricaricaType")
	@Enumerated(EnumType.STRING)
	private RicaricaType ricaricaType;
	@Column(name = "fatturaId")
	private String fatturaId;
	@OneToOne
	private Operator operatoreRicaricato;

	public enum PaymentMethod {
		BANK_TRANSFER("Bonifico bancario"), CREDIT_CARD("Carta di credito"), VOUCHER("Voucher");

		private final String value;

		PaymentMethod(String value) {
			this.value = value;
		}

		public static PaymentMethod fromValue(final String value) {
			if (value == null) {
				return null;
			}
			for (PaymentMethod paymentMethod : PaymentMethod.values()) {
				if (paymentMethod.value.equalsIgnoreCase(value)) {
					return paymentMethod;
				}
			}
			throw new IllegalArgumentException("No constant with value " + value);
		}

		public String getValue() {
			return value;
		}
	}
}
