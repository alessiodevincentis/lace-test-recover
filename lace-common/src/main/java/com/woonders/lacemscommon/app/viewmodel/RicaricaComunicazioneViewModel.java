package com.woonders.lacemscommon.app.viewmodel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.woonders.lacemscommon.db.entity.RicaricaComunicazione;
import com.woonders.lacemscommon.db.entityenum.RicaricaType;

import lombok.*;

/**
 * Created by Emanuele on 16/01/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString
public class RicaricaComunicazioneViewModel extends AbstractBaseViewModel {

	private long id;
	private LocalDateTime dateTime;
	private OperatorViewModel payer;
	private BigDecimal amount;
	private RicaricaComunicazione.PaymentMethod paymentMethod;
	private int quantity;
	private RicaricaType ricaricaType;
	private String fatturaId;
	private OperatorViewModel operatoreRicaricato;

	@Override
	protected long getIdToCompare() {
		return id;
	}
}
