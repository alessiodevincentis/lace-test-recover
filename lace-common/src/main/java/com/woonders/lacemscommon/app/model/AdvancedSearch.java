package com.woonders.lacemscommon.app.model;

import com.woonders.lacemscommon.db.entity.Amministrazione;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.laceenum.TipoRinnovo;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvancedSearch {

    private Long aziendaId;
    private List<String> operatorList;
    private Cliente.Sesso sesso;
    private Date dataNascitaInizio;
    private Date dataNascitaFine;
    private String provinciaResidenza;
    private List<String> statoPraticaList;
    private List<String> statoNominativoList;
    private List<String> provenienzaList;
    
    private List<String> provenienzaDescList;
    
    private List<String> impieghiList;
    private Date dataAssunzione;
    private Date dataRecallInizio;
    private Date dataRecallFine;
    private TipoRinnovo tipoRinnovo;
    private Date dataRinnovoInizio;
    private Date dataRinnovoFine;
    private Date dataInizioCaricamento;
    private Date dataFineCaricamento;
    private Date dataIstruttoriaInizio;
    private Date dataIstruttoriaFine;
    private Date dataIstruitaInizio;
    private Date dataIstruitaFine;
    private Date dataDeliberaInizio;
    private Date dataDeliberaFine;
    private Date dataFirmaContrattiInizio;
    private Date dataFirmaContrattiFine;
    private Date dataLiquidazioneInizio;
    private Date dataLiquidazioneFine;
    private Date dataDecorrenzaInizio;
    private Date dataDecorrenzaFine;
    private Date dataPerfezionamentoInizio;
    private Date dataPerfezionamentoFine;
    private Date dataInizioInserimento;
    private Date dataFineInserimento;
    private Amministrazione amministrazione;
}
