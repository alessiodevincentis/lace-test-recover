package enumPdf;

public enum NameFieldsPdfFattura {

	FATTURA_NUMERO("fattura_numero"), DATA_FATTURA("data_fattura"), PIVA("piva"), NOME_CLIENTE(
			"nome_cliente"), INDIRIZZO_CLIENTE("indirizzo_cliente"), CF("cf"), TELEFONO(
					"telefono"), FAX("fax"), DESCRIZIONE("descrizione"), QUANTITA(
							"quantita"), PREZZO_UNITARIO("prezzo_unitario"), IMPORTO(
									"importo"), IMPONIBILE("imponibile"), IVA("iva"), TOTALE_FATTURA("totale_fattura");

	private final String fieldName;

	private NameFieldsPdfFattura(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
