package com.woonders.lacemsjsf.view.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.StreamedContent;

import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Emanuele on 28/03/2017.
 */
@ManagedBean(name = "pdfView")
@RequestScoped
@Getter
@Setter
@Slf4j
public class PdfView implements Serializable {
	private StreamPdf stream;
	private ByteArrayOutputStream file;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;

	public PdfView() {
		stream = new StreamPdf();
	}

    /**
     * Per ora questo non e mai usato in questa classe quindi facciamo tornare null
     * @return
     * @throws IOException
     */
	public StreamedContent getStream() throws IOException {
		return null;
	}

	public String getPdfLink() {
		return (String) passaggioParametriUtils.getParametri().get(PassaggioParametriUtils.Parametro.PDF_LINK);
	}

	/**
	 * * Nella pagina ci sono 2 componenti media, uno per visualizzare un pdf da
	 * * uno streamedContent, uno per visualizzare un pdf da un link. Questo *
	 * metodo ci dice quale dei 2 deve essere renderizzato in base al parametro
	 * * che abbiamo. Per ora l'unico caso e quello del link, visto che questa e
	 * * una classe che abbiamo fatto appena adesso e dovremmo poi spostarci gli
	 * * altri pdf * * @return true se stiamo mostrando il pdf da un link, false
	 * se da * streamedContent
	 */
	public boolean isLinkToShow() {
		ClienteManagerServiceImpl.PdfCategory pdfCategory = (ClienteManagerServiceImpl.PdfCategory) passaggioParametriUtils.getParametri()
				.get(PassaggioParametriUtils.Parametro.PDF_CATEGORY_PARAMETER);
		return ClienteManagerServiceImpl.PdfCategory.FATTURA.equals(pdfCategory);
	}

	public String getIdFile() {
		return java.util.UUID.randomUUID().toString();
	}
}