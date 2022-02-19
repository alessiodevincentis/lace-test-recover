package com.woonders.lacemsjsf.view.pdf;

import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import enumPdf.NameFieldsPdfCliente;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static enumPdf.PdfFileType.SCHEDA_ATC;

@Component
public class AmministrazionePdf extends BasePdfComponent {
    public static final String NAME = "amministrazionePdf";
    public static final String JSF_NAME = "#{" + NAME + "}";

    public ByteArrayOutputStream riempiAmministration(final AmministrazioneViewModel amministrazione,
                                                      final AziendaViewModel aziendaViewModel, final FinanziariaViewModel finanziariaViewModel)
            throws IOException, NoFileOnS3Exception {

        // load the document
        final ByteArrayOutputStream fout = new ByteArrayOutputStream();
        final PDDocument pdfDocument = loadPDDocumentFromDisk(SCHEDA_ATC, finanziariaViewModel);

        // get the document catalog
        final PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

        // as there might not be an AcroForm entry a null check is necessary
        if (acroForm != null) {

            if (acroForm.getField(NameFieldsPdfCliente.RAGIONE_SOCIALE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.RAGIONE_SOCIALE.getFieldName())
                        .setValue(amministrazione.getRagioneSociale());
            }
            if (acroForm.getField(NameFieldsPdfCliente.DATA_CORRENTE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_CORRENTE.getFieldName())
                        .setValue(DateToCalendar.patternData(new Date()));
            }
            if (acroForm.getField(NameFieldsPdfCliente.PIVA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.PIVA.getFieldName()).setValue(amministrazione.getPiva());
            }
            if (acroForm.getField(NameFieldsPdfCliente.CF.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.CF.getFieldName()).setValue(amministrazione.getCodiceFiscale());
            }
            if (acroForm.getField(NameFieldsPdfCliente.NOME_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NOME_AZIENDA.getFieldName())
                        .setValue(aziendaViewModel.getNomeAzienda());
            }

            if (amministrazione.isCqs()) {
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_CQS.getFieldName()) != null) {
                    final PDCheckBox cqs = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_CQS.getFieldName());
                    cqs.check();
                }
            }
            if (amministrazione.isDlg()) {
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_DLG.getFieldName()) != null) {
                    final PDCheckBox dlg = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_DLG.getFieldName());
                    dlg.check();
                }
            }

            if (acroForm.getField(NameFieldsPdfCliente.SEDE_LEGALE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.SEDE_LEGALE.getFieldName())
                        .setValue(Objects
                                .toString(Objects.toString(amministrazione.getIndirizzoSedeLegale(), "") + " "
                                        + Objects.toString(amministrazione.getCivicoSedeLegale(), "") + " "
                                        + Objects.toString(amministrazione.getCapSedeLegale(), "") + " "
                                        + Objects.toString(amministrazione.getLuogoSedeLegale(), "") + " "
                                        + Objects.toString(amministrazione.getProvSedeLegale(), ""), "")
                                .trim());
            }
            if (acroForm.getField(NameFieldsPdfCliente.SEDE_NOTIFICA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.SEDE_NOTIFICA.getFieldName())
                        .setValue(Objects
                                .toString(Objects.toString(amministrazione.getIndirizzoSedeNotifica(), "") + " "
                                        + Objects.toString(amministrazione.getCivicoSedeNotifica(), "") + " "
                                        + Objects.toString(amministrazione.getCapSedeNotifica(), "") + " "
                                        + Objects.toString(amministrazione.getLuogoSedeNotifica(), "") + " "
                                        + Objects.toString(amministrazione.getProvSedeNotifica(), ""), "")
                                .trim());
            }

            if (amministrazione.isNotificaPec()) {
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PEC_SI.getFieldName()) != null) {
                    final PDCheckBox pecsi = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_PEC_SI.getFieldName());
                    pecsi.check();
                }
                if (acroForm.getField("PEC") != null) {
                    acroForm.getField("PEC").setValue(amministrazione.getIndirizzoPec());
                }
            } else {
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PEC_NO.getFieldName()) != null) {
                    final PDCheckBox pecno = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_PEC_NO.getFieldName());
                    pecno.check();
                }
            }
            if (acroForm.getField(NameFieldsPdfCliente.REFERENTE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.REFERENTE.getFieldName())
                        .setValue(amministrazione.getReferente());
            }
            if (acroForm.getField(NameFieldsPdfCliente.RUOLO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.RUOLO.getFieldName()).setValue(amministrazione.getRuolo());
            }
            if (acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName()).setValue(amministrazione.getTelefono());
            }
            if (acroForm.getField(NameFieldsPdfCliente.FAX.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.FAX.getFieldName()).setValue(amministrazione.getFax());
            }
            if (acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()).setValue(amministrazione.getEmail());
            }
            if (amministrazione.getNote() != null
                    && acroForm.getField(NameFieldsPdfCliente.NOTE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NOTE.getFieldName())
                        .setValue(amministrazione.getNote().replaceAll(Constants.getNewLine(), System.lineSeparator()));
            }
        }

        pdfDocument.save(fout);
        pdfDocument.close();

        return fout;
    }

}
