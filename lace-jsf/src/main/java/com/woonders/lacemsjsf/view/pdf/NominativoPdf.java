package com.woonders.lacemsjsf.view.pdf;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.TrattenuteViewModel;
import com.woonders.lacemscommon.app.viewmodel.converter.StringAndBigDecimalConverter;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import enumPdf.NameFieldsPdfNominativi;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static enumPdf.PdfFileType.MARKETING;

@Component
public class NominativoPdf extends BasePdfComponent {
	public static final String NAME = "nominativoPdf";
	public static final String JSF_NAME = "#{" + NAME + "}";

	@Autowired
	private StringAndBigDecimalConverter stringAndBigDecimalConverter;

	public ByteArrayOutputStream riempiAllegatiPDF(final ClienteViewModel clienteViewModel,
			final List<TrattenuteViewModel> trattenuteViewModelList, final List<PraticaViewModel> praticaViewModelList)
			throws IOException, NoFileOnS3Exception {

		// load the document
		final ByteArrayOutputStream fout = new ByteArrayOutputStream();
		final PDDocument pdfDocument = loadPDDocumentFromDisk(MARKETING, null);

		// get the document catalog
		final PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();
		final ControlNull emptyValue = new ControlNull();

		// as there might not be an AcroForm entry a null check is necessary
		if (acroForm != null) {

			if (acroForm.getField(NameFieldsPdfNominativi.COGNOME.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.COGNOME.getFieldName())
						.setValue(clienteViewModel.getCognome());
			}
			if (acroForm.getField(NameFieldsPdfNominativi.NOME.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.NOME.getFieldName()).setValue(clienteViewModel.getNome());
			}
			if (acroForm.getField(NameFieldsPdfNominativi.LUOGO_NASCITA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.LUOGO_NASCITA.getFieldName())
						.setValue(clienteViewModel.getLuogoNascita());
			}
			if (acroForm.getField(NameFieldsPdfNominativi.DATA_NASCITA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.DATA_NASCITA.getFieldName())
						.setValue(DateToCalendar.patternData(clienteViewModel.getDataNascita()));
			}
			if (acroForm.getField(NameFieldsPdfNominativi.TELEFONO.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.TELEFONO.getFieldName())
						.setValue(clienteViewModel.getTelefono());
			}
			if (acroForm.getField(NameFieldsPdfNominativi.MAIL.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.MAIL.getFieldName()).setValue(clienteViewModel.getEmail());
			}

			if (clienteViewModel.getEta() != null
					&& acroForm.getField(NameFieldsPdfNominativi.ETA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.ETA.getFieldName())
						.setValue(clienteViewModel.getEta().toString());
			}

			if (acroForm.getField(NameFieldsPdfNominativi.DATA_CARICAMENTO.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.DATA_CARICAMENTO.getFieldName())
						.setValue(DateToCalendar.patternDataOra(clienteViewModel.getDataIns()));
			}
			if (acroForm.getField(NameFieldsPdfNominativi.OPERATORE.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.OPERATORE.getFieldName())
						.setValue(clienteViewModel.getOperatoreNominativo().getUsername());
			}

			if (clienteViewModel.getNettoMensileNominativo() != null
					&& acroForm.getField(NameFieldsPdfNominativi.NETTO_STIPENDIO.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.NETTO_STIPENDIO.getFieldName())
						.setValue(clienteViewModel.getNettoMensileNominativo().toString());
			}
			if (acroForm.getField(NameFieldsPdfNominativi.STATO_NOMINATIVO.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.STATO_NOMINATIVO.getFieldName())
						.setValue(clienteViewModel.getStatoNominativo());
			}
			if (acroForm.getField(NameFieldsPdfNominativi.DATA_RECALL.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.DATA_RECALL.getFieldName())
						.setValue(DateToCalendar.patternDataOra(clienteViewModel.getDataRecallNominativo()));
			}

			if (clienteViewModel.getNote() != null
					&& acroForm.getField(NameFieldsPdfNominativi.NOTE.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.NOTE.getFieldName()).setValue(
						clienteViewModel.getNote().replaceAll(Constants.getNewLine(), System.lineSeparator()));
			}

			if (acroForm.getField(NameFieldsPdfNominativi.IMPIEGO.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.IMPIEGO.getFieldName())
						.setValue(clienteViewModel.getImpiego());
			}
			if (acroForm.getField(NameFieldsPdfNominativi.PROVENIENZA.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.PROVENIENZA.getFieldName())
						.setValue(clienteViewModel.getProvenienza());
			}
			if (acroForm.getField(NameFieldsPdfNominativi.PROVENIENZA_DESC.getFieldName()) != null) {
				acroForm.getField(NameFieldsPdfNominativi.PROVENIENZA_DESC.getFieldName())
						.setValue(clienteViewModel.getProvenienzaDesc());
			}

			// TODO migliorare con index
			if (trattenuteViewModelList != null) {
				if (trattenuteViewModelList.size() >= 1) {
					if (acroForm.getField(NameFieldsPdfNominativi.TIPO_TRATTENUTA_1.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.TIPO_TRATTENUTA_1.getFieldName())
								.setValue(trattenuteViewModelList.get(0).getTipoTrat());
					}
					if (acroForm.getField(NameFieldsPdfNominativi.RATA_TRATTENUTA_1.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.RATA_TRATTENUTA_1.getFieldName())
								.setValue(stringAndBigDecimalConverter
										.getStringFromBigDecimalValue((trattenuteViewModelList.get(0).getRataTrat())));
					}
					if (trattenuteViewModelList.get(0).getDurataTrat() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.DURATA_TRATTENUTA_1.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.DURATA_TRATTENUTA_1.getFieldName())
									.setValue(trattenuteViewModelList.get(0).getDurataTrat().toString());
						}
					}
					if (acroForm.getField(NameFieldsPdfNominativi.DATA_RINNOVO_TRATTENUTA_1.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.DATA_RINNOVO_TRATTENUTA_1.getFieldName()).setValue(
								DateToCalendar.patternData(trattenuteViewModelList.get(0).getDataRinnovoTrat()));
					}
				}

				if (trattenuteViewModelList.size() >= 2) {
					if (acroForm.getField(NameFieldsPdfNominativi.TIPO_TRATTENUTA_2.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.TIPO_TRATTENUTA_2.getFieldName())
								.setValue(trattenuteViewModelList.get(1).getTipoTrat());
					}
					if (acroForm.getField(NameFieldsPdfNominativi.RATA_TRATTENUTA_2.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.RATA_TRATTENUTA_2.getFieldName())
								.setValue(stringAndBigDecimalConverter
										.getStringFromBigDecimalValue((trattenuteViewModelList.get(1).getRataTrat())));
					}

					if (trattenuteViewModelList.get(1).getDurataTrat() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.DURATA_TRATTENUTA_2.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.DURATA_TRATTENUTA_2.getFieldName())
									.setValue(trattenuteViewModelList.get(1).getDurataTrat().toString());
						}
					}

					if (acroForm.getField(NameFieldsPdfNominativi.DATA_RINNOVO_TRATTENUTA_2.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.DATA_RINNOVO_TRATTENUTA_2.getFieldName()).setValue(
								DateToCalendar.patternData(trattenuteViewModelList.get(1).getDataRinnovoTrat()));
					}
				}

				if (trattenuteViewModelList.size() >= 3) {
					if (acroForm.getField(NameFieldsPdfNominativi.TIPO_TRATTENUTA_3.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.TIPO_TRATTENUTA_3.getFieldName())
								.setValue(trattenuteViewModelList.get(2).getTipoTrat());
					}
					if (acroForm.getField(NameFieldsPdfNominativi.RATA_TRATTENUTA_3.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.RATA_TRATTENUTA_3.getFieldName())
								.setValue(stringAndBigDecimalConverter
										.getStringFromBigDecimalValue((trattenuteViewModelList.get(2).getRataTrat())));
					}

					if (trattenuteViewModelList.get(0).getDurataTrat() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.DURATA_TRATTENUTA_3.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.DURATA_TRATTENUTA_3.getFieldName())
									.setValue(trattenuteViewModelList.get(2).getDurataTrat().toString());
						}
					}
					if (acroForm.getField(NameFieldsPdfNominativi.DATA_RINNOVO_TRATTENUTA_3.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.DATA_RINNOVO_TRATTENUTA_3.getFieldName()).setValue(
								DateToCalendar.patternData(trattenuteViewModelList.get(2).getDataRinnovoTrat()));
					}
				}
			}

			// TODO migliorare con index
			if (praticaViewModelList != null) {
				if (praticaViewModelList.size() >= 1) {
					if (acroForm.getField(NameFieldsPdfNominativi.TIPO_PREVENTIVO_1.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.TIPO_PREVENTIVO_1.getFieldName())
								.setValue(praticaViewModelList.get(0).getTipoPratica());
					}

					if (praticaViewModelList.get(0).getRata() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.RATA_PREVENTIVO_1.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.RATA_PREVENTIVO_1.getFieldName())
									.setValue(stringAndBigDecimalConverter
											.getStringFromBigDecimalValue((praticaViewModelList.get(0).getRata())));
						}
					}

					if (praticaViewModelList.get(0).getDurata() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.DURATA_PREVENTIVO_1.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.DURATA_PREVENTIVO_1.getFieldName())
									.setValue(praticaViewModelList.get(0).getDurata().toString());
						}
					}
					if (acroForm.getField(NameFieldsPdfNominativi.IMPORTO_PREVENTIVO_1.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.IMPORTO_PREVENTIVO_1.getFieldName())
								.setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(
										(praticaViewModelList.get(0).getImportoErogato())));
					}
				}

				if (praticaViewModelList.size() >= 2) {
					if (acroForm.getField(NameFieldsPdfNominativi.TIPO_PREVENTIVO_2.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.TIPO_PREVENTIVO_2.getFieldName())
								.setValue(praticaViewModelList.get(1).getTipoPratica());
					}

					if (praticaViewModelList.get(1).getRata() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.RATA_PREVENTIVO_2.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.RATA_PREVENTIVO_2.getFieldName())
									.setValue(stringAndBigDecimalConverter
											.getStringFromBigDecimalValue((praticaViewModelList.get(1).getRata())));
						}
					}

					if (praticaViewModelList.get(1).getDurata() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.DURATA_PREVENTIVO_2.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.DURATA_PREVENTIVO_2.getFieldName())
									.setValue(praticaViewModelList.get(1).getDurata().toString());
						}
					}

					if (acroForm.getField(NameFieldsPdfNominativi.IMPORTO_PREVENTIVO_2.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.IMPORTO_PREVENTIVO_2.getFieldName())
								.setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(
										(praticaViewModelList.get(1).getImportoErogato())));
					}
				}

				if (praticaViewModelList.size() >= 3) {
					if (acroForm.getField(NameFieldsPdfNominativi.TIPO_PREVENTIVO_3.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.TIPO_PREVENTIVO_3.getFieldName())
								.setValue(praticaViewModelList.get(2).getTipoPratica());
					}

					if (praticaViewModelList.get(2).getRata() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.RATA_PREVENTIVO_3.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.RATA_PREVENTIVO_3.getFieldName())
									.setValue(stringAndBigDecimalConverter
											.getStringFromBigDecimalValue((praticaViewModelList.get(2).getRata())));
						}
					}

					if (praticaViewModelList.get(2).getDurata() != null) {
						if (acroForm.getField(NameFieldsPdfNominativi.DURATA_PREVENTIVO_3.getFieldName()) != null) {
							acroForm.getField(NameFieldsPdfNominativi.DURATA_PREVENTIVO_3.getFieldName())
									.setValue(praticaViewModelList.get(2).getDurata().toString());
						}
					}

					if (acroForm.getField(NameFieldsPdfNominativi.IMPORTO_PREVENTIVO_3.getFieldName()) != null) {
						acroForm.getField(NameFieldsPdfNominativi.IMPORTO_PREVENTIVO_3.getFieldName())
								.setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(
										(praticaViewModelList.get(2).getImportoErogato())));
					}
				}
			}

		}

		emptyValue.setValueEmpty(acroForm);

		pdfDocument.save(fout);
		pdfDocument.close();

		return fout;

	}
}
