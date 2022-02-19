package enumPdf.italcredi;

public enum PagesDelegaCreditoNet {

	// Autorizzazione per la compilazione del modulo credito net C
	DELEGA_CREDITO_NET(12);

	private final Integer page;

	private PagesDelegaCreditoNet(Integer page) {
		this.page = page;
	}

	public static Integer[] allValues() {
		PagesDelegaCreditoNet[] allegati = values();
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
