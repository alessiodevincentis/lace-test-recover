package com.woonders.lacemsjsf.view.pdf;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.app.viewmodel.converter.StringAndBigDecimalConverter;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemsjsf.app.model.Occupazione;
import com.woonders.lacemsjsf.db.app.model.util.ClienteViewModelUtil;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import com.woonders.lacemsjsf.view.util.ConstantResolverView;
import enumPdf.NameFieldsPdfCliente;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.faces.bean.ManagedProperty;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static enumPdf.PdfFileType.ALLEGATI;

@Component
public class AllegatiPdf extends BasePdfComponent {

    public static final String NAME = "allegatiPdf";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @Autowired
    private ClienteViewModelUtil clienteViewModelUtil;
    @ManagedProperty(ConstantResolverView.NAME)
    private ConstantResolverView constantResolverView;
    @Autowired
    private StringAndBigDecimalConverter stringAndBigDecimalConverter;
    @Autowired
    private SelectionPagesAllegati selectionPagesAllegati;

    public ConstantResolverView getConstantResolverView() {
        return constantResolverView;
    }

    public void setConstantResolverView(final ConstantResolverView constantResolverView) {
        this.constantResolverView = constantResolverView;
    }

    public ByteArrayOutputStream riempiAllegatiPDF(final ClienteViewModel clienteViewModel,
                                                   final ResidenzaViewModel residenzaViewModel, final PraticaViewModel praticaViewModel,
                                                   final AziendaViewModel aziendaViewModel, final List<EstinzioneViewModel> estinzioneViewModelList)
            throws IOException, NoFileOnS3Exception {

        // load the document
        final FinanziariaViewModel finanziariaViewModel = praticaViewModel.getFinanziariaViewModel();
        final PDDocument pdfDocument = loadPDDocumentFromDisk(ALLEGATI, finanziariaViewModel);
        final ByteArrayOutputStream fout = new ByteArrayOutputStream();
        final PDPageTree pages = pdfDocument.getPages();
        final SelectPages selectPages = new SelectPages();
        final ControlNull emptyValue = new ControlNull();
        final PDDocument document = new PDDocument();
        final Occupazione occupazione = clienteViewModelUtil.getOccupazionePredefinita(clienteViewModel);
        final String tipoPratica = praticaViewModel.getTipoPratica();
        final String impiego = occupazione.getImpiego();

        if (finanziariaViewModel != null && !StringUtils.isEmpty(impiego)) {
            if (FinanziariaEnum.ITALCREDI.getFieldName().equalsIgnoreCase(finanziariaViewModel.getName())) {
                selectionPagesAllegati.setPagesAllegatiItalcredi(impiego, selectPages, tipoPratica, document, pages);
            } else if (FinanziariaEnum.MEDIOLANUM.getFieldName().equalsIgnoreCase(finanziariaViewModel.getName())) {
                selectionPagesAllegati.setPagesAllegatiMediolanum(impiego, selectPages, tipoPratica, document, pages);
            }
        }
        // get the document catalog
        final PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

        final String cognomeNome = clienteViewModel.getCognome().concat("  ").concat(clienteViewModel.getNome());

        // as there might not be an AcroForm entry aziendaViewModel null check
        // is necessary
        if (acroForm != null) {

            if (acroForm.getField(NameFieldsPdfCliente.SOTTOSCRITTO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.SOTTOSCRITTO.getFieldName()).setValue(cognomeNome);
            }
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
            if (acroForm.getField(NameFieldsPdfCliente.PROV_NASCITA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.PROV_NASCITA.getFieldName())
                        .setValue(clienteViewModel.getProvNascita());
            }
            if (acroForm.getField(NameFieldsPdfCliente.DATA_NASCITA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.DATA_NASCITA.getFieldName())
                        .setValue(DateToCalendar.patternData(clienteViewModel.getDataNascita()));
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

            if (acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_DOMICILIO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.INDIRIZZO_DOMICILIO.getFieldName())
                        .setValue(Objects.toString(Objects.toString(residenzaViewModel.getIndirizzoDomicilio(), "")
                                + " " + Objects.toString(residenzaViewModel.getCivicoDomicilio(), ""), "").trim());
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

            if (acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TELEFONO.getFieldName())
                        .setValue(clienteViewModel.getTelefono());
            }
            if (acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.EMAIL.getFieldName()).setValue(clienteViewModel.getEmail());
            }
            if (acroForm.getField(NameFieldsPdfCliente.COGNOME_TITOLARE_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.COGNOME_TITOLARE_AZIENDA.getFieldName())
                        .setValue(aziendaViewModel.getCognomeTitolare());
            }
            if (acroForm.getField(NameFieldsPdfCliente.NOME_TITOLARE_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.NOME_TITOLARE_AZIENDA.getFieldName())
                        .setValue(aziendaViewModel.getNomeTitolare());
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
            if (acroForm.getField(NameFieldsPdfCliente.PEC_AZIENDA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.PEC_AZIENDA.getFieldName()).setValue(aziendaViewModel.getPec());
            }

            if (Pratica.TipoPratica.CESSIONE_S.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                if (acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                            .setValue(NameFieldsPdfCliente.CQS_LETTERE.getFieldName());
                }
                if (acroForm.getField(NameFieldsPdfCliente.DURATA_CQ.getFieldName()) != null
                        && praticaViewModel.getDurata() != null) {
                    acroForm.getField(NameFieldsPdfCliente.DURATA_CQ.getFieldName())
                            .setValue(praticaViewModel.getDurata().toString());
                }
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_CQS.getFieldName()) != null) {
                    final PDCheckBox cqs = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_CQS.getFieldName());
                    cqs.check();
                }
            } else if (Pratica.TipoPratica.DELEGA.getValue().equalsIgnoreCase(praticaViewModel.getTipoPratica())) {
                if (acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.TIPO_PRATICA_LETTERE.getFieldName())
                            .setValue(NameFieldsPdfCliente.DLG_LETTERE.getFieldName());
                }
                if (acroForm.getField(NameFieldsPdfCliente.DURATA_DLG.getFieldName()) != null
                        && praticaViewModel.getDurata() != null) {
                    acroForm.getField(NameFieldsPdfCliente.DURATA_DLG.getFieldName())
                            .setValue(praticaViewModel.getDurata().toString());
                }
                if (acroForm.getField(NameFieldsPdfCliente.CHECKBOX_DLG.getFieldName()) != null) {
                    final PDCheckBox dlg = (PDCheckBox) acroForm
                            .getField(NameFieldsPdfCliente.CHECKBOX_DLG.getFieldName());
                    dlg.check();
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

            if (praticaViewModel.getImportoErogato() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.IMPORTO_EROGATO.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.IMPORTO_EROGATO.getFieldName())
                            .setValue(stringAndBigDecimalConverter
                                    .getStringFromBigDecimalValue(praticaViewModel.getImportoErogato()));
                }
                if (acroForm.getField(NameFieldsPdfCliente.IMPORTO_EROGATO_LETTERE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.IMPORTO_EROGATO_LETTERE.getFieldName())
                            .setValue(DateToCalendar.NumberToText(praticaViewModel.getImportoErogato()).toUpperCase());
                }
            }

            if (praticaViewModel.getMontante() != null) {
                if (acroForm.getField(NameFieldsPdfCliente.MONTANTE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.MONTANTE.getFieldName()).setValue(
                            stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getMontante()));
                }
                if (acroForm.getField(NameFieldsPdfCliente.MONTANTE_LETTERE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.MONTANTE_LETTERE.getFieldName())
                            .setValue(DateToCalendar.NumberToText(praticaViewModel.getMontante()).toUpperCase());
                }
            }

            if (praticaViewModel.getSpese() != null
                    && acroForm.getField(NameFieldsPdfCliente.SPESE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.SPESE.getFieldName()).setValue(
                        stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getSpese()));

                if (acroForm.getField(NameFieldsPdfCliente.SPESE_LETTERE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.SPESE_LETTERE.getFieldName())
                            .setValue(DateToCalendar.NumberToText(praticaViewModel.getSpese()).toUpperCase());
                }
            }

            if (praticaViewModel.getProvvigione() != null
                    && acroForm.getField(NameFieldsPdfCliente.EURO_PROVVIGIONI.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.EURO_PROVVIGIONI.getFieldName()).setValue(
                        stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getProvvigione()));

                if (acroForm.getField(NameFieldsPdfCliente.EURO_PROVVIGIONI_LETTERE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.EURO_PROVVIGIONI_LETTERE.getFieldName())
                            .setValue(DateToCalendar.NumberToText(praticaViewModel.getProvvigione()).toUpperCase());
                }
            }

            if (praticaViewModel.getTan() != null
                    && acroForm.getField(NameFieldsPdfCliente.TAN.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TAN.getFieldName())
                        .setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getTan()));
            }

            if (praticaViewModel.getTaeg() != null
                    && acroForm.getField(NameFieldsPdfCliente.TAEG.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TAEG.getFieldName()).setValue(
                        stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getTaeg()));
            }

            if (praticaViewModel.getTeg() != null
                    && acroForm.getField(NameFieldsPdfCliente.TEG.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.TEG.getFieldName())
                        .setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getTeg()));
            }

            if (praticaViewModel.getInteressi() != null
                    && acroForm.getField(NameFieldsPdfCliente.INTERESSI.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.INTERESSI.getFieldName()).setValue(
                        stringAndBigDecimalConverter.getStringFromBigDecimalValue(praticaViewModel.getInteressi()));

                if (acroForm.getField(NameFieldsPdfCliente.INTERESSI_LETTERE.getFieldName()) != null) {
                    acroForm.getField(NameFieldsPdfCliente.INTERESSI_LETTERE.getFieldName())
                            .setValue(DateToCalendar.NumberToText(praticaViewModel.getInteressi()).toUpperCase());
                }
            }

            if (praticaViewModel.getGaranzia() != null
                    && acroForm.getField(NameFieldsPdfCliente.GARANZIA.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.GARANZIA.getFieldName())
                        .setValue(praticaViewModel.getGaranzia().toUpperCase());
            }

            if (estinzioneViewModelList != null) {
                if (!estinzioneViewModelList.isEmpty()) {
                    if (acroForm.getField(NameFieldsPdfCliente.RATA_EST.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.RATA_EST.getFieldName())
                                .setValue(stringAndBigDecimalConverter.getStringFromBigDecimalValue(
                                        estinzioneViewModelList.get(0).getRataEstinzione()));
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.ISTITUTO_EST.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.ISTITUTO_EST.getFieldName())
                                .setValue(estinzioneViewModelList.get(0).getIstitutoEst().toUpperCase());
                    }
                    if (acroForm.getField(NameFieldsPdfCliente.SCADENZA_EST.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.SCADENZA_EST.getFieldName()).setValue(
                                DateToCalendar.patternData(estinzioneViewModelList.get(0).getScadenzaEstinzione()));
                    }
                }
                if (estinzioneViewModelList.size() > 1) {
                    if (acroForm.getField(NameFieldsPdfCliente.SECONDA_EST.getFieldName()) != null) {
                        acroForm.getField(NameFieldsPdfCliente.SECONDA_EST.getFieldName())
                                .setValue("Si Estingue anche: Rata: "
                                        + estinzioneViewModelList.get(1).getRataEstinzione() + " Scadenza: "
                                        + DateToCalendar.patternData(
                                        estinzioneViewModelList.get(1).getScadenzaEstinzione())
                                        + " Istituto: "
                                        + estinzioneViewModelList.get(1).getIstitutoEst().toUpperCase());
                    }
                }
            }

            if (acroForm.getField(NameFieldsPdfCliente.OCCUPAZIONE.getFieldName()) != null) {
                acroForm.getField(NameFieldsPdfCliente.OCCUPAZIONE.getFieldName())
                        .setValue(occupazione.getOccupazione());
            }

        }

        emptyValue.setValueEmpty(acroForm);
        document.getDocumentCatalog().setAcroForm(acroForm);

        document.save(fout);
        document.close();

        return fout;

    }

}
