package com.woonders.lacemscommon.fattureincloud.network.request.newdoc;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class Articolo implements Serializable {

	/**
	 * Identificativo del prodotto (se nullo o mancante, la registrazione non
	 * viene collegata a nessun prodotto presente nell'elenco prodotti)
	 */
	@SerializedName("id")
	public String id;
	@SerializedName("codice")
	public String codice;
	@SerializedName("nome")
	public String nome;
	@SerializedName("um")
	public String unitaDiMisura;
	@SerializedName("quantita")
	public BigDecimal quantita;
	@SerializedName("descrizione")
	public String descrizione;
	@SerializedName("categoria")
	public String categoria;
	/**
	 * Prezzo unitario netto (IVA esclusa) [Obbligatorio se prezzi_netti!=true]
	 */
	@SerializedName("prezzo_netto")
	public BigDecimal prezzoNetto;
	/**
	 * Prezzo unitario lordo (comprensivo di IVA) [Obbligatorio se
	 * prezzi_ivati=true]
	 */
	@SerializedName("prezzo_lordo")
	public BigDecimal prezzoLordo;
	/**
	 * Codice aliquota IVA (ottenibili con il parametro "lista_iva" della
	 * funzione "/info/account")
	 */
	@SerializedName("cod_iva")
	public int codIva;
	@SerializedName("tassabile")
	public boolean tassabile;
	/**
	 * Sconto (percentuale)
	 */
	@SerializedName("sconto")
	public BigDecimal sconto;
	/**
	 * Indica se a questo articolo vengono applicate ritenute e contributi
	 */
	@SerializedName("applica_ra_contributi")
	public boolean applicaRaContributi;
	/**
	 * Ordine dell'articolo nel documento (ordinamento ascendente da 0 in poi)
	 */
	@SerializedName("ordine")
	public int ordine;
	/**
	 * Se vale 1, evidenzia in rosso l'eventuale sconto in fattura = ['0' o '1']
	 */
	@SerializedName("sconto_rosso")
	public int scontoRosso;
	/**
	 * Indica se l'articolo è incluso nel ddt (se presente un ddt allegato,
	 * altrimenti non è significativo)
	 */
	@SerializedName("in_ddt")
	public boolean inDdt;
	/**
	 * Indica se viene movimentato il magazzino (true: viene movimentato; false:
	 * non viene movimentato) [Non influente se il prodotto non è collegato
	 * all'elenco prodotti, oppure la funzionalità magazzino è disattivata] [Se
	 * tipo=ndc viene forzato a false]
	 */
	@SerializedName("magazzino")
	public boolean magazzino;

	public enum TipoArticolo {
		SMS1V1(1, 2000, 197, "0.1015"), SMS2V1(2, 5000, 511, "0.0978"), SMS3V1(3, 10000, 1066, "0.0938"), SMS4V1(4,
				25000, 2744, "0.0911"), SMS5V1(5, 50000, 5894, "0.0848");

		private static final String PACCHETTO_SMS = "Pacchetto SMS";
		private int id;
		private int amount;
		private int smsQuantity;
		private String smsUnitCost;
		private BigDecimal prezzoLordo;

		TipoArticolo(int id, int amount, int smsQuantity, String smsUnitCost) {
			this.id = id;
			this.amount = amount;
			this.smsQuantity = smsQuantity;
			this.smsUnitCost = smsUnitCost;
			this.prezzoLordo = new BigDecimal(amount / 100);
		}

		public String getName() {
			return PACCHETTO_SMS + " " + id;
		}

		public String getDescription() {
			return getName() + ": " + getSmsQuantity() + " SMS";
		}

		public int getAmount() {
			return amount;
		}

		public int getSmsQuantity() {
			return smsQuantity;
		}

		public String getSmsUnitCost() {
			return smsUnitCost;
		}

		public BigDecimal getPrezzoLordo() {
			return prezzoLordo;
		}

		public String getAmountToShowInEur() {
			return String.valueOf(amount / 100);
		}

	}
}
