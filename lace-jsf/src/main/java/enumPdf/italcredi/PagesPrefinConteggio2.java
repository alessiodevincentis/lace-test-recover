package enumPdf.italcredi;

public enum PagesPrefinConteggio2 {
    //La prima pagina è considerata con il numero 0 quindi ogni pagina ha valore numPagina-1
    RICHIESTA_CONTEGGIO_2(18),
    PAGINA_VUOTA_CONTEGGIO_2(19);

    private final Integer page;

    private PagesPrefinConteggio2(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesPrefinConteggio2[] prefin = values();
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
