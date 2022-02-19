package enumPdf.italcredi;

public enum PagesCreditoNetC {

	CREDITO_NET_C(14);

	private final Integer page;

	private PagesCreditoNetC(Integer page) {
		this.page = page;
	}

	public static Integer[] allValues() {
		PagesCreditoNetC[] allegati = values();
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
