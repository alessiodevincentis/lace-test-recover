package enumPdf;

public enum PdfFileType {

	ANAGRAFICA("anagrafica.pdf"), PREFIN("precontrattuale.pdf"), ALLEGATI("allegati.pdf"), MARKETING(
			"marketing.pdf"), SCHEDA_ATC("schedaAtc.pdf"), PRIVACY("privacy.pdf"), DEFAULT_TEMPLATE("template.pdf");

	private final String name;

	PdfFileType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
