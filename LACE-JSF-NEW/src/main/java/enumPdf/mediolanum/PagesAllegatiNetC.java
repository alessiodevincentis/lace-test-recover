package enumPdf.mediolanum;

public enum PagesAllegatiNetC {

    CREDITO_NET_C(9);

    private final Integer page;

    private PagesAllegatiNetC(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesAllegatiNetC[] allegati = values();
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
