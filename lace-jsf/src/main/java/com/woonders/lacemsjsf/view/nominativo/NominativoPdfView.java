package com.woonders.lacemsjsf.view.nominativo;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.pdf.NominativoPdf;
import com.woonders.lacemsjsf.view.pdf.StreamPdf;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "nominativoPdfView")
@RequestScoped
@Getter
@Setter
public class NominativoPdfView implements Serializable {

	private final StreamPdf stream;
	@ManagedProperty(NominativoPdf.JSF_NAME)
	private NominativoPdf nominativoPdf;
	private ByteArrayOutputStream file;
	@ManagedProperty(PassaggioParametriUtils.JSF_NAME)
	private PassaggioParametriUtils passaggioParametriUtils;

	public NominativoPdfView() {
		stream = new StreamPdf();
	}

	public StreamedContent getStream() throws IOException {
		final ClienteViewModel nominativoViewModel = (ClienteViewModel) passaggioParametriUtils.getParametri()
				.get(Parametro.NOMINATIVO_PDF_PARAMETER);
		List<TrattenuteViewModel> trattenuteNominativoViewModelList = (List<TrattenuteViewModel>) passaggioParametriUtils
				.getParametri().get(Parametro.IMPEGNI_NOMINATIVO_PDF_PARAMETER);
		List<PraticaViewModel> preventivoViewModelList = (List<PraticaViewModel>) passaggioParametriUtils.getParametri()
				.get(Parametro.PREVENTIVI_NOMINATIVO_PDF_PARAMETER);
		try {
			file = nominativoPdf.riempiAllegatiPDF(nominativoViewModel, trattenuteNominativoViewModelList,
                    preventivoViewModelList);
		} catch (NoFileOnS3Exception e) {
			file = e.getByteArrayOutputStreamDefaultContent();
		}
		return stream.getStream(file);
	}

	public String getIdFile() {
		return java.util.UUID.randomUUID().toString();
	}

}
