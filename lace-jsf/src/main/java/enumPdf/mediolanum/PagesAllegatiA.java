package enumPdf.mediolanum;

public enum PagesAllegatiA {

    ALLEGATO_A(2),
    ALLEGATO_A1(3);

    private final Integer page;

    private PagesAllegatiA(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesAllegatiA[] allegati = values();
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
