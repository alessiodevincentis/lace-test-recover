package com.woonders.lacemsjsf.view.pdf;

import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import enumPdf.italcredi.PagesAllegatiCertificati;
import enumPdf.italcredi.PagesCreditoNetC;
import enumPdf.italcredi.PagesDelegaAllegatoB;
import enumPdf.italcredi.PagesDelegaCreditoNet;
import enumPdf.mediolanum.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.springframework.stereotype.Component;

@Component
class SelectionPagesAllegati {

    void setPagesAllegatiMediolanum(final String impiego, final SelectPages selectPages, final String tipoPratica, final PDDocument document, final PDPageTree pages) {
        if (impiego != null) {
            if (Impiego.STATALE.getValue().equalsIgnoreCase(impiego)
                    || Impiego.AERONAUTICA.getValue().equalsIgnoreCase(impiego)
                    || Impiego.FINANZA.getValue().equalsIgnoreCase(impiego)
                    || Impiego.MILITARE.getValue().equalsIgnoreCase(impiego)
                    || Impiego.POLIZIA.getValue().equalsIgnoreCase(impiego)) {

                if (Pratica.TipoPratica.CESSIONE_S.getValue().equalsIgnoreCase(tipoPratica)) {
                    // pages:2,3,4,5,8,9
                    selectPages.addPages(document, pages, PagesAllegatiA.allValues()); //page 2,3
                    selectPages.addPages(document, pages, PagesAllegatiB.allValues()); //page 4,5
                    selectPages.addPages(document, pages, PagesAllegatiNetB.allValues()); //page 8
                    selectPages.addPages(document, pages, PagesAllegatiNetC.allValues()); //page 9
                } else if (Pratica.TipoPratica.DELEGA.getValue().equalsIgnoreCase(tipoPratica)) {
                    // pages:4,5,6,7,8,9
                    selectPages.addPages(document, pages, PagesAllegatiB.allValues()); //pages 4,5
                    selectPages.addPages(document, pages, PagesAllegatiE.allValues()); //pages 6,7
                    selectPages.addPages(document, pages, PagesAllegatiNetB.allValues()); //page 8
                    selectPages.addPages(document, pages, PagesAllegatiNetC.allValues()); //page 9
                }
            } else if (Impiego.PRIVATO.getValue().equalsIgnoreCase(impiego)
                    || Impiego.PUBBLICO.getValue().equalsIgnoreCase(impiego)
                    || Impiego.PARAPUBBLICO.getValue().equalsIgnoreCase(impiego)) {
                // pages:1,11,12
                selectPages.addPages(document, pages, PagesAllegatiRichiestaQuotaStipendio.allValues()); //page 1
                selectPages.addPages(document, pages, PagesAllegatiCertificatoStipendio.allValues()); //pages 11,12
            } else if (Impiego.PENSIONATO.getValue().equalsIgnoreCase(impiego)) {
                //pages 10
                selectPages.addPages(document, pages, PagesAllegatiAccessoInps.allValues()); //page 10
            } else if (Impiego.ALTRO.getValue().equalsIgnoreCase(impiego)) {
                selectPages.addPages(document, pages, PagesAllegatiRichiestaQuotaStipendio.allValues()); //page 1
                selectPages.addPages(document, pages, PagesAllegatiA.allValues()); //page 2,3
                selectPages.addPages(document, pages, PagesAllegatiB.allValues()); //pages 4,5
                selectPages.addPages(document, pages, PagesAllegatiE.allValues()); //pages 6,7
                selectPages.addPages(document, pages, PagesAllegatiNetB.allValues()); //page 8
                selectPages.addPages(document, pages, PagesAllegatiNetC.allValues()); //page 9
                selectPages.addPages(document, pages, PagesAllegatiCertificatoStipendio.allValues()); //pages 11,12
            }
        }
    }

    public void setPagesAllegatiItalcredi(final String impiego, final SelectPages selectPages, final String tipoPratica, final PDDocument document, final PDPageTree pages) {
        if (Impiego.STATALE.getValue().equalsIgnoreCase(impiego)
                || Impiego.AERONAUTICA.getValue().equalsIgnoreCase(impiego)
                || Impiego.FINANZA.getValue().equalsIgnoreCase(impiego)
                || Impiego.MILITARE.getValue().equalsIgnoreCase(impiego)) {
            if (Pratica.TipoPratica.CESSIONE_S.getValue().equalsIgnoreCase(tipoPratica)) {
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiA.allValues());
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiB.allValues());
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiNetB.allValues());
                selectPages.addPages(document, pages, PagesDelegaCreditoNet.allValues());
                selectPages.addPages(document, pages, PagesCreditoNetC.allValues());
            } else if (Pratica.TipoPratica.DELEGA.getValue().equalsIgnoreCase(tipoPratica)) {
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiB.allValues());
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiE.allValues());
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiNetB.allValues());
                selectPages.addPages(document, pages, PagesDelegaAllegatoB.allValues());
            }
        } else if (impiego.equalsIgnoreCase(Impiego.POLIZIA.getValue())) {
            if (Pratica.TipoPratica.CESSIONE_S.getValue().equalsIgnoreCase(tipoPratica)) {
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiA.allValues());
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiB.allValues());
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiNetB.allValues());
            } else if (Pratica.TipoPratica.DELEGA.getValue().equalsIgnoreCase(tipoPratica)) {
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiB.allValues());
                selectPages.addPages(document, pages, enumPdf.italcredi.PagesAllegatiE.allValues());
            }
        } else if (Impiego.PRIVATO.getValue().equalsIgnoreCase(impiego)
                || Impiego.PUBBLICO.getValue().equalsIgnoreCase(impiego)
                || Impiego.PARAPUBBLICO.getValue().equalsIgnoreCase(impiego)) {
            selectPages.addPages(document, pages, PagesAllegatiCertificati.allValues());
        } else if (impiego.equalsIgnoreCase(Impiego.ALTRO.getValue())) {
            selectPages.addPages(document, pages, PagesAllegatiA.allValues());
            selectPages.addPages(document, pages, PagesAllegatiB.allValues());
            selectPages.addPages(document, pages, PagesAllegatiE.allValues());
            selectPages.addPages(document, pages, PagesAllegatiNetB.allValues());
            selectPages.addPages(document, pages, PagesAllegatiCertificati.allValues());
            selectPages.addPages(document, pages, PagesDelegaCreditoNet.allValues());
            selectPages.addPages(document, pages, PagesDelegaAllegatoB.allValues());
            selectPages.addPages(document, pages, PagesCreditoNetC.allValues());
        }
    }
}
