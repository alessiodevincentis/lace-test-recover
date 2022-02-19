package com.woonders.lacemscommon.fattureincloud.network.request.newdoc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.woonders.lacemscommon.fattureincloud.network.request.BaseRequestBody;

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
public class NewDocRequestBody extends BaseRequestBody {

	/**
	 * Identificativo univoco del cliente (serve per creare un collegamento tra
	 * il documento e un cliente in anagrafica; se nullo, il documento non viene
	 * collegato a nessun cliente già esistente in anagrafica; se mancante,
	 * viene fatto il collegamento con piva o cf) [solo con tipo!="ordforn"]
	 */
	@SerializedName("id_cliente")
	public String idCliente;
	/**
	 * Identificativo univoco del fornitore (serve per creare un collegamento
	 * tra il documento e un fornitore in anagrafica; se nullo, il documento non
	 * viene collegato a nessun fornitore già esistente in anagrafica; se
	 * mancante, viene fatto il collegamento con piva o cf) [solo con
	 * tipo="ordforn"]
	 */
	@SerializedName("id_fornitore")
	public String idFornitore;
	/**
	 * Nome o ragione sociale del cliente/fornitore
	 */
	@SerializedName("nome")
	public String nome;
	@SerializedName("indirizzo_via")
	public String indirizzoVia;
	@SerializedName("indirizzo_cap")
	public String indirizzoCap;
	@SerializedName("indirizzo_citta")
	public String indirizzoCitta;
	@SerializedName("indirizzo_provincia")
	public String indirizzoProvincia;
	@SerializedName("indirizzo_extra")
	public String indirizzoExtra;
	@SerializedName("paese")
	public String paese;
	/**
	 * [Solo se "paese" non è valorizzato] Codice ISO del paese (nazionalità)
	 * del cliente/fornitore (formato ISO alpha-2) in alternativa al parametro
	 * "paese"
	 */
	@SerializedName("paese_iso")
	public String paeseIso;
	/**
	 * Lingua del documento (sigla) = ['it' o 'en' o 'de']
	 */
	@SerializedName("lingua")
	public String lingua;
	/**
	 * Partita IVA cliente/fornitore; viene utilizzata per ricercare e collegare
	 * il cliente/fornitore in anagrafica se non specificato il parametro
	 * id_cliente/id_fornitore (in caso di doppioni viene collegato solo un
	 * soggetto)
	 */
	@SerializedName("piva")
	public String piva;
	/**
	 * Codice fiscale cliente/fornitore; viene utilizzato per ricercare e
	 * collegare il cliente/fornitore in anagrafica se non specificati i
	 * parametri id_cliente/id_fornitore e piva (in caso di doppioni viene
	 * collegato solo un soggetto)
	 */
	@SerializedName("cf")
	public String cf;
	/**
	 * Se "true", completa i dati anagrafici della fattura con quelli presenti
	 * nell'anagrafica cliente/fornitore (sovrascrivendo quelli presenti),
	 * effettuando la ricerca tramite i campi id_cliente/id_fornitore, piva e cf
	 * (in quest'ordine)
	 */
	@SerializedName("autocompila_anagrafica")
	public boolean autocompilaAnagrafica;
	/**
	 * Se "true", aggiorna l'anagrafica clienti/fornitori con i dati anagrafici
	 * della fattura: se il cliente/fornitore non esiste viene creato, mentre se
	 * il cliente/fornitore esiste già i dati vengono aggiornati; il
	 * cliente/fornitore viene ricercato tramite i campi
	 * id_cliente/id_fornitore, piva e cf (in quest'ordine)
	 */
	@SerializedName("salva_anagrafica")
	public boolean salvaAnagrafica;
	/**
	 * Numero (e serie) del documento; se mancante viene utilizzato il
	 * successivo proposto per la serie principale; se viene specificata solo la
	 * serie (stringa che inizia con un carattere non numerico) viene utilizzato
	 * il successivo per quella serie; per i ddt agisce anche sul campo
	 * "ddt_numero" e non accetta una serie
	 */
	@SerializedName("numero")
	public String numero;
	/**
	 * Data di emissione del documento (per i ddt agisce anche sul campo
	 * "ddt_data")
	 */
	@SerializedName("data")
	public Date data;
	@SerializedName("valuta")
	public String valuta;
	/**
	 * Tasso di cambio EUR/{valuta} [Se non specificato viene utilizzato il
	 * tasso di cambio odierno]
	 */
	@SerializedName("valuta_cambio")
	public BigDecimal valutaCambio;
	/**
	 * Specifica se i prezzi da utilizzare per il calcolo del totale documento
	 * sono quelli netti, oppure quello lordi (comprensivi di iva)
	 */
	@SerializedName("prezzi_ivati")
	public boolean prezziIvati;
	/**
	 * [Non presente in ddt e ordforn] Percentuale rivalsa INPS
	 */
	@SerializedName("rivalsa")
	public BigDecimal rivalsa;
	/**
	 * [Non presente in ddt e ordforn] Percentuale cassa previdenziale
	 */
	@SerializedName("cassa")
	public BigDecimal percentualeCassaPrevidenziale;
	/**
	 * [Non presente in ddt e ordforn] Percentuale ritenuta d'acconto
	 */
	@SerializedName("rit_acconto")
	public BigDecimal percentualeRitenutaAcconto;
	/**
	 * [Non presente in ddt e ordforn] Imponibile della ritenuta d'acconto
	 * (percentuale sul totale)
	 */
	@SerializedName("imponibile_ritenuta")
	public BigDecimal percentualeImponibileRitenuta;
	/**
	 * [Non presente in ddt e ordforn] Percentuale altra ritenuta (ritenuta
	 * previdenziale, Enasarco etc.)
	 */
	@SerializedName("rit_altra")
	public BigDecimal percentualeAltrRitenuta;
	/**
	 * [Non presente in ddt e ordforn] Valore della marca da bollo (0 se non
	 * presente)
	 */
	@SerializedName("marca_bollo")
	public BigDecimal marcaBollo;
	/**
	 * [Non presente in ddt] Oggetto mostrato sul documento (precedentemente
	 * "oggetto_fattura")
	 */
	@SerializedName("oggetto_visibile")
	public String oggettoVisibile;
	/**
	 * [Non presente in ddt] Oggetto (per organizzazione interna)
	 */
	@SerializedName("oggetto_interno")
	public String oggettoInterno;
	/**
	 * [Non presente in ddt e ordforn] Centro ricavo
	 */
	@SerializedName("centro_ricavo")
	public String centroRicavo;
	/**
	 * [Solo in ordforn] Centro di costo
	 */
	@SerializedName("centro_costo")
	public String centroCosto;
	/**
	 * [Non presente in ddt] Note (in formato HTML)
	 */
	@SerializedName("note")
	public String note;
	/**
	 * [Non presente in ddt] Nasconde o mostra la scadenza sul documento
	 */
	@SerializedName("nascondi_scadenza")
	public boolean nascondiScadenza;
	/**
	 * [Non presente in ndc e ordforn] Indica la presenza di un DDT incluso nel
	 * documento (per i ddt è sempre true)
	 */
	@SerializedName("ddt")
	public boolean existDDT;
	/**
	 * [Solo se tipo=fatture] Indica la presenza di una fattura accompagnatoria
	 * inclusa nel documento
	 */
	@SerializedName("ftacc")
	public boolean existFatturaAccompagnatoria;
	/**
	 * [Solo se tipo!=ddt] Identificativo del template del documento [Se non
	 * specificato o inesistente, viene utilizzato quello di default]
	 */
	@SerializedName("id_template")
	public String idTemplate;
	/**
	 * [Solo se ddt=true] Identificativo del template del ddt [Se non
	 * specificato o inesistente, viene utilizzato quello di default]
	 */
	@SerializedName("ddt_id_template")
	public String ddtIdTemplate;
	/**
	 * [Solo se ftacc=true] Identificativo del template della fattura
	 * accompagnatoria [Se non specificato o inesistente, viene utilizzato
	 * quello di default]
	 */
	@SerializedName("ftacc_id_template")
	public String ftaccIdTemplate;
	/**
	 * [Non presente in ddt e ndc] Mostra o meno le informazioni sul metodo di
	 * pagamento sul documento
	 */
	@SerializedName("mostra_info_pagamento")
	public boolean mostraInfoPagamento;
	/**
	 * [Solo se mostra_info_pagamento=true] Nome del metodo di pagamento
	 * metodo_pagamento
	 */
	@SerializedName("metodo_pagamento")
	public String metodoPagamento;
	/**
	 * [Solo se mostra_info_pagamento=true] Titolo della riga N del metodo di
	 * pagamento (N da 1 a 5)
	 */
	@SerializedName("metodo_titoloN")
	public String metodoTitoloN;
	/**
	 * [Solo se mostra_info_pagamento=true] Descrizione della riga N del metodo
	 * di pagamento (N da 1 a 5)
	 */
	@SerializedName("metodo_descN")
	public String metodoDescN;
	/**
	 * [Solo per preventivi, rapporti e ordforn] Nasconde o mostra la scadenza
	 * sul documento = ['tutti' o 'netto' o 'nessuno']
	 */
	@SerializedName("mostra_totali")
	public String mostraTotali;
	/**
	 * [Solo per ricevute, fatture, proforma, ordini] Mostra il bottone "Paga
	 * con Paypal"
	 */
	@SerializedName("mostra_bottone_paypal")
	public boolean mostraBottonePaypal;
	/**
	 * [Solo per ricevute, fatture, proforma, ordini] Mostra il bottone "Paga
	 * con Bonifico Immediato"
	 */
	@SerializedName("mostra_bottone_bonifico")
	public boolean mostraBottoneBonifico;
	/**
	 * [Solo per ricevute, fatture, proforma, ordini] Mostra il bottone
	 * "Notifica pagamento effettuato"
	 */
	@SerializedName("mostra_bottone_notifica")
	public boolean mostraBottoneNotifica;
	/**
	 * Lista degli articoli/righe del documento
	 */
	@SerializedName("lista_articoli")
	public List<Articolo> listaArticoli;
	/**
	 * [Non presente in preventivi, ddt e ordforn] Lista delle tranches di
	 * pagamento
	 */
	@SerializedName("lista_pagamenti")
	public List<Pagamento> listaPagamenti;
	/**
	 * [Se ddt=true] Numero del ddt (se tipo="ddt" corrisponde al campo
	 * "numero") [Se non specificato viene autocompletato]
	 */
	@SerializedName("ddt_numero")
	public String ddtNumero;
	/**
	 * [Se ddt=true] Data del ddt Numero del ddt [Obbligatoria solo se e solo se
	 * il documento non è un ddt ma ddt=true]
	 */
	@SerializedName("ddt_data")
	public Date ddtData;
	/**
	 * [Se ddt/ftacc=true] Numero di colli specificato nel ddt
	 */
	@SerializedName("ddt_colli")
	public String ddtColli;
	/**
	 * [Se ddt/ftacc=true] Peso specificato nel ddt
	 */
	@SerializedName("ddt_peso")
	public String ddtPeso;
	/**
	 * [Se ddt/ftacc=true] Causale specificata nel ddt
	 */
	@SerializedName("ddt_causale")
	public String ddtCausale;
	/**
	 * [Se ddt/ftacc=true] Luogo di spedizione specificato nel ddt
	 */
	@SerializedName("ddt_luogo")
	public String ddtLuogo;
	/**
	 * [Se ddt/ftacc=true] Dati trasportatore specificati nel ddt
	 */
	@SerializedName("ddt_trasportatore")
	public String ddtTrasportatore;
	/**
	 * [Se ddt/ftacc=true] Annotazioni specificate nel ddt
	 */
	@SerializedName("ddt_annotazioni")
	public String ddtAnnotazioni;
	/**
	 * [Solo per fatture e ndc elettroniche a PA] Indica se il documento è nel
	 * formato FatturaPA; se "true", vengono presi in considerazione tutti i
	 * successivi campi con prefisso "PA_", con eventuali eccezioni (se non
	 * valorizzati, vengono utilizzati i valori di default)
	 */
	@SerializedName("PA")
	public boolean pA;
	/**
	 * [Solo se PA=true] Indica la tipologia del cliente: Pubblica
	 * Amministrazione ("PA") oppure privato ("B2B") = ['PA' o 'B2B']
	 */
	@SerializedName("PA_tipo_cliente")
	public String pATipoCliente;
	/**
	 * [Solo se PA=true] Tipo di documento a cui fa seguito la fattura/ndc in
	 * questione = ['ordine' o 'convenzione' o 'contratto' o 'nessuno']
	 */
	@SerializedName("PA_tipo")
	public String pATipo;
	/**
	 * [Solo se PA=true] Numero del documento a cui fa seguito la fattura/ndc in
	 * questione
	 */
	@SerializedName("PA_numero")
	public String pANumero;
	/**
	 * [Solo se PA=true] Data del documento a cui fa seguito la fattura/ndc in
	 * questione
	 */
	@SerializedName("PA_data")
	public Date pAData;
	/**
	 * [Solo se PA_tipo_cliente=PA] Codice Unitario Progetto
	 */
	@SerializedName("PA_cup")
	public String pACodiceUnitarioProgetto;
	@SerializedName("PA_cig")
	/**
	 * [Solo se PA_tipo_cliente=PA] Codice Identificativo della Gara
	 */
	public String paCodiceIdentificativoGara;
	/**
	 * [Solo se PA=true] Codice Ufficio della Pubblica Amministrazione o Codice
	 * Destinatario
	 */
	@SerializedName("PA_codice")
	public String pACodice;
	/**
	 * [Solo se PA_tipo_cliente=B2B] Indirizzo PEC del destinatario, utilizzato
	 * in assenza di Codice Destinatario
	 */
	@SerializedName("PA_pec")
	public String pAPec;
	/**
	 * [Solo se PA=true] Esigibilità IVA e modalità di versamento (I=immediata,
	 * D=differita, S=split payment, N=non specificata) = ['I' o 'D' o 'S' o
	 * 'N']
	 */
	@SerializedName("PA_esigibilita")
	public String pAEsigibilita;
	/**
	 * [Solo se PA=true] Modalità di pagamento (vedi tabella codifiche sulla
	 * documentazione ufficiale)
	 */
	@SerializedName("PA_modalita_pagamento")
	public String pAModalitaPagamento;
	/**
	 * [Solo se PA=true] Nome dell'istituto di credito
	 */
	@SerializedName("PA_istituto_credito")
	public String pAIstitutoCredito;
	/**
	 * [Solo se PA=true] Codice IBAN del conto corrente del beneficiario
	 */
	@SerializedName("PA_iban")
	public String pAIban;
	/**
	 * [Solo se PA=true] Beneficiario del pagamento
	 */
	@SerializedName("PA_beneficiario")
	public String pABeneficiario;
	/**
	 * Informazioni anagrafiche aggiuntive da associare al cliente/fornitore
	 * [solo se salva_anagrafica=true]
	 */
	@SerializedName("extra_anagrafica")
	public ExtraAnagrafica extraAnagrafica;
}
