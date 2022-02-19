package enumPdf.italcredi;

public enum PagesPrefinEstratti {
	RICHIESTA_ESTRATTI_PAG1(23),
	PAGINA_VUOTA_ESTRATTI_1(24),
	RICHIESTA_ESTRATTI_PAG2(25),
	PAGINA_VUOTA_ESTRATTI_2(26),
	RICHIESTA_ESTRATTI_PAG3(27),
	PAGINA_VUOTA_ESTRATTI_3(28);
	
	private final Integer page;

	private PagesPrefinEstratti(Integer page) {
		this.page = page;
	}  
	
	public static Integer[] allValues() {
		PagesPrefinEstratti[] prefin = values();
	    Integer[] val = new Integer[prefin.length];

	    for (int i = 0; i < prefin.length; i++) {
	        val[i] = prefin[i].page;
	    }

	    return val;
	}
	
	public Integer getPage() {
		return page;
	}

}
