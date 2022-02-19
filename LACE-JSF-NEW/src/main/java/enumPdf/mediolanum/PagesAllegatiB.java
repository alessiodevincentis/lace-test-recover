package enumPdf.mediolanum;

public enum PagesAllegatiB {

    ALLEGATO_B(4),
    ALLEGATO_B1(5);

    private final Integer page;

    private PagesAllegatiB(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesAllegatiB[] allegati = values();
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
