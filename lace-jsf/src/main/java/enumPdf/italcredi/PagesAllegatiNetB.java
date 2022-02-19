package enumPdf.italcredi;

public enum PagesAllegatiNetB {

    CREDITO_NET_ALL_B(9);

    private final Integer page;

    private PagesAllegatiNetB(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesAllegatiNetB[] allegati = values();
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
