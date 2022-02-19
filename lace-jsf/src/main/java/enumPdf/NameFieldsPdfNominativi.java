package enumPdf;

public enum NameFieldsPdfNominativi {
	
	//ANAGARFICA
	DATA_CARICAMENTO("Text1"),
	OPERATORE("Text2"),
	STATO_NOMINATIVO("Text3"),
	PROVENIENZA("Text5"),
	PROVENIENZA_DESC("Text6"),
	NOTE("Text7"),
	COGNOME("Text8"),
	NOME("Text9"),
	TELEFONO("Text10"),
	IMPIEGO("Text11"),
	DATA_NASCITA("Text12"),
	ETA("Text13"),
	LUOGO_NASCITA("Text14"),
	MAIL("Text15"),
	NETTO_STIPENDIO("Text16"),
	DATA_RECALL("Text17"),
	
	//IMPEGNI
	TIPO_TRATTENUTA_1("Text18"),
	RATA_TRATTENUTA_1("Text19"),
	DURATA_TRATTENUTA_1("Text20"),
	DATA_RINNOVO_TRATTENUTA_1("Text21"),
	
	TIPO_TRATTENUTA_2("Text22"),
	RATA_TRATTENUTA_2("Text23"),
	DURATA_TRATTENUTA_2("Text24"),
	DATA_RINNOVO_TRATTENUTA_2("Text25"),	
	
	
	TIPO_TRATTENUTA_3("Text26"),
	RATA_TRATTENUTA_3("Text27"),
	DURATA_TRATTENUTA_3("Text28"),
	DATA_RINNOVO_TRATTENUTA_3("Text29"),
	
	
	//PREVENTIVO
	TIPO_PREVENTIVO_1("Text30"),
	RATA_PREVENTIVO_1("Text31"),
	DURATA_PREVENTIVO_1("Text32"),
	IMPORTO_PREVENTIVO_1("Text33"),
	
	TIPO_PREVENTIVO_2("Text34"),
	RATA_PREVENTIVO_2("Text35"),
	DURATA_PREVENTIVO_2("Text36"),
	IMPORTO_PREVENTIVO_2("Text37"),	
	
	TIPO_PREVENTIVO_3("Text38"),
	RATA_PREVENTIVO_3("Text39"),
	DURATA_PREVENTIVO_3("Text40"),
	IMPORTO_PREVENTIVO_3("Text41");	

	private final String fieldName;

	private NameFieldsPdfNominativi(String fieldName) {
		this.fieldName = fieldName;
	}  
	
	public String getFieldName() {
		return fieldName;
	}
	
}
