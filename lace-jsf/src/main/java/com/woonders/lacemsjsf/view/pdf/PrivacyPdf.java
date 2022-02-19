package com.woonders.lacemsjsf.view.pdf;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.ResidenzaViewModel;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import enumPdf.NameFieldsPdfCliente;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static enumPdf.PdfFileType.PRIVACY;

@Component
public class PrivacyPdf extends BasePdfComponent {

	public static final String NAME = "privacyPdf";
	public static final String JSF_NAME = "#{" + NAME + "}";

	public ByteArrayOutputStream riempiPrivacyPDF(final ClienteViewModel clienteViewModel,
			final AziendaViewModel aziendaViewModel, final PraticaViewModel praticaViewModel,
			final ResidenzaViewModel residenzaViewModel) throws IOException, NoFileOnS3Exception {

		// load the document
		final ByteArrayOutputStream fout = new ByteArrayOutputStream();
		final PDDocument pdfDocument = loadPDDocumentFromDisk(PRIVACY, praticaViewModel.getFinanziariaViewModel());

		// get the document catalog
		final PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

		final String cognomeNome = clienteViewModel.getCognome().concat("  ").concat(clienteViewModel.getNome());
		// as there might not be an AcroForm entry a null check is necessary
		if (acroForm != null) {
			if (acroForm.getField(NameFieldsPdfCliente.SOTTOSCRITTO.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.SOTTOSCRITTO.getFieldName()).setValue(cognomeNome);
			}

			if (acroForm.getField(NameFieldsPdfCliente.COGNOME.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.COGNOME.getFieldName()).setValue(clienteViewModel.getCognome());
			}

			if (acroForm.getField(NameFieldsPdfCliente.NOME.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.NOME.getFieldName()).setValue(clienteViewModel.getNome());
			}

			if (acroForm.getField(NameFieldsPdfCliente.LUOGO_NASCITA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.LUOGO_NASCITA.getFieldName())
						.setValue(clienteViewModel.getLuogoNascita());
			}

			if (acroForm.getField(NameFieldsPdfCliente.CF.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.CF.getFieldName()).setValue(clienteViewModel.getCf());
			}

			if (acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName())
						.setValue(clienteViewModel.getTelefono());
			}

			if (acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()).setValue(clienteViewModel.getEmail());
			}

			if (acroForm.getField(NameFieldsPdfCliente.PROV_NASCITA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.PROV_NASCITA.getFieldName())
						.setValue(clienteViewModel.getProvNascita());
			}

			if (acroForm.getField(NameFieldsPdfCliente.DATA_NASCITA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.DATA_NASCITA.getFieldName())
						.setValue(DateToCalendar.patternData(clienteViewModel.getDataNascita()));
			}

			if (acroForm.getField(NameFieldsPdfCliente.NOME_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.NOME_AZIENDA.getFieldName())
						.setValue(aziendaViewModel.getNomeAzienda());
			}

			if (acroForm.getField(NameFieldsPdfCliente.FAX_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.FAX_AZIENDA.getFieldName()).setValue(aziendaViewModel.getFax());
			}

			if (acroForm.getField(NameFieldsPdfCliente.EMAIL_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.EMAIL_AZIENDA.getFieldName())
						.setValue(aziendaViewModel.getEmail());
			}

			if (acroForm.getField(NameFieldsPdfCliente.TELEFONO_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.TELEFONO_AZIENDA.getFieldName())
						.setValue(aziendaViewModel.getTelefono());
			}

			if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_AZIENDA.getFieldName())
						.setValue(aziendaViewModel.getIndirizzo());
			}

			if (acroForm.getField(NameFieldsPdfCliente.LUOGO_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.LUOGO_AZIENDA.getFieldName())
						.setValue(aziendaViewModel.getLuogo());
			}

			if (acroForm.getField(NameFieldsPdfCliente.PROVINCIA_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.PROVINCIA_AZIENDA.getFieldName())
						.setValue(aziendaViewModel.getProvincia());
			}

			if (acroForm.getField(NameFieldsPdfCliente.CAP_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.CAP_AZIENDA.getFieldName()).setValue(aziendaViewModel.getCap());
			}

			if (acroForm.getField(NameFieldsPdfCliente.PIVA_AZIENDA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.PIVA_AZIENDA.getFieldName())
						.setValue(aziendaViewModel.getPiva());
			}

			if (acroForm.getField(NameFieldsPdfCliente.DATA_CARICAMENTO.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.DATA_CARICAMENTO.getFieldName())
						.setValue(DateToCalendar.patternData(praticaViewModel.getDataCaricamento()));
			}

			if (acroForm.getField(NameFieldsPdfCliente.LUOGO_RESIDENZA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.LUOGO_RESIDENZA.getFieldName())
						.setValue(residenzaViewModel.getLuogoResidenza());
			}

			if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_RESIDENZA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_RESIDENZA.getFieldName())
						.setValue(Objects.toString(Objects.toString(residenzaViewModel.getIndirizzoResidenza(), "")
								+ " " + Objects.toString(residenzaViewModel.getCivicoResidenza(), ""), "").trim());
			}
		}
		pdfDocument.save(fout);
		pdfDocument.close();

		return fout;

	}
}
