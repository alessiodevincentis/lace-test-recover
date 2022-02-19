package com.woonders.lacemsjsf.view.pdf;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.app.viewmodel.converter.StringAndBigDecimalConverter;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.db.entity.Documento;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.Corrispondenza;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.StatoPatrimoniale;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemsjsf.app.model.Occupazione;
import com.woonders.lacemsjsf.db.app.model.util.ClienteViewModelUtil;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import enumPdf.NameFieldsPdfCliente;
import enumPdf.mediolanum.PagesPrefinTfrMediolanum;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static enumPdf.PdfFileType.PREFIN;

@Component
public class PrefinPdf extends BasePdfComponent {

    public static final String NAME = "prefinPdf";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ClienteViewModelUtil clienteViewModelUtil;
    @Autowired
    private StringAndBigDecimalConverter stringAndBigDecimalConverter;

    public ByteArrayOutputStream riempiPreconPDF(final ClienteViewModel clienteViewModel,
                                                 final ResidenzaViewModel residenzaViewModel, final DocumentoViewModel documentoViewModel,
                                                 final ContoViewModel contoViewModel, final PraticaViewModel praticaViewModel,
                                                 final AziendaViewModel aziendaViewModel, final List<EstinzioneViewModel> estinzioneViewModelList)
            throws IOException, NoFileOnS3Exception {

        // load the document
        final FinanziariaViewModel finanziariaViewModel = praticaViewModel.getFinanziariaViewModel();
        final ByteArrayOutputStream fout = new ByteArrayOutputStream();
        final PDDocument pdfDocument = loadPDDocumentFromDisk(PREFIN, finanziariaViewModel);
        final Occupazione occupazione = clienteViewModelUtil.getOccupazionePredefinita(clienteViewModel);

        if (finanziariaViewModel != null) {
            final int pageSize = pdfDocument.getNumberOfPages();
            if (FinanziariaEnum.ITALCREDI.getFieldName().equalsIgnoreCase(finanziariaViewModel.getName())) {
                //TODO porcata da rimuovere quando faremo la tabella sul db per impostare le pagine dei pdf
                if (estinzioneViewModelList == null || estinzioneViewModelList.isEmpty()) {
                    pdfDocument.removePage(pageSize - 1);
                    pdfDocument.removePage(pageSize - 2);
                    pdfDocument.removePage(pageSize - 3);
                    pdfDocument.removePage(pageSize - 4);

                } else if (estinzioneViewModelList.size() == 1) {
                    pdfDocument.removePage(pageSize - 1);
                    pdfDocument.removePage(pageSize - 2);
                }

                if (occupazione.getImpiego() != null && !Impiego.PRIVATO.getValue().equalsIgnoreCase(occupazione.getImpiego())) {
                    pdfDocument.removePage(pageSize - 5);
                    pdfDocument.removePage(pageSize - 6);
                }

            } else if (FinanziariaEnum.MEDIOLANUM.getFieldName().equalsIgnoreCase(finanziariaViewModel.getName())) {
                if (occupazione.getImpiego() != null && !Impiego.PRIVATO.getValue().equalsIgnoreCase(occupazione.getImpiego())) {
                    pdfDocument.removePage(PagesPrefinTfrMediolanum.AUTOCERTIFICA_TFR.getPage());
                    pdfDocument.removePage(PagesPrefinTfrMediolanum.PAGINA_VUOTA_TFR.getPage());
                }
            }
        }

        // get the document catalog
        final PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();
        final ControlNull emptyValue = new ControlNull();
        final String cognome = clienteViewModel.getCognome();
        final String nome = clienteViewModel.getNome();

        final String cognomeNome = cognome.concat("  ").concat(nome);

        // as there might not be an AcroForm entry a null check is necessary
        if (acroForm != null) {

            if (acroForm.getField(NameFieldsPdfCliente.SOTTOSCRITTO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.SOTTOSCRITTO.getFieldName()).setValue(cognomeNome);
            }
            if (acroForm.getField(NameFieldsPdfCliente.COGNOME.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.COGNOME.getFieldName()).setValue(cognome);
            }
            if (acroForm.getField(NameFieldsPdfCliente.NOME.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NOME.getFieldName()).setValue(nome);
            }
            if (acroForm.getField(NameFieldsPdfCliente.CF.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.CF.getFieldName()).setValue(clienteViewModel.getCf());
            }

            if (clienteViewModel.getSesso() != null) {
                if (Cliente.Sesso.MASCHIO.toString().equalsIgnoreCase(clienteViewModel.getSesso().toString())) {
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

            }

            if (acroForm.getField(NameFieldsPdfCliente.LUOGO_NASCITA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.LUOGO_NASCITA.getFieldName())
                        .setValue(clienteViewModel.getLuogoNascita());
            }
            if (acroForm.getField(NameFieldsPdfCliente.PROV_NASCITA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.PROV_NASCITA.getFieldName())
                        .setValue(clienteViewModel.getProvNascita());
            }
            if (acroForm.getField(NameFieldsPdfCliente.DATA_NASCITA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_NASCITA.getFieldName())
                        .setValue(DateToCalendar.patternData(clienteViewModel.getDataNascita()));
            }
            if (acroForm.getField(NameFieldsPdfCliente.CITTADINANZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.CITTADINANZA.getFieldName())
                        .setValue(clienteViewModel.getCittadinanza());
            }
            if (acroForm.getField(NameFieldsPdfCliente.STATO_CIVILE.getFieldName()) != null) {
                if (clienteViewModel.getStatoCivile() != null && !clienteViewModel.getStatoCivile().isEmpty()) {
                    acroForm.getField(NameFieldsPdfCliente.STATO_CIVILE.getFieldName())
                            .setValue(clienteViewModel.getStatoCivile().toUpperCase());
                }
            }

            if (acroForm.getField(NameFieldsPdfCliente.NOME_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NOME_AZIENDA.getFieldName())
                        .setValue(aziendaViewModel.getNomeAzienda());
            }

            if (clienteViewModel.getBeni() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.COMUNIONE.getFieldName()) != null) {
                    if (StatoPatrimoniale.COMUNIONE.getValue().equalsIgnoreCase(clienteViewModel.getBeni())) {
                        final PDCheckBox comunione = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.COMUNIONE.getFieldName());
                        comunione.check();
                    }
                }
                if (acroForm.getField(NameFieldsPdfCliente.SEPARAZIONE.getFieldName()) != null) {
                    if (StatoPatrimoniale.SEPARAZIONE.getValue().equalsIgnoreCase(clienteViewModel.getBeni())) {
                        final PDCheckBox separazione = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.SEPARAZIONE.getFieldName());
                        separazione.check();
                    }
                }
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

            if (acroForm.getField(NameFieldsPdfCliente.LUOGO_CORRISPONDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.LUOGO_CORRISPONDENZA.getFieldName())
                        .setValue(residenzaViewModel.getLuogoResidenza());
            }
            if (acroForm.getField(NameFieldsPdfCliente.PROV_CORRISPONDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.PROV_CORRISPONDENZA.getFieldName())
                        .setValue(residenzaViewModel.getProvResidenza());
            }
            if (acroForm.getField(NameFieldsPdfCliente.CAP_CORRISPONDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.CAP_CORRISPONDENZA.getFieldName())
                        .setValue(residenzaViewModel.getCapResidenza());
            }
            if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_CORRISPONDENZA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_CORRISPONDENZA.getFieldName())
                        .setValue(Objects.toString(Objects.toString(residenzaViewModel.getIndirizzoResidenza(), "")
                                + " " + Objects.toString(residenzaViewModel.getCivicoResidenza(), ""), "").trim());
            }

            if (acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName())
                        .setValue(clienteViewModel.getTelefono());
            }
            if (acroForm.getField(NameFieldsPdfCliente.TELEFONO_FISSO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TELEFONO_FISSO.getFieldName())
                        .setValue(clienteViewModel.getTelefonoFisso());
            }
            if (acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()).setValue(clienteViewModel.getEmail());
            }

            if (acroForm.getField(NameFieldsPdfCliente.FAX_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.FAX_AZIENDA.getFieldName()).setValue(aziendaViewModel.getFax());
            }
            if (acroForm.getField(NameFieldsPdfCliente.EMAIL_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.EMAIL_AZIENDA.getFieldName())
                        .setValue(aziendaViewModel.getEmail());
            }

            if (residenzaViewModel.getCorrispondenza() != null
                    && Corrispondenza.DOMICILIO.getValue().equalsIgnoreCase(residenzaViewModel.getCorrispondenza())) {
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_DOMICILIO.getFieldName()) != null) {
                    final PDCheckBox domicilio = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_DOMICILIO.getFieldName());
                    domicilio.check();
                }
                if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_DOMICILIO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_DOMICILIO.getFieldName())
                            .setValue(Objects.toString(Objects.toString(residenzaViewModel.getIndirizzoDomicilio(), "")
                                    + " " + Objects.toString(residenzaViewModel.getCivicoDomicilio(), ""), "").trim());
                }
                if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_DOMICILIO_NO_CIVICO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_DOMICILIO_NO_CIVICO.getFieldName())
                            .setValue(residenzaViewModel.getIndirizzoDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.CIVICO_DOMICILIO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.CIVICO_DOMICILIO.getFieldName())
                            .setValue(residenzaViewModel.getCivicoDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.LUOGO_DOMICILIO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.LUOGO_DOMICILIO.getFieldName())
                            .setValue(residenzaViewModel.getLuogoDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.PROVINCIA_DOMICILIO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.PROVINCIA_DOMICILIO.getFieldName())
                            .setValue(residenzaViewModel.getProvDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.CAP_DOMICILIO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.CAP_DOMICILIO.getFieldName())
                            .setValue(residenzaViewModel.getCapDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_CORRISPONDENZA.getFieldName())
                            .setValue(Objects.toString(Objects.toString(residenzaViewModel.getIndirizzoDomicilio(), "")
                                    + " " + Objects.toString(residenzaViewModel.getCivicoDomicilio(), ""), "").trim());
                }
                if (acroForm.getField(NameFieldsPdfCliente.LUOGO_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.LUOGO_CORRISPONDENZA.getFieldName())
                            .setValue(residenzaViewModel.getLuogoDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.PROV_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.PROV_CORRISPONDENZA.getFieldName())
                            .setValue(residenzaViewModel.getProvDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.CAP_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.CAP_CORRISPONDENZA.getFieldName())
                            .setValue(residenzaViewModel.getCapDomicilio());
                }

            } else if (residenzaViewModel.getCorrispondenza() != null
                    && Corrispondenza.ALTRO.getValue().equalsIgnoreCase(residenzaViewModel.getCorrispondenza())) {
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_ALTRA_CORRISPONDENZA.getFieldName()) != null) {
                    final PDCheckBox altraCorrispondenza = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_ALTRA_CORRISPONDENZA.getFieldName());
                    altraCorrispondenza.check();
                }
                if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_ALTRA_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_ALTRA_CORRISPONDENZA.getFieldName())
                            .setValue(residenzaViewModel.getIndirizzoDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.CIVICO_ALTRA_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.CIVICO_ALTRA_CORRISPONDENZA.getFieldName())
                            .setValue(residenzaViewModel.getCivicoDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.LUOGO_ALTRA_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.LUOGO_ALTRA_CORRISPONDENZA.getFieldName())
                            .setValue(residenzaViewModel.getLuogoDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.PROVINCIA_ALTRA_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.PROVINCIA_ALTRA_CORRISPONDENZA.getFieldName())
                            .setValue(residenzaViewModel.getProvDomicilio());
                }
                if (acroForm.getField(NameFieldsPdfCliente.CAP_ALTRA_CORRISPONDENZA.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.CAP_ALTRA_CORRISPONDENZA.getFieldName())
                            .setValue(residenzaViewModel.getCapDomicilio());
                }
            } else {
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_RESIDENZA.getFieldName()) != null) {
                    final PDCheckBox residenza = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_RESIDENZA.getFieldName());
                    residenza.check();
                }
            }

            if (occupazione.getImpiego() != null && !occupazione.getImpiego().isEmpty()) {
                if (Impiego.PENSIONATO.getValue().equalsIgnoreCase(occupazione.getImpiego())) {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PENSIONATO.getFieldName()) != null) {
                        final PDCheckBox pensionato = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_PENSIONATO.getFieldName());
                        pensionato.check();
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.ENTE_PENSIONISTICO.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.ENTE_PENSIONISTICO.getFieldName())
                                .setValue(occupazione.getEnte());
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.DATA_INIZIO_PENSIONE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.DATA_INIZIO_PENSIONE.getFieldName())
                                .setValue(DateToCalendar.patternData(occupazione.getInizioOccupazioneAssunzione()));
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.CATEGORIA_PENSIONE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.CATEGORIA_PENSIONE.getFieldName())
                                .setValue(occupazione.getCategoria());
                    }

                } else {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_DIPENDENTE.getFieldName()) != null) {
                        final PDCheckBox dipendente = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_DIPENDENTE.getFieldName());
                        dipendente.check();
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.OCCUPAZIONE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.OCCUPAZIONE.getFieldName())
                                .setValue(occupazione.getOccupazione());
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.DATA_INIZIO_OCCUPAZIONE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.DATA_INIZIO_OCCUPAZIONE.getFieldName())
                                .setValue(DateToCalendar.patternData(occupazione.getInizioOccupazioneAssunzione()));
                    }

                }
            }

            if (praticaViewModel.getTipoPratica() != null) {
                if (Pratica.TipoPratica.CESSIONE_S.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    if (acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                                .setValue(NameFieldsPdfCliente.CQS_LETTERE.getFieldName());
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_CQS.getFieldName()) != null) {
                        final PDCheckBox cqs = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_CQS.getFieldName());
                        cqs.check();
                    }
                } else if (Pratica.TipoPratica.CESSIONE_P.getValue()
                        .equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    if (acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                                .setValue(NameFieldsPdfCliente.CQP_LETTERE.getFieldName());
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_CQP.getFieldName()) != null) {
                        final PDCheckBox cqp = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_CQP.getFieldName());
                        cqp.check();
                    }
                } else if (Pratica.TipoPratica.DELEGA.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    if (acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                                .setValue(NameFieldsPdfCliente.DLG_LETTERE.getFieldName());
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_DLG.getFieldName()) != null) {
                        final PDCheckBox dlg = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_DLG.getFieldName());
                        dlg.check();
                    }
                } else if (Pratica.TipoPratica.PRESTITO.getValue()
                        .equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                    if (acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                                .setValue(NameFieldsPdfCliente.PP_LETTERE.getFieldName());
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
            if (praticaViewModel.getImportoErogato() != null
                    && acroForm.getField(NameFieldsPdfCliente.IMPORTO_EROGATO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.IMPORTO_EROGATO.getFieldName())
                        .setValue(stringAndBigDecimalConverter
                                .getStringFromBigDecimalValue(praticaViewModel.getImportoErogato()));
            }

            if (acroForm.getField(NameFieldsPdfCliente.NUMERO_CTR.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NUMERO_CTR.getFieldName())
                        .setValue(praticaViewModel.getNrctr());
            }

            // TODO mettere il 14 in una costante! e cmq il calcolo non dovrebbe
            // essere eseguito qui, questa classe DEVE SOLO stampare!!
            if (praticaViewModel.getNettoMensile() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.NETTO_MENSILE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.NETTO_MENSILE.getFieldName()).setValue(
                            stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getNettoMensile()));
                }

                if (acroForm.getField(NameFieldsPdfCliente.REDDITO_ANNUO.getFieldName()) != null) {
                    final BigDecimal redditoannuo = praticaViewModel.getNettoMensile().multiply(new BigDecimal("14"))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                    acroForm.getField(NameFieldsPdfCliente.REDDITO_ANNUO.getFieldName())
                            .setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue((redditoannuo)));
                }
            }

            if (praticaViewModel.getMontante() != null
                    && acroForm.getField(NameFieldsPdfCliente.MONTANTE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.MONTANTE.getFieldName()).setValue(
                        stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getMontante()));
            }

            if (praticaViewModel.getCapitaleFinanziato() != null
                    && acroForm.getField(NameFieldsPdfCliente.CAPITALE_FIN.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.CAPITALE_FIN.getFieldName())
                        .setValue(praticaViewModel.getCapitaleFinanziato());
            }

            if (acroForm.getField(NameFieldsPdfCliente.DATA_APERTURA_CONTO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_APERTURA_CONTO.getFieldName())
                        .setValue(DateToCalendar.patternData(contoViewModel.getDataConto()));
            }
            if (acroForm.getField(NameFieldsPdfCliente.FILIALE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.FILIALE.getFieldName()).setValue(contoViewModel.getBanca());
            }
            if (acroForm.getField(NameFieldsPdfCliente.IBAN.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.IBAN.getFieldName()).setValue(contoViewModel.getIban());
            }
            if (acroForm.getField(NameFieldsPdfCliente.SPORTELLO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.SPORTELLO.getFieldName())
                        .setValue(contoViewModel.getSportello());
            }

            if (documentoViewModel.getTipoDocumento() != null) {
                if (Documento.TipoDocumento.CARTA_IDENTITA.getValue().equalsIgnoreCase(documentoViewModel.getTipoDocumento())) {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_CARTA_IDENTITA.getFieldName()) != null) {
                        final PDCheckBox cartaIdentita = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_CARTA_IDENTITA.getFieldName());
                        cartaIdentita.check();
                    }
                } else if (Documento.TipoDocumento.PATENTE.getValue().equalsIgnoreCase(documentoViewModel.getTipoDocumento())) {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PATENTE.getFieldName()) != null) {
                        final PDCheckBox patente = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_PATENTE.getFieldName());
                        patente.check();
                    }
                } else if (Documento.TipoDocumento.PASSAPORTO.getValue()
                        .equalsIgnoreCase(documentoViewModel.getTipoDocumento())) {
                    if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_PASSAPORTO.getFieldName()) != null) {
                        final PDCheckBox passaporto = (PDCheckBox) acroForm
                                .getField(NameFieldsPdfCliente.CHECKBOX_PASSAPORTO.getFieldName());
                        passaporto.check();
                    }
                }

                if (acroForm.getField(NameFieldsPdfCliente.TIPO_DOC.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.TIPO_DOC.getFieldName()).setValue(documentoViewModel.getTipoDocumento());
                }
            }

            if (acroForm.getField(NameFieldsPdfCliente.NUMERO_DOC.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NUMERO_DOC.getFieldName())
                        .setValue(documentoViewModel.getNumeroDoc());
            }
            if (acroForm.getField(NameFieldsPdfCliente.RILASCIO_DOC.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.RILASCIO_DOC.getFieldName())
                        .setValue(documentoViewModel.getLuogoRilascioDoc());
            }
            if (acroForm.getField(NameFieldsPdfCliente.DATA_RILASCIO_DOC.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_RILASCIO_DOC.getFieldName())
                        .setValue(DateToCalendar.patternData(documentoViewModel.getRilascioDoc()));
            }
            if (acroForm.getField(NameFieldsPdfCliente.SCADENZA_DOC.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.SCADENZA_DOC.getFieldName())
                        .setValue(DateToCalendar.patternData(documentoViewModel.getScadenzaDoc()));
            }

            if (occupazione.getQualifica() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.IMPIEGO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.IMPIEGO.getFieldName())
                            .setValue(occupazione.getQualifica().toUpperCase());
                }
            }

            // TODO questo si potrebbe migliorare con un for e una lista di
            // fields, considerando l'indice per decidere dove salvare
            if (estinzioneViewModelList != null && !estinzioneViewModelList.isEmpty()) {
                if (estinzioneViewModelList.get(0).getRataEstinzione() != null
                        && acroForm.getField(NameFieldsPdfCliente.RATA_EST.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.RATA_EST.getFieldName())
                            .setValue(stringAndBigDecimalConverter
                                    .getStringFromBigDecimalValue(estinzioneViewModelList.get(0).getRataEstinzione()));
                }
                if (estinzioneViewModelList.get(0).getIstitutoEst() != null
                        && acroForm.getField(NameFieldsPdfCliente.ISTITUTO_EST.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.ISTITUTO_EST.getFieldName())
                            .setValue(estinzioneViewModelList.get(0).getIstitutoEst().toUpperCase());
                }
                // TODO non sarebbe meglio se uguale a 2?
                if (estinzioneViewModelList.size() > 1) {
                    if (estinzioneViewModelList.get(1).getRataEstinzione() != null
                            && acroForm.getField(NameFieldsPdfCliente.RATA_SECONDA_EST.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.RATA_SECONDA_EST.getFieldName())
                                .setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(
                                        estinzioneViewModelList.get(1).getRataEstinzione()));
                    }
                    if (estinzioneViewModelList.get(1).getIstitutoEst() != null
                            && acroForm.getField(NameFieldsPdfCliente.SECONDA_EST.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.SECONDA_EST.getFieldName())
                                .setValue(estinzioneViewModelList.get(1).getIstitutoEst().toUpperCase());
                    }

                }
            }

        }

        emptyValue.setValueEmpty(acroForm);
        pdfDocument.getDocumentCatalog().setAcroForm(acroForm);

        pdfDocument.save(fout);
        pdfDocument.close();

        return fout;

    }

}
