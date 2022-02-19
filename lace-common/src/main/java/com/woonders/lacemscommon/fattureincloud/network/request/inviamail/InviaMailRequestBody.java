package com.woonders.lacemscommon.fattureincloud.network.request.inviamail;

import com.google.gson.annotations.SerializedName;
import com.woonders.lacemscommon.fattureincloud.network.request.BaseRequestBody;

import lombok.*;

/**
 * Created by Emanuele on 23/02/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InviaMailRequestBody extends BaseRequestBody {

	@SerializedName("id")
	public String id;
	@SerializedName("token")
	public String token;
	/**
	 * Indirizzo e-mail del mittente da utilizzare per l'invio (deve essere
	 * presente e configurato in Fatture In Cloud)
	 */
	@SerializedName("mail_mittente")
	public String mailMittente;
	@SerializedName("mail_destinatario")
	public String mailDestinatario;
	@SerializedName("oggetto")
	public String oggetto;
	/**
	 * Corpo del messaggio da inviare (in HTML); dimesione massima: 10kB; il
	 * campo {{allegati}} è da lasciare dove si vuole che vengano inseriti i
	 * bottoni di download dei documenti
	 */
	@SerializedName("messaggio")
	public String messaggio;
	/**
	 * Se "true", include nel campo {allegati} un tasto che reindirizza al file
	 * PDF del documento principale
	 */
	@SerializedName("includi_documento")
	public boolean includiDocumento;
	/**
	 * Se "true", include nel campo {allegati} un tasto che reindirizza al file
	 * PDF del ddt invia_ddt
	 */
	@SerializedName("invia_ddt")
	public boolean inviaDdt;
	/**
	 * Se "true", include nel campo {allegati} un tasto che reindirizza alla
	 * fattura accompagnatoria allegata
	 */
	@SerializedName("invia_fa")
	public boolean inviaFa;
	/**
	 * Se "true", include nel campo {allegati} un tasto che reindirizza al file
	 * PDF del ddt
	 */
	@SerializedName("includi_allegato")
	public boolean includiAllegato;
	/**
	 * Se "true", inserisce in cc il proprio indirizzo (associato all'account di
	 * Fatture in Cloud)
	 */
	@SerializedName("invia_copia")
	public boolean inviaCopia;
	/**
	 * Se "true", inserisce i documenti selezionati come allegati fisici
	 * (rinunciando al tracking completo dell'e-mail)
	 */
	@SerializedName("allega_pdf")
	public boolean allegaPdf;
}
