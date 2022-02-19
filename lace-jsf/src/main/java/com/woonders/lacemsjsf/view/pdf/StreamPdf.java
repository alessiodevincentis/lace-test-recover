package com.woonders.lacemsjsf.view.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class StreamPdf {

	public StreamedContent getStream(ByteArrayOutputStream file) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the HTML. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the media. Return a real
			// StreamedContent with the media bytes.
			return new DefaultStreamedContent(new ByteArrayInputStream(file.toByteArray()), "application/pdf");
		}
	}

}
