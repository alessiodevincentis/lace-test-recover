package enumPdf.italcredi;

public enum PagesAllegatiCertificati {

	DELEGA_RICHIESTA_CERTIFICATI(10),
	CERTIFICATO_STIPENDIO(11);
	
	private final Integer page;

	private PagesAllegatiCertificati(Integer page) {
		this.page = page;
	}  
	
	public static Integer[] allValues() {
		PagesAllegatiCertificati[] allegati = values();
	    Integer[] val = new Integer[allegati.length];

	    for (int i = 0; i < allegati.length; i++) {
	        val[i] = allegati[i].page;
	    }

	    return val;
	}
	
	public Integer getPage() {
		return page;
	}

}
