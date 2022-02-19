package enumPdf.mediolanum;

public enum PagesAllegatiCertificatoStipendio {

    DELEGA_RICHIESTA_CERTIFICATI(11),
    CERTIFICATO_STIPENDIO(12);

    private final Integer page;

    private PagesAllegatiCertificatoStipendio(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesAllegatiCertificatoStipendio[] allegati = values();
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
