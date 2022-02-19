package com.woonders.lacemscommon.fattureincloud;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.woonders.lacemscommon.fattureincloud.network.request.BaseRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.request.dettaglidoc.DettagliDocRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.request.eliminadoc.EliminaDocRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.request.inviamail.InviaMailRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.request.newdoc.NewDocRequestBody;
import com.woonders.lacemscommon.fattureincloud.network.response.dettaglidoc.DettagliDocResponse;
import com.woonders.lacemscommon.fattureincloud.network.response.eliminadoc.EliminaDocResponse;
import com.woonders.lacemscommon.fattureincloud.network.response.inviamail.InviaMailResponse;
import com.woonders.lacemscommon.fattureincloud.network.response.newdoc.NewDocResponse;
import com.woonders.lacemscommon.network.BaseClient;
import com.woonders.lacemscommon.network.typeadapter.ClickSendLocalDateTimeTypeAdapter;
import com.woonders.lacemscommon.network.typeadapter.ItalianPatternLocalDateTypeAdapter;

/**
 * Created by Emanuele on 21/02/2017. https://secure.fattureincloud.it/api
 */
@Component
public class FattureInCloudClient extends BaseClient {

	private static final String API_UID = "";
	private static final String API_KEY = "";
	private static final String API_UID_NAME = "api_uid";
	private static final String API_KEY_NAME = "api_key";
	private static final String FATTURE_IN_CLOUD_BASE_URL = "https://api.fattureincloud.it/v1";
	private static final String FATTURE_URL = FATTURE_IN_CLOUD_BASE_URL + "/" + TipoDocumento.FATTURE.getValue();
	private static final String NEW_FATTURE_URL = FATTURE_URL + "/" + "nuovo";
	private static final String INVIA_FATTURA_URL = FATTURE_URL + "/" + "inviamail";
	private static final String ELIMINA_FATTURA_URL = FATTURE_URL + "/" + "elimina";
	private static final String DETTAGLI_FATTURA_URL = FATTURE_URL + "/" + "dettagli";

	@Override
	protected void addAuthorization(HttpRequest httpRequest) {
		// non deve fare niente, per fatture in cloud apiKey e apiUid vanno
		// messi direttamente nell'oggetto della request, non possiamo farlo a
		// questo punto della generazione della request
	}

	@PostConstruct
	public void init() {
		// TODO UN GIORNO, QUANDO COMMON NON ESISTERA PIU, QUESTO CODICE VA
		// MESSO IN UNA CLASSE DI CONFIGURAZIONE DI SPRING!!!
		Unirest.setObjectMapper(new ObjectMapper() {
			private Gson fattureInCloudGson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new ItalianPatternLocalDateTypeAdapter()).create();
			private Gson clickSendGson = new GsonBuilder()
					.registerTypeAdapter(LocalDateTime.class, new ClickSendLocalDateTimeTypeAdapter()).create();

			@Override
			public <T> T readValue(String value, Class<T> valueType) {
				if (value != null) {
					if (valueType.toString().contains("fatture")) {
						return fattureInCloudGson.fromJson(value, valueType);
					} else {
						return clickSendGson.fromJson(value, valueType);
					}
				}
				return null;
			}

			@Override
			public String writeValue(Object value) {
				if (value != null) {
					if (value.getClass().getPackage().toString().contains("fatture")) {
						return fattureInCloudGson.toJson(value);
					} else {
						return clickSendGson.toJson(value);
					}
				}
				return null;
			}
		});

	}

	private BaseRequestBody addAuthorizationToBody(BaseRequestBody baseRequestBody) {
		baseRequestBody.setApiKey(API_KEY);
		baseRequestBody.setApiUid(API_UID);
		return baseRequestBody;
	}

	public NewDocResponse createNewDoc(NewDocRequestBody newDocRequestBody) throws UnirestException {
		return createPostRequest(NEW_FATTURE_URL, addAuthorizationToBody(newDocRequestBody))
				.asObject(NewDocResponse.class).getBody();
	}

	public InviaMailResponse inviaMailFattura(InviaMailRequestBody inviaMailRequestBody) throws UnirestException {
		return createPostRequest(INVIA_FATTURA_URL, addAuthorizationToBody(inviaMailRequestBody))
				.asObject(InviaMailResponse.class).getBody();
	}

	public EliminaDocResponse eliminaFattura(EliminaDocRequestBody eliminaDocRequestBody) throws UnirestException {
		return createPostRequest(ELIMINA_FATTURA_URL, addAuthorizationToBody(eliminaDocRequestBody))
				.asObject(EliminaDocResponse.class).getBody();
	}

	public DettagliDocResponse getDettagliFattura(DettagliDocRequestBody dettagliDocRequestBody)
			throws UnirestException {
		return createPostRequest(DETTAGLI_FATTURA_URL, addAuthorizationToBody(dettagliDocRequestBody))
				.asObject(DettagliDocResponse.class).getBody();
	}

	private enum TipoDocumento {
		FATTURE("fatture");

		private String value;

		TipoDocumento(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	private enum FattureInCloudErrorCode {
		ERRORE_GENERICO(0), AUTENTICAZIONE_FALLITA(1000), PARAMETRO_MANCANTE_O_INVALIDO(
				1001), AZIENDA_INESISTENTE_O_DISABILITATA(1004), CONTENUTO_RICHIESTA_NON_CORRETTO(
						1100), LICENZA_SCADUTA(2000), LIMITE_RICHIESTE_SUPERATO(2002), API_BLOCCATE(
								2004), FUNZIONE_NON_CONSENTITA(2005), ACCESSO_NEGATO(
										2006), LIMITE_MAX_DOCUMENTI(4001), TOTALE_CALCOLATO_NON_CORRISPONDENTE(5000);

		private int value;

		FattureInCloudErrorCode(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
