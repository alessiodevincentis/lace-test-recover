package com.woonders.lacemsjsf.view.amministrazione;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import com.woonders.lacemsjsf.util.HttpSessionUtil;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.pdf.AmministrazionePdf;
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

@ManagedBean(name = AmministrazionePdfView.NAME)
@RequestScoped
@Getter
@Setter
public class AmministrazionePdfView implements Serializable {

    public static final String NAME = "amministrazionePdfView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private final StreamPdf stream;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    @ManagedProperty(AmministrazionePdf.JSF_NAME)
    private AmministrazionePdf amministrazionePdf;
    private ByteArrayOutputStream file;
    @ManagedProperty(HttpSessionUtil.JSF_NAME)
    private HttpSessionUtil httpSessionUtil;

    public AmministrazionePdfView() {
        stream = new StreamPdf();
    }

    public StreamedContent getStream() throws IOException {
        try {
            file = amministrazionePdf.riempiAmministration(
                    (AmministrazioneViewModel) passaggioParametriUtils.getParametri()
                            .get(Parametro.AMMINISTRAZIONE_PDF_SCHEDA_ATC_PARAMETER),
                    aziendaService.getOne(httpSessionUtil.getAziendaId()),
                    (FinanziariaViewModel) passaggioParametriUtils.getParametri().get(Parametro.SELECTED_FINANZIARIA));
        } catch (NoFileOnS3Exception e) {
            file = e.getByteArrayOutputStreamDefaultContent();
        }
        return stream.getStream(file);
    }

    public String getIdFile() {
        return java.util.UUID.randomUUID().toString();
    }

}
