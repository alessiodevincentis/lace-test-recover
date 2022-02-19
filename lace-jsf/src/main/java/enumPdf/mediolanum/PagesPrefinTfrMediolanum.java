package enumPdf.mediolanum;

public enum PagesPrefinTfrMediolanum {
    //La prima pagina è considerata con il numero 0 quindi ogni pagina ha valore numPagina-1
    PAGINA_VUOTA_TFR(17),
    AUTOCERTIFICA_TFR(18);

    private final Integer page;

    PagesPrefinTfrMediolanum(final Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

}
