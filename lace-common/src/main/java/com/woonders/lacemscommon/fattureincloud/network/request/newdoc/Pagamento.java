package com.woonders.lacemscommon.fattureincloud.network.request.newdoc;

import java.io.Serializable;
import java.time.LocalDate;

import com.google.gson.annotations.SerializedName;

import lombok.*;

/**
 * Created by Emanuele on 21/02/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Pagamento implements Serializable {

	@SerializedName("data_scadenza")
	public LocalDate dataScadenza;
	/**
	 * Importo del pagamento (se vale 0 la tranche di pagamento non viene
	 * inserita; se vale "auto" e la tranche è una sola viene completato
	 * automaticamente)
	 */
	@SerializedName("importo")
	public String importo;
	/**
	 * Medodo di pagamento = ['not' o 'rev' o il nome del conto] ('not' indica
	 * che non è stato saldato, 'rev' che è stato stornato; se non esiste un
	 * conto con il nome specificato viene creato un nuovo conto in FIC)
	 */
	@SerializedName("metodo")
	public String metodo;
	/**
	 * Data del saldo dell'importo indicato [Obbligatorio se metodo!="not"]
	 */
	@SerializedName("data_saldo")
	public LocalDate dataSaldo;

	public enum Tipo {
		STRIPE("Stripe");

		private String value;

		Tipo(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
