package com.woonders.lacemscommon.util;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.db.entity.Cliente.Sesso;
import com.woonders.lacemscommon.exception.ComuneMancanteException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CfGenerator {

	public static final String NAME = "cfGenerator";
	public static final String JSF_NAME = "#{" + NAME + "}";
	private static final String COMUNI_FILE = "/files/Comuni.txt";
	// Array statici
	private final char[] elencoPari = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private final int[] elencoDispari = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4,
			18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
	private final String[][] mese = { { "JANUARY", "A" }, { "FEBRUARY", "B" }, { "MARCH", "C" }, { "APRIL", "D" },
			{ "MAY", "E" }, { "JUNE", "H" }, { "JULY", "L" }, { "AUGUST", "M" }, { "SEPTEMBER", "P" },
			{ "OCTOBER", "R" }, { "NOVEMBER", "S" }, { "DECEMBER", "T" } };
	private final String[][] codiceMeseNumero = { { "01", "A" }, { "02", "B" }, { "03", "C" }, { "04", "D" },
			{ "05", "E" }, { "06", "H" }, { "07", "L" }, { "08", "M" }, { "09", "P" }, { "10", "R" }, { "11", "S" },
			{ "12", "T" } };
	private Map<String, String> comuneCodiceMap;
	private Map<String, String> codiceComuneMap;
	@Autowired
	private DateConversionUtil dateConversionUtil;

	public CfGenerator() {
		comuneCodiceMap = generaComuneCodiceMap();
		codiceComuneMap = generaCodiceComuneMap();
	}

	// Elabora codice del comune
	// ------
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	private Map<String, String> generaComuneCodiceMap() {
		return generaComuniMap(TipoMappaComuni.COMUNE);
	}

	private Map<String, String> generaCodiceComuneMap() {
		return generaComuniMap(TipoMappaComuni.CODICE);
	}

	private Map<String, String> generaComuniMap(TipoMappaComuni tipoMappaComuni) {
		Map<String, String> comuniMap = new HashMap<>();
		final InputStream in = getClass().getResourceAsStream(COMUNI_FILE);
		try {
			Scanner scanner = new Scanner(in);
			scanner.useDelimiter("\r\n");

			while (scanner.hasNext()) {
				String s1 = scanner.nextLine();
				String nomeComune = s1.substring(0, s1.indexOf('-') - 1);
				String codiceComune = s1.substring(s1.lastIndexOf(' ') + 1);
				if (TipoMappaComuni.CODICE.equals(tipoMappaComuni)) {
					comuniMap.put(codiceComune, nomeComune);
				} else {
					comuniMap.put(nomeComune, codiceComune);
				}
			}

			scanner.close();
		} catch (Exception e) {
			log.error("Errore lettura file dei comuni", e);
		}
		return comuniMap;
	}

	private String getNomeCalc(String nome) {
		return modificaNC(nome, true);
	}

	// Variabili di istanza
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------

	private String getCognomeCalc(String cognome) {
		return modificaNC(cognome, false);
	}

	private String getAnnoCalc(int anno) {
		int modulo = anno % 100;
		if (modulo < 10) {
			return "0" + Integer.toString(modulo);
		} else {
			return Integer.toString(modulo);
		}
	}

	private String getGiornoCalc(String sesso, int giorno) {
		if ("MASCHIO".equals(sesso)) {
			if (giorno < 10) {
				return "0" + Integer.toString(giorno);
			}
			return Integer.toString(giorno);
		} else {
			return Integer.toString(giorno + 40);
		}
	}

	public String getCodiceFiscale(String cognome, String nome, Date dataNascita, String luogoNascita, String sesso)
			throws ComuneMancanteException {

		LocalDate dataNascitaLocaDate = dateConversionUtil.calcLocalDateFromDate(dataNascita);
		if (dataNascitaLocaDate != null) {
			int giorno = dataNascitaLocaDate.getDayOfMonth();
			int anno = dataNascitaLocaDate.getYear();
			String mese = dataNascitaLocaDate.getMonth().name();
			return calcolaCf(cognome, nome, anno, giorno, mese, luogoNascita, sesso);
		}
		return null;
	}

	/**
	 * @param stringa
	 *            Corrisponde al nome/cognome da modificare
	 * @param cod
	 *            Se cod e' true, indica il nome; altrimenti il cognome
	 * @return nuovaStringa Restituisce la stringa modificata
	 */
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String modificaNC(String stringa, boolean cod) {
		String nuovastringa = "";
		stringa = stringa.replaceAll(" ", ""); // Rimuovo eventuali spazi
		stringa = stringa.toLowerCase();

		String consonanti = getConsonanti(stringa); // Ottengo tutte le
													// consonanti e tutte le
													// vocali della stringa
		String vocali = getVocali(stringa);

		// Controlla i possibili casi
		if (consonanti.length() == 3) { // La stringa contiene solo 3
										// consonanti, quindi ho gia' la
										// modifica
			nuovastringa = consonanti;
		}
		// Le consonanti non sono sufficienti, e la stinga e' pi� lunga o
		// uguale a 3 caratteri [aggiungo le vocali mancanti]
		else if ((consonanti.length() < 3) && (stringa.length() >= 3)) {
			nuovastringa = consonanti;
			nuovastringa = aggiungiVocali(nuovastringa, vocali);
		}
		// Le consonanti non sono sufficienti, e la stringa
		// contiene meno di 3 caratteri [aggiungo consonanti e vocali, e le x]
		else if ((consonanti.length() < 3) && (stringa.length() < 3)) {
			nuovastringa = consonanti;
			nuovastringa += vocali;
			nuovastringa = aggiungiX(nuovastringa);
		}
		// Le consonanti sono in eccesso, prendo solo le
		// prime 3 nel caso del cognome; nel caso del nome la 0, 2, 3
		else if (consonanti.length() > 3) {
			// true indica il nome e false il cognome
			if (!cod)
				nuovastringa = consonanti.substring(0, 3);
			else
				nuovastringa = consonanti.charAt(0) + "" + consonanti.charAt(2) + "" + consonanti.charAt(3);
		}

		return nuovastringa;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	// I seguenti metodi svolgono le operazioni specifiche sui dati

	// Aggiunge le X sino a raggiungere una lunghezza complessiva di 3 caratteri
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String aggiungiX(String stringa) {
		while (stringa.length() < 3) {
			stringa += "x";
		}
		return stringa;
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// Aggiunge le vocali alla stringa passata per parametro
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String aggiungiVocali(String stringa, String vocali) {
		int index = 0;
		while (stringa.length() < 3) {
			stringa += vocali.charAt(index);
			index++;
		}
		return stringa;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// Toglie dalla stringa tutte le consonanti
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String getVocali(String stringa) {
		stringa = stringa.replaceAll("[^aeiou]", "");
		return stringa;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// Toglie dalla stringa tutte le vocali
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String getConsonanti(String stringa) {
		stringa = stringa.replaceAll("[aeiou]", "");
		return stringa;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// Restituisce il codice del mese
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String modificaMese(String meseNascita) {
		for (int i = 0; i < mese.length; i++) {
			if (mese[i][0].equalsIgnoreCase(meseNascita))
				return mese[i][1];
		}
		return null;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// Calcolo del Codice di Controllo
	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String calcolaCodice(String cognome, String nome, int anno, int giorno, String mese, String luogoNascita,
			String sesso) {
		String str = getCognomeCalc(cognome).toUpperCase() + getNomeCalc(nome).toUpperCase() + getAnnoCalc(anno)
				+ modificaMese(mese) + getGiornoCalc(sesso, giorno) + comuneCodiceMap.get(luogoNascita.toUpperCase());
		int pari = 0, dispari = 0;

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i); // i-esimo carattere della stringa

			// Il primo carattere e' il numero 1 non 0
			if ((i + 1) % 2 == 0) {
				int index = Arrays.binarySearch(elencoPari, ch);
				pari += (index >= 10) ? index - 10 : index;
			} else {
				int index = Arrays.binarySearch(elencoPari, ch);
				dispari += elencoDispari[index];
			}
		}

		int controllo = (pari + dispari) % 26;
		controllo += 10;

		return elencoPari[controllo] + "";
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	public String calcolaCf(String cognome, String nome, int anno, int giorno, String mese, String luogoNascita,
			String sesso) throws ComuneMancanteException {
		if (!comuneCodiceMap.containsKey(luogoNascita.toUpperCase())) {
			throw new ComuneMancanteException("Comune inesistente");
		}
		return getCognomeCalc(cognome).toUpperCase() + getNomeCalc(nome).toUpperCase() + getAnnoCalc(anno)
				+ modificaMese(mese) + getGiornoCalc(sesso, giorno) + comuneCodiceMap.get(luogoNascita.toUpperCase())
				+ calcolaCodice(cognome, nome, anno, giorno, mese, luogoNascita, sesso);
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// Calcolo inverso cf
	public String elaboraComuneDaCodiceComune(String cf) {
		String codiceComune;
		if (cf != null && cf.length() == 16) {
			codiceComune = cf.substring(11, 15);
		} else {
			return null;
		}
		return codiceComuneMap.get(codiceComune.toUpperCase());
	}

	public Date elaboraDataNascitaDaCf(String cf) {
		if (cf != null && cf.length() == 16) {
			String codiceAnno = cf.substring(6, 8);
			String codiceMese = cf.substring(8, 9);
			String codiceGiorno = cf.substring(9, 11);
			String dateToParse = "";

			try {
				Integer giornoFemmina = Integer.parseInt(codiceGiorno) - 40;
				if (giornoFemmina > 0) {
					dateToParse = dateToParse.concat(Integer.toString(giornoFemmina)).concat("/");
				} else {
					dateToParse = dateToParse.concat(codiceGiorno).concat("/");
				}
			} catch (NumberFormatException e) {
				return null;
			}

			for (int i = 0; i < codiceMeseNumero.length; i++) {
				if (codiceMeseNumero[i][1].equalsIgnoreCase(codiceMese)) {
					dateToParse = dateToParse.concat(codiceMeseNumero[i][0]).concat("/");
					break;
				}
			}
			dateToParse = dateToParse.concat(codiceAnno);

			try {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
				Date dateBase = format.parse(dateToParse);
				format.applyPattern("dd/MM/yyyy");
				String dateString = format.format(dateBase);
				Date dataNascita = format.parse(dateString);
				return dataNascita;
			} catch (ParseException e) {
				return null;
			}

		} else {
			return null;
		}
	}

	public Sesso elaboraSessoDaCf(String cf) {
		if (cf != null && cf.length() == 16) {
			try {
				if (Integer.parseInt(cf.substring(9, 11)) - 40 < 0) {
					return Sesso.MASCHIO;
				} else {
					return Sesso.FEMMINA;
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	private enum TipoMappaComuni {
		CODICE, COMUNE
	}

}