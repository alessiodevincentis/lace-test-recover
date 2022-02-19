package com.woonders.lacemscommon.app.model;


import com.woonders.lacemscommon.app.viewmodel.AmministrazioneViewModel;
import com.woonders.lacemscommon.db.entity.Cliente;
import com.woonders.lacemscommon.laceenum.TipoRinnovo;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvancedSearchViewModel {

    private Long aziendaId;
    private List<String> operatorList;
    private Cliente.Sesso sesso;
    private Integer etaFrom;
    private Integer etaTo;
    private String provinciaResidenza;
    private List<String> statoPraticaList;
    private List<String> statoNominativoList;
    private List<String> provenienzaList;
    
    private List<String> provenienzaDescList;
    
    private List<String> impieghiList;
    private Integer anniAssunzione;
    private LocalDateTime dataRecallInizio;
    private LocalDateTime dataRecallFine;
    private TipoRinnovo tipoRinnovo;
    private LocalDate dataRinnovoInizio;
    private LocalDate dataRinnovoFine;
    private LocalDate dataInizioCaricamento;
    private LocalDate dataFineCaricamento;
    private LocalDate dataIstruttoriaInizio;
    private LocalDate dataIstruttoriaFine;
    private LocalDate dataIstruitaInizio;
    private LocalDate dataIstruitaFine;
    private LocalDate dataDeliberaInizio;
    private LocalDate dataDeliberaFine;
    private LocalDate dataFirmaContrattiInizio;
    private LocalDate dataFirmaContrattiFine;
    private LocalDate dataLiquidazioneInizio;
    private LocalDate dataLiquidazioneFine;
    private LocalDate dataDecorrenzaInizio;
    private LocalDate dataDecorrenzaFine;
    private LocalDate dataPerfezionamentoInizio;
    private LocalDate dataPerfezionamentoFine;
    private LocalDate dataInizioInserimento;
    private LocalDate dataFineInserimento;
    private AmministrazioneViewModel amministrazioneViewModel;
}
