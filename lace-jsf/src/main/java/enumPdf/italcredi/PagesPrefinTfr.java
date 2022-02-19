package enumPdf.italcredi;

public enum PagesPrefinTfr {
    //La prima pagina è considerata con il numero 0 quindi ogni pagina ha valore numPagina-1
    AUTOCERTIFICA_TFR(14),
    PAGINA_VUOTA_TFR(15);

    private final Integer page;

    private PagesPrefinTfr(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesPrefinTfr[] prefin = values();
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
