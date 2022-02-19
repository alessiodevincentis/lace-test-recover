package enumPdf.italcredi;

public enum PagesDelegaAllegatoB {

	// Autorizzazione per la compilazione del modulo allegato B
	DELEGA_ALLEGATO_B(13);

	private final Integer page;

	private PagesDelegaAllegatoB(Integer page) {
		this.page = page;
	}

	public static Integer[] allValues() {
		PagesDelegaAllegatoB[] allegati = values();
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
