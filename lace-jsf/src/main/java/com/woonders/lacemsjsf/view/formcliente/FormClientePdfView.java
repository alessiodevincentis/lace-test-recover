package com.woonders.lacemsjsf.view.formcliente;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.service.AziendaService;
import com.woonders.lacemscommon.service.ClienteManagerService;
import com.woonders.lacemscommon.service.impl.AziendaServiceImpl;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl;
import com.woonders.lacemscommon.service.impl.ClienteManagerServiceImpl.PdfCategory;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import com.woonders.lacemsjsf.log.LoggerConstants;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils;
import com.woonders.lacemsjsf.util.PassaggioParametriUtils.Parametro;
import com.woonders.lacemsjsf.view.pdf.*;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = FormClientePdfView.NAME)
@RequestScoped
@Getter
@Setter
public class FormClientePdfView implements Serializable {

    public static final String NAME = "formClientePdfView";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final Logger logger = LoggerFactory.getLogger(FormClientePdfView.class);
    private final StreamPdf stream;
    @ManagedProperty(PassaggioParametriUtils.JSF_NAME)
    private PassaggioParametriUtils passaggioParametriUtils;
    private ByteArrayOutputStream file;
    @ManagedProperty(AziendaServiceImpl.JSF_NAME)
    private AziendaService aziendaService;
    @ManagedProperty(AllegatiPdf.JSF_NAME)
    private AllegatiPdf allegatiPdf;
    @ManagedProperty(PrefinPdf.JSF_NAME)
    private PrefinPdf prefinPdf;
    @ManagedProperty(AnagraficaPdf.JSF_NAME)
    private AnagraficaPdf anagraficaPdf;
    @ManagedProperty(PrivacyPdf.JSF_NAME)
    private PrivacyPdf privacyPdf;
    @ManagedProperty(ClienteManagerServiceImpl.JSF_NAME)
    private ClienteManagerService clienteManagerService;

    public FormClientePdfView() {
        stream = new StreamPdf();
    }

    public StreamedContent getStream() throws IOException {
        final ClienteViewModel clienteViewModel = (ClienteViewModel) passaggioParametriUtils.getParametri()
                .get(Parametro.CLIENTE_PDF_PARAMETER);
        final DocumentoViewModel documentoViewModel = (DocumentoViewModel) passaggioParametriUtils.getParametri()
                .get(Parametro.DOCUMENTO_PDF_PARAMETER);
        final ContoViewModel contoViewModel = (ContoViewModel) passaggioParametriUtils.getParametri()
                .get(Parametro.CONTO_PDF_PARAMETER);
        final ResidenzaViewModel residenzaViewModel = (ResidenzaViewModel) passaggioParametriUtils.getParametri()
                .get(Parametro.RESIDENZA_PDF_PARAMETER);
        final PraticaViewModel praticaViewModel = (PraticaViewModel) passaggioParametriUtils.getParametri()
                .get(Parametro.PRATICA_PDF_PARAMETER);
        final List<EstinzioneViewModel> estinzioneViewModelList = (List<EstinzioneViewModel>) passaggioParametriUtils
                .getParametri().get(Parametro.ESTINZIONE_LIST_PDF_PARAMETER);
        final List<AmministrazioneViewModel> amministrazioneViewModelList = (List<AmministrazioneViewModel>) passaggioParametriUtils.getParametri()
                .get(Parametro.AMMINISTRAZIONE_PDF_PARAMETER);
        final PdfCategory category = (PdfCategory) passaggioParametriUtils.getParametri()
                .get(Parametro.PDF_CATEGORY_PARAMETER);
        final AziendaViewModel aziendaViewModel = aziendaService.getOne((Long) passaggioParametriUtils.getParametri().get(Parametro.PDF_AZIENDA_OPERATORE_ASSEGNATO_PARAMETER));

        try {
            if (category != null)
                switch (category) {
                    case ALLEGATI:
                        file = allegatiPdf.riempiAllegatiPDF(clienteViewModel, residenzaViewModel, praticaViewModel,
                                aziendaViewModel, estinzioneViewModelList);
                        break;
                    case ANAGRAFICA:
                        file = anagraficaPdf.riempiAnagraficaPDF(clienteViewModel, residenzaViewModel, documentoViewModel,
                                contoViewModel, praticaViewModel, aziendaViewModel, estinzioneViewModelList, amministrazioneViewModelList);
                        break;
                    case PRIVACY:
                        file = privacyPdf.riempiPrivacyPDF(clienteViewModel, aziendaViewModel, praticaViewModel,
                                residenzaViewModel);
                        break;
                    case PRECONTRATTUALE:
                        file = prefinPdf.riempiPreconPDF(clienteViewModel, residenzaViewModel, documentoViewModel,
                                contoViewModel, praticaViewModel, aziendaViewModel, estinzioneViewModelList);
                        break;
                }

        } catch (final IOException e) {
            logger.error(LoggerConstants.msgErrorPdf, e);
            return null;
        } catch (final NoFileOnS3Exception e) {
            file = e.getByteArrayOutputStreamDefaultContent();
        }
        return stream.getStream(file);
    }

    public String getIdFile() {
        return java.util.UUID.randomUUID().toString();
    }

}
