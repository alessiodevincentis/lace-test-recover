package enumPdf.italcredi;

public enum PagesPrefinConteggio {
    RICHIESTA_CONTEGGIO_1(16),
    PAGINA_VUOTA_CONTEGGIO_1(17);

    private final Integer page;

    private PagesPrefinConteggio(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesPrefinConteggio[] prefin = values();
        final Integer[] val = new Integer[prefin.length];

        for (int i = 0; i < prefin.length; i++) {
            val[i] = prefin[i].page;
        }

        return val;
    }

    public Integer getPage() {
        return page;
    }

}
