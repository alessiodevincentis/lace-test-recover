package enumPdf.italcredi;

public enum PagesPrefin {

    MOD_CQ2252_PAG1(1),
    MOD_CQ2252_PAG2(2),
    MOD_NORME_1104_PAG1(3),
    MOD_NORME_1104_PAG2(4),
    MOD_FI1596_PAG1(5),
    MOD_FI1596_PAG2(6),
    MOD_FI1596_PAG3(7),
    MOD_FI1596_PAG4(8),
    MOD_NORME_1103_PAG1(9),
    MOD_NORME_1103_PAG2(10),
    MOD_AM1542_PAG1(11),
    PAGINA_VUOTA_AM1542_1(12),
    MOD_AM1542_PAG2(13),
    PAGINA_VUOTA_AM1542_2(14);


    private final Integer page;

    private PagesPrefin(final Integer page) {
        this.page = page;
    }

    public static Integer[] allValues() {
        final PagesPrefin[] prefin = values();
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


