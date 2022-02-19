package enumPdf.mediolanum;

public enum PagesAllegatiE {

    ALLEGATO_E_PAG1(6),
    ALLEGATO_E_PAG2(7);

    private final Integer page;

    private PagesAllegatiE(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesAllegatiE[] allegati = values();
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
