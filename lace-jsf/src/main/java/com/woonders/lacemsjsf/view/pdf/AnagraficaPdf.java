package com.woonders.lacemsjsf.view.pdf;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.app.viewmodel.converter.StringAndBigDecimalConverter;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entity.Pratica.TipoPratica;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemsjsf.app.model.Occupazione;
import com.woonders.lacemsjsf.db.app.model.util.ClienteViewModelUtil;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import enumPdf.NameFieldsPdfCliente;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static enumPdf.PdfFileType.ANAGRAFICA;

@Component
public class AnagraficaPdf extends BasePdfComponent {

    public static final String NAME = "anagraficaPdf";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ClienteViewModelUtil clienteViewModelUtil;
    @Autowired
    private StringAndBigDecimalConverter stringAndBigDecimalConverter;

    public ByteArrayOutputStream riempiAnagraficaPDF(final ClienteViewModel clienteViewModel,
                                                     final ResidenzaViewModel residenzaViewModel, final DocumentoViewModel documentoViewModel,
                                                     final ContoViewModel contoViewModel, final PraticaViewModel praticaViewModel,
                                                     final AziendaViewModel aziendaViewModel, final List<EstinzioneViewModel> estinzioneViewModelList,
                                                     final List<AmministrazioneViewModel> amministrazioneList)
            throws IOException, NoFileOnS3Exception {

        // load the document
        final ByteArrayOutputStream fout = new ByteArrayOutputStream();
        final PDDocument pdfDocument = loadPDDocumentFromDisk(ANAGRAFICA, praticaViewModel.getFinanziariaViewModel());

        // get the document catalog
        final PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();
        final ControlNull emptyValue = new ControlNull();
        final Occupazione occupazione = clienteViewModelUtil.getOccupazionePredefinita(clienteViewModel);

        // as there might not be an AcroForm entry a null check is necessary
        if (acroForm != null) {

            if (acroForm.getField(NameFieldsPdfCliente.CF.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.CF.getFieldName()).setValue(clienteViewModel.getCf());
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
            if (acroForm.getField(NameFieldsPdfCliente.DATA_NASCITA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_NASCITA.getFieldName())
                        .setValue(DateToCalendar.patternData(clienteViewModel.getDataNascita()));
            }
            if (acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName())
                        .setValue(clienteViewModel.getTelefono());
            }
            if (acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()).setValue(clienteViewModel.getEmail());
            }
            if (acroForm.getField(NameFieldsPdfCliente.FISSO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.FISSO.getFieldName())
                        .setValue(clienteViewModel.getTelefonoFisso());
            }
            if (acroForm.getField(NameFieldsPdfCliente.LUOGO_RESIDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.LUOGO_RESIDENZA.getFieldName())
                        .setValue(residenzaViewModel.getLuogoResidenza());
            }
            if (acroForm.getField(NameFieldsPdfCliente.PROVINCIA_RESIDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.PROVINCIA_RESIDENZA.getFieldName())
                        .setValue(residenzaViewModel.getProvResidenza());
            }
            if (acroForm.getField(NameFieldsPdfCliente.CAP_RESIDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.CAP_RESIDENZA.getFieldName())
                        .setValue(residenzaViewModel.getCapResidenza());
            }
            if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_RESIDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_RESIDENZA.getFieldName())
                        .setValue(Objects.toString(Objects.toString(residenzaViewModel.getIndirizzoResidenza(), "")
                                + " " + Objects.toString(residenzaViewModel.getCivicoResidenza(), ""), "").trim());
            }
            if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_RESIDENZA_NO_CIVICO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_RESIDENZA_NO_CIVICO.getFieldName())
                        .setValue(residenzaViewModel.getIndirizzoResidenza());
            }
            if (acroForm.getField(NameFieldsPdfCliente.CIVICO_RESIDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.CIVICO_RESIDENZA.getFieldName())
                        .setValue(residenzaViewModel.getCivicoResidenza());
            }

            if (documentoViewModel.getTipoDocumento() != null
                    && acroForm.getField(NameFieldsPdfCliente.TIPO_DOC.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TIPO_DOC.getFieldName())
                        .setValue(documentoViewModel.getTipoDocumento().toUpperCase());
            }

            if (acroForm.getField(NameFieldsPdfCliente.NUMERO_DOC.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NUMERO_DOC.getFieldName())
                        .setValue(documentoViewModel.getNumeroDoc());
            }
            if (acroForm.getField(NameFieldsPdfCliente.DATA_RILASCIO_DOC.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_RILASCIO_DOC.getFieldName())
                        .setValue(DateToCalendar.patternData(documentoViewModel.getRilascioDoc()));
            }
            if (acroForm.getField(NameFieldsPdfCliente.SCADENZA_DOC.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.SCADENZA_DOC.getFieldName())
                        .setValue(DateToCalendar.patternData(documentoViewModel.getScadenzaDoc()));
            }
            if (acroForm.getField(NameFieldsPdfCliente.NOME_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NOME_AZIENDA.getFieldName())
                        .setValue(aziendaViewModel.getNomeAzienda());
            }

            if (praticaViewModel.getNotePratica() != null
                    && acroForm.getField(NameFieldsPdfCliente.NOTE_PRATICA.getFieldName()) != null)
                acroForm.getField(NameFieldsPdfCliente.NOTE_PRATICA.getFieldName()).setValue(
                        praticaViewModel.getNotePratica().replaceAll(Constants.getNewLine(), System.lineSeparator()));

            if (clienteViewModel.getSesso() != null)
                if (clienteViewModel.getSesso().toString().equalsIgnoreCase(Cliente.Sesso.MASCHIO.toString())) {
                    if (acroForm.getField(NameFieldsPdfCliente.MASCHIO.getFieldName()) != null) {
                        final PDCheckBox sesso = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.MASCHIO.getFieldName());
                        sesso.check();
                    }
                } else {
                    if (acroForm.getField(NameFieldsPdfCliente.FEMMINA.getFieldName()) != null) {
                        final PDCheckBox sesso = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.FEMMINA.getFieldName());
                        sesso.check();
                    }
                }

            if (occupazione.getImpiego() != null && !(occupazione.getImpiego().contentEquals(""))) {
                if (occupazione.getImpiego().equalsIgnoreCase(Impiego.PENSIONATO.getValue())
                        && acroForm.getField(NameFieldsPdfCliente.ENTE_PENSIONISTICO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.ENTE_PENSIONISTICO.getFieldName())
                            .setValue(occupazione.getEnte());
                } else {
                    if (acroForm.getField(NameFieldsPdfCliente.OCCUPAZIONE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.OCCUPAZIONE.getFieldName())
                                .setValue(occupazione.getOccupazione());
                    }
                }
            }
            if (praticaViewModel.getRata() != null
                    && acroForm.getField(NameFieldsPdfCliente.RATA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.RATA.getFieldName()).setValue(
                        stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getRata()));
            }
            if (praticaViewModel.getDurata() != null
                    && acroForm.getField(NameFieldsPdfCliente.DURATA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DURATA.getFieldName())
                        .setValue(praticaViewModel.getDurata().toString());
            }
            if (praticaViewModel.getTaeg() != null
                    && acroForm.getField(NameFieldsPdfCliente.TAEG.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TAEG.getFieldName()).setValue(
                        stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getTaeg()));
            }

            if (estinzioneViewModelList != null && !estinzioneViewModelList.isEmpty()) {
                if (acroForm.getField(NameFieldsPdfCliente.RATA_EST.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.RATA_EST.getFieldName())
                            .setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(
                                    (estinzioneViewModelList.get(0).getRataEstinzione())));
                }
                if (acroForm.getField(NameFieldsPdfCliente.ISTITUTO_EST.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.ISTITUTO_EST.getFieldName())
                            .setValue(estinzioneViewModelList.get(0).getIstitutoEst().toUpperCase());
                }
                if (acroForm.getField(NameFieldsPdfCliente.SCADENZA_EST.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.SCADENZA_EST.getFieldName()).setValue(
                            DateToCalendar.patternData(estinzioneViewModelList.get(0).getScadenzaEstinzione()));
                }
                if (acroForm.getField(NameFieldsPdfCliente.CONTEGGIO_EST.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.CONTEGGIO_EST.getFieldName())
                            .setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(
                                    (estinzioneViewModelList.get(0).getConteggioEstinzione())));
                }
                if (estinzioneViewModelList.size() == 2) {
                    if (acroForm.getField(NameFieldsPdfCliente.SECONDA_EST.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.SECONDA_EST.getFieldName())
                                .setValue(estinzioneViewModelList.get(1).getIstitutoEst().toUpperCase());
                    }
                }
            }
            if (praticaViewModel.getImportoErogato() != null
                    && acroForm.getField(NameFieldsPdfCliente.IMPORTO_EROGATO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.IMPORTO_EROGATO.getFieldName())
                        .setValue(stringAndBigDecimalConverter
                                .getStringFromBigDecimalValue(praticaViewModel.getImportoErogato()));
            }

            if (praticaViewModel.getNettoRicavo() != null
                    && acroForm.getField(NameFieldsPdfCliente.NETTO_RICAVO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NETTO_RICAVO.getFieldName()).setValue(
                        stringAndBigDecimalConverter.getStringFromBigDecimalValue((praticaViewModel.getNettoRicavo())));
            }

            if (acroForm.getField(NameFieldsPdfCliente.FILIALE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.FILIALE.getFieldName()).setValue(contoViewModel.getBanca());
            }
            if (acroForm.getField(NameFieldsPdfCliente.IBAN.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.IBAN.getFieldName()).setValue(contoViewModel.getIban());
            }

            if (acroForm.getField(NameFieldsPdfCliente.NUMERO_CTR.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NUMERO_CTR.getFieldName()).setValue(praticaViewModel.getNrctr());
            }
            if (acroForm.getField(NameFieldsPdfCliente.NUMERO_SECCI.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NUMERO_SECCI.getFieldName())
                        .setValue(praticaViewModel.getNrsecci());
            }

            if (acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName()) != null) {
                if (Pratica.TipoPratica.CESSIONE_S.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                            .setValue(NameFieldsPdfCliente.CQS_LETTERE.getFieldName());
                } else if (Pratica.TipoPratica.CESSIONE_P.getValue()
                        .equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                            .setValue(NameFieldsPdfCliente.CQP_LETTERE.getFieldName());
                } else if (Pratica.TipoPratica.DELEGA.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                            .setValue(NameFieldsPdfCliente.DLG_LETTERE.getFieldName());
                }
            }

            if (acroForm.getField(NameFieldsPdfCliente.DATA_CORRENTE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_CORRENTE.getFieldName())
                        .setValue(DateToCalendar.patternData(new Date()));
            }

            // NOOOO!
            if (acroForm.getField(NameFieldsPdfCliente.SOTTOSCRITTO.getFieldName()) != null
                    && clienteViewModel.getCognome() != null) {
                final String cognomeNome = clienteViewModel.getCognome().concat("  ")
                        .concat(clienteViewModel.getNome());
                acroForm.getField(NameFieldsPdfCliente.SOTTOSCRITTO.getFieldName()).setValue(cognomeNome);
            }

            if (praticaViewModel.getTipoPratica() != null) {
                if (TipoPratica.CESSIONE_S.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())
                        || TipoPratica.CESSIONE_P.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_CQS.getFieldName()) != null) {
                        final PDCheckBox cq = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_CQS.getFieldName());
                        cq.check();
                    }
                } else if (TipoPratica.DELEGA.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_DLG.getFieldName()) != null) {
                        final PDCheckBox dlg = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_DLG.getFieldName());
                        dlg.check();
                    }
                } else if (TipoPratica.PRESTITO.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PP.getFieldName()) != null) {
                        final PDCheckBox pp = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_PP.getFieldName());
                        pp.check();
                    }
                }
            }

            if (occupazione.getImpiego() != null) {
                if (Impiego.PRIVATO.getValue().equalsIgnoreCase(occupazione.getImpiego())
                        || Impiego.AUTONOMO.getValue().equalsIgnoreCase(occupazione.getImpiego())
                        || Impiego.ALTRO.getValue().equalsIgnoreCase(occupazione.getImpiego())) {

                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PRIVATO.getFieldName()) != null) {
                        final PDCheckBox privato = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_PRIVATO.getFieldName());
                        privato.check();
                    }

                } else if (Impiego.STATALE.getValue().equalsIgnoreCase(occupazione.getImpiego())
                        || Impiego.CARABINIERE.getValue().equalsIgnoreCase(occupazione.getImpiego())
                        || Impiego.AERONAUTICA.getValue().equalsIgnoreCase(occupazione.getImpiego())
                        || Impiego.FINANZA.getValue().equalsIgnoreCase(occupazione.getImpiego())
                        || Impiego.MILITARE.getValue().equalsIgnoreCase(occupazione.getImpiego())
                        || Impiego.POLIZIA.getValue().equalsIgnoreCase(occupazione.getImpiego())) {

                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_STATALE.getFieldName()) != null) {
                        final PDCheckBox statale = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_STATALE.getFieldName());
                        statale.check();

                    }
                } else if (Impiego.PUBBLICO.getValue().equalsIgnoreCase(occupazione.getImpiego())
                        || Impiego.PARAPUBBLICO.getValue().equalsIgnoreCase(occupazione.getImpiego())) {

                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PUBBLICO.getFieldName()) != null) {
                        final PDCheckBox pubblico = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_PUBBLICO.getFieldName());
                        pubblico.check();
                    }

                } else if (Impiego.PENSIONATO.getValue().equalsIgnoreCase(occupazione.getImpiego())) {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PENSIONATO.getFieldName()) != null) {
                        final PDCheckBox pensionato = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_PENSIONATO.getFieldName());
                        pensionato.check();
                    }
                }
            }

            if (praticaViewModel.getOperatore() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.OPERATORE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.OPERATORE.getFieldName())
                            .setValue(praticaViewModel.getOperatore().getUsername());
                }
                if (praticaViewModel.getOperatore().getFirstName() != null && praticaViewModel.getOperatore().getLastName() != null) {
                    if (acroForm.getField(NameFieldsPdfCliente.NOME_COGNOME_OPERATORE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.NOME_COGNOME_OPERATORE.getFieldName())
                                .setValue(praticaViewModel.getOperatore().getFirstName()
                                        .concat(" " + praticaViewModel.getOperatore().getLastName()));
                    }
                }
            }

            if (clienteViewModel.getNote() != null
                    && acroForm.getField(NameFieldsPdfCliente.NOTE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NOTE.getFieldName()).setValue(
                        clienteViewModel.getNote().replaceAll(Constants.getNewLine(), System.lineSeparator()));
            }

            if (praticaViewModel.getDecorrenza() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.DATA_DECORRENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.DATA_DECORRENZA.getFieldName())
                            .setValue(DateToCalendar.patternData(praticaViewModel.getDecorrenza()));
                }
            }

            if (praticaViewModel.getPercProv() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.PERC_PROVVIGIONI.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.PERC_PROVVIGIONI.getFieldName())
                            .setValue(stringAndBigDecimalConverter
                                    .getStringFromBigDecimalValue(praticaViewModel.getPercProv(), 6));
                }
            }

            if (acroForm.getField(NameFieldsPdfCliente.ID_PRATICA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.ID_PRATICA.getFieldName())
                        .setValue(praticaViewModel.getNrctr());
            }

            if (acroForm.getField(NameFieldsPdfCliente.DATA_CARICAMENTO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_CARICAMENTO.getFieldName())
                        .setValue(DateToCalendar.patternData(praticaViewModel.getDataCaricamento()));
            }

            if (praticaViewModel.getMontante() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.MONTANTE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.MONTANTE.getFieldName()).setValue(
                            stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getMontante()));
                }
            }

            if (amministrazioneList != null && amministrazioneList.get(0) != null) {
                if (acroForm.getField(NameFieldsPdfCliente.RAGIONE_SOCIALE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.RAGIONE_SOCIALE.getFieldName())
                            .setValue(amministrazioneList.get(0).getRagioneSociale());
                }
            }

            if (acroForm.getField(NameFieldsPdfCliente.FAX_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.FAX_AZIENDA.getFieldName()).setValue(aziendaViewModel.getFax());
            }

            if (acroForm.getField(NameFieldsPdfCliente.EMAIL_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.EMAIL_AZIENDA.getFieldName())
                        .setValue(aziendaViewModel.getEmail());
            }
        }

        emptyValue.setValueEmpty(acroForm);

        pdfDocument.save(fout);
        pdfDocument.close();

        return fout;
    }

}