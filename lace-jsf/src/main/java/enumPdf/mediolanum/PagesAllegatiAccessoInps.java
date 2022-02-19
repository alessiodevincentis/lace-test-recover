package enumPdf.mediolanum;

public enum PagesAllegatiAccessoInps {

    MODULO_INPS(10);

    private final Integer page;

    private PagesAllegatiAccessoInps(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesAllegatiAccessoInps[] allegati = values();
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
