package com.woonders.lacemscommon.util;

import com.woonders.lacemscommon.app.model.AdvancedSearchViewModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class AdvancedSearchUtil {

    public static final String NAME = "advancedSearchUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";


    public boolean checkIfFilterAdvancedSearchIsNull(final AdvancedSearchViewModel advancedSearchViewModel) {

    	//*** Mod Crsistian 11-11-2021 devo aggiungere il campo di ricerca provenienzaDescList ****
        return Stream.of(advancedSearchViewModel.getAmministrazioneViewModel(),
                advancedSearchViewModel.getAziendaId(),
                advancedSearchViewModel.getOperatorList(), advancedSearchViewModel.getImpieghiList(),
                advancedSearchViewModel.getProvenienzaList(), advancedSearchViewModel.getProvenienzaDescList(),
                advancedSearchViewModel.getAnniAssunzione(), advancedSearchViewModel.getAnniAssunzione(),
                advancedSearchViewModel.getEtaFrom(), advancedSearchViewModel.getEtaTo(),
                advancedSearchViewModel.getProvinciaResidenza(), advancedSearchViewModel.getSesso(),
                advancedSearchViewModel.getStatoPraticaList(), advancedSearchViewModel.getDataFineCaricamento(),
                advancedSearchViewModel.getStatoNominativoList(), advancedSearchViewModel.getDataFineInserimento(),
                advancedSearchViewModel.getDataInizioCaricamento(), advancedSearchViewModel.getDataInizioInserimento(),
                advancedSearchViewModel.getDataRinnovoFine(), advancedSearchViewModel.getDataRinnovoInizio(),
                advancedSearchViewModel.getDataRecallInizio(), advancedSearchViewModel.getDataRecallFine(),
                advancedSearchViewModel.getDataIstruttoriaInizio(), advancedSearchViewModel.getDataIstruttoriaFine(),
                advancedSearchViewModel.getDataIstruitaInizio(), advancedSearchViewModel.getDataIstruitaFine(),
                advancedSearchViewModel.getDataDeliberaInizio(), advancedSearchViewModel.getDataDeliberaFine(),
                advancedSearchViewModel.getDataFirmaContrattiInizio(), advancedSearchViewModel.getDataFirmaContrattiFine(),
                advancedSearchViewModel.getDataDecorrenzaInizio(), advancedSearchViewModel.getDataDecorrenzaFine(),
                advancedSearchViewModel.getDataLiquidazioneInizio(), advancedSearchViewModel.getDataLiquidazioneFine(),
                advancedSearchViewModel.getDataPerfezionamentoInizio(), advancedSearchViewModel.getDataPerfezionamentoFine())
                .allMatch(this::isNullOrEmptyString);
    }

    private boolean isNullOrEmptyString(final Object obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj instanceof String) {
                if (((String) obj).isEmpty()) {
                    return true;
                }
            }
            if (obj instanceof List) {
                if (((List) obj).isEmpty()) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isDateRightFormat(final AdvancedSearchViewModel advancedSearchViewModel) {

        if ((advancedSearchViewModel.getDataInizioInserimento() == null && advancedSearchViewModel.getDataFineInserimento() != null)
                || (advancedSearchViewModel.getDataInizioInserimento() != null && advancedSearchViewModel.getDataFineInserimento() == null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataInizioCaricamento() != null && advancedSearchViewModel.getDataFineCaricamento() == null)
                || (advancedSearchViewModel.getDataInizioCaricamento() == null && advancedSearchViewModel.getDataFineCaricamento() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataRinnovoInizio() != null && advancedSearchViewModel.getDataRinnovoFine() == null
                && advancedSearchViewModel.getTipoRinnovo() == null)

                || (advancedSearchViewModel.getDataRinnovoInizio() != null && advancedSearchViewModel.getDataRinnovoFine() == null
                && advancedSearchViewModel.getTipoRinnovo() != null)

                || (advancedSearchViewModel.getDataRinnovoInizio() == null && advancedSearchViewModel.getDataRinnovoFine() != null
                && advancedSearchViewModel.getTipoRinnovo() == null)

                || (advancedSearchViewModel.getDataRinnovoInizio() == null && advancedSearchViewModel.getDataRinnovoFine() != null
                && advancedSearchViewModel.getTipoRinnovo() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataRecallInizio() != null && advancedSearchViewModel.getDataRecallFine() == null)
                || (advancedSearchViewModel.getDataRecallInizio() == null && advancedSearchViewModel.getDataRecallFine() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataIstruttoriaInizio() != null && advancedSearchViewModel.getDataIstruttoriaFine() == null)
                || (advancedSearchViewModel.getDataIstruttoriaInizio() == null && advancedSearchViewModel.getDataIstruttoriaFine() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataIstruitaInizio() != null && advancedSearchViewModel.getDataIstruitaFine() == null)
                || (advancedSearchViewModel.getDataIstruitaInizio() == null && advancedSearchViewModel.getDataIstruitaFine() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataDeliberaInizio() != null && advancedSearchViewModel.getDataDeliberaFine() == null)
                || (advancedSearchViewModel.getDataDeliberaInizio() == null && advancedSearchViewModel.getDataDeliberaFine() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataFirmaContrattiInizio() != null && advancedSearchViewModel.getDataFirmaContrattiFine() == null)
                || (advancedSearchViewModel.getDataFirmaContrattiInizio() == null && advancedSearchViewModel.getDataFirmaContrattiFine() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataDecorrenzaInizio() != null && advancedSearchViewModel.getDataDecorrenzaFine() == null)
                || (advancedSearchViewModel.getDataDecorrenzaInizio() == null && advancedSearchViewModel.getDataDecorrenzaFine() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataLiquidazioneInizio() != null && advancedSearchViewModel.getDataLiquidazioneFine() == null)
                || (advancedSearchViewModel.getDataLiquidazioneInizio() == null && advancedSearchViewModel.getDataLiquidazioneFine() != null)) {
            return false;
        }

        if ((advancedSearchViewModel.getDataPerfezionamentoInizio() != null && advancedSearchViewModel.getDataPerfezionamentoFine() == null)
                || (advancedSearchViewModel.getDataPerfezionamentoInizio() == null && advancedSearchViewModel.getDataPerfezionamentoFine() != null)) {
            return false;
        }

        return true;
    }

    public boolean isDateRightComparable(final AdvancedSearchViewModel advancedSearchClienti) {

        if (advancedSearchClienti.getDataInizioInserimento() != null && advancedSearchClienti.getDataFineInserimento() != null) {
            if (advancedSearchClienti.getDataInizioInserimento().compareTo(advancedSearchClienti.getDataFineInserimento()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataInizioCaricamento() != null && advancedSearchClienti.getDataFineCaricamento() != null) {
            if (advancedSearchClienti.getDataInizioCaricamento().compareTo(advancedSearchClienti.getDataFineCaricamento()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataRinnovoInizio() != null && advancedSearchClienti.getDataRinnovoFine() != null
                && advancedSearchClienti.getTipoRinnovo() != null) {
            if (advancedSearchClienti.getDataRinnovoInizio().compareTo(advancedSearchClienti.getDataRinnovoFine()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataRecallInizio() != null && advancedSearchClienti.getDataRecallFine() != null) {
            if (advancedSearchClienti.getDataRecallInizio().compareTo(advancedSearchClienti.getDataRecallFine()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataIstruttoriaInizio() != null && advancedSearchClienti.getDataIstruttoriaFine() != null) {
            if (advancedSearchClienti.getDataIstruttoriaInizio().compareTo(advancedSearchClienti.getDataIstruttoriaFine()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataIstruitaInizio() != null && advancedSearchClienti.getDataIstruitaFine() != null) {
            if (advancedSearchClienti.getDataIstruitaInizio().compareTo(advancedSearchClienti.getDataIstruitaFine()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataDeliberaInizio() != null && advancedSearchClienti.getDataDeliberaFine() != null) {
            if (advancedSearchClienti.getDataDeliberaInizio().compareTo(advancedSearchClienti.getDataDeliberaFine()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataFirmaContrattiInizio() != null && advancedSearchClienti.getDataFirmaContrattiFine() != null) {
            if (advancedSearchClienti.getDataFirmaContrattiInizio().compareTo(advancedSearchClienti.getDataFirmaContrattiFine()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataDecorrenzaInizio() != null && advancedSearchClienti.getDataDecorrenzaFine() != null) {
            if (advancedSearchClienti.getDataDecorrenzaInizio().compareTo(advancedSearchClienti.getDataDecorrenzaFine()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataLiquidazioneInizio() != null && advancedSearchClienti.getDataLiquidazioneFine() != null) {
            if (advancedSearchClienti.getDataLiquidazioneInizio().compareTo(advancedSearchClienti.getDataLiquidazioneFine()) > 0) {
                return false;
            }
        }

        if (advancedSearchClienti.getDataPerfezionamentoInizio() != null && advancedSearchClienti.getDataPerfezionamentoFine() != null) {
            if (advancedSearchClienti.getDataPerfezionamentoInizio().compareTo(advancedSearchClienti.getDataPerfezionamentoFine()) > 0) {
                return false;
            }
        }

        return true;
    }

}
