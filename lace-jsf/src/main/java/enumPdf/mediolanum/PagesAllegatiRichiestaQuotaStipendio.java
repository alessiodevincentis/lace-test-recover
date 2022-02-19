package enumPdf.mediolanum;

public enum PagesAllegatiRichiestaQuotaStipendio {

    MODULO_RICHIESTA(1);

    private final Integer page;

    PagesAllegatiRichiestaQuotaStipendio(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesAllegatiRichiestaQuotaStipendio[] allegati = values();
        final Integer[] val = new Integer[allegati.length];

        for (int i = 0; i < allegati.length; i++) {
            val[i] = allegati[i].page;
        }

        return val;
    }

    public Integer getPage() {
        return page;
    }
}
