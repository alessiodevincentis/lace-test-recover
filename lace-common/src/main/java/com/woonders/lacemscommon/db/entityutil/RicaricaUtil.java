package com.woonders.lacemscommon.db.entityutil;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.RicaricaComunicazioneViewModel;

@Component
public class RicaricaUtil {

	public static final String NAME = "ricaricaUtil";
	public static final String JSF_NAME = "#{" + NAME + "}";
	public static BigDecimal IVA = new BigDecimal(22);

	public BigDecimal calcPrezzoUnitario(final RicaricaComunicazioneViewModel ricaricaComunicazioneViewModel) {
		if (ricaricaComunicazioneViewModel != null) {
			return calcPrezzoUnitario(ricaricaComunicazioneViewModel.getAmount(),
					ricaricaComunicazioneViewModel.getQuantity());
		}
		return BigDecimal.ZERO;
	}

	private BigDecimal calcPrezzoUnitario(final BigDecimal totalAmmount, int quantity) {
		if (totalAmmount != null && quantity != 0) {
			BigDecimal prezzoUnitario = totalAmmount.divide(new BigDecimal(quantity), 4, RoundingMode.HALF_DOWN);
			return prezzoUnitario;
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal calcPrezzoUnitarioIvaEsclusa(
			final RicaricaComunicazioneViewModel ricaricaComunicazioneViewModel) {
		if (ricaricaComunicazioneViewModel != null) {
			return calcPrezzoUnitario(calcImportoIvaEsclusa(ricaricaComunicazioneViewModel.getAmount()),
					ricaricaComunicazioneViewModel.getQuantity());
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal calcImportoIvaEsclusa(final RicaricaComunicazioneViewModel ricaricaComunicazioneViewModel) {
		if (ricaricaComunicazioneViewModel != null) {
			return calcImportoIvaEsclusa(ricaricaComunicazioneViewModel.getAmount());
		}
		return BigDecimal.ZERO;
	}

	private BigDecimal calcImportoIvaEsclusa(final BigDecimal importoIvato) {
		if (importoIvato != null) {
			BigDecimal importoIvaEsclusa = importoIvato.multiply(new BigDecimal(100))
					.divide((new BigDecimal(100).add(IVA)), 2, RoundingMode.HALF_UP);
			return importoIvaEsclusa;
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal calcImpostaIva(final RicaricaComunicazioneViewModel ricaricaComunicazioneViewModel) {
		if (ricaricaComunicazioneViewModel != null) {
			return calcImpostaIva(ricaricaComunicazioneViewModel.getAmount());
		}
		return BigDecimal.ZERO;
	}

	private BigDecimal calcImpostaIva(final BigDecimal importo) {
		if (importo != null) {
			BigDecimal impostaIva = calcImportoIvaEsclusa(importo)
					.multiply(IVA.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
			return impostaIva;
		}
		return BigDecimal.ZERO;
	}

}
